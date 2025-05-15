import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './CartPage.css';

const CartPage = () => {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulate fetching cart data
    setTimeout(() => {
      // Example cart data - in a real app, this would come from state management or API
      const mockCartItems = [
        {
          id: 1,
          title: 'The Great Gatsby',
          author: 'F. Scott Fitzgerald',
          price: 12.99,
          quantity: 1,
          coverImage: '/images/great-gatsby.jpg'
        },
        {
          id: 2,
          title: 'To Kill a Mockingbird',
          author: 'Harper Lee',
          price: 10.95,
          quantity: 2,
          coverImage: '/images/mockingbird.jpg'
        }
      ];
      
      setCartItems(mockCartItems);
      setLoading(false);
    }, 800);
  }, []);

  const updateQuantity = (id, newQuantity) => {
    if (newQuantity < 1) return;
    
    setCartItems(cartItems.map(item => 
      item.id === id ? { ...item, quantity: newQuantity } : item
    ));
  };

  const removeItem = (id) => {
    setCartItems(cartItems.filter(item => item.id !== id));
  };

  const calculateSubtotal = () => {
    return cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
  };

  const calculateTax = () => {
    return calculateSubtotal() * 0.1; // 10% tax example
  };

  const calculateTotal = () => {
    return calculateSubtotal() + calculateTax();
  };

  const handleCheckout = () => {
    // Navigate to checkout page
    navigate('/checkout');
  };

  if (loading) {
    return <div className="loading">Loading your cart...</div>;
  }

  if (cartItems.length === 0) {
    return (
      <div className="empty-cart-container">
        <div className="empty-cart">
          <i className="fas fa-shopping-cart empty-cart-icon"></i>
          <h2>Your cart is empty</h2>
          <p>Looks like you haven't added anything to your cart yet.</p>
          <Link to="/books" className="continue-shopping-btn">
            Continue Shopping
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <div className="container">
        <h1>Your Shopping Cart</h1>
        
        <div className="cart-content">
          <div className="cart-items">
            {cartItems.map(item => (
              <div key={item.id} className="cart-item">
                <div className="item-image">
                  <img 
                    src={item.coverImage || '/default-book-cover.jpg'} 
                    alt={item.title} 
                  />
                </div>
                
                <div className="item-details">
                  <h3>{item.title}</h3>
                  <p className="item-author">by {item.author}</p>
                  <p className="item-price">${item.price.toFixed(2)}</p>
                </div>
                
                <div className="item-quantity">
                  <button 
                    className="quantity-btn"
                    onClick={() => updateQuantity(item.id, item.quantity - 1)}
                  >
                    -
                  </button>
                  <span>{item.quantity}</span>
                  <button 
                    className="quantity-btn"
                    onClick={() => updateQuantity(item.id, item.quantity + 1)}
                  >
                    +
                  </button>
                </div>
                
                <div className="item-total">
                  ${(item.price * item.quantity).toFixed(2)}
                </div>
                
                <button 
                  className="remove-item-btn"
                  onClick={() => removeItem(item.id)}
                >
                  <i className="fas fa-trash"></i>
                </button>
              </div>
            ))}
          </div>
          
          <div className="cart-summary">
            <h2>Order Summary</h2>
            
            <div className="summary-row">
              <span>Subtotal</span>
              <span>${calculateSubtotal().toFixed(2)}</span>
            </div>
            
            <div className="summary-row">
              <span>Tax (10%)</span>
              <span>${calculateTax().toFixed(2)}</span>
            </div>
            
            <div className="summary-row total">
              <span>Total</span>
              <span>${calculateTotal().toFixed(2)}</span>
            </div>
            
            <button className="checkout-btn" onClick={handleCheckout}>
              Proceed to Checkout
            </button>
            
            <Link to="/books" className="continue-shopping">
              <i className="fas fa-arrow-left"></i> Continue Shopping
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage;
