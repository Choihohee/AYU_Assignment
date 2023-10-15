def polynomial_to_binary(poly_str):
    poly_str = poly_str.replace(' ', '').replace('+', '')
    max_degree = 0
    terms = poly_str.split('x')
    for term in terms:
        if len(term) > 0:
            degree = int(term)
            max_degree = max(max_degree, degree)
    binary_string = ''
    for degree in range(max_degree, -1, -1):
        binary_string += '1' if f'x{degree}' in poly_str else '0'
    return binary_string

def calculate_checksum(data, binary_representation):
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
                data[i + j] = str(int(data[i + j]) ^ int(binary_representation[j]))

    # 체크섬 (나머지) 부분을 반환
    return data[-remainder_bits:]


# 사용자로부터 데이터와 생성 다항식을 입력 받음
data = input("데이터를 입력하세요 (이진수 형태, 예: 101101001): ")
poly_str = input("다항식을 입력하세요 (예: x5+x2+1): ")
binary_representation = polynomial_to_binary(poly_str)
# 체크섬 (나머지) 계산
checksum = calculate_checksum(list(data), list(binary_representation))

# 결과 출력
print("체크섬 (나머지):", ''.join(checksum))