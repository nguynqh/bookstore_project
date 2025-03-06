# Báo cáo khảo sát hiện trạng và quy trình nghiệp vụ

> Xem chi tiết báo cáo đầy đủ tại [business-requirements (Google Docs)](https://docs.google.com/document/d/12U_EiiYgK2G9FnqtHHnawJDF1Lto5R2v/edit)

## Thông tin dự án
**Giảng viên phụ trách:** TS. Đỗ Như Tài

### Thành viên nhóm
| STT | Họ tên | Khả năng |
|-----|---------|----------|
| 1 | Nguyễn Quang Hiếu | Thu thập dữ liệu, khảo sát thực tế |
| 2 | Đoàn Minh Khôi | Phân tích, tổng hợp thông tin |
| 3 | Nguyễn Ngọc Hạnh Nguyên | Viết báo cáo, thiết kế sơ đồ tổ chức |

### Công cụ sử dụng
| STT | Tên phần mềm | Hãng sản xuất | Giá |
|-----|--------------|---------------|-----|
| 1 | VS Code | Microsoft | Miễn phí |
| 2 | PostgreSQL | PostgreSQL Global Development Group | Miễn phí |
| 3 | Draw.io | JGraph Ltd | Miễn phí |
| 4 | Figma | Figma Inc. | Miễn phí |
| 5 | Microsoft Word | Microsoft | Có |
| 6 | Microsoft Excel | Microsoft | Có |

## 1. Hiện trạng hệ thống

### 1.1 Tổng quan
- Hệ thống quản lý khoảng 5000+ đầu sách
- Phân loại theo thể loại, tác giả và nhà xuất bản
- Hệ thống quản lý còn thủ công một phần
- Cần xây dựng CSDL tập trung

### 1.2 Các vấn đề tồn tại
1. **Quy trình nhập xuất:**
   - Cập nhật thông tin thủ công
   - Thiếu tự động hóa trong kiểm kê

2. **Công nghệ:**
   - Phần mềm quản lý kho cũ
   - Chưa có nền tảng web-based và cloud-based
   - Hệ thống POS chưa đồng bộ với kho
   - Bảo mật cần cải thiện

3. **Quản lý dữ liệu:**
   - Dữ liệu khách hàng lưu trữ phân tán
   - Thiếu hệ thống CRM tích hợp
   - Công cụ phân tích dữ liệu marketing chưa đầy đủ

### 1.3 Hiện trạng phần cứng
| Thiết bị | Số lượng | Tình trạng | Ghi chú |
|----------|-----------|------------|----------|
| Máy chủ | 2 | Cũ, hiệu suất thấp | Cần nâng cấp |
| Thiết bị mạng | 5 Router, 10 Switch | Hoạt động ổn định | Nâng cấp bảo mật |
| Thiết bị POS | 4 | Chưa đồng bộ | Cần tích hợp |

### 1.4 Hiện trạng phần mềm
| Phần mềm | Chức năng | Tình trạng | Ghi chú |
|----------|-----------|------------|----------|
| Quản lý kho | Quản lý danh mục sách | Cũ, lỗi thời | Cần nâng cấp |
| POS | Xử lý giao dịch | Hoạt động độc lập | Cần đồng bộ |
| CRM | Quản lý khách hàng | Không có | Cần xây dựng mới |

## 2. Quy trình nghiệp vụ

### 2.1 Quy trình hiện tại
1. **Quản lý sách:**
   - Quản lý danh mục và phân loại sách
   - Cập nhật thông tin sách mới
   - Theo dõi sách bán chạy

2. **Quản lý kho:**
   - Nhập xuất sách
   - Kiểm kê hàng tồn
   - Báo cáo tồn kho

3. **Bán hàng và CSKH:**
   - Xử lý giao dịch
   - Quản lý thông tin khách hàng
   - Giải quyết khiếu nại

### 2.2 Đề xuất cải tiến
1. Tin học hóa quy trình phân loại và cập nhật sách
2. Xây dựng hệ thống quản lý kho thông minh với RFID
3. Phát triển hệ thống CRM tích hợp
4. Áp dụng DevOps và CI/CD
5. Nâng cấp hệ thống lên nền tảng web-based