import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './UserProfilePage.css';

const UserProfilePage = () => {
  const [activeTab, setActiveTab] = useState('personal');
  const [userData, setUserData] = useState(null);
  const [orderHistory, setOrderHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    address: '',
    city: '',
    state: '',
    zip: '',
    country: ''
  });
  const [isEditing, setIsEditing] = useState(false);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    // Simulate API call to get user data
    setTimeout(() => {
      const mockUserData = {
        id: 1,
        name: 'John Doe',
        email: 'john.doe@example.com',
        phone: '(555) 123-4567',
        address: '123 Book St',
        city: 'Reading',
        state: 'CA',
        zip: '90210',
        country: 'USA',
        avatar: null
      };
      
      const mockOrderHistory = [
        {
          id: 'ORD-1234',
          date: '2023-04-15',
          total: 54.97,
          status: 'Delivered',
          items: [
            { id: 1, title: 'The Great Gatsby', quantity: 1, price: 12.99 },
            { id: 2, title: 'To Kill a Mockingbird', quantity: 2, price: 10.99 },
            { id: 3, title: '1984', quantity: 1, price: 9.99 }
          ]
        },
        {
          id: 'ORD-5678',
          date: '2023-03-22',
          total: 32.95,
          status: 'Delivered',
          items: [
            { id: 4, title: 'Pride and Prejudice', quantity: 1, price: 8.99 },
            { id: 5, title: 'The Catcher in the Rye', quantity: 1, price: 11.99 },
            { id: 6, title: 'Animal Farm', quantity: 1, price: 7.99 }
          ]
        }
      ];
      
      setUserData(mockUserData);
      setOrderHistory(mockOrderHistory);
      setFormData({
        name: mockUserData.name,
        email: mockUserData.email,
        phone: mockUserData.phone || '',
        address: mockUserData.address || '',
        city: mockUserData.city || '',
        state: mockUserData.state || '',
        zip: mockUserData.zip || '',
        country: mockUserData.country || ''
      });
      setLoading(false);
    }, 1000);
  }, []);

  const handleTabChange = (tab) => {
    setActiveTab(tab);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
    
    // Clear errors when typing
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: undefined
      });
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.name.trim()) newErrors.name = 'Name is required';
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleEditToggle = () => {
    if (isEditing) {
      // Reset form data to original user data
      setFormData({
        name: userData.name,
        email: userData.email,
        phone: userData.phone || '',
        address: userData.address || '',
        city: userData.city || '',
        state: userData.state || '',
        zip: userData.zip || '',
        country: userData.country || ''
      });
      setErrors({});
    }
    setIsEditing(!isEditing);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    setLoading(true);
    
    // Simulate API call to update user data
    setTimeout(() => {
      // Update userData with formData
      setUserData({
        ...userData,
        ...formData
      });
      setIsEditing(false);
      setLoading(false);
      
      alert('Profile updated successfully!');
    }, 1000);
  };

  if (loading && !userData) {
    return <div className="loading">Loading user profile...</div>;
  }

  return (
    <div className="user-profile-page">
      <div className="container">
        <h1>My Account</h1>
        
        <div className="profile-content">
          <div className="profile-sidebar">
            <div className="user-avatar">
              {userData.avatar ? (
                <img src={userData.avatar} alt={userData.name} />
              ) : (
                <div className="avatar-placeholder">
                  {userData.name.charAt(0)}
                </div>
              )}
              <h3>{userData.name}</h3>
              <p>{userData.email}</p>
            </div>
            
            <nav className="profile-nav">
              <button
                className={`nav-item ${activeTab === 'personal' ? 'active' : ''}`}
                onClick={() => handleTabChange('personal')}
              >
                <i className="fas fa-user"></i> Personal Information
              </button>
              <button
                className={`nav-item ${activeTab === 'orders' ? 'active' : ''}`}
                onClick={() => handleTabChange('orders')}
              >
                <i className="fas fa-shopping-bag"></i> Order History
              </button>
              <button
                className={`nav-item ${activeTab === 'security' ? 'active' : ''}`}
                onClick={() => handleTabChange('security')}
              >
                <i className="fas fa-lock"></i> Security
              </button>
            </nav>
          </div>
          
          <div className="profile-main">
            {activeTab === 'personal' && (
              <div className="tab-content">
                <div className="tab-header">
                  <h2>Personal Information</h2>
                  <button 
                    className="edit-btn" 
                    onClick={handleEditToggle}
                    disabled={loading}
                  >
                    {isEditing ? 'Cancel' : 'Edit'}
                  </button>
                </div>
                
                <form onSubmit={handleSubmit} className="profile-form">
                  <div className="form-row">
                    <div className="form-group">
                      <label htmlFor="name">Full Name</label>
                      <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        disabled={!isEditing}
                        className={errors.name ? 'error' : ''}
                      />
                      {errors.name && <div className="error-message">{errors.name}</div>}
                    </div>
                    
                    <div className="form-group">
                      <label htmlFor="email">Email</label>
                      <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        disabled={!isEditing}
                        className={errors.email ? 'error' : ''}
                      />
                      {errors.email && <div className="error-message">{errors.email}</div>}
                    </div>
                  </div>
                  
                  <div className="form-row">
                    <div className="form-group">
                      <label htmlFor="phone">Phone Number</label>
                      <input
                        type="text"
                        id="phone"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>
                  
                  <h3>Shipping Address</h3>
                  
                  <div className="form-group">
                    <label htmlFor="address">Street Address</label>
                    <input
                      type="text"
                      id="address"
                      name="address"
                      value={formData.address}
                      onChange={handleChange}
                      disabled={!isEditing}
                    />
                  </div>
                  
                  <div className="form-row">
                    <div className="form-group">
                      <label htmlFor="city">City</label>
                      <input
                        type="text"
                        id="city"
                        name="city"
                        value={formData.city}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                    
                    <div className="form-group">
                      <label htmlFor="state">State/Province</label>
                      <input
                        type="text"
                        id="state"
                        name="state"
                        value={formData.state}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>
                  
                  <div className="form-row">
                    <div className="form-group">
                      <label htmlFor="zip">Zip/Postal Code</label>
                      <input
                        type="text"
                        id="zip"
                        name="zip"
                        value={formData.zip}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                    
                    <div className="form-group">
                      <label htmlFor="country">Country</label>
                      <input
                        type="text"
                        id="country"
                        name="country"
                        value={formData.country}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>
                  
                  {isEditing && (
                    <div className="form-actions">
                      <button 
                        type="submit" 
                        className="save-btn"
                        disabled={loading}
                      >
                        {loading ? 'Saving...' : 'Save Changes'}
                      </button>
                    </div>
                  )}
                </form>
              </div>
            )}
            
            {activeTab === 'orders' && (
              <div className="tab-content">
                <h2>Order History</h2>
                
                {orderHistory.length === 0 ? (
                  <div className="empty-state">
                    <p>You haven't placed any orders yet.</p>
                    <Link to="/books" className="browse-books-btn">
                      Browse Books
                    </Link>
                  </div>
                ) : (
                  <div className="order-list">
                    {orderHistory.map(order => (
                      <div key={order.id} className="order-card">
                        <div className="order-header">
                          <div className="order-info">
                            <div className="order-number">
                              <span>Order #:</span> {order.id}
                            </div>
                            <div className="order-date">
                              <span>Date:</span> {order.date}
                            </div>
                          </div>
                          <div className="order-status">
                            <span className={`status-badge ${order.status.toLowerCase()}`}>
                              {order.status}
                            </span>
                          </div>
                        </div>
                        
                        <div className="order-items">
                          <h4>Items</h4>
                          <table className="items-table">
                            <thead>
                              <tr>
                                <th>Book Title</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Subtotal</th>
                              </tr>
                            </thead>
                            <tbody>
                              {order.items.map(item => (
                                <tr key={item.id}>
                                  <td>{item.title}</td>
                                  <td>{item.quantity}</td>
                                  <td>${item.price.toFixed(2)}</td>
                                  <td>${(item.price * item.quantity).toFixed(2)}</td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </div>
                        
                        <div className="order-footer">
                          <div className="order-total">
                            <span>Total:</span> ${order.total.toFixed(2)}
                          </div>
                          <Link to={`/order/${order.id}`} className="view-details-btn">
                            View Details
                          </Link>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}
            
            {activeTab === 'security' && (
              <div className="tab-content">
                <h2>Security Settings</h2>
                
                <div className="security-section">
                  <h3>Change Password</h3>
                  
                  <form className="change-password-form">
                    <div className="form-group">
                      <label htmlFor="currentPassword">Current Password</label>
                      <input
                        type="password"
                        id="currentPassword"
                        name="currentPassword"
                      />
                    </div>
                    
                    <div className="form-group">
                      <label htmlFor="newPassword">New Password</label>
                      <input
                        type="password"
                        id="newPassword"
                        name="newPassword"
                      />
                      <div className="password-requirements">
                        <p>Password must contain:</p>
                        <ul>
                          <li>At least 8 characters</li>
                          <li>At least one uppercase letter</li>
                          <li>At least one number</li>
                          <li>At least one special character</li>
                        </ul>
                      </div>
                    </div>
                    
                    <div className="form-group">
                      <label htmlFor="confirmPassword">Confirm New Password</label>
                      <input
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                      />
                    </div>
                    
                    <button type="submit" className="change-password-btn">
                      Change Password
                    </button>
                  </form>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfilePage;
