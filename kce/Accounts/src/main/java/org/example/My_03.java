package org.example;
/*
 [계좌 정보 조회] (최근 거래 내역 5개)(select)
 ->계좌번호 입력 + 비밀번호 입력(int)
 -존재하지 않는 계좌번호
 -비밀번호 틀림

[계좌 개설 및 수정] (insert+update+delete)
(개설)
1. 신규개설
u_idx,a_password
2. 계좌삭제
삭제할 a_number -> a_password
(수정)
1.계좌 소유주 수정
수정할 a_number -> 1.추가 / 2.삭제 u_idx
2.계좌 비밀번호 수정
수정할 a_number -> 수정 a_password

[본인 계좌 조회](select)
1.전체조회
->로그인 된 사용자의 u_idx에 맞는 계좌번호,잔액 출력해줌

 */

public class My_03 {



}
