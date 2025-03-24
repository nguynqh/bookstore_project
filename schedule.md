# Kế hoạch Dự án

## Project Proposal
- [Link to Project Proposal Document](docs/project-proposal.md)
- Ngày hoàn thành: [Date]
- Trạng thái: [Status]

## Project Plan

### Phân công nhiệm vụ
| Thành viên | Vai trò | Nhiệm vụ |
|------------|---------|----------|
| Đoàn Minh Khôi | Frontend Developer | Thiết kế và phát triển UI/UX, xây dựng giao diện người dùng |
| [Thành viên khác] | Backend Developer | Phát triển API, xử lý logic nghiệp vụ, quản lý CSDL |
| [Thành viên khác] | Project Manager | Quản lý tiến độ, phối hợp giữa các thành viên |

## Kế hoạch và tiến độ dự án

### Giai đoạn 1: Khởi tạo dự án (13/03/2025 - 16/03/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Nghiên cứu yêu cầu và phạm vi | Phân tích yêu cầu dự án | 13/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Thiết kế ERD | Xác định các thực thể và mối quan hệ | 14/03/2025 - 15/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Xây dựng tài liệu kiến trúc | Mô tả các lớp và luồng dữ liệu | 15/03/2025 - 16/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Khởi tạo dự án Spring Boot | Cấu hình Maven và cấu trúc cơ bản | 16/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |

### Giai đoạn 2: Thiết lập cơ sở dữ liệu và cấu trúc (16/03/2025 - 17/03/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Tái cấu trúc thư mục | Tách biệt backend và frontend | 16/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Tạo script SQL | Định nghĩa bảng và dữ liệu mẫu | 16/03/2025 - 17/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Cấu hình kết nối CSDL | Thiết lập application.properties | 17/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Chạy thử nghiệm backend | Kiểm tra kết nối CSDL | 17/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |

### Giai đoạn 3: Tái cấu trúc và tổ chức mã nguồn (17/03/2025 - 18/03/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Phân tách FE và BE | Tổ chức lại cấu trúc thư mục | 17/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Tổ chức mã nguồn theo package | Chia thành controller, service, repository, entity | 18/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |

### Giai đoạn 4: Phát triển các lớp Entity và API (18/03/2025 - 20/03/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Xây dựng lớp Entity | Tạo các entity và định nghĩa mối quan hệ | 18/03/2025 - 19/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Phát triển Repository | Tạo interface repository cho các entity | 19/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Xây dựng Service | Triển khai logic nghiệp vụ | 19/03/2025 - 20/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Phát triển Controller | Tạo các endpoint REST API | 20/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |

### Giai đoạn 5: Thiết kế UI/UX (20/03/2025 - 25/03/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Phân tích và lập danh sách màn hình | Xác định các màn hình dựa trên ERD | 20/03/2025 - 21/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Thiết kế Wireframe | Tạo wireframe cho các màn hình chính | 21/03/2025 - 22/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Thiết kế UI chi tiết | Xác định bảng màu, phông chữ, thiết kế mockup | 22/03/2025 - 24/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |
| Triển khai React Components | Thiết lập project React và xây dựng components | 24/03/2025 - 25/03/2025 | Đoàn Minh Khôi | ✅ Hoàn thành |

### Giai đoạn 6: Hoàn thiện Backend (25/03/2025 - 15/04/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Hoàn thiện API endpoints | Phát triển và tối ưu các API còn lại | 25/03/2025 - 01/04/2025 | Đoàn Minh Khôi | 🔄 Đang thực hiện |
| Triển khai xác thực JWT | Tích hợp JWT cho bảo mật | 01/04/2025 - 05/04/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Phân quyền với Spring Security | Thiết lập quyền người dùng | 05/04/2025 - 10/04/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Viết unit test và integration test | Kiểm thử các thành phần backend | 10/04/2025 - 15/04/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |

### Giai đoạn 7: Phát triển Frontend (15/04/2025 - 05/05/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Phát triển trang chủ và danh sách sách | Xây dựng UI cho trang chính | 15/04/2025 - 20/04/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Xây dựng trang chi tiết sách | UI cho xem thông tin chi tiết | 20/04/2025 - 23/04/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Triển khai quản lý giỏ hàng | Chức năng thêm/xóa/cập nhật giỏ hàng | 23/04/2025 - 27/04/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Phát triển trang quản lý admin | Giao diện quản trị | 27/04/2025 - 05/05/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |

### Giai đoạn 8: Tích hợp và Kiểm thử (05/05/2025 - 20/05/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Tích hợp frontend và backend | Kết nối API với giao diện | 05/05/2025 - 10/05/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Kiểm thử chức năng | Kiểm tra toàn bộ chức năng | 10/05/2025 - 15/05/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Sửa lỗi và tối ưu | Giải quyết các vấn đề phát hiện | 15/05/2025 - 20/05/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |

### Giai đoạn 9: Hoàn thiện và Triển khai (20/05/2025 - 31/05/2025)
| Công việc | Mô tả | Thời gian | Người phụ trách | Trạng thái |
|-----------|-------|-----------|-----------------|------------|
| Tài liệu hướng dẫn sử dụng | Viết hướng dẫn chi tiết | 20/05/2025 - 25/05/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Triển khai thử nghiệm | Triển khai trên môi trường test | 25/05/2025 - 28/05/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |
| Triển khai chính thức | Đưa ứng dụng vào sử dụng | 28/05/2025 - 31/05/2025 | Đoàn Minh Khôi | 📅 Theo kế hoạch |

## Chú thích trạng thái
- ✅ Hoàn thành: Công việc đã được hoàn tất
- 🔄 Đang thực hiện: Công việc đang được tiến hành
- 📅 Theo kế hoạch: Công việc đã được lên kế hoạch nhưng chưa bắt đầu
- ⚠️ Trễ tiến độ: Công việc đã quá thời hạn dự kiến
