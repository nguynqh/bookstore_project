# HỆ THỐNG QUẢN LÝ NHÀ SÁCH

Một ứng dụng hiện đại, thân thiện với người dùng được thiết kế để quản lý kho sách, doanh số và tương tác khách hàng cho các hiệu sách.

## 📚 Tính năng

### Điểm nổi bật về UI/UX

- **Bảng điều khiển trực quan**: Xem nhanh các số liệu quan trọng và hoạt động gần đây
- **Thiết kế Responsive**: Hoạt động liền mạch trên máy tính để bàn, máy tính bảng và thiết bị di động
- **Tập trung vào khả năng tiếp cận**: Tuân thủ hướng dẫn WCAG để tối đa hóa khả năng sử dụng
- **Chế độ sáng/tối**: Trải nghiệm xem thoải mái trong mọi môi trường
- **Trình duyệt sách tương tác**: Với khả năng lọc, sắp xếp và tìm kiếm
- **Phân trang hiện đại**: Điều hướng thân thiện qua bộ sưu tập sách


## 🖥️ Ảnh chụp màn hình

Hệ thống bao gồm:
- Trang danh sách sách với tìm kiếm và lọc
- Xem chi tiết sách với thông tin đầy đủ
- Bảng điều khiển quản trị để quản lý kho
- Trực quan hóa bán hàng và phân tích
- Giao diện quản lý khách hàng

## 🛠️ Cài đặt

### Yêu cầu hệ thống

- Node.js (v14.0.0 trở lên)
- npm hoặc yarn
- Git

### Hướng dẫn cài đặt

1. **Clone repository**
   ```bash
   git clone https://github.com/yourusername/bookstore_project.git
   cd bookstore_project/Code
   ```

2. **Cài đặt các dependencies**
   ```bash
   npm install
   # hoặc
   yarn install
   ```

3. **Cấu hình môi trường**
   ```bash
   cp .env.example .env
   # Chỉnh sửa file .env với cấu hình của bạn
   ```

4. **Khởi động server phát triển**
   ```bash
   npm start
   # hoặc
   yarn start
   ```

5. **Truy cập ứng dụng**
   Mở trình duyệt của bạn và truy cập `http://localhost:3000`

## 🚀 Hướng dẫn sử dụng

### Dành cho quản lý cửa hàng:
- Truy cập bảng điều khiển quản trị qua đường dẫn `/admin`
- Quản lý kho thông qua phần Quản lý Sách
- Xử lý đơn hàng và theo dõi doanh số theo thời gian thực
- Tạo báo cáo thông qua bảng điều khiển Analytics

### Dành cho nhân viên:
- Chức năng tìm kiếm nhanh để tìm sách
- Quét mã vạch để thanh toán nhanh
- Quản lý khách hàng cho chương trình khách hàng thân thiết

## 💻 Stack công nghệ

- **Frontend**: React.js với styled components
- **Quản lý state**: Redux / Context API
- **CSS Framework**: Styling tùy chỉnh với CSS modules
- **Icons**: Font Awesome
- **UI Components**: Được xây dựng tùy chỉnh cho trải nghiệm người dùng tối ưu

## Designer
- **Khoi Minh Doan**