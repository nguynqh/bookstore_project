import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  const currentYear = new Date().getFullYear();
  
  return (
    <footer className="site-footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-section about">
            <h3>About Us</h3>
            <p>
              Our bookstore is dedicated to providing quality books at affordable prices. 
              We believe that reading opens doors to new worlds and experiences.
            </p>
            <div className="social-links">
              <a href="https://facebook.com" target="_blank" rel="noopener noreferrer">
                <i className="fab fa-facebook-f"></i>
              </a>
              <a href="https://twitter.com" target="_blank" rel="noopener noreferrer">
                <i className="fab fa-twitter"></i>
              </a>
              <a href="https://instagram.com" target="_blank" rel="noopener noreferrer">
                <i className="fab fa-instagram"></i>
              </a>
            </div>
          </div>
          
          <div className="footer-section links">
            <h3>Quick Links</h3>
            <ul>
              <li><Link to="/">Home</Link></li>
              <li><Link to="/books">Books</Link></li>
              <li><Link to="/cart">Cart</Link></li>
              <li><Link to="/profile">My Account</Link></li>
            </ul>
          </div>
          
          <div className="footer-section contact">
            <h3>Contact Us</h3>
            <p>
              <i className="fas fa-map-marker-alt"></i> 123 An duong vuong, HCM
            </p>
            <p>
              <i className="fas fa-phone"></i> (123) 456-7890
            </p>
            <p>
              <i className="fas fa-envelope"></i> khoiminhdoan@bookstore.com
            </p>
          </div>
        </div>
        
        <div className="footer-bottom">
          <p>&copy; {currentYear} Bookstore. All rights reserved.</p>
          <p>Developed by Khoi Minh Doan</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
