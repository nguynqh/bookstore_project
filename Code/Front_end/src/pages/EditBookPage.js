import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { Form, Input, Button, message, Card } from 'antd';
import { updateBook } from '../services/bookService';

const EditBookPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (location.state?.book) {
      form.setFieldsValue(location.state.book);
    }
  }, [location.state, form]);

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
    <div style={{ padding: 24 }}>
      <Card title="Sửa thông tin sách">
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
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
            <Input type="number" />
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
            <Input type="number" />
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
      </Card>
    </div>
  );
};

export default EditBookPage; 