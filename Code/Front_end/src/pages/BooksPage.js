import React, { useState, useEffect } from 'react';
import BookList from '../components/BookList';
import './BooksPage.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { getAllBooks, deleteBook } from '../services/bookService';
import { message, Popconfirm } from 'antd';
import { EditOutlined, DeleteOutlined, EyeOutlined } from '@ant-design/icons';

const BooksPage = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [booksPerPage] = useState(10);
  const [sortOrder, setSortOrder] = useState('A-Z');
  const [selectedCategory, setSelectedCategory] = useState('');
  const navigate = useNavigate();
  const location = useLocation();

  // Lấy danh sách sách từ API
  const fetchBooks = async () => {
    try {
      setLoading(true);
      const data = await getAllBooks();
      setBooks(data || []); // Đảm bảo books luôn là một mảng
    } catch (error) {
      console.error('Error fetching books:', error);
      message.error('Lỗi khi tải danh sách sách. Vui lòng kiểm tra kết nối với server.');
      setBooks([]); // Đặt books thành mảng rỗng nếu có lỗi
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBooks();
  }, []);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const category = params.get('category');
    if (category) {
      setSelectedCategory(category);
    }
  }, [location]);

  // Lấy danh sách thể loại từ books, đảm bảo books là mảng
  const categories = books && books.length > 0 
    ? [...new Set(books.map(book => book.category))]
    : [];

  const handleSort = (books) => {
    if (!books || !Array.isArray(books)) return [];
    
    if (sortOrder === 'A-Z') {
      return [...books].sort((a, b) => a.title.localeCompare(b.title));
    } else if (sortOrder === 'Z-A') {
      return [...books].sort((a, b) => b.title.localeCompare(a.title));
    }
    return books;
  };

  const handleFilter = (books) => {
    if (!books || !Array.isArray(books)) return [];
    
    if (selectedCategory) {
      return books.filter(book => book.category === selectedCategory);
    }
    return books;
  };

  const handleDelete = async (id) => {
    try {
      await deleteBook(id);
      message.success('Xóa sách thành công');
      fetchBooks(); // Tải lại danh sách sách sau khi xóa
    } catch (error) {
      console.error('Error deleting book:', error);
      message.error('Lỗi khi xóa sách. Vui lòng thử lại sau.');
    }
  };

  const handleView = (book) => {
    navigate(`/book/${book.id}`);
  };

  const handleEdit = (book) => {
    navigate(`/book/edit/${book.id}`);
  };

  const filteredBooks = handleSort(handleFilter(books)).filter(book =>
    book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    book.author.toLowerCase().includes(searchTerm.toLowerCase()) ||
    book.category.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastBook = currentPage * booksPerPage;
  const indexOfFirstBook = indexOfLastBook - booksPerPage;
  const currentBooks = filteredBooks.slice(indexOfFirstBook, indexOfLastBook);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const totalPages = Math.ceil(filteredBooks.length / booksPerPage);

  const handleAddbooks = () => {
    navigate('/book/add');
  };

  // Cấu hình cột cho bảng
  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Name',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: 'Author',
      dataIndex: 'author',
      key: 'author',
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      render: (price) => `${price.toLocaleString('vi-VN')} VNĐ`,
    },
    {
      title: 'Category',
      dataIndex: 'category',
      key: 'category',
    },
    {
      title: 'Stock',  
      dataIndex: 'stock',
      key: 'stock',
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <div className="action-buttons">
          <button
            className="action-button view"
            onClick={() => handleView(record)}
            title="View"
          >
            <EyeOutlined />
          </button>
          <button
            className="action-button edit"
            onClick={() => handleEdit(record)}
            title="Edit"
          >
            <EditOutlined />
          </button>
          <Popconfirm
            title="Delete book"
            description="Are you sure you want to delete this book?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <button
              className="action-button delete"
              title="Delete"
            >
              <DeleteOutlined />
            </button>
          </Popconfirm>
        </div>
      ),
    },
  ];

  return (
    <div className="books-page">
      <div className="container">
        <div className="page-header">
          <h1>Books Management</h1>
          <button className="btn-add" onClick={handleAddbooks}>
            <i className="fas fa-plus"></i> Add New Book
          </button>
        </div>

        <div className="filter-bar">
          <select value={sortOrder} onChange={(e) => setSortOrder(e.target.value)}>
            <option value="A-Z">Sort A-Z</option>
            <option value="Z-A">Sort Z-A</option>
          </select>
          <select value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)}>
            <option value="">All Categories</option>
            {categories.map(category => (
              <option key={category} value={category}>{category}</option>
            ))}
          </select>
        </div>

        <div className="search-bar">
          <input
            type="text"
            placeholder="Search for books by title, author, or category..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <i className="fas fa-search search-icon"></i>
        </div>

        {loading ? (
          <div className="loading">Loading books...</div>
        ) : books.length === 0 ? (
          <div className="no-books">Không có sách nào. Vui lòng thêm sách mới.</div>
        ) : (
          <>
            <BookList books={currentBooks} columns={columns} />

            <div className="pagination">
              <button
                onClick={() => paginate(currentPage - 1)}
                disabled={currentPage === 1}
                className="pagination-button"
              >
                &laquo; Previous
              </button>

              {Array.from({ length: totalPages }, (_, i) => (
                <button
                  key={i + 1}
                  onClick={() => paginate(i + 1)}
                  className={`pagination-button ${currentPage === i + 1 ? 'active' : ''}`}
                >
                  {i + 1}
                </button>
              ))}

              <button
                onClick={() => paginate(currentPage + 1)}
                disabled={currentPage === totalPages}
                className="pagination-button"
              >
                Next &raquo;
              </button>
            </div>

            <div className="pagination-info">
              Showing {indexOfFirstBook + 1} to {Math.min(indexOfLastBook, filteredBooks.length)} of {filteredBooks.length} books
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default BooksPage;
