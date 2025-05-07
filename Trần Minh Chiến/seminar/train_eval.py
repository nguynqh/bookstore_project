from data import train_loader, test_loader, vocab
from model import RNNModel
import torch
import torch.nn as nn
import torch.optim as optim
from sklearn.metrics import accuracy_score, f1_score
from tqdm import tqdm
import json
from nltk.tokenize import word_tokenize
from data import clean_text, to_indices, max_len_text


device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
# ====================
def evaluate(model, loader, device):
    model.eval()
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
def predict_sentence(model, sentence, vocab, device):
    model.eval()
    with torch.no_grad():
        cleaned = clean_text(sentence)
        tokens = word_tokenize(cleaned)
        indices = to_indices(tokens, vocab, max_len_text)
        input_tensor = torch.tensor(indices).unsqueeze(0).to(device)

        output = model(input_tensor)
        pred_label = torch.argmax(output, dim=1).item()

        label_map = {0: 'Positive', 1: 'Negative', 2: 'Neutral'}
        return label_map[pred_label]


def train_and_evaluate(model, train_loader, test_loader, epochs=10, lr=0.001):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.to(device)

    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model.parameters(), lr=lr)

    for epoch in range(epochs):
        model.train()
        total_loss = 0
        for text, labels in tqdm(train_loader, desc=f"Epoch {epoch+1}"):
            text, labels = text.to(device), labels.to(device)
            optimizer.zero_grad()
            outputs = model(text)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()
            total_loss += loss.item()

        avg_loss = total_loss / len(train_loader)
        train_acc, train_f1 = evaluate(model, train_loader, device)
        test_acc, test_f1 = evaluate(model, test_loader, device)

        print(f"Epoch {epoch+1} - Loss: {avg_loss:.4f}, Train Acc: {train_acc:.3f}, Test Acc: {test_acc:.3f}, F1: {test_f1:.3f}")

    return test_acc, test_f1

# Thử nghiệm Pretrained vs Scratch
results = {}

for pretrained in [True, False]:
    model = RNNModel(
    vocab_size=len(vocab),
    embedding_dim=100,
    hidden_dim=128,
    output_dim=3,
    vocab_dict=vocab,          # thêm dòng này
    pretrained=True            # hoặc False để so sánh
)
    key = f"RNN_Pretrained={pretrained}"
    acc, f1 = train_and_evaluate(model, train_loader, test_loader)
    results[key] = {"Accuracy": acc, "F1-score": f1}
    print(f"{key} - Accuracy: {acc:.3f}, F1-score: {f1:.3f}")

# Ghi kết quả ra file
with open("results.json", "w") as f:
    json.dump(results, f, indent=4)
test_sentences = [
    "Buổi hòa nhạc thật tuyệt, tôi rất thích.",
    "Dịch vụ này thật tệ, tôi rất thất vọng.",
    "Tôi đang đi học."
]

for sentence in test_sentences:
    predicted = predict_sentence(model, sentence, vocab, device)
    print(f"Câu: '{sentence}' --> Dự đoán cảm xúc: {predicted}")
