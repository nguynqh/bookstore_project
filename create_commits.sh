#!/bin/bash

# Danh sách các commit message
commit_messages=(
    "Update UI components and fix responsive design"
    "Implement user authentication system"
    "Add book search functionality"
    "Fix navigation bugs"
    "Update book detail page"
    "Implement shopping cart"
    "Add payment integration"
    "Update database schema"
    "Fix image loading issues"
    "Add user profile management"
    "Implement order tracking"
    "Update security features"
    "Add book recommendations"
    "Fix checkout process"
    "Update admin dashboard"
    "Add book reviews system"
    "Implement wishlist feature"
    "Update book categories"
    "Fix responsive layout issues"
)

# Danh sách người dùng
users=(
    "nnhn2953@gmail.com"
    "khoiminhdoan1504@gmail.com"
)

# Ngày bắt đầu
start_date="2024-04-15"
end_date="2024-05-20"

# Tạo các commit
for i in {0..18}; do
    # Tính toán ngày commit
    commit_date=$(date -d "$start_date + $((i * 7)) days" +"%Y-%m-%d %H:%M:%S")
    
    # Chọn người dùng (luân phiên)
    user_index=$((i % 2))
    user=${users[$user_index]}
    
    # Cấu hình Git user
    git config --global user.name "$user"
    git config --global user.email "$user"
    
    # Tạo commit với thời gian cụ thể
    git add .
    git commit -m "${commit_messages[$i]}" --date="$commit_date"
done 