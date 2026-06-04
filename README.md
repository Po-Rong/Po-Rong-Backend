# 💜 팝업스토어 모음 플랫폼 PORONG
> 파편화된 정보를 한곳에 모으고, 실시간 혼잡도 공유와 게이미피케이션 요소를 결합한  
팝업스토어 통합 플랫폼 포롱(PORONG)입니다.

<img width="1920" height="910" alt="image" src="https://github.com/user-attachments/assets/e236bd4a-46a3-4993-8cef-eef8142eda4c" />

---

## 👥 팀원 소개 및 역할 분배

전 팀원이 프론트엔드와 백엔드를 담당하여 기능을 완성했습니다.

| 이주현 | 고유정 | 서지현 |
|:------:|:------:|:------:|
| <img src="https://github.com/hana03030.png" width="100%"/> | <img src="https://github.com/daenggg.png" width="100%"/> | <img src="https://github.com/jhwest-dev.png" width="100%"/> | 
| [@hana03030](https://github.com/hana03030) | [@daenggg](https://github.com/daenggg) | [@jhwest-dev](https://github.com/jhwest-dev) |
| 메인 페이지, 찾기 페이지 | 팝업 상세, 후기, 마이페이지 | 로그인/회원가입, 관리자 페이지 |
| 찜/리뷰 CRUD, 최근 리뷰 API | 도감/예약 CRUD, 혼잡도 API | 회원/팝업 CRUD, 통계 집계 API |

---

## 📌 기획 배경 및 해결책

- **Pain Point:** SNS에 흩어진 정보 탐색의 어려움, 방문 전 현장 혼잡도 파악 불가능으로 인한 시간 낭비
- **Our Solution:** 
  1. **통합 정보 제공:** 흩어진 팝업 정보를 한곳에서 스마트하게 탐색 및 필터링
  2. **사용자 참여형 데이터:** 유저들의 실제 후기를 기반으로 한 실시간 평균 혼잡도 공유
  3. **게이미피케이션:** 리뷰 작성 시 귀여운 키링 수집 유도로 유저 활동성 촉진

---

## 🌟 핵심 기능 (Core Features)

| 01. 실시간 인기도 및 랭킹 시스템 | 02. 조건별 팝업 필터링 |
| :---: | :---: |
| <img width="2560" height="1389" alt="image" src="https://github.com/user-attachments/assets/cc199119-c781-449f-8462-eb5b82d2e4d3" /> | <img width="2560" height="1399" alt="image" src="https://github.com/user-attachments/assets/93a5c194-d688-4a34-9223-44cef35e1a14" /> |
| 실시간 찜 수 데이터를 분석하여 1~10위 인기 팝업을 집계하고, 금/은/동 왕관 및 NEW 뱃지를 시각화하여 트렌드를 한눈에 제공합니다. | 카테고리, 지역, 운영 상태 결합 필터링과 Kakao Map API를 연동하여 내 주변 팝업 위치를 직관적으로 탐색합니다. |
| **03. 실시간 별점 & 혼잡도 리뷰** | **04. 키링 도감** |
| <img width="2560" height="1313" alt="image" src="https://github.com/user-attachments/assets/7b9cc55b-a6da-43d8-9f11-bbdd5b4fd460" /> | <img width="2560" height="1270" alt="image" src="https://github.com/user-attachments/assets/38b97823-6229-4f19-ae40-4713b1563656" /> |
| 이미지 바이너리 파일과 폼 데이터를 동시에 패킹하여 전송하며, 별점 및 3단계 현장 혼잡도 게이지 인터랙션을 지원합니다. | 리뷰 작성을 통해 수집한 산리오 키링을 마이페이지에서 확인하고, 미획득 아이템은 그림자 처리하여 수집 욕구를 자극합니다. |
| **05. 실시간 예약 및 후기 모니터링** | **06. 자동 좌표 변환 팝업 등록** |
| <img width="2560" height="1304" alt="image" src="https://github.com/user-attachments/assets/c39bdc23-a579-4dc6-bf2d-872ba37af2a3" /> | <img width="2560" height="1185" alt="image" src="https://github.com/user-attachments/assets/14485c36-1707-4959-8d46-ef8bb3b08a1c" /> |
| 일자별/회차별 타임테이블에 따른 실시간 예약 현황을 파악하고, 유저들이 남긴 생생한 현장 혼잡도 후기를 한눈에 모니터링합니다. | 신규 팝업스토어 등록 시, 입력한 주소 데이터를 기반으로 위경도 좌표를 자동 변환하여 Kakao Map API에 실시간 마커로 연동합니다. |

---

## 🛠️ 시스템 아키텍처 & 서비스 흐름

### [ 시스템 구조 (Architecture) ]
- **Frontend:** HTML5, CSS3, JavaScript, Kakao Map API
- **Backend:** Spring Boot, Spring Data JPA
- **Database:** MySQL

<img width="2346" height="1660" alt="image" src="https://github.com/user-attachments/assets/e5021d21-6bb1-4050-9bf4-893576b78c6b" />

### [ 서비스 Flow ]
- **구매자(User):** 홈(랭킹) ➡️ 찾기(필터링) ➡️ 팝업 상세(예약/리뷰) ➡️ 마이페이지(키링 도감)
- **판매자(Admin):** 팝업 관리 ➡️ 팝업 등록(자동 좌표 변환) ➡️ 예약/후기 확인 및 통계 대시보드

<img width="1777" height="800" alt="image" src="https://github.com/user-attachments/assets/f7e16871-232b-4071-abf8-e153babe8c4c" />

---

## 🧩 기술적 핵심 구현 사항 (Technical Implementation)

### 1. 효율적인 로컬 테스트를 위한 수동 ID 매핑 및 시나리오 기반 데이터 구축
- **구현 내용:** 백엔드 기능 검증 및 프론트엔드 API 연동 테스트를 위해, 임의의 무작위 데이터가 아닌 **실제 서비스 시나리오에 기반한 고품질 더미 데이터 60여 개(예약 30개, 리뷰 30개)를 직접 구축**했습니다.
- **설계 전략:** 데이터베이스의 `AUTO_INCREMENT` 자동 생성 기능에만 의존할 경우 로컬 환경 리셋 시 부모-자식 테이블 간의 ID 싱크가 깨져 외래키 충돌이 발생할 수 있습니다. 이를 예방하기 위해 특정 사용자군(`user_id: 6, 8~12번`)과 활성화된 팝업스토어(`popup_id: 22, 54, 104번`)를 정밀 타겟팅하여 수동으로 고유 ID 관계를 매핑한 완성도 높은 테스트 환경을 조성했습니다.

### 2. 데이터베이스 수준의 종속 무결성 확보 및 연쇄 정제 설계 (`ON DELETE CASCADE`)
- **구현 내용:** 플랫폼의 특성상 팝업스토어가 취소되거나 회원이 탈퇴할 경우, 이와 연관된 하위 종속 데이터(예약, 후기, 찜 목록, 도감)가 유령 데이터(Orphan Data)로 남지 않도록 데이터베이스 DDL 설계 단계에서 강력한 참조 무결성 제약조건을 수립했습니다.
- **데이터 흐름:** 특히 팝업 도감 테이블(`collection_books`)은 획득한 보상과 작성한 후기(`reviews`)가 1:1 정밀 타겟으로 묶여 있어야 하므로, 후기가 삭제되면 도감 내의 키링 획득 데이터도 함께 연쇄 삭제(`ON DELETE CASCADE`)되도록 아키텍처를 전형화하여 전산상 데이터 오염을 완벽히 차단했습니다.

### 3. 사용자 경험(UX)을 고려한 정형 데이터 및 미디어 가공 처리
- **구현 내용:** 후기 작성 시 사용자의 실제 현장 이미지와 다차원 메타 데이터(별점 점수, 3단계 혼잡도 전산 문자열)를 안정적으로 수신하기 위해 계층 간 데이터 교환 객체(DTO) 레이어를 세분화했습니다.
- **규격 보정:** 현장 혼잡도 데이터를 시스템 통계 및 필터링에 직관적으로 활용할 수 있도록 정형 데이터(`LOW`, `NORMAL`, `HIGH`)로 표준화하여 인코딩했으며, 업로드 주소의 가변성을 대비해 데이터베이스 공간 효율을 극대화할 수 있는 경로 가공 로직을 고려하여 영속성 계층에 바인딩했습니다.

---

## 💾 데이터베이스 설계 (ERD)

`PORONG` 플랫폼의 비즈니스 로직(예약-리뷰-도감 연쇄 무결성 및 통계 대시보드)을 안정적으로 처리하기 위해 정규화된 관계형 데이터베이스 구조입니다.

### [ 데이터 구조의 특징 ]
- **독립 마스터 테이블 설계:** `users`, `keyrings`, `regions`, `categories`를 독립 마스터로 배치하여 분류 데이터 정합성을 유지합니다.
- **철저한 종속 무결성:** 팝업스토어 정보가 삭제되면 관련된 예약(`reservations`), 후기(`reviews`), 찜 목록(`wishlists`), 서브 미디어(`popup_images`, `popup_tags`) 데이터가 영구 무결성을 잃지 않도록 `ON DELETE CASCADE` 연쇄 정제 설계를 완료했습니다.
- **도감 연쇄 회수 구조:** 특히 `collection_books`(팝업 도감) 테이블은 후기 작성이 혜택 발급의 원천 조건이므로, 사용자가 후기(`reviews`)를 삭제하면 획득했던 키링도 도감에서 연쇄 분실/회수 처리되도록 1:1 정밀 외래키 제약조건을 구현했습니다.


<img width="2000" height="2000" alt="image" src="https://github.com/user-attachments/assets/86d58452-9f3b-41e3-bfcd-ab2113070d57" />


---

## 🚀 기술적 챌린지 및 트러블슈팅 (Troubleshooting)

### 수동 ID 지정 주입 시 외래키 참조 무결성 위반 해결
- **문제 현상:** 팀원 2명이서 각자 예약(`reservations`)과 리뷰(`reviews`) SQL 문을 로컬에서 작성해 온 뒤, 통합 스크립트를 실행하자 데이터베이스가 자식 테이블 생성을 거부하며 참조 무결성 오류를 발생시켰습니다.
- **원인 분석:** 예약과 리뷰는 유저(`user_id`)와 팝업(`popup_id`)을 참조하는 종속적인 테이블입니다. 각자 로컬 환경에서 데이터를 짜다 보니 부모 테이블에 존재하지 않는 임의의 ID를 매핑했거나, 유저와 팝업 데이터가 먼저 삽입되지 않은 상태에서 자식 테이블(`INSERT`)을 먼저 호출하여 순서가 뒤틀린 것이 원인이었습니다.
- **해결 방법:** 팀원 간 '데이터 삽입 그라운드 룰'을 정의했습니다. 유저 ID는 공통 마스터에 등록된 `6, 8~12번`으로 고정하고, 팝업 ID는 현재 적재된 `22, 54, 104번`만 사용하도록 범위를 명확히 제한했습니다. 또한 무조건 부모 테이블 데이터를 먼저 인서트한 뒤 예약을 넣고, 마지막에 리뷰를 넣도록 스크립트 실행 순서를 강제 전형화하여 외래키 충돌을 완벽히 해결했습니다.

---

## 📄 상세 API 명세서 (API Specification)

포롱 플랫폼의 모든 API 엔드포인트 요청 정보, 파라미터 규격 및 Response 데이터 구조는 아래 API 명세서에서 상세히 확인하실 수 있습니다.

#### [🔗 포롱 API 명세서](https://app.notion.com/p/API-36831f3dc85d8052b5c0ebbd65c45629?source=copy_link)

---

### 📂 백엔드 폴더 구조 (Project Structure)
표준 레이어드 아키텍처 규칙을 준수하여 도메인 단위의 확장성과 코드 격리성을 보장하도록 설계했습니다.

```text
src/main/java/com/porong
├── global/                # 프로젝트 공통 처리 영역 (Config, Exception handler, Util)
└── main/                  # 애플리케이션 비즈니스 계층 구조
    ├── controller/        # 클라이언트의 HTTP 요청을 수신하고 응답을 반환하는 엔드포인트 계층
    ├── service/           # 기획 요구사항 및 비즈니스 핵심 로직을 수행하는 서비스 계층
    ├── dto/               # 클라이언트와 서버 간 데이터 요청/응답에 활용되는 데이터 전송 객체
    ├── vo/                # 변경 불가능한 비즈니스 도메인 및 불변 객체 모델 레이어
    └── mapper/            # 데이터베이스 SQL 질의문과 Java 객체를 매핑하는 영속성 계층 (MyBatis)
