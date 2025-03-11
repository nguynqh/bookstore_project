import React, { useState, useEffect } from 'react';
import './CategoriesPage.css';

const CategoriesPage = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newCategory, setNewCategory] = useState('');

  // API sẽ viết ở đây
  useEffect(() => {
    // API sẽ viết ở đây
    setTimeout(() => {
      const sampleCategories = [
        { id: 1, name: 'Fiction', booksCount: 120 },
        { id: 2, name: 'Non-Fiction', booksCount: 85 },
        { id: 3, name: 'Programming', booksCount: 34 },
        { id: 4, name: 'Science', booksCount: 56 },
        { id: 5, name: 'History', booksCount: 42 }
      ];
      setCategories(sampleCategories);
      setLoading(false);
    }, 800);
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (newCategory.trim()) {
      // API sẽ viết ở đây
      const newCategoryObj = {
        id: Date.now(),
        name: newCategory,
        booksCount: 0
      };
      setCategories([...categories, newCategoryObj]);
      setNewCategory('');
    }
  };

  const handleDelete = (id) => {
     // API sẽ viết ở đây
    setCategories(categories.filter(category => category.id !== id));
  };

  return (
    <div className="categories-page">
      <div className="container">
        <div className="page-header">
          <h1>Categories Management</h1>
        </div>
        
        <div className="category-form">
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <input
                type="text"
                placeholder="Enter new category name"
                value={newCategory}
                onChange={(e) => setNewCategory(e.target.value)}
                required
              />
              <button type="submit" className="btn-add">Add Category</button>
            </div>
          </form>
        </div>
        
        {loading ? (
          <div className="loading">Loading categories...</div>
        ) : (
          <div className="categories-grid">
            {categories.map(category => (
              <div key={category.id} className="category-card">
                <div className="category-info">
                  <h2>{category.name}</h2>
                  <span className="books-count">{category.booksCount} books</span>
                </div>
                <div className="category-actions">
                  <button className="btn-edit" title="Edit category">
                    <i className="fas fa-edit"></i>
                  </button>
                  <button 
                    className="btn-delete" 
                    title="Delete category"
                    onClick={() => handleDelete(category.id)}
                  >
                    <i className="fas fa-trash"></i>
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default CategoriesPage;
