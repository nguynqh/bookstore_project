import axios from 'axios';
import booksData from '../data/books.json';
import fs from 'fs';
import path from 'path';

const API_URL = 'http://localhost:5000/api/books';

// Sử dụng dữ liệu tĩnh từ file books.json
let books = [...booksData.books];

// Hàm lưu dữ liệu vào file JSON
const saveToJson = async (updatedBooks) => {
  try {
    const filePath = path.join(__dirname, '../data/books.json');
    await fs.promises.writeFile(
      filePath,
      JSON.stringify({ books: updatedBooks }, null, 2),
      'utf8'
    );
  } catch (error) {
    console.error('Error saving to JSON:', error);
    throw new Error('Không thể lưu dữ liệu vào file');
  }
};

// Lấy danh sách tất cả sách
export const getAllBooks = async () => {
  try {
    const response = await axios.get(API_URL);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Lấy thông tin một cuốn sách theo id
export const getBookById = async (id) => {
  try {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Thêm sách mới
export const addBook = async (bookData) => {
  try {
    const response = await axios.post(API_URL, bookData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Cập nhật thông tin sách
export const updateBook = async (id, bookData) => {
  try {
    const response = await axios.put(`${API_URL}/${id}`, bookData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Xóa sách
export const deleteBook = async (id) => {
  try {
    const response = await axios.delete(`${API_URL}/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Tìm kiếm sách
export const searchBooks = async (query) => {
  try {
    const response = await axios.get(API_URL);
    const books = response.data;
    const searchTerm = query.toLowerCase();
    return books.filter(book => 
      book.title.toLowerCase().includes(searchTerm) ||
      book.author.toLowerCase().includes(searchTerm) ||
      book.category.toLowerCase().includes(searchTerm)
    );
  } catch (error) {
    throw error;
  }
}; 