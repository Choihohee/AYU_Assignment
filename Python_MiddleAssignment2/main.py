def calculate_crc(data, polynomial):
    data = list(map(int, data))
    polynomial = [int(i) for i in polynomial.replace(" ", "").replace("x", "").split('+')]
    polynomial_length = max(polynomial) + 1

    # 데이터 끝에 0을 추가
    data += [0] * (polynomial_length - 1)

    # 나머지 계산 (XOR 연산)
    for i in range(len(data) - (polynomial_length - 1)):
        if data[i] == 1:
            for j in range(polynomial_length):
                data[i + j] ^= polynomial[j]

    # 체크섬 계산
    crc = ''.join(map(str, data[-(polynomial_length - 1):]))

    return crc.zfill(polynomial_length - 1)

# 사용자로부터 생성 다항식 입력 받기
polynomial = input("생성 다항식을 다항식 형태로 입력하세요 (예: x5 + x2 + 1): ")

# 사용자로부터 원래 데이터 입력 받기
data = input("원래 데이터를 입력하세요 (예: 101101001): ")

# 체크섬 계산
checksum = calculate_crc(data, polynomial)
print("체크섬:", checksum)
