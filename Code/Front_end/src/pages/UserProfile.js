import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUserByUsername, updateUser } from '../services/userApi';
import './UserProfile.css';

const TABS = [
  { key: 'personal', label: 'Personal Information', icon: 'fas fa-user' },
  { key: 'orders', label: 'Order History', icon: 'fas fa-box' },
  { key: 'security', label: 'Security', icon: 'fas fa-lock' },
];

const UserProfile = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('personal');
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({ fullName: '', email: '', avatar: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Giả lập lấy username từ localStorage/session (hoặc context)
  const username = localStorage.getItem('username');

  useEffect(() => {
    if (!username) {
      navigate('/login');
      return;
    }
    getUserByUsername(username)
      .then(data => {
        setUser(data);
        setFormData({
          fullName: data.fullName || '',
          email: data.email || '',
          avatar: data.avatar || ''
        });
      })
      .catch(() => navigate('/login'));
    // eslint-disable-next-line
  }, [username]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      const updated = await updateUser(user.id, formData);
      setUser(updated);
      setSuccess('Profile updated successfully');
      setIsEditing(false);
    } catch (err) {
      setError('Failed to update profile');
    }
  };

  if (!user) return null;

  return (
    <div className="profile-page">
      <div className="profile-main-layout-vertical">
     
        <div className="profile-header-horizontal">
          {/* <div className="profile-avatar-horizontal">
            <img src={user.avatar} alt={user.fullName} />
          </div> */}
          <div className="profile-header-info">
            <div className="profile-header-name">{user.fullName}</div>
            <div className="profile-header-email">{user.email}</div>
          </div>
        </div>
        {/* Tab menu ngang */}
        <nav className="profile-navbar-horizontal">
          {TABS.map(tab => (
            <button
              key={tab.key}
              className={`profile-navbar-tab${activeTab === tab.key ? ' active' : ''}`}
              onClick={() => {
                setActiveTab(tab.key);
                setIsEditing(false);
                setError('');
                setSuccess('');
              }}
            >
              <i className={tab.icon}></i>
              <span>{tab.label}</span>
            </button>
          ))}
        </nav>
        {/* Nội dung tab */}
        <div className="profile-content-area-horizontal">
          {activeTab === 'personal' && (
            <div className="tab-content">
              <h2 className="tab-title">Personal Information</h2>
              {error && <div className="error-message">{error}</div>}
              {success && <div className="success-message">{success}</div>}
              {isEditing ? (
                <form onSubmit={handleSubmit} className="profile-form">
                  <div className="form-group">
                    <label htmlFor="fullName">Full Name</label>
                    <input
                      type="text"
                      id="fullName"
                      name="fullName"
                      value={formData.fullName}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="avatar">Avatar URL</label>
                    <input
                      type="url"
                      id="avatar"
                      name="avatar"
                      value={formData.avatar}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  <div className="form-actions">
                    <button type="submit" className="save-btn">Save Changes</button>
                    <button
                      type="button"
                      className="cancel-btn"
                      onClick={() => {
                        setIsEditing(false);
                        setFormData({
                          fullName: user.fullName,
                          email: user.email,
                          avatar: user.avatar
                        });
                      }}
                    >
                      Cancel
                    </button>
                  </div>
                </form>
              ) : (
                <div className="profile-info-view">
                  <div className="info-row">
                    <span className="info-label">Full Name:</span>
                    <span className="info-value">{user.fullName}</span>
                  </div>
                  <div className="info-row">
                    <span className="info-label">Email:</span>
                    <span className="info-value">{user.email}</span>
                  </div>
                  <div className="info-row">
                    <span className="info-label">Role:</span>
                    <span className="info-value">{user.role === 'admin' ? 'Administrator' : 'User'}</span>
                  </div>
                  <button className="edit-btn" onClick={() => setIsEditing(true)}>
                    <i className="fas fa-edit"></i> Edit
                  </button>
                </div>
              )}
            </div>
          )}
          {activeTab === 'orders' && (
            <div className="tab-content">
              <h2 className="tab-title">Order History</h2>
              {user.orders && user.orders.length > 0 ? (
                <div className="orders-list">
                  {user.orders.map((order, index) => (
                    <div key={index} className="order-item">
                      <span className="order-id">Order #{order.id}</span>
                      <span className="order-date">{new Date(order.date).toLocaleDateString()}</span>
                      <span className="order-total">${order.total}</span>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="no-orders">No orders yet</p>
              )}
            </div>
          )}
          {activeTab === 'security' && (
            <div className="tab-content">
              <h2 className="tab-title">Security</h2>
              <div className="security-info">
                <p><strong>Username:</strong> {user.username}</p>
                <p><strong>Role:</strong> {user.role === 'admin' ? 'Administrator' : 'User'}</p>
                <p>For password or security changes, please contact support.</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserProfile; 