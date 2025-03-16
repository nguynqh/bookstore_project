-- Database: bookstore

-- DROP DATABASE IF EXISTS bookstore;

-- CREATE DATABASE bookstore
--     WITH 
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'en_US.UTF-8'
--     LC_CTYPE = 'en_US.UTF-8'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1;

-- \c bookstore;

-- SEQUENCES

CREATE SEQUENCE IF NOT EXISTS users_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS categories_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS authors_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS publishers_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS books_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS reviews_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS orders_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS order_items_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS carts_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS cart_items_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS promotions_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS wishlists_id_seq START 1;
CREATE SEQUENCE IF NOT EXISTS wishlist_items_id_seq START 1;

-- TABLES

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT DEFAULT nextval('users_id_seq') PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(50),
    state VARCHAR(50),
    postal_code VARCHAR(20),
    country VARCHAR(50),
    role VARCHAR(20) DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- Table: categories
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT DEFAULT nextval('categories_id_seq') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE CASCADE
);

-- Table: authors
CREATE TABLE IF NOT EXISTS authors (
    id BIGINT DEFAULT nextval('authors_id_seq') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    biography TEXT,
    birth_date DATE,
    country VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: publishers
CREATE TABLE IF NOT EXISTS publishers (
    id BIGINT DEFAULT nextval('publishers_id_seq') PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    website VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: books
CREATE TABLE IF NOT EXISTS books (
    id BIGINT DEFAULT nextval('books_id_seq') PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    description TEXT,
    publisher_id BIGINT,
    publication_date DATE,
    language VARCHAR(50),
    page_count INTEGER,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    discount_price DECIMAL(10,2),
    quantity INTEGER DEFAULT 0 CHECK (quantity >= 0),
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (publisher_id) REFERENCES publishers (id) ON DELETE SET NULL
);

-- Table: book_authors (many-to-many)
CREATE TABLE IF NOT EXISTS book_authors (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE
);

-- Table: book_categories (many-to-many)
CREATE TABLE IF NOT EXISTS book_categories (
    book_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

-- Table: reviews
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT DEFAULT nextval('reviews_id_seq') PRIMARY KEY,
    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Table: orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT DEFAULT nextval('orders_id_seq') PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
    status VARCHAR(20) DEFAULT 'PENDING',
    shipping_address VARCHAR(255) NOT NULL,
    shipping_city VARCHAR(50) NOT NULL,
    shipping_state VARCHAR(50),
    shipping_postal_code VARCHAR(20) NOT NULL,
    shipping_country VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50),
    tracking_number VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Table: order_items
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT DEFAULT nextval('order_items_id_seq') PRIMARY KEY,
    order_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);

-- Table: carts
CREATE TABLE IF NOT EXISTS carts (
    id BIGINT DEFAULT nextval('carts_id_seq') PRIMARY KEY,
    user_id BIGINT,
    session_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Table: cart_items
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT DEFAULT nextval('cart_items_id_seq') PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);

-- Table: promotions
CREATE TABLE IF NOT EXISTS promotions (
    id BIGINT DEFAULT nextval('promotions_id_seq') PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    discount_type VARCHAR(20) NOT NULL, -- 'PERCENTAGE' or 'FIXED_AMOUNT'
    discount_value DECIMAL(10,2) NOT NULL CHECK (discount_value > 0),
    min_purchase DECIMAL(10,2),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    max_uses INTEGER,
    current_uses INTEGER DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_date > start_date)
);

-- Table: wishlists
CREATE TABLE IF NOT EXISTS wishlists (
    id BIGINT DEFAULT nextval('wishlists_id_seq') PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) DEFAULT 'Default Wishlist',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Table: wishlist_items
CREATE TABLE IF NOT EXISTS wishlist_items (
    id BIGINT DEFAULT nextval('wishlist_items_id_seq') PRIMARY KEY,
    wishlist_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wishlist_id) REFERENCES wishlists (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    UNIQUE (wishlist_id, book_id)
);

-- INDEXES

-- users indexes
-- CREATE INDEX idx_users_email ON users(email);
-- CREATE INDEX idx_users_username ON users(username);

-- -- books indexes
-- CREATE INDEX idx_books_isbn ON books(isbn);
-- CREATE INDEX idx_books_title ON books(title);
-- CREATE INDEX idx_books_price ON books(price);

-- -- orders indexes
-- CREATE INDEX idx_orders_user_id ON orders(user_id);
-- CREATE INDEX idx_orders_order_date ON orders(order_date);
-- CREATE INDEX idx_orders_status ON orders(status);

