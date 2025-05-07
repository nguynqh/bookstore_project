from data import vocab_size  # Import vocab_size từ data.py

import torch
import torch.nn as nn
import torchtext.vocab as vocab

class RNNModel(nn.Module):
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

        self.rnn = nn.RNN(input_size=embedding_dim, hidden_size=hidden_dim, batch_first=True)
        self.fc = nn.Linear(hidden_dim, output_dim)

    def forward(self, text):
        embedded = self.embedding(text)
        output, hidden = self.rnn(embedded)
        logits = self.fc(hidden.squeeze(0))
        return logits

# Khởi tạo mô hình
model = RNNModel(
    vocab_size=vocab_size,
    embedding_dim=100,
    hidden_dim=128,
    output_dim=3,
    pretrained=True
)