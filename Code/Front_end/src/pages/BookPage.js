import React, { useState, useEffect } from 'react';
import { getAllBooks, addBook, updateBook, deleteBook } from '../services/bookService';
import { Button, Table, Modal, Form, message, Space, Popconfirm, Descriptions } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined, EyeOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';

const BookPage = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [viewModalVisible, setViewModalVisible] = useState(false);
  const [selectedBook, setSelectedBook] = useState(null);
  const [form] = Form.useForm();
  const navigate = useNavigate();

  // Lấy danh sách sách
  const fetchBooks = async () => {
    try {
      setLoading(true);
      const data = await getAllBooks();
      setBooks(data);
    } catch (error) {
      message.error('Lỗi khi tải danh sách sách');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBooks();
  }, []);

  // Xử lý thêm sách
  const handleSubmit = async (values) => {
    try {
      await addBook(values);
      message.success('Thêm sách mới thành công');
      setModalVisible(false);
      form.resetFields();
      fetchBooks();
    } catch (error) {
      message.error('Có lỗi xảy ra');
    }
  };

  // Xử lý xóa sách
  const handleDelete = async (id) => {
    try {
      await deleteBook(id);
      message.success('Xóa sách thành công');
      fetchBooks();
    } catch (error) {
      message.error('Lỗi khi xóa sách');
    }
  };

  // Xử lý xem chi tiết sách
  const handleView = (book) => {
    setSelectedBook(book);
    setViewModalVisible(true);
  };

  // Xử lý chuyển đến trang sửa
  const handleEdit = (book) => {
    navigate(`/books/edit/${book.id}`, { state: { book } });
  };

  // Cấu hình cột cho bảng
  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Title',
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
      title: 'Actions',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button
            type="primary"
            icon={<EyeOutlined />}
            onClick={() => handleView(record)}
          />
          <Button
            type="primary"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          />
          <Popconfirm
            title="Delete Book"
            description="Are you sure you want to delete this book?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button
              type="primary"
              danger
              icon={<DeleteOutlined />}
            />
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Button
        type="primary"
        icon={<PlusOutlined />}
        onClick={() => {
          form.resetFields();
          setModalVisible(true);
        }}
        style={{ marginBottom: 16 }}
      >
        Add New Book
      </Button>

      <Table
        columns={columns}
        dataSource={books}
        rowKey="id"
        loading={loading}
      />

      {/* Add New Book Modal */}
      <Modal
        title="Add New Book"
        open={modalVisible}
        onCancel={() => {
          setModalVisible(false);
          form.resetFields();
        }}
        footer={null}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
        >
          <Form.Item
            name="title"
            label="Title"
            rules={[{ required: true, message: 'Please enter book title' }]}
          >
            <input />
          </Form.Item>
          <Form.Item
            name="author"
            label="Author"
            rules={[{ required: true, message: 'Please enter author name' }]}
          >
            <input />
          </Form.Item>
          <Form.Item
            name="price"
            label="Price"
            rules={[{ required: true, message: 'Please enter price' }]}
          >
            <input type="number" />
          </Form.Item>
          <Form.Item
            name="category"
            label="Category"
            rules={[{ required: true, message: 'Please enter category' }]}
          >
            <input />
          </Form.Item>
          <Form.Item
            name="description"
            label="Description"
            rules={[{ required: true, message: 'Please enter description' }]}
          >
            <textarea />
          </Form.Item>
          <Form.Item
            name="stock"
            label="Số lượng"
            rules={[{ required: true, message: 'Vui lòng nhập số lượng' }]}
          >
            <input type="number" />
          </Form.Item>
          <Form.Item
            name="image"
            label="Link ảnh"
            rules={[{ required: true, message: 'Vui lòng nhập link ảnh' }]}
          >
            <input />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Thêm mới
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal xem chi tiết sách */}
      <Modal
        title="Chi tiết sách"
        open={viewModalVisible}
        onCancel={() => setViewModalVisible(false)}
        footer={null}
        width={800}
      >
        {selectedBook && (
          <Descriptions bordered>
            <Descriptions.Item label="ID" span={3}>{selectedBook.id}</Descriptions.Item>
            <Descriptions.Item label="Tên sách" span={3}>{selectedBook.title}</Descriptions.Item>
            <Descriptions.Item label="Tác giả" span={3}>{selectedBook.author}</Descriptions.Item>
            <Descriptions.Item label="Giá" span={3}>{selectedBook.price.toLocaleString('vi-VN')} VNĐ</Descriptions.Item>
            <Descriptions.Item label="Thể loại" span={3}>{selectedBook.category}</Descriptions.Item>
            <Descriptions.Item label="Số lượng" span={3}>{selectedBook.stock}</Descriptions.Item>
            <Descriptions.Item label="Mô tả" span={3}>{selectedBook.description}</Descriptions.Item>
            <Descriptions.Item label="Ảnh bìa" span={3}>
              <img 
                src={selectedBook.image} 
                alt={selectedBook.title} 
                style={{ maxWidth: '200px', maxHeight: '200px' }}
              />
            </Descriptions.Item>
          </Descriptions>
        )}
      </Modal>
    </div>
  );
};

export default BookPage; 