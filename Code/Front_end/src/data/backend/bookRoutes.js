const express = require('express');
const fs = require('fs');
const path = require('path');
const router = express.Router();

const booksFile = path.join(__dirname, 'books.json');

// Lấy danh sách sách
router.get('/', (req, res) => {
  const data = JSON.parse(fs.readFileSync(booksFile));
  res.json(data.books);
});

// Lấy 1 sách theo id
router.get('/:id', (req, res) => {
  const data = JSON.parse(fs.readFileSync(booksFile));
  const book = data.books.find(b => b.id == req.params.id);
  if (!book) return res.status(404).json({ message: 'Book not found' });
  res.json(book);
});

// Thêm sách mới
router.post('/', (req, res) => {
  const data = JSON.parse(fs.readFileSync(booksFile));
  const newBook = { ...req.body, id: data.books.length ? data.books[data.books.length-1].id + 1 : 1 };
  data.books.push(newBook);
  fs.writeFileSync(booksFile, JSON.stringify(data, null, 2));
  res.json(newBook);
});

// Sửa sách
router.put('/:id', (req, res) => {
  const data = JSON.parse(fs.readFileSync(booksFile));
  const idx = data.books.findIndex(b => b.id == req.params.id);
  if (idx === -1) return res.status(404).json({ message: 'Book not found' });
  data.books[idx] = { ...data.books[idx], ...req.body };
  fs.writeFileSync(booksFile, JSON.stringify(data, null, 2));
  res.json(data.books[idx]);
});

// Xoá sách
router.delete('/:id', (req, res) => {
  const data = JSON.parse(fs.readFileSync(booksFile));
  const idx = data.books.findIndex(b => b.id == req.params.id);
  if (idx === -1) return res.status(404).json({ message: 'Book not found' });
  const deleted = data.books[idx];
  data.books = data.books.filter(b => b.id != req.params.id);
  fs.writeFileSync(booksFile, JSON.stringify(data, null, 2));
  res.json(deleted);
});

module.exports = router; 