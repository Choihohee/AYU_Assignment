def calculate_checksum(data, polynomial):
    data_len = len(data)
    polynomial_len = len(polynomial)

    # 데이터 뒤에 나머지를 보관할 비트 수
    remainder_bits = polynomial_len - 1

    # 데이터 뒤에 나머지 비트를 추가
    data += '0' * remainder_bits

    # 데이터를 이동하면서 나머지 계산
    for i in range(data_len):
        if data[i] == '1':
            for j in range(polynomial_len):
                data[i + j] = str(int(data[i + j]) ^ int(polynomial[j]))

    # 체크섬 (나머지) 부분을 반환
    return data[-remainder_bits:]


# 사용자로부터 데이터와 생성 다항식을 입력 받음
data = input("데이터를 입력하세요 (이진수 형태, 예: 101101001): ")
polynomial = input("생성 다항식을 입력하세요 (이진수 형태, 예: 100101): ")

# 체크섬 (나머지) 계산
checksum = calculate_checksum(list(data), list(polynomial))

# 결과 출력
print("체크섬 (나머지):", ''.join(checksum))