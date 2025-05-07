import torch
from model import RNNModel  # Đảm bảo rằng mô hình RNNModel được import đúng
from data import test_loader, vocab  # Đảm bảo rằng test_loader và vocab được import đúng
import torch.nn as nn
from sklearn.metrics import accuracy_score, f1_score

# Đánh giá mô hình
def evaluate(model, loader, device):
    model.eval()  # Đặt mô hình vào chế độ đánh giá
    all_preds, all_labels = [], []
    with torch.no_grad():
        for text, labels in loader:
            text, labels = text.to(device), labels.to(device)
            outputs = model(text)
            preds = torch.argmax(outputs, dim=1)
            all_preds.extend(preds.cpu().tolist())
            all_labels.extend(labels.cpu().tolist())
    
    acc = accuracy_score(all_labels, all_preds)
    f1 = f1_score(all_labels, all_preds, average='macro')
    return acc, f1

# Hàm test mô hình
def test_model(model_path):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    
    # Khởi tạo lại mô hình
    model = RNNModel(
        vocab_size=len(vocab),
        embedding_dim=100,
        hidden_dim=128,
        output_dim=3,
        vocab_dict=vocab,
        pretrained=True  # Nếu bạn muốn dùng pretrained embeddings, có thể sửa lại
    )

    # Tải trọng số từ file
    model.load_state_dict(torch.load(model_path))
    model.to(device)  # Đặt mô hình lên device (CPU hoặc GPU)
    
    # Đánh giá mô hình trên tập kiểm tra
    test_acc, test_f1 = evaluate(model, test_loader, device)
    
    # In kết quả
    print(f"Test Accuracy: {test_acc:.3f}")
    print(f"Test F1-score: {test_f1:.3f}")
    
    return test_acc, test_f1

# Đường dẫn tới mô hình đã huấn luyện
model_path = r'D:\bookstore_project\Trần Minh Chiến\seminar\RNN_epoch_10.pth'

if __name__ == "__main__":
    test_model(model_path)
