import books from "./books";

const categories = Array.from(
  new Set(books.map((book) => book.category))
).map((category, index) => ({ id: index + 1, name: category }));

export default categories;
