from data import vocab_size  # Import vocab_size từ data.py

import torch
import torch.nn as nn
import torchtext.vocab as vocab

class LSTMModel(nn.Module):
    def __init__(self, vocab_size, embedding_dim, hidden_dim, output_dim, pretrained=False):
        super().__init__()
        if pretrained:
            glove = vocab.GloVe(name='6B', dim=embedding_dim)
            embedding_weights = torch.zeros(vocab_size, embedding_dim)
            for word, idx in glove.stoi.items():
                if idx < vocab_size:
                    embedding_weights[idx] = glove.vectors[idx]
            self.embedding = nn.Embedding.from_pretrained(embedding_weights, freeze=False, padding_idx=0)
        else:
            self.embedding = nn.Embedding(vocab_size, embedding_dim, padding_idx=0)

        self.lstm = nn.LSTM(input_size=embedding_dim, hidden_size=hidden_dim, batch_first=True)
        self.dropout = nn.Dropout(0.5)  # Dropout để giảm overfitting
        self.fc = nn.Linear(hidden_dim, output_dim)

    def forward(self, text):
        embedded = self.embedding(text)
        output, (hidden, cell) = self.lstm(embedded)
        hidden = self.dropout(hidden[-1])  # Áp dụng Dropout
        logits = self.fc(hidden)
        return logits

# Khởi tạo mô hình
model = LSTMModel(
    vocab_size=vocab_size,  # Sử dụng vocab_size từ data.py
    embedding_dim=100,
    hidden_dim=128,
    output_dim=3,
    pretrained=True
)