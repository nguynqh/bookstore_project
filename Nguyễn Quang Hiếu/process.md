# Quá trình thực hiện phần mềm Bookstore

## 📚 Giới thiệu dự án

Dự án Bookstore là một ứng dụng quản lý hiệu sách được phát triển nhằm mục đích áp dụng các công nghệ lập trình hiện đại. Hệ thống được thiết kế theo kiến trúc phân tách giữa frontend và backend, cho phép quản lý hiệu quả các quy trình kinh doanh của một hiệu sách.

### Chức năng chính
- **Quản lý kho sách:** Thêm, sửa, xóa, tìm kiếm
- **Quản lý khách hàng và tài khoản:** Hệ thống tài khoản người dùng
- **Xử lý đơn hàng:** Quản lý giao dịch mua bán
- **Thống kê báo cáo:** Doanh thu, hàng tồn

---

## 🚀 Quá trình phát triển

### 📅 Khởi tạo dự án (13/03/2025 - 16/03/2025)

#### Các hoạt động chính:
- **Nghiên cứu yêu cầu và phạm vi dự án**
- **Thiết kế sơ đồ ERD (Entity Relationship Diagram)**
  - Xác định các thực thể chính: Book, Author, Category, User, Order, OrderItem
  - Thiết lập mối quan hệ giữa các thực thể
- **Xây dựng tài liệu cho kiến trúc monolithic**
  - Mô tả các lớp và package chính
  - Định nghĩa luồng dữ liệu và xử lý
- **Khởi tạo dự án Spring Boot với Maven**
  - Cấu hình pom.xml với các dependency cần thiết
  - Tạo cấu trúc thư mục cơ bản cho dự án

> *Trong giai đoạn này, tôi đã phân tích kỹ lưỡng các yêu cầu và quyết định sử dụng Spring Boot làm nền tảng backend vì khả năng mở rộng và tích hợp dễ dàng với nhiều công nghệ khác.*

**Commit liên quan:**
```
commit 79d499c414fe0c41899828600957ac0e77ed5554
Date:   Thu Mar 13 13:14:57 2025 +0700

commit 6eefb49fd90922f5b55f739b0eef98fe4b98df2e
Date:   Sun Mar 16 22:41:48 2025 +0700
Message: Initialize bookstore project with Spring Boot setup and Maven, add file ERD, document for monolithic
```

### 📅 Thiết lập cơ sở dữ liệu và cấu trúc dự án (16/03/2025 - 17/03/2025)

#### Các hoạt động chính:
- **Tái cấu trúc vị trí các thành phần backend và frontend**
  - Tạo thư mục riêng biệt cho backend và frontend
  - Thiết lập cấu trúc package trong backend
- **Tạo script SQL cho cơ sở dữ liệu**
  - Định nghĩa các bảng và ràng buộc khóa
  - Thêm dữ liệu mẫu cho quá trình phát triển
- **Xây dựng và kiểm tra chạy thử dự án backend**
  - Cấu hình kết nối đến cơ sở dữ liệu MySQL
  - Tạo application.properties với các thiết lập cần thiết
  - Chạy thử nghiệm để xác nhận kết nối thành công

> *Sau khi khởi tạo project, tôi nhận thấy cấu trúc thư mục ban đầu chưa tối ưu cho việc phát triển song song frontend và backend. Vì vậy, tôi đã thực hiện tái cấu trúc để tách biệt rõ ràng hai phần này, giúp cho việc phát triển và quản lý mã nguồn trở nên dễ dàng hơn.*

**Commit liên quan:**
```
commit 5d1ad53186f6a8935d0dcd624781e65df0615a77
Date:   Mon Mar 17 04:37:23 2025 +0700
Message: Change back_end and front_end location, test run back_end project created, add sql script
```

### 📅 Tái cấu trúc và tổ chức mã nguồn (17/03/2025 - 18/03/2025)

#### Các hoạt động chính:
- **Phân tách rõ ràng giữa frontend và backend trong cấu trúc thư mục**
  - Tách biệt hoàn toàn mã nguồn của backend và frontend
  - Thiết lập cấu trúc thư mục chuẩn cho mỗi phần
- **Tổ chức lại mã nguồn trong thư mục Code**
  - Phân chia backend thành các package theo chức năng: controller, service, repository, entity
  - Chuẩn bị cấu trúc cho frontend với các thư mục components, pages, services

> *Trong giai đoạn này, tôi đã tập trung hoàn thiện cấu trúc dự án để tuân theo các nguyên tắc thiết kế phần mềm tốt nhất. Việc tổ chức mã nguồn hợp lý ngay từ đầu sẽ giúp dự án dễ bảo trì và mở rộng trong tương lai.*

**Commit liên quan:**
```
commit ca9634c19751fe5e10ab1a5ddc4932f3883c80bf
Date:   Tue Mar 18 16:59:13 2025 +0700
Message: update split FE and BE in folder Code
```

### 📅 Phát triển các lớp Entity và API (18/03/2025 - 20/03/2025)

#### Các hoạt động chính:
- **Xây dựng các lớp Entity dựa trên mô hình ERD**
  - Tạo các entity chính: Book, Author, Category, User, Order, OrderItem
  - Thiết lập các annotation JPA và định nghĩa mối quan hệ giữa các entity
- **Phát triển các Repository để thao tác với cơ sở dữ liệu**
  - Tạo interface repository cho mỗi entity
  - Định nghĩa các phương thức tìm kiếm và truy vấn tùy chỉnh
- **Xây dựng các Service xử lý logic nghiệp vụ**
  - Xây dựng các lớp service với các phương thức CRUD cơ bản
  - Triển khai logic nghiệp vụ cho các chức năng quan trọng
- **Phát triển các Controller cung cấp REST API**
  - Tạo các endpoint API cho từng chức năng
  - Xử lý request và response, bao gồm validation

> *Trong giai đoạn này, tôi đã tập trung vào việc xây dựng các thành phần cốt lõi của backend theo mô hình phân lớp. Việc này giúp đảm bảo tính module hóa và dễ dàng mở rộng trong tương lai.*

**Commit liên quan:**
```
commit 4f45292dd598db372cccf65af5e6dfbfa0bdaf22
Date:   Thu Mar 20 17:44:48 2025 +0700
Message: Add initial entity, controller, repository, and service classes for bookstore project
```

---

## 🔌 API Endpoints đã được triển khai

### Book Management
| Phương thức | Endpoint | Chức năng |
|-------------|----------|-----------|
| GET | `/api/books` | Lấy danh sách sách |
| GET | `/api/books/{id}` | Lấy thông tin chi tiết một cuốn sách |
| POST | `/api/books` | Thêm sách mới |
| PUT | `/api/books/{id}` | Cập nhật thông tin sách |
| DELETE | `/api/books/{id}` | Xóa sách |

---

## 📝 Kế hoạch tiếp theo

### Hoàn thiện Backend (21/03/2025 - 04/2025)
- ✅ Hoàn thiện tất cả các API endpoints
- ✅ Triển khai hệ thống xác thực JWT
- ✅ Thiết lập phân quyền với Spring Security
- ✅ Viết unit test và integration test