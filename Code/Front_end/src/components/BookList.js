import React from 'react';
import './BookList.css';

const BookList = ({ books }) => {
  if (books.length === 0) {
    return <div className="no-books">No books found. Add some books to your inventory.</div>;
  }

  return (
    <div className="book-list">
      <table>
        <thead>
          <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map(book => (
            <tr key={book.id}>
              <td>{book.title}</td>
              <td>{book.author}</td>
              <td>{book.category}</td>
              <td>${book.price.toFixed(2)}</td>
              <td>{book.stock}</td>
              <td className="actions">
                <button className="btn-view" title="View details">
                  <i className="fas fa-eye"></i>
                </button>
                <button className="btn-edit" title="Edit book">
                  <i className="fas fa-edit"></i>
                </button>
                <button className="btn-delete" title="Delete book">
                  <i className="fas fa-trash"></i>
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default BookList;
