import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import allBooks from '../data/books';
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
      const foundBook = allBooks.find(book => book.id === parseInt(id));
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
            <img src={book.coverImage || '/default-book-cover.jpg'} alt={book.title} />
          </div>
          
          <div className="book-info">
            <h1>{book.title}</h1>
            <p className="author">by <span>{book.author}</span></p>
            <p className="category">{book.category}</p>
            <div className="price">${book.price.toFixed(2)}</div>
            
            <div className="book-stats">
              <div className="stat">
                <span className="label">ISBN:</span>
                <span className="value">{book.isbn || 'N/A'}</span>
              </div>
              <div className="stat">
                <span className="label">Published:</span>
                <span className="value">{book.publishedDate || 'N/A'}</span>
              </div>
              <div className="stat">
                <span className="label">Pages:</span>
                <span className="value">{book.pages || 'N/A'}</span>
              </div>
              <div className="stat">
                <span className="label">Available:</span>
                <span className="value">{book.inStock ? 'Yes' : 'No'}</span>
              </div>
            </div>
            
            <div className="description">
              <h3>Description</h3>
              <p>{book.description || 'No description available.'}</p>
            </div>
            
            <div className="purchase-options">
              <div className="quantity-selector">
                <label htmlFor="quantity">Quantity:</label>
                <select 
                  id="quantity" 
                  value={quantity} 
                  onChange={handleQuantityChange}
                  disabled={!book.inStock}
                >
                  {[...Array(10).keys()].map(num => (
                    <option key={num + 1} value={num + 1}>{num + 1}</option>
                  ))}
                </select>
              </div>
              
              <button 
                className="add-to-cart-btn" 
                onClick={handleAddToCart}
                disabled={!book.inStock}
              >
                <i className="fas fa-shopping-cart"></i> Add to Cart
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookDetailPage;
