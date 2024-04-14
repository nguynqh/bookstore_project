import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import booksData from '../data/books.json';
import './BookDetailPage.css';

const BookDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      const foundBook = booksData.books.find(book => book.id === parseInt(id));
      setBook(foundBook);
      setLoading(false);
    }, 500);
  }, [id]);

  const handleAddToCart = () => {
    // Add logic to add book to cart
    alert(`Added ${quantity} copy/copies of "${book.title}" to cart`);
    // In a real application, you would dispatch an action or call an API
  };

  const handleQuantityChange = (e) => {
    setQuantity(parseInt(e.target.value));
  };

  const goBack = () => {
    navigate(-1);
  };

  if (loading) {
    return <div className="loading">Loading book details...</div>;
  }

  if (!book) {
    return <div className="error-message">Book not found!</div>;
  }

  return (
    <div className="book-detail-page">
      <div className="container">
        <button className="back-button" onClick={goBack}>
          <i className="fas fa-arrow-left"></i> Back
        </button>
        
        <div className="book-detail">
          <div className="book-image">
            <img src={book.image || '/default-book-cover.jpg'} alt={book.title} />
          </div>
          
          <div className="book-info">
            <h1>{book.title}</h1>
            <p className="author">by <span>{book.author}</span></p>
            <p className="category">{book.category}</p>
            <div className="price">{book.price.toLocaleString('vi-VN')} VNĐ</div>
            
            <div className="book-stats">
              <div className="stat">
                <span className="label">Stock:</span>
                <span className="value">{book.stock}</span>
              </div>
            </div>
            
            <div className="description">
              <h3>Description</h3>
              <p>{book.description || 'No description available.'}</p>
            </div>
            
            <div className="add-to-cart">
              <div className="quantity-selector">
                <label htmlFor="quantity">Quantity:</label>
                <select
                  id="quantity"
                  value={quantity}
                  onChange={handleQuantityChange}
                  disabled={book.stock === 0}
                >
                  {[...Array(Math.min(book.stock, 10))].map((_, i) => (
                    <option key={i + 1} value={i + 1}>
                      {i + 1}
                    </option>
                  ))}
                </select>
              </div>
              
              <button
                className="add-to-cart-button"
                onClick={handleAddToCart}
                disabled={book.stock === 0}
              >
                {book.stock === 0 ? 'Out of Stock' : 'Add to Cart'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookDetailPage;