-- -- reviews indexes
-- CREATE INDEX idx_reviews_book_id ON reviews(book_id);
-- CREATE INDEX idx_reviews_user_id_book_id ON reviews(user_id, book_id);

-- -- FUNCTIONS AND TRIGGERS

-- -- Update timestamp function
-- CREATE OR REPLACE FUNCTION update_modified_column()
-- RETURNS TRIGGER AS $$
-- BEGIN
--    NEW.updated_at = now(); 
--    RETURN NEW;
-- END;
-- $$ language 'plpgsql';

-- Triggers for updated_at columns
-- CREATE TRIGGER update_users_modtime
--     BEFORE UPDATE ON users
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_categories_modtime
--     BEFORE UPDATE ON categories
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_authors_modtime
--     BEFORE UPDATE ON authors
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_publishers_modtime
--     BEFORE UPDATE ON publishers
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_books_modtime
--     BEFORE UPDATE ON books
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_reviews_modtime
--     BEFORE UPDATE ON reviews
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_orders_modtime
--     BEFORE UPDATE ON orders
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_order_items_modtime
--     BEFORE UPDATE ON order_items
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_carts_modtime
--     BEFORE UPDATE ON carts
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_cart_items_modtime
--     BEFORE UPDATE ON cart_items
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_promotions_modtime
--     BEFORE UPDATE ON promotions
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- CREATE TRIGGER update_wishlists_modtime
--     BEFORE UPDATE ON wishlists
--     FOR EACH ROW
--     EXECUTE FUNCTION update_modified_column();

-- Function to update promotion's current_uses when used
-- CREATE OR REPLACE FUNCTION increment_promotion_uses()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     UPDATE promotions
--     SET current_uses = current_uses + 1
--     WHERE id = NEW.promotion_id;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;

-- Insert sample data for testing

-- Insert admin user
INSERT INTO users (username, email, password, first_name, last_name, role) 
VALUES ('admin', 'admin@example.com', '$2a$10$uAJxMYJFKMQUPMcPdHTGWOqxX.6jh..5ldAMIiVjFQxRU371CnNNi', 'Admin', 'User', 'ADMIN');

-- Insert sample categories
INSERT INTO categories (name, description) VALUES 
('Fiction', 'Fictional literature'),
('Non-Fiction', 'Non-fictional literature'),
('Science Fiction', 'Science fiction books'),
('Fantasy', 'Fantasy genre books'),
('Biography', 'Biography and autobiography books'),
('History', 'Historical books'),
('Self-Help', 'Self-help and personal development books'),
('Business', 'Business and economics books');

-- Insert sample authors
INSERT INTO authors (name, biography, birth_date, country) VALUES
('J.K. Rowling', 'British author best known for the Harry Potter series', '1965-07-31', 'United Kingdom'),
('Stephen King', 'American author of horror, supernatural fiction, suspense, and fantasy novels', '1947-09-21', 'United States'),
('Agatha Christie', 'English writer known for her detective novels', '1890-09-15', 'United Kingdom'),
('George Orwell', 'English novelist, essayist, journalist, and critic', '1903-06-25', 'United Kingdom'),
('Dale Carnegie', 'American writer and lecturer', '1888-11-24', 'United States');

-- Insert sample publishers
INSERT INTO publishers (name, website) VALUES
('Penguin Random House', 'https://www.penguinrandomhouse.com'),
('HarperCollins', 'https://www.harpercollins.com'),
('Simon & Schuster', 'https://www.simonandschuster.com'),
('Macmillan Publishers', 'https://www.macmillan.com'),
('Hachette Book Group', 'https://www.hachettebookgroup.com');

-- Insert sample books
INSERT INTO books (title, isbn, description, publisher_id, publication_date, language, page_count, price, quantity, image_url) VALUES
('Harry Potter and the Philosopher''s Stone', '9780747532699', 'The first novel in the Harry Potter series', 1, '1997-06-26', 'English', 223, 19.99, 100, 'https://example.com/hp1.jpg'),
('The Shining', '9780307743657', 'A horror novel by Stephen King', 2, '1977-01-28', 'English', 447, 14.99, 75, 'https://example.com/shining.jpg'),
('Murder on the Orient Express', '9780062693662', 'A detective novel by Agatha Christie', 3, '1934-01-01', 'English', 256, 12.99, 50, 'https://example.com/orient.jpg'),
('1984', '9780451524935', 'A dystopian novel by George Orwell', 4, '1949-06-08', 'English', 328, 11.99, 80, 'https://example.com/1984.jpg'),
('How to Win Friends and Influence People', '9780671027032', 'A self-help book by Dale Carnegie', 5, '1936-10-01', 'English', 256, 10.99, 100, 'https://example.com/win.jpg');