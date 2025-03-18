import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import BooksPage from './pages/BooksPage';
import CategoriesPage from './pages/CategoriesPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <Header />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/books" element={<BooksPage />} />
            <Route path="/categories" element={<CategoriesPage />} />
          </Routes>
        </main>
        <footer className="footer">
          <div className="container">
            <p>&copy; {new Date().getFullYear()} BookStore Management. All rights reserved. </p>
            <p>DESIGN BY KHOI MINH DOAN</p>
          </div>
        </footer>
      </div>
    </Router>
  );
}

export default App;
