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
    "Buổi trưa, tôi ghé quán ăn quen thuộc, gọi một phần cơm sườn. Quán vẫn đông như mọi khi. Tôi ăn xong rồi trở về làm việc. Không có gì đặc biệt xảy ra trong suốt buổi chiều, chỉ là một ngày bình thường.",
    "Tôi vừa hoàn thành một dự án quan trọng trước thời hạn. Cảm giác nhẹ nhõm và vui sướng khiến tôi muốn tự thưởng cho bản thân một bữa tối thịnh soạn. Tôi tin rằng sự chăm chỉ luôn mang lại kết quả tốt đẹp.",
    "Cuộc họp sáng nay kéo dài và đầy căng thẳng. Tôi bị chỉ trích vì lỗi không hoàn toàn do mình. Cảm giác ức chế và mệt mỏi cứ đeo bám cả ngày, khiến tôi không thể tập trung vào công việc còn lại.",
    "Hôm nay là thứ tư, tôi đi làm như thường lệ. Giao thông khá ổn, công việc không có gì quá tải. Tôi ăn trưa với đồng nghiệp và trở về bàn làm việc. Ngày trôi qua nhẹ nhàng mà không có biến động gì đáng kể.",
    "Sau buổi tập thể dục buổi chiều, tôi thấy cơ thể khỏe mạnh và tinh thần thoải mái hơn hẳn. Không gì bằng việc duy trì thói quen lành mạnh mỗi ngày. Tôi hào hứng với những kế hoạch sắp tới cho bản thân.",
    "Tôi cảm thấy thất vọng khi người bạn thân không giữ lời hứa. Dù đã chờ đợi rất lâu, cuối cùng chỉ nhận lại sự im lặng. Điều đó khiến tôi nghi ngờ tình bạn mà trước đây từng rất trân trọng và tin tưởng.",
    "Buổi tối, tôi ngồi xem một bộ phim tài liệu về thiên nhiên. Hình ảnh đẹp và kiến thức thú vị giúp tôi thư giãn đầu óc. Mặc dù không quá ấn tượng, nhưng đó là một trải nghiệm thư giãn nhẹ nhàng trước giờ đi ngủ.",
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
