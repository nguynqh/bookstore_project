import torch
import torch.nn as nn
import torchtext.vocab as torchvocab


class RNNModel(nn.Module):
    def __init__(self, vocab_size, embedding_dim, hidden_dim, output_dim, vocab_dict=None, pretrained=False):
        super().__init__()

        if pretrained:
            print("Loading GloVe embeddings...")
            glove = torchvocab.GloVe(name='6B', dim=embedding_dim)
            embedding_weights = torch.zeros(vocab_size, embedding_dim)
            for word, idx in vocab_dict.items():
                if word in glove.stoi:
                    embedding_weights[idx] = glove[word]
                else:
                    embedding_weights[idx] = torch.randn(embedding_dim) * 0.05
            self.embedding = nn.Embedding.from_pretrained(embedding_weights, freeze=False, padding_idx=0)
        else:
            self.embedding = nn.Embedding(vocab_size, embedding_dim, padding_idx=0)

        # Bidirectional LSTM + Dropout
        self.lstm = nn.LSTM(
            input_size=embedding_dim,
            hidden_size=hidden_dim,
            batch_first=True,
            bidirectional=True
        )
        self.dropout = nn.Dropout(0.5)
        self.fc = nn.Linear(hidden_dim * 2, output_dim)

    def forward(self, text):
        embedded = self.embedding(text)                          # (batch_size, seq_len, emb_dim)
        output, (hidden, cell) = self.lstm(embedded)             # hidden: (num_layers*2, batch, hidden_dim)
        hidden_cat = torch.cat((hidden[-2], hidden[-1]), dim=1)  # (batch, hidden_dim*2)
        logits = self.fc(self.dropout(hidden_cat))               # (batch, output_dim)
        return logits
