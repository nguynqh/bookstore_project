from data import train_loader, test_loader, vocab_size
from model import LSTMModel
import torch
import torch.nn as nn
import torch.optim as optim
from sklearn.metrics import accuracy_score, f1_score
import json

def train_and_evaluate(model, train_loader, test_loader, epochs=30, lr=0.0001):
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model.parameters(), lr=lr)  # Sử dụng Adam optimizer

    for epoch in range(epochs):
        model.train()
        epoch_loss = 0
        for text, labels in train_loader:
            optimizer.zero_grad()
            outputs = model(text)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()
            epoch_loss += loss.item()

        print(f"Epoch {epoch+1}/{epochs}, Loss: {epoch_loss/len(train_loader):.4f}")

    # Đánh giá mô hình
    model.eval()
    all_preds, all_labels = [], []
    with torch.no_grad():
        for text, labels in test_loader:
            outputs = model(text)
            preds = torch.argmax(outputs, dim=1)
            all_preds.extend(preds.tolist())
            all_labels.extend(labels.tolist())

    acc = accuracy_score(all_labels, all_preds)
    f1 = f1_score(all_labels, all_preds, average='macro')
    return acc, f1

# Thử nghiệm với LSTM và Pretrained Embedding
results = {}

pretrained = True
model = LSTMModel(
    vocab_size=vocab_size,
    embedding_dim=100,
    hidden_dim=128,
    output_dim=3,
    pretrained=pretrained
)
key = f"LSTM_Pretrained={pretrained}"
acc, f1 = train_and_evaluate(model, train_loader, test_loader)
results[key] = {"Accuracy": acc, "F1-score": f1}
print(f"{key} - Accuracy: {acc:.4f}, F1-score: {f1:.4f}")

# Ghi kết quả ra file
with open("results.json", "w") as f:
    json.dump(results, f, indent=4)