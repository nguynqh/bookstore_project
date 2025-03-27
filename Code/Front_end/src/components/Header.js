import React from 'react';
import { Link, NavLink } from 'react-router-dom';
import './Header.css';

const Header = () => {
  return (
    <header className="header">
      <div className="container">
        <div className="header-content">
          <Link to="/" className="logo">
            <i className="fas fa-book"></i> BookStore Management
          </Link>
          <nav className="nav">
            <ul>
              <li>
                <NavLink to="/" className={({ isActive }) => isActive ? "active" : ""}>
                  Home
                </NavLink>
              </li>
              <li>
                <NavLink to="/books" className={({ isActive }) => isActive ? "active" : ""}>
                  Books
                </NavLink>
              </li>
              <li>
                <NavLink to="/categories" className={({ isActive }) => isActive ? "active" : ""}>
                  Categories
                </NavLink>
              </li>
            </ul>
          </nav>
          <div className="user-actions">
            <Link to="/profile" className="user-icon">
              <i className="fas fa-user"></i>
            </Link>
            <Link to="/login" className="logout-btn">
              <i className="fas fa-sign-out-alt"></i> Logout
            </Link>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
