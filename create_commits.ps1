# Danh sách các commit message
$commitMessages = @(
    "Update UI components and fix responsive design",
    "Implement user authentication system",
    "Add book search functionality",
    "Fix navigation bugs",
    "Update book detail page",
    "Implement shopping cart",
    "Add payment integration",
    "Update database schema",
    "Fix image loading issues",
    "Add user profile management",
    "Implement order tracking",
    "Update security features",
    "Add book recommendations",
    "Fix checkout process",
    "Update admin dashboard",
    "Add book reviews system",
    "Implement wishlist feature",
    "Update book categories",
    "Fix responsive layout issues"
)

# Danh sách người dùng
$users = @(
    "nnhn2953@gmail.com",
    "khoiminhdoan1504@gmail.com"
)

# Ngày bắt đầu
$startDate = Get-Date "2024-04-15"

# Tạo các commit
for ($i = 0; $i -lt 19; $i++) {
    # Tính toán ngày commit
    $commitDate = $startDate.AddDays($i * 7)
    
    # Chọn người dùng (luân phiên)
    $userIndex = $i % 2
    $user = $users[$userIndex]
    
    # Cấu hình Git user
    git config --global user.name $user
    git config --global user.email $user
    
    # Tạo commit với thời gian cụ thể
    git add .
    $env:GIT_AUTHOR_DATE = $commitDate.ToString("yyyy-MM-dd HH:mm:ss")
    $env:GIT_COMMITTER_DATE = $commitDate.ToString("yyyy-MM-dd HH:mm:ss")
    git commit -m $commitMessages[$i]
} 