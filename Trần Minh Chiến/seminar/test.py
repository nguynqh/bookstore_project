import torch
from model import RNNModel
from data import vocab, clean_text, tokenize_vi, to_indices, max_len_text, load_pretrained_embeddings

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Hàm dự đoán 1 câu
def predict_sentence(model, sentence, vocab, device):
    model.eval()
    with torch.no_grad():
        cleaned = clean_text(sentence)
        tokens = tokenize_vi(cleaned)
        indices = to_indices(tokens, vocab, max_len_text)
        input_tensor = torch.tensor(indices).unsqueeze(0).to(device)
        output = model(input_tensor)
        pred_label = torch.argmax(output, dim=1).item()
        label_map = {0: 'Positive', 1: 'Negative', 2: 'Neutral'}
        return label_map[pred_label]

# Danh sách câu cần dự đoán
test_sentences = [
    "Hôm nay tôi nhận được tin vui từ công việc. Những nỗ lực suốt thời gian qua cuối cùng cũng được đền đáp. Đồng nghiệp chúc mừng, cấp trên ghi nhận. Tôi cảm thấy tràn đầy năng lượng và tự tin hơn vào con đường mình đã chọn.",
    "Tôi thức dậy vào lúc 6 giờ sáng, pha một tách cà phê và đọc vài trang sách trước khi bắt đầu công việc. Ngày hôm nay trôi qua như mọi ngày khác, không có gì đặc biệt xảy ra, nhưng cũng không có điều gì khiến tôi bận tâm.",
    "Trời mưa suốt cả ngày khiến tôi không thể ra ngoài. Kế hoạch bị hoãn lại, mọi việc trở nên bế tắc. Tôi cảm thấy bực bội và mất kiên nhẫn khi mọi thứ không đi theo đúng dự định đã đặt ra từ trước."
    
]

# Chạy dự đoán với cả hai mô hình
for pretrained in [True, False]:
    print(f"\n--- Dự đoán với mô hình pretrained = {pretrained} ---")
    
    # Nạp embedding nếu cần
    if pretrained:
        embedding_matrix = load_pretrained_embeddings(
            vocab, embedding_dim=100,
            glove_file_path='D:/bookstore_project/Trần Minh Chiến/seminar/.vector_cache/glove.6B.100d.txt'
        )
    else:
        embedding_matrix = None

    # Khởi tạo model và load trạng thái
    model = RNNModel(
        vocab_size=len(vocab),
        embedding_dim=100,
        hidden_dim=128,
        output_dim=3,
        vocab_dict=vocab,
        pretrained=pretrained,
        embedding_matrix=embedding_matrix
    )
    model_path = f"rnn_model_pretrained_{pretrained}.pt"
    model.load_state_dict(torch.load(model_path, map_location=device))
    model.to(device)

    # Dự đoán từng câu
    for sentence in test_sentences:
        prediction = predict_sentence(model, sentence, vocab, device)
        print(f"Câu: '{sentence}' --> Dự đoán cảm xúc: {prediction}")
