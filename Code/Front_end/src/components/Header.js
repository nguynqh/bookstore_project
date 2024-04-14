import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { getCurrentUser, logout } from '../services/authService';
import './Header.css';

const Header = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentUser, setCurrentUser] = useState(getCurrentUser());

  // Theo dõi thay đổi url để cập nhật trạng thái đăng nhập
  useEffect(() => {
    setCurrentUser(getCurrentUser());
  }, [location]);

  const handleLogout = () => {
    logout();
    setCurrentUser(null);
    navigate('/login', { replace: true });
  };

  return (
    <header className="header">
      <div className="header-left">
        <Link to="/" className="logo">BookStore</Link>
        <nav className="nav">
          <Link to="/">Home</Link>
          <Link to="/books">Books</Link>
        </nav>
      </div>
      <div className="header-right">
        <Link to="/cart" className="cart-link">
          <i className="fas fa-shopping-cart"></i>
        </Link>
        {currentUser ? (
          <div className="user-menu">
            <Link to="/profile" className="user-name">{currentUser.fullName}</Link>
            <button className="logout-btn" onClick={handleLogout}>
              <i className="fas fa-sign-out-alt"></i> Logout
            </button>
          </div>
        ) : (
          <Link to="/login" className="login-btn">
            <span className="login-btn-content">
              <i className="fas fa-sign-in-alt"></i>
              <span>Login</span>
            </span>
          </Link>
        )}
      </div>
    </header>
  );
};

export default Header;
