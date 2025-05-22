import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { Form, Input, Button, message, InputNumber } from 'antd';
import { updateBook, getBookById } from '../services/bookService';

const EditBook = () => {
  const [form] = Form.useForm();
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchBookData = async () => {
      try {
        setLoading(true);
        const bookData = await getBookById(id);
        form.setFieldsValue(bookData);
      } catch (error) {
        message.error('Không thể tải thông tin sách');
        navigate('/books');
      } finally {
        setLoading(false);
      }
    };

    fetchBookData();
  }, [id, form, navigate]);

  const handleSubmit = async (values) => {
    try {
      setLoading(true);
      await updateBook(id, values);
      message.success('Cập nhật sách thành công');
      navigate('/books');
    } catch (error) {
      message.error('Có lỗi xảy ra khi cập nhật sách');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: 24, maxWidth: 800, margin: '0 auto' }}>
      <h2>Chỉnh sửa thông tin sách</h2>
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
        disabled={loading}
      >
        <Form.Item
          name="title"
          label="Book Name"
          rules={[{ required: true, message: 'Please enter the book name' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          name="author"
          label="Author"
          rules={[{ required: true, message: 'Please enter the author name' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          name="price"
          label="Price"
          rules={[{ required: true, message: 'Please enter the price' }]}
        >
          <InputNumber
            style={{ width: '100%' }}
            formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
            parser={value => value.replace(/\$\s?|(,*)/g, '')}
          />
        </Form.Item>

        <Form.Item
          name="category"
          label="Category"
          rules={[{ required: true, message: 'Please enter the category' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          name="description"
          label="Description"
          rules={[{ required: true, message: 'Please enter the description' }]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>

        <Form.Item
          name="stock"
          label="Stock"
          rules={[{ required: true, message: 'Please enter the stock' }]}
        >
          <InputNumber style={{ width: '100%' }} min={0} />
        </Form.Item>

        <Form.Item
          name="image"
          label="Link Image"
          rules={[{ required: true, message: 'Please enter the image link' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>
            Update
          </Button>
          <Button 
            style={{ marginLeft: 8 }} 
            onClick={() => navigate('/books')}
          >
            Cancel
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default EditBook; 