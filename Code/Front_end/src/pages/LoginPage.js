import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login, getCurrentUser, logout } from '../services/authService';
import { registerUser } from '../services/userApi';
import './LoginPage.css';

const LoginPage = () => {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    fullName: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [showToast, setShowToast] = useState(false);
  const [toastMsg, setToastMsg] = useState('');
  const currentUser = getCurrentUser();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (isLogin) {
      // Xử lý đăng nhập
      const user = await login(formData.username, formData.password);
      if (user) {
        setToastMsg('Đăng nhập thành công!');
        setShowToast(true);
        setTimeout(() => {
          setShowToast(false);
          navigate('/');
        }, 1200);
      } else {
        setError('Invalid username or password');
      }
    } else {
      // Xử lý đăng ký
      if (formData.password !== formData.confirmPassword) {
        setError('Passwords do not match');
        return;
      }
      try {
        const result = await registerUser({
          username: formData.username,
          password: formData.password,
          email: formData.email,
          fullName: formData.fullName
        });
        if (result && result.username) {
          setToastMsg('Đăng ký thành công!');
          setShowToast(true);
          setTimeout(() => {
            setShowToast(false);
            setIsLogin(true);
            setFormData({
              username: '', password: '', email: '', fullName: '', confirmPassword: ''
            });
          }, 1200);
        } else {
          setError('Registration failed');
        }
      } catch (err) {
        setError(err.message || 'Registration failed');
      }
    }
  };

  const toggleForm = () => {
    setIsLogin(!isLogin);
    setError('');
    setFormData({
      username: '',
      password: '',
      email: '',
      fullName: '',
      confirmPassword: ''
    });
  };

  // Xử lý logout toast (nếu chuyển về login từ nơi khác)
  React.useEffect(() => {
    if (window.location.pathname === '/login' && window.history.state && window.history.state.logout) {
      setToastMsg('Đăng xuất thành công!');
      setShowToast(true);
      setTimeout(() => setShowToast(false), 1200);
    }
  }, []);

  if (currentUser) {
    return (
      <div className="login-page">
        <div className="container">
          <div className="login-box">
            <div className="welcome-message">
              <h3>Welcome back!</h3>
              <p>{currentUser.fullName}</p>
            </div>
            <button 
              className="logout-btn"
              onClick={() => {
                logout();
                setToastMsg('Đăng xuất thành công!');
                setShowToast(true);
                setTimeout(() => {
                  setShowToast(false);
                  window.location.href = '/login';
                }, 1200);
              }}
            >
              Logout
            </button>
            {showToast && <div className="toast-message">{toastMsg}</div>}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="login-page">
      <div className="container">
        <div className="login-box">
          <h2>{isLogin ? 'Welcome Back' : 'Create Account'}</h2>
          {error && <div className="error-message">{error}</div>}
          {showToast && <div className="toast-message">{toastMsg}</div>}
          <form onSubmit={handleSubmit}>
            {!isLogin && (
              <>
                <div className="form-group">
                  <label htmlFor="fullName">Full Name</label>
                  <input
                    type="text"
                    id="fullName"
                    name="fullName"
                    value={formData.fullName}
                    onChange={handleChange}
                    required
                    placeholder="Enter your full name"
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
                    placeholder="Enter your email"
                  />
                </div>
              </>
            )}
            <div className="form-group">
              <label htmlFor="username">Username</label>
              <input
                type="text"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
                placeholder="Enter your username"
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                placeholder="Enter your password"
              />
            </div>
            {!isLogin && (
              <div className="form-group">
                <label htmlFor="confirmPassword">Confirm Password</label>
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  required
                  placeholder="Confirm your password"
                />
              </div>
            )}
            <button type="submit" className="login-btn">
              {isLogin ? 'Sign In' : 'Create Account'}
            </button>
          </form>
          <div className="form-footer">
            <p>
              {isLogin ? "Don't have an account?" : 'Already have an account?'}
              <button type="button" className="toggle-btn" onClick={toggleForm}>
                {isLogin ? 'Sign Up' : 'Sign In'}
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
