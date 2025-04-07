import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import allBooks from '../data/books';
import './AddEditBookPage.css';

const AddEditBookPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditMode = !!id;
  
  const initialFormState = {
    title: '',
    author: '',
    description: '',
    isbn: '',
    publishedDate: '',
    publisher: '',
    category: '',
    price: '',
    pages: '',
    language: 'English',
    inStock: true,
    coverImage: ''
  };
  
  const [formData, setFormData] = useState(initialFormState);
  const [loading, setLoading] = useState(isEditMode);
  const [errors, setErrors] = useState({});
  const [previewImage, setPreviewImage] = useState('');
  
  useEffect(() => {
    if (isEditMode) {
      // Simulate fetching book data for editing
      setTimeout(() => {
        const bookToEdit = allBooks.find(book => book.id === parseInt(id));
        if (bookToEdit) {
          setFormData({
            title: bookToEdit.title || '',
            author: bookToEdit.author || '',
            description: bookToEdit.description || '',
            isbn: bookToEdit.isbn || '',
            publishedDate: bookToEdit.publishedDate || '',
            publisher: bookToEdit.publisher || '',
            category: bookToEdit.category || '',
            price: bookToEdit.price ? bookToEdit.price.toString() : '',
            pages: bookToEdit.pages ? bookToEdit.pages.toString() : '',
            language: bookToEdit.language || 'English',
            inStock: bookToEdit.inStock !== undefined ? bookToEdit.inStock : true,
            coverImage: bookToEdit.coverImage || ''
          });
          setPreviewImage(bookToEdit.coverImage || '');
        }
        setLoading(false);
      }, 800);
    }
  }, [id, isEditMode]);
  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value
    });
    
    // Clear error when user starts typing again
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: undefined
      });
    }
  };
  
  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    
    // In a real application, you would upload the file to a server
    // Here we just create a local preview
    const reader = new FileReader();
    reader.onloadend = () => {
      setPreviewImage(reader.result);
      setFormData({
        ...formData,
        coverImage: reader.result
      });
    };
    reader.readAsDataURL(file);
  };
  
  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.title.trim()) newErrors.title = 'Title is required';
    if (!formData.author.trim()) newErrors.author = 'Author is required';
    if (!formData.category.trim()) newErrors.category = 'Category is required';
    
    if (!formData.price.trim()) {
      newErrors.price = 'Price is required';
    } else if (isNaN(formData.price) || parseFloat(formData.price) <= 0) {
      newErrors.price = 'Price must be a positive number';
    }
    
    if (formData.pages && (isNaN(formData.pages) || parseInt(formData.pages) <= 0)) {
      newErrors.pages = 'Pages must be a positive number';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      // Scroll to first error
      const firstError = document.querySelector('.error-message');
      if (firstError) {
        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
      return;
    }
    
    // Simulate saving the book
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      
      // In a real application, you would make an API call here
      alert(`Book ${isEditMode ? 'updated' : 'added'} successfully!`);
      navigate('/books');
    }, 1000);
  };
  
  const handleCancel = () => {
    navigate('/books');
  };
  
  if (loading && isEditMode) {
    return <div className="loading">Loading book data...</div>;
  }
  
  return (
    <div className="add-edit-book-page">
      <div className="container">
        <h1>{isEditMode ? 'Edit Book' : 'Add New Book'}</h1>
        
        <form onSubmit={handleSubmit} className="book-form">
          <div className="form-grid">
            <div className="form-column">
              <div className="form-group">
                <label htmlFor="title">Title *</label>
                <input
                  type="text"
                  id="title"
                  name="title"
                  value={formData.title}
                  onChange={handleChange}
                  className={errors.title ? 'error' : ''}
                />
                {errors.title && <div className="error-message">{errors.title}</div>}
              </div>
              
              <div className="form-group">
                <label htmlFor="author">Author *</label>
                <input
                  type="text"
                  id="author"
                  name="author"
                  value={formData.author}
                  onChange={handleChange}
                  className={errors.author ? 'error' : ''}
                />
                {errors.author && <div className="error-message">{errors.author}</div>}
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="category">Category *</label>
                  <input
                    type="text"
                    id="category"
                    name="category"
                    value={formData.category}
                    onChange={handleChange}
                    className={errors.category ? 'error' : ''}
                  />
                  {errors.category && <div className="error-message">{errors.category}</div>}
                </div>
                
                <div className="form-group">
                  <label htmlFor="price">Price ($) *</label>
                  <input
                    type="text"
                    id="price"
                    name="price"
                    value={formData.price}
                    onChange={handleChange}
                    className={errors.price ? 'error' : ''}
                  />
                  {errors.price && <div className="error-message">{errors.price}</div>}
                </div>
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="isbn">ISBN</label>
                  <input
                    type="text"
                    id="isbn"
                    name="isbn"
                    value={formData.isbn}
                    onChange={handleChange}
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="pages">Pages</label>
                  <input
                    type="text"
                    id="pages"
                    name="pages"
                    value={formData.pages}
                    onChange={handleChange}
                    className={errors.pages ? 'error' : ''}
                  />
                  {errors.pages && <div className="error-message">{errors.pages}</div>}
                </div>
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="publishedDate">Published Date</label>
                  <input
                    type="date"
                    id="publishedDate"
                    name="publishedDate"
                    value={formData.publishedDate}
                    onChange={handleChange}
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="publisher">Publisher</label>
                  <input
                    type="text"
                    id="publisher"
                    name="publisher"
                    value={formData.publisher}
                    onChange={handleChange}
                  />
                </div>
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="language">Language</label>
                  <select
                    id="language"
                    name="language"
                    value={formData.language}
                    onChange={handleChange}
                  >
                    <option value="English">English</option>
                    <option value="Spanish">Spanish</option>
                    <option value="French">French</option>
                    <option value="German">German</option>
                    <option value="Chinese">Chinese</option>
                    <option value="Japanese">Japanese</option>
                    <option value="Other">Other</option>
                  </select>
                </div>
                
                <div className="form-group checkbox-group">
                  <label htmlFor="inStock">
                    <input
                      type="checkbox"
                      id="inStock"
                      name="inStock"
                      checked={formData.inStock}
                      onChange={handleChange}
                    />
                    In Stock
                  </label>
                </div>
              </div>
            </div>
            
            <div className="form-column">
              <div className="form-group">
                <label htmlFor="description">Description</label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  rows="6"
                ></textarea>
              </div>
              
              <div className="form-group">
                <label htmlFor="coverImage">Cover Image</label>
                <div className="image-upload-container">
                  <div className="image-preview">
                    {previewImage ? (
                      <img src={previewImage} alt="Book cover preview" />
                    ) : (
                      <div className="no-image">No image</div>
                    )}
                  </div>
                  <input
                    type="file"
                    id="coverImage"
                    accept="image/*"
                    onChange={handleImageUpload}
                  />
                  <p className="help-text">Recommended size: 300x450 pixels</p>
                </div>
              </div>
            </div>
          </div>
          
          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={handleCancel}>
              Cancel
            </button>
            <button type="submit" className="save-btn" disabled={loading}>
              {loading ? 'Saving...' : isEditMode ? 'Update Book' : 'Add Book'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddEditBookPage;
