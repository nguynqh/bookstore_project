import { users } from '../data/users';
import { getUserByUsername } from './userApi';

// Lưu thông tin người dùng vào localStorage
const saveUserToStorage = (user) => {
  try {
    localStorage.setItem('currentUser', JSON.stringify(user));
  } catch (error) {
    console.error('Lỗi khi lưu thông tin người dùng:', error);
  }
};

// Lấy thông tin người dùng từ localStorage
export const getCurrentUser = () => {
  const user = localStorage.getItem('user');
  return user ? JSON.parse(user) : null;
};

// Đăng ký tài khoản mới
export const register = (userData) => {
  try {
    // Kiểm tra username đã tồn tại chưa
    if (users.some(user => user.username === userData.username)) {
      return { success: false, message: 'Tên đăng nhập đã tồn tại' };
    }

    // Kiểm tra email đã tồn tại chưa
    if (users.some(user => user.email === userData.email)) {
      return { success: false, message: 'Email đã được sử dụng' };
    }

    // Tạo user mới
    const newUser = {
      id: users.length + 1,
      ...userData,
      role: 'user', // Mặc định là user thường
      avatar: 'https://via.placeholder.com/150',
      orders: []
    };

    // Thêm vào mảng users
    users.push(newUser);

    // Tự động đăng nhập sau khi đăng ký
    const { password, ...userWithoutPassword } = newUser;
    saveUserToStorage(userWithoutPassword);

    return { success: true, user: userWithoutPassword };
  } catch (error) {
    console.error('Lỗi khi đăng ký:', error);
    return { success: false, message: 'Có lỗi xảy ra khi đăng ký' };
  }
};

// Đăng nhập: lấy user từ backend, kiểm tra password
export const login = async (username, password) => {
  try {
    const user = await getUserByUsername(username);
    if (user && user.password === password) {
      const { password, ...userWithoutPassword } = user;
      localStorage.setItem('user', JSON.stringify(userWithoutPassword));
      localStorage.setItem('username', user.username);
      return userWithoutPassword;
    }
    return null;
  } catch {
    return null;
  }
};

// Đăng xuất
export const logout = () => {
  localStorage.removeItem('user');
  localStorage.removeItem('username');
};

// Kiểm tra xem người dùng đã đăng nhập chưa
export const isAuthenticated = () => {
  return !!getCurrentUser();
};

// Kiểm tra xem người dùng có phải là admin không
export const isAdmin = () => {
  const user = getCurrentUser();
  return user && user.role === 'admin';
};

// Cập nhật thông tin người dùng
export const updateUserProfile = (userId, newData) => {
  try {
    const userIndex = users.findIndex(u => u.id === userId);
    if (userIndex === -1) {
      return { success: false, message: 'Không tìm thấy người dùng' };
    }

    // Cập nhật thông tin
    users[userIndex] = {
      ...users[userIndex],
      ...newData
    };

    // Cập nhật localStorage nếu là user hiện tại
    const currentUser = getCurrentUser();
    if (currentUser && currentUser.id === userId) {
      const { password, ...userWithoutPassword } = users[userIndex];
      saveUserToStorage(userWithoutPassword);
    }

    return { success: true, user: users[userIndex] };
  } catch (error) {
    console.error('Lỗi khi cập nhật thông tin:', error);
    return { success: false, message: 'Có lỗi xảy ra khi cập nhật thông tin' };
  }
}; 