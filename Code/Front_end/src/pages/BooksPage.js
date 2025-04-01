import React, { useState, useEffect } from 'react';
import BookList from '../components/BookList';
import './BooksPage.css';
import allBooks from '../data/books';
import { useNavigate, useLocation } from 'react-router-dom';

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

  const categories = [...new Set(allBooks.map(book => book.category))];

  useEffect(() => {
    setTimeout(() => {
      setBooks(allBooks);
      setLoading(false);
    }, 1000);
  }, []);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const category = params.get('category');
    if (category) {
      setSelectedCategory(category);
    }
  }, [location]);

  const handleSort = (books) => {
    if (sortOrder === 'A-Z') {
      return books.sort((a, b) => a.title.localeCompare(b.title));
    } else if (sortOrder === 'Z-A') {
      return books.sort((a, b) => b.title.localeCompare(a.title));
    }
    return books;
  };

  const handleFilter = (books) => {
    if (selectedCategory) {
      return books.filter(book => book.category === selectedCategory);
    }
    return books;
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
