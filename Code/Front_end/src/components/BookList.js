import React from 'react';
import { Table } from 'antd';
import './BookList.css';

const BookList = ({ books, columns }) => {
  return (
    <div className="book-list">
      <Table
        columns={columns}
        dataSource={books}
        rowKey="id"
        pagination={false}
      />
    </div>
  );
};

export default BookList;
