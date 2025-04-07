import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Bar, Pie, Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
} from 'chart.js';
import allBooks from '../data/books';
import './HomePage.css';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  PointElement,
  LineElement,
  Tooltip,
  Legend
);

const HomePage = () => {
  const [featuredBooks, setFeaturedBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [chartType, setChartType] = useState('bar');

  const data = {
    labels: ['Books', 'Users', 'Categories'],
    datasets: [
      {
        label: 'Statistics',
        data: [120, 45, 10], // lượng sách lấy tạm chưa biết cách lấy từ data :)))
        backgroundColor: ['#4a90e2', '#50e3c2', '#f5a623'],
        borderColor: ['#357ab8', '#3cbf9e', '#d4881f'],
        borderWidth: 1,
      },
    ],
  };

  const renderChart = () => {
    switch (chartType) {
      case 'bar':
        return <Bar data={data} />;
      case 'pie':
        return <Pie data={data} />;
      case 'line':
        return <Line data={data} />;
      default:
        return null;
    }
  };

  useEffect(() => {
   //set loading books :))
    setTimeout(() => {
      // mày thì học trên mạng lấy có cách random cũng vui vui :))
      const shuffled = [...allBooks].sort(() => 0.5 - Math.random());
      setFeaturedBooks(shuffled.slice(0, 10));
      
      // Extract unique categories
      const uniqueCategories = [...new Set(allBooks.map(book => book.category))];
      setCategories(uniqueCategories);
      
      setLoading(false);
    }, 1000);
  }, []);

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="home-page">
      <div className="hero-section">
        <div className="container">
          <h1>Welcome to Our Bookstore</h1>
          <p>Discover the joy of reading with our vast collection of books</p>
          <Link to="/books" className="browse-button">Browse Books</Link>
        </div>
        <div className="chart-controls">
          <button onClick={() => setChartType('bar')} className={chartType === 'bar' ? 'active' : ''}>
            Bar Chart
          </button>
          <button onClick={() => setChartType('pie')} className={chartType === 'pie' ? 'active' : ''}>
            Pie Chart
          </button>
          <button onClick={() => setChartType('line')} className={chartType === 'line' ? 'active' : ''}>
            Line Chart
          </button>
        </div>
        <div className="chart-container">{renderChart()}</div>
      </div>
      
      <div className="container">
        <section className="featured-section">
          <h2>Featured Books</h2>
          <div className="featured-books">
            {featuredBooks.map(book => (
              <div key={book.id} className="featured-book-card">
                <Link to={`/book/${book.id}`}>
                  <div className="book-cover">
                    <img src={book.coverImage || '/default-book-cover.jpg'} alt={book.title} />
                  </div>
                  <div className="book-info">
                    <h3>{book.title}</h3>
                    <p className="author">{book.author}</p>
                    <p className="price">{new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(book.price)}</p>
                  </div>
                </Link>
              </div>
            ))}
          </div>
        </section>
        
        <section className="categories-section">
          <h2>Browse by Category</h2>
          <div className="categories-grid">
            {categories.map(category => (
              <Link 
                key={category} 
                to={`/books?category=${encodeURIComponent(category)}`} 
                className="category-card"
              >
                {category}
              </Link>
            ))}
          </div>
        </section>
        
        <section className="about-section">
          <h2>About Our Store</h2>
          <div className="about-content">
            <div className="about-text">
              <p>Our bookstore offers a curated selection of books across various genres. Whether you're looking for fiction, non-fiction, educational resources, or children's books, we have something for everyone.</p>
              <p>We are passionate about reading and want to share that passion with you. Our team of book enthusiasts is always ready to help you discover your next favorite book.</p>
            </div>
            <div className="store-image">
              <img src="/store-image.jpg" alt="Our Bookstore" />
            </div>
          </div>
        </section>
      </div>
    </div>
  );
};

export default HomePage;
