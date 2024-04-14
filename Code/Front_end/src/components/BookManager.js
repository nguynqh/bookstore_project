import React, { useState, useEffect } from 'react';
import { getAllBooks, addBook, updateBook, deleteBook, searchBooks } from '../services/bookService';

const BookManager = () => {
  const [books, setBooks] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [editingBook, setEditingBook] = useState(null);
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    category: '',
    price: '',
    stock: '',
    image: '',
    description: ''
  });

  useEffect(() => {
    setBooks(getAllBooks());
  }, []);

  const handleSearch = (e) => {
    const query = e.target.value;
    setSearchQuery(query);
    if (query) {
      setBooks(searchBooks(query));
    } else {
      setBooks(getAllBooks());
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (editingBook) {
      const updated = updateBook(editingBook.id, formData);
      if (updated) {
        setBooks(getAllBooks());
        setEditingBook(null);
      }
    } else {
      const added = addBook(formData);
      if (added) {
        setBooks(getAllBooks());
      }
    }
    setFormData({
      title: '',
      author: '',
      category: '',
      price: '',
      stock: '',
      image: '',
      description: ''
    });
  };

  const handleEdit = (book) => {
    setEditingBook(book);
    setFormData(book);
  };

  const handleDelete = (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa cuốn sách này?')) {
      const deleted = deleteBook(id);
      if (deleted) {
        setBooks(getAllBooks());
      }
    }
  };

  return (
    <div className="container">
      <h2 className="page-title">Quản lý sách</h2>
      
      <div className="search-bar">
        <input
          type="text"
          placeholder="Tìm kiếm sách..."
          value={searchQuery}
          onChange={handleSearch}
        />
      </div>

      <form onSubmit={handleSubmit} className="book-form">
        <h3>{editingBook ? 'Chỉnh sửa sách' : 'Thêm sách mới'}</h3>
        <div className="form-group">
          <input
            type="text"
            name="title"
            placeholder="Tên sách"
            value={formData.title}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            name="author"
            placeholder="Tác giả"
            value={formData.author}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            name="category"
            placeholder="Thể loại"
            value={formData.category}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <input
            type="number"
            name="price"
            placeholder="Giá"
            value={formData.price}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <input
            type="number"
            name="stock"
            placeholder="Số lượng"
            value={formData.stock}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            name="image"
            placeholder="URL hình ảnh"
            value={formData.image}
            onChange={handleInputChange}
            required
          />
        </div>
        <div className="form-group">
          <textarea
            name="description"
            placeholder="Mô tả"
            value={formData.description}
            onChange={handleInputChange}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary">
          {editingBook ? 'Cập nhật' : 'Thêm sách'}
        </button>
        {editingBook && (
          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => {
              setEditingBook(null);
              setFormData({
                title: '',
                author: '',
                category: '',
                price: '',
                stock: '',
                image: '',
                description: ''
              });
            }}
          >
            Hủy
          </button>
        )}
      </form>

      <div className="book-grid">
        {books.map(book => (
          <div key={book.id} className="book-card">
            <img src={book.image} alt={book.title} />
            <div className="book-card-body">
              <h3>{book.title}</h3>
              <p className="author">{book.author}</p>
              <p className="price">{book.price.toLocaleString('vi-VN')} VNĐ</p>
              <p>Kho: {book.stock}</p>
              <div className="actions">
                <button
                  className="btn btn-edit"
                  onClick={() => handleEdit(book)}
                >
                  Sửa
                </button>
                <button
                  className="btn btn-delete"
                  onClick={() => handleDelete(book.id)}
                >
                  Xóa
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default BookManager; 