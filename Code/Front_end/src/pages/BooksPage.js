import React, { useState, useEffect } from 'react';
import BookList from '../components/BookList';
import './BooksPage.css';
import allBooks from '../data/books'; // Nhập sách từ file dữ liệu của chúng ta

const BooksPage = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  
  // Trạng thái phân trang
  const [currentPage, setCurrentPage] = useState(1);
  const [booksPerPage] = useState(10);

  useEffect(() => {
    // Set độ trễ khi gọi API
    setTimeout(() => {
      setBooks(allBooks);
      setLoading(false);
    }, 1000);
  }, []);

  // Lọc sách dựa trên từ khóa tìm kiếm
  const filteredBooks = books.filter(book => 
    book.title.toLowerCase().includes(searchTerm.toLowerCase()) || 
    book.author.toLowerCase().includes(searchTerm.toLowerCase()) ||
    book.category.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // lấy sách
  const indexOfLastBook = currentPage * booksPerPage;
  const indexOfFirstBook = indexOfLastBook - booksPerPage;
  const currentBooks = filteredBooks.slice(indexOfFirstBook, indexOfLastBook);

  // Thay đổi trang
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  // Tính tổng số trang
  const totalPages = Math.ceil(filteredBooks.length / booksPerPage);

  return (
    <div className="books-page">
      <div className="container">
        <div className="page-header">
          <h1>Books Management</h1>
          <button className="btn-add">
            <i className="fas fa-plus"></i> Add New Book
          </button>
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
        ) : (
          <>
            <BookList books={currentBooks} />
            
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
