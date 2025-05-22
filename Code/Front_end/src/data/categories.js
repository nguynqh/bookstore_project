import booksData from './books.json';

const categories = Array.from(
  new Set(booksData.books.map((book) => book.category))
).map((category, index) => ({ id: index + 1, name: category }));

export default categories;
