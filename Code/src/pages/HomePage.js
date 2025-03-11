import React from 'react';
import { Link } from 'react-router-dom';
import './HomePage.css';

const HomePage = () => {
  return (
    <div className="home-page">
      <div className="container">
        <div className="welcome-section">
          <h1>Welcome to BookStore Management System</h1>
          <p>Your comprehensive solution for managing book inventory and sales.</p>
        </div>
        
        <div className="quick-links">
          <div className="quick-link-card">
            <h2>Book Management</h2>
            <p>Add, update, or remove books from your inventory.</p>
            <Link to="/books" className="btn-primary">Manage Books</Link>
          </div>
          
          <div className="quick-link-card">
            <h2>Category Management</h2>
            <p>Organize your books into different categories.</p>
            <Link to="/categories" className="btn-primary">Manage Categories</Link>
          </div>
          
          <div className="quick-link-card">
            <h2>Reports</h2>
            <p>View detail about sales and inventory reports.</p>
            <Link to="/reports" className="btn-primary">View Reports</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
