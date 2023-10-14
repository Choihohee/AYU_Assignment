from collections import Counter

# 원문 파일 경로
file_path = "원문.txt"

# 알파벳 문자의 발생 횟수를 저장할 카운터 초기화
alphabet_count = Counter()

# 파일 열기 및 알파벳 문자 카운트
with open(file_path, 'r', encoding='utf-8') as file:
    for line in file:
        for char in line:
            if char.isalpha():
                alphabet_count[char.lower()] += 1

# 알파벳 카운트를 가장 많은 알파벳 순서대로 정렬
sorted_count = sorted(alphabet_count.items(), key=lambda x: x[1], reverse=True)

# 결과를 키값.txt 파일에 결과 저장
output_key_file = "키값.txt"
with open(output_key_file, 'w') as output:
    for letter, count in sorted_count:
        output.write(f"{letter}={count}\n")

print(f"원문 알파벳을 count한 내용이 '{output_key_file}' 파일에 저장되었습니다.")

# 원문에 나타나는 알파벳만을 처리하는 암호화 및 복호화 매핑 생성
encryption_mapping = {}
decryption_mapping = {}
i = 0
for letter, count in sorted_count:
    if letter.isalpha():
        original_char = chr(ord('a') + i)
        encryption_mapping[original_char] = letter
        decryption_mapping[letter] = original_char
        i += 1

# 암호화된 결과를 저장할 파일 경로
output_encrypted_file = "암호문.txt"
with open(file_path, 'r', encoding='utf-8') as input_file:
    with open(output_encrypted_file, 'w', encoding='utf-8') as output:
        for line in input_file:
            encrypted_line = ''.join(encryption_mapping.get(char.lower(), char) if char.isalpha() else char for char in line)
            output.write(encrypted_line)

print(f"암호화된 내용이 '{output_encrypted_file}' 파일에 저장되었습니다.")

# 복호화할 파일 경로
encrypted_file_path = output_encrypted_file

# 복호화된 결과를 저장할 파일 경로
output_decrypted_file = "복호화.txt"

# 암호화된 파일 열기 및 복호화
with open(encrypted_file_path, 'r', encoding='utf-8') as encrypted_file:
    decrypted_text = ""
    for line in encrypted_file:
        decrypted_line = ''.join(decryption_mapping.get(char.lower(), char) if char.isalpha() else char for char in line)
        decrypted_text += decrypted_line

# 복호화 결과를 파일에 저장
with open(output_decrypted_file, 'w', encoding='utf-8') as output:
    output.write(decrypted_text)

print(f"복호화된 내용이 '{output_decrypted_file}' 파일에 저장되었습니다.")
