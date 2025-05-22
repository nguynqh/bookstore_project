import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import HomePage from './pages/HomePage';
import BooksPage from './pages/BooksPage';
import BookDetailPage from './pages/BookDetailPage';
import LoginPage from './pages/LoginPage';
import CartPage from './pages/CartPage';
import AddEditBookPage from './pages/AddEditBookPage';
import UserProfilePage from './pages/UserProfilePage';
import CategoriesPage from './pages/CategoriesPage';
import EditBook from './pages/EditBook';
import './App.css';
import BookManager from './components/BookManager';
import { getCurrentUser } from './services/authService';

// PrivateRoute: chỉ cho phép truy cập nếu đã đăng nhập
const PrivateRoute = ({ children }) => {
  const user = getCurrentUser();
  return user ? children : <Navigate to="/login" replace />;
};

function App() {
  return (
    <Router>
      <div className="app">
        <Header />
        <main className="main-content">
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/" element={<PrivateRoute><HomePage /></PrivateRoute>} />
            <Route path="/books" element={<PrivateRoute><BooksPage /></PrivateRoute>} />
            <Route path="/book/:id" element={<BookDetailPage />} />
            <Route path="/cart" element={<PrivateRoute><CartPage /></PrivateRoute>} />
            <Route path="/book/add" element={<AddEditBookPage />} />
            <Route path="/book/edit/:id" element={<PrivateRoute><EditBook /></PrivateRoute>} />
            <Route path="/profile" element={<PrivateRoute><UserProfilePage /></PrivateRoute>} />
            <Route path="/categories" element={<PrivateRoute><CategoriesPage /></PrivateRoute>} />
            <Route path="/manage-books" element={<PrivateRoute><BookManager /></PrivateRoute>} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
