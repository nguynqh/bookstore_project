const express = require('express');
const fs = require('fs');
const path = require('path');
const router = express.Router();

const usersFile = path.join(__dirname, 'users.json');

// Lấy danh sách user
router.get('/', (req, res) => {
  const users = JSON.parse(fs.readFileSync(usersFile));
  res.json(users);
});

// Đăng ký user mới
router.post('/register', (req, res) => {
  const users = JSON.parse(fs.readFileSync(usersFile));
  const { username, email } = req.body;
  if (users.find(u => u.username === username)) {
    return res.status(400).json({ message: 'Username already exists' });
  }
  if (users.find(u => u.email === email)) {
    return res.status(400).json({ message: 'Email already exists' });
  }
  const newUser = { ...req.body, id: users.length + 1, role: 'user', orders: [] };
  users.push(newUser);
  fs.writeFileSync(usersFile, JSON.stringify(users, null, 2));
  res.json(newUser);
});

// Lấy thông tin user theo username
router.get('/:username', (req, res) => {
  const users = JSON.parse(fs.readFileSync(usersFile));
  const user = users.find(u => u.username === req.params.username);
  if (!user) return res.status(404).json({ message: 'User not found' });
  res.json(user);
});

// Cập nhật thông tin user
router.put('/:id', (req, res) => {
  const users = JSON.parse(fs.readFileSync(usersFile));
  const idx = users.findIndex(u => u.id == req.params.id);
  if (idx === -1) return res.status(404).json({ message: 'User not found' });
  users[idx] = { ...users[idx], ...req.body };
  fs.writeFileSync(usersFile, JSON.stringify(users, null, 2));
  res.json(users[idx]);
});

module.exports = router; 