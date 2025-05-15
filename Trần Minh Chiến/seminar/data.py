import numpy as np
import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
from pyvi import ViTokenizer
import re
from sklearn.model_selection import train_test_split
from collections import Counter

# ====================
# 1. Tiền xử lý văn bản
# ====================
def clean_text(text):
    text = text.lower()
    text = re.sub(r"[^a-zA-Z0-9à-ỹÀ-Ỹ\s]", "", text)  # giữ lại dấu tiếng Việt
    return text

def tokenize_vi(text):
    return ViTokenizer.tokenize(text).split()

# ====================
# Tải Pretrained Embeddings
# ====================
def load_pretrained_embeddings(vocab, embedding_dim=100, glove_file_path='path/to/glove.6B.100d.txt'):
    """
    Hàm này tải ma trận embedding từ file GloVe và trả về ma trận embedding tương ứng với vocab.
    Args:
        vocab (dict): Từ điển chứa các từ và chỉ mục (index).
        embedding_dim (int): Kích thước embedding.
        glove_file_path (str): Đường dẫn đến tệp GloVe (ví dụ 'glove.6B.100d.txt').

    Returns:
        torch.Tensor: Ma trận embedding có kích thước (vocab_size, embedding_dim).
    """
    # Tạo ma trận embedding ngẫu nhiên
    embedding_matrix = np.random.randn(len(vocab), embedding_dim) * 0.01

    # Mở tệp GloVe và tải embeddings vào từ điển
    glove_vectors = {}
    with open(glove_file_path, 'r', encoding='utf-8') as f:
        for line in f:
            split = line.split()
            word = split[0]
            vector = np.asarray(split[1:], dtype='float32')
            glove_vectors[word] = vector

    # Cập nhật ma trận embedding với từ vựng từ GloVe
    for word, idx in vocab.items():
        if word in glove_vectors:
            embedding_matrix[idx] = glove_vectors[word]
    
    # Chuyển ma trận embedding thành tensor của PyTorch
    return torch.tensor(embedding_matrix, dtype=torch.float32)


# Đọc dữ liệu
data = pd.read_csv('sentiment_data.csv').dropna()
texts = data['text'].apply(clean_text).tolist()
labels = data['label'].map({'Positive': 0, 'Negative': 1, 'Neutral': 2}).tolist()

# Tokenize tiếng Việt
tokenized_texts = [tokenize_vi(t) for t in texts]

# ====================
# 2. Phân tích độ dài để xác định max_len
# ====================
lengths = [len(t) for t in tokenized_texts]
max_len_text = min(max(lengths), 70)  # giữ giới hạn hợp lý để tránh quá dài

# ====================
# 3. Tạo từ điển
# ====================
def build_vocab(tokenized_texts, vocab_size=10000, min_freq=2):
    all_words = [w for txt in tokenized_texts for w in txt]
    word_freq = Counter(all_words)
    most_common = [w for w, c in word_freq.items() if c >= min_freq]
    most_common = most_common[:vocab_size - 2]  # trừ <PAD>, <UNK>
    vocab = {'<PAD>': 0, '<UNK>': 1}
    vocab.update({w: i + 2 for i, w in enumerate(most_common)})
    return vocab

vocab = build_vocab(tokenized_texts)
vocab_size = len(vocab)

# ====================
# 4. Chuyển token sang chỉ số
# ====================
def to_indices(tokens, vocab, max_len):
    idxs = [vocab.get(t, 1) for t in tokens][:max_len]  # 1 là <UNK>
    return idxs + [0] * (max_len - len(idxs))  # pad = 0

text_indices = [to_indices(t, vocab, max_len_text) for t in tokenized_texts]

# ====================
# 5. Dataset và Dataloader
# ====================
class SentimentDataset(Dataset):
    def __init__(self, texts, labels):
        self.texts = torch.tensor(texts, dtype=torch.long)
        self.labels = torch.tensor(labels, dtype=torch.long)

    def __len__(self):
        return len(self.labels)

    def __getitem__(self, idx):
        return self.texts[idx], self.labels[idx]

# Chia train/test
train_texts, test_texts, train_labels, test_labels = train_test_split(
    text_indices, labels, test_size=0.2, random_state=42, stratify=labels
)

train_dataset = SentimentDataset(train_texts, train_labels)
test_dataset = SentimentDataset(test_texts, test_labels)

train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)
test_loader = DataLoader(test_dataset, batch_size=32, shuffle=False)
