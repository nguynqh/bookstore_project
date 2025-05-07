import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
from nltk.tokenize import word_tokenize
import nltk
import re
from sklearn.model_selection import train_test_split
from collections import Counter

nltk.download('punkt')

# ====================
# 1. Tiền xử lý văn bản
# ====================
def clean_text(text):
    text = text.lower()
    text = re.sub(r"[^a-zA-Z0-9\s]", "", text)  # loại bỏ dấu câu và ký tự đặc biệt
    return text

# Đọc dữ liệu
data = pd.read_csv('sentiment_data.csv').dropna()
texts = data['text'].apply(clean_text).tolist()
labels = data['label'].map({'Positive': 0, 'Negative': 1, 'Neutral': 2}).tolist()

# Tokenize
tokenized_texts = [word_tokenize(t) for t in texts]

# ====================
# 2. Tạo từ điển
# ====================
def build_vocab(tokenized_texts, vocab_size=5000, min_freq=2):
    all_words = [w for txt in tokenized_texts for w in txt]
    word_freq = Counter(all_words)
    most_common = [w for w, c in word_freq.items() if c >= min_freq]
    most_common = most_common[:vocab_size - 2]  # trừ 2 cho <PAD> và <UNK>
    vocab = {'<PAD>': 0, '<UNK>': 1}
    vocab.update({w: i + 2 for i, w in enumerate(most_common)})
    return vocab

vocab = build_vocab(tokenized_texts)
vocab_size = len(vocab)

# ====================
# 3. Chuyển token sang chỉ số
# ====================
def to_indices(tokens, vocab, max_len):
    idxs = [vocab.get(t, 1) for t in tokens][:max_len]  # 1 là <UNK>
    return idxs + [0] * (max_len - len(idxs))  # pad = 0

max_len_text = 50
text_indices = [to_indices(t, vocab, max_len_text) for t in tokenized_texts]

# ====================
# 4. Dataset và Dataloader
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
