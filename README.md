## PJT WEEK 2

### 7월 17일

- 요구사항 명세서 수정 중
- GIT(COMMIT) CONVENTION 설정
- branch 분화


### 7월 18일
- SEL01 ~ SEL08 논의
- 오늘 회의 실화냐

notion 링크
https://www.notion.so/d147cbda52e24e439890736f1eb7fca9?p=e10ac1ebf4444c36aa7fa288f4262536&pm=s

#### 회의
- 상태 필드 목록 명확하게 정하기
##### 테이블 생성 및 수정
1. 이용약관 table 생성
    - 필드 : 약관 ID, 약관 data, 필수 약관, sns 광고 수신 여부
2. 사업자등록인증 정보 table 생성
    - 텍스트 입력 + 사진 정보(pdf, 일반)
3. 가게 table
    - 상호명, 변경된 상호명
4. 사장님 계좌 table 추가
5. 가게 table
    - 사장님 한마디 추가
6. 제휴 상품 : 재고 X, 제휴중지 원할 때로 나누기
7. 이미지
    - 로고 : 원본 사진 저장(용량 작으니까)
    - 메뉴 사진 : 썸네일 + 원본 사진(용량 문제 때문에)
8. 상품옵션
    - 메뉴 옵션 이름, 가격, 카테고리(메뉴 옵션 custom)
9. 세일 table 추가
10. 번들(1가지 or n가지)
11. 상품
    - 설정 종료, 실제 종료 시간 출력

### 7월 19일
1. 요구사항 명세서 CUS 부분 업데이트
2. 신고 테이블 생성

#### 회의
<판매자>
1. 검색 알고리즘, 점포+음식 카테고리 지정
    - 우리 만의 기준 카테고리 설정
    - '마'만 입력해도 밑에 '마'가 들어간 음식들(마라탕, 마라샹궈 등)이 뜸
2. 찜 정렬 : 최신순
3. 커뮤니티 정체성 -> 추후 고민
4. 채팅방
    - 오픈채팅방(지역 톡방) : 사람들이 많을 수 있음
    - 가게 카테고리 채팅방 필요 + 익명 X, 닉네임으로
5. 알림
    - 사장님이 이벤트 생성(쿠폰)
    - 시스템에서 공지사항 or 이벤트 생성
6. 개인정보수집 -> 자유롭게
7. 필터 -> 네이버 쇼핑몰 참고
8. 리스트
    - 상세 정보 페이지에 있는 데이터를 이용해 필터링
    -> 처음에 모든 정보를 가져오고, 판매자에게 보여주고 싶은 것만 보여줌
9. 카테고리
    - 카페/베이커리, 족발/보쌈, 치킨, 피자, 한식, 양식, 중식, 일식, 아시안, 도시락/고기, 분식, 패스트푸드, 야식, 샐러드, 내 찜 목록

<소비자>
1. 별점, 거리 필터 => 기능으로 뺄 필요 없음
    - 거리는 직선거리로 계산
2. 찜
    - 최근 주문일 컬럼 추가 : 마지막 주문일로 변경
    - 마지막 주문일을 기준으로 내 찜 목록 정렬
3. 찜알림
    - 가게 알림 : 찜한 가게에 세일 상품이 들어온 경우
    - 가게별 알림 on/off, 전체 알림 on/off
4. 장바구니
    - 주문 여러개
    - 포인트 충전 + 결제 시스템
5. 픽업 예정 시간 삭제
6. 픽업
    - 방법 1 : 소비자에게 '랜덤 번호' 제공 -> 판매자는 번호 확인 후 '물품 제공'
    - 방법 2 : 소비자는 판매자에게 '구매 내역' 보여줌 -> 판매자는 '판매완료' 클릭
7. 장바구니 -> 결제 프로세스
    - 결제 전부 취소
    - 1. 제휴 상품 포함 : 제휴 상품 삭제 -> 나머지 상품은 장바구니에 그대로 존재
    - 2. 제휴 상품 제외 : 구매를 할 수 없는 상품만 환불
8. 장바구니 : local session에 저장

<관리자>
1. 쿠폰 등록
    - 이벤트 banner 클릭 -> 이벤트 페이지 이동
    => 이벤트 table 생성
2. 이벤트
    - 배너사진, 이벤트명, 이벤트 내용, 생성일, 종료일 + 쿠폰 DB 연결

### 7월 20일
- ERD 확인중
