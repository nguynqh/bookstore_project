import React from 'react';
import { Layout, Menu } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import {
  HomeOutlined,
  BookOutlined,
  ShoppingCartOutlined,
  UserOutlined,
  AppstoreOutlined,
  SettingOutlined
} from '@ant-design/icons';

const { Sider } = Layout;

const Sidebar = () => {
  const location = useLocation();

  const menuItems = [
    {
      key: '/',
      icon: <HomeOutlined />,
      label: <Link to="/">Trang chủ</Link>,
    },
    {
      key: '/books',
      icon: <BookOutlined />,
      label: <Link to="/books">Quản lý sách</Link>,
    },
    {
      key: '/cart',
      icon: <ShoppingCartOutlined />,
      label: <Link to="/cart">Giỏ hàng</Link>,
    },
    {
      key: '/profile',
      icon: <UserOutlined />,
      label: <Link to="/profile">Tài khoản</Link>,
    },
    {
      key: '/categories',
      icon: <AppstoreOutlined />,
      label: <Link to="/categories">Danh mục</Link>,
    },
    {
      key: '/manage-books',
      icon: <SettingOutlined />,
      label: <Link to="/manage-books">Quản lý</Link>,
    },
  ];

  return (
    <Sider
      theme="light"
      width={250}
      style={{
        overflow: 'auto',
        height: '100vh',
        position: 'fixed',
        left: 0,
        top: 0,
        bottom: 0,
      }}
    >
      <div style={{ height: 32, margin: 16, background: 'rgba(0, 0, 0, 0.2)' }} />
      <Menu
        mode="inline"
        selectedKeys={[location.pathname]}
        items={menuItems}
        style={{ borderRight: 0 }}
      />
    </Sider>
  );
};

export default Sidebar; 