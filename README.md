# 가구 마켓(Homaura) 프로젝트

### 🏞️ 프로젝트 소개
가구 마켓인 Homaura는 가구를 사고 파는 eCommerce 플랫폼입니다. eCommerce의 기본적인 기능들을 사용하여 다양한 상품을 판매하고 구매할 수 있는 서비스를 경험하게 됩니다. 또한 해당 서비스는 특정 이벤트를 통해 제한적 수량이 존재하는 할인 쿠폰을 발매하고 해당 쿠폰을 통해 상품을 구매할때 저렴한 가격으로 구매도 할 수 있습니다.

### 🗓️ 프로젝트 기간
2024년 04월 17일 ~ 2024년 05월 17일 (1달)

### 🎫 서비스 실행 방법
``` Dock```

### 📜 프로젝트 설명 리스트
1. [서비스 구성 및 아키텍쳐](#-프로젝트-구성)   
2. [주요 기능](#-주요-기능)   
3. [기술 스택](#-기술-스택)
   - [기술적 의사 결정](#-기술적-의사-결정)
4. [성능 최적화 및 트러블 슈팅](#-성능-최적화-및-트러블-슈팅) 
   - [성능 최적화](#-성능-최적화) 
   - [트러블슈팅](#-트러블슈팅) 
5. [ERD](#-ERD)   
6. [API문서](#-API문서) 
7. [유용한 링크](#-유용한-링크)  
---
## 🚧 서비스 구성 및 아키텍처
![MSA 아키텍쳐](https://github.com/Tedeeeee/homaura/assets/118357403/b21c1658-5fda-4613-aae7-ea0de0255b82)
## 🌟 주요 기능
- **유저 관리** : 회원가입시 이메일을 인증하여 유저의 신뢰도를 높입니다.
- **상품 구매 시 배송 관리** : 상품을 주문한 후 해당 상품의 배달 상태를 파악하고 배달을 진행합니다.
- **상품 취소와 반품** : 구매일자엔 취소가 가능하고 상품을 받고 다음날까지 반품이 가능합니다.
- **쿠폰 발급 이벤트** : 특정 시간에 쿠폰이 발급되고 선착순 이벤트 방식으로 쿠폰을 발급합니다.
## 🛠 기술 스택
### 🖥️ BackEnd
- Java 17, Spring Boot 3.2.5
- Spring Security, Spring Cloud Gateway, Spring Eureka, JPA
- MySQL
- Redis 7.2.4
- Docker 26.0.1
- Kafka 3.7.0, Zookeeper 3.8.3

### 🧳 기술적 의사 결정 
- [기술적 의사 결정 정리](https://understood-sphere-fcd.notion.site/82e6d0660ef545849dd3cfd78aa19f13?pvs=4)

## 📈 성능 최적화 및 트러블슈팅
프로젝트를 개발하는 과정에서 발생한 주요 성능 최적화 작업과 트러블 슈팅 사례입니다. 이는 프로젝트 진행 중 직면한 기술적 문제를 해결하고, 프로젝트의 전반적인 성능을 향상시킨 경험을 공유하는 자료입니다
### 성능 최적화
- **Pagenation의 Slice 사용** : Pagenation에서 Page가 아닌 Slice를 사용하면서 Full-Scan하는 쿼리문 삭제 [<u>[자세한 내용](https://latewalk.tistory.com/154)</u>]
- **APIGateway와 Redis의 기능을 합쳐 로그아웃 응답 속도 개선** : 인가를 모든 서비스에서 진행하지 않고 ApiGateway에서 진행하고 또한 Redis의 기능을 도입하게 되며 로그아웃 이후 같은 토큰으로 요청을 할 경우 비인가 응답속도가 29ms -> 5ms로 약 80%의 성능 향상을 확인했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/235)</u>]
- **JPA의 N+1 문제** : 1:N 상황에서 양방향 관계가 형성되면서 LAZY 한 fetch 정책으로 쿼리문이 추가적으로 발생하던 것을 jpql을 repository에 추가하여 한 개의 쿼리문이 작성되며 49ms -> 21ms로 약 50%의 성능 향상을 확인했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/249)</u>]
- **쿠폰 발급 로직에 Kafka 도입** : Feign 방식으로 인해 응답을 기다리게 되면서 서비스 성능의 하락으로 이어졌기에 굳이 기다릴 필요없는 상황에서 MQ방식을 통해 메세지만 전달하여 서비스의 처리를 진행하도록 하기 위해 Kafka를 도입했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/251)</u>]
---
### 트러블슈팅
- **이메일 인증을 위해 사용한 저장소** : Stateless 로 인해 기존의 세션 저장소를 사용할 수 없어져 Redis에 key와 value값을 기반으로 만료 시간을 통해 값을 저장해서 사용했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/238)</u>]
- **로그아웃시 이후 요청에 해당 토큰 사용 불가** : Redis에 로그아웃한 AccessToken을 저장해서 로그아웃을 기억하는 방식으로 해결했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/236)</u>]
- **재고 수량의 동시성 문제** : 데이터 처리 로직이 복잡하지 않고 여러개의 인스턴스가 한개의 DB를 바라보기 때문에 DB의 Lock을 선택하고 데이터 충돌이 빈번하게 발생할 것으로 예상되어 비관적 락을 선택했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/244)</u>] 
- **스케줄러 도입** : 배송 상태를 하루마다 한 번씩 체크하면서 취소와 반품 가능 여부를 판단하여 진행했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/229)</u>]
- **Redis의 Sorted Set 타입의 데이터로 선착순 정리** : 쿠폰을 발급받기위해 수많은 사람들이 몰리기 때문에 응답속도의 저하, 동시성 제어 처리 복잡, 사용자의 경험 하락으로 인해 나아가 서비스의 장애로 이어질 수 있는 상황이 발생할 가능성이 높아졌기 때문에 이 문제들을 처리하기 위해 Redis의 SortedSet을 사용하여 사용자를 줄세워 문제점을 없앴습니다. [<u>[자세한 내용](https://latewalk.tistory.com/250)</u>] 
- **Feign 방식에서 수신 서비스가 문제가 발생했을때 회복탄력성 처리** : 요청을 보낸 서비스가 문제가 발생했을때 해당 서비스의 문제가 아닌 다른 곳의 문제가 발생하게 되지만 응답이 마치 해당 서비스에서 발생한것처럼 처리되는 것을 방지하기 위해resilience4J의 circuitBreaker와 retry방식을 도입해서 관리를 진행했습니다. [<u>[자세한 내용](https://latewalk.tistory.com/245)</u>] 

전체 프로젝트 관련 글은 해당 <u>[블로그](https://latewalk.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%ED%95%AD%ED%95%B499%20%EA%B0%9C%EC%9D%B8%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8)</u>에서 모두 확인하실 수 있습니다
## 🗂 ERD
![image](https://github.com/Tedeeeee/homaura/assets/118357403/023cc1c8-2680-43cb-a878-38270f55dab0)
https://drawsql.app/teams/shopingmall/diagrams/shopdiagram

## 📚 API 문서
https://documenter.getpostman.com/view/26288836/2sA3JRZKM6
## 🔗 유용한 링크
- 프로젝트 정리 문서 : [homaura 프로젝트 블로그](https://latewalk.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%ED%95%AD%ED%95%B499%20%EA%B0%9C%EC%9D%B8%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?page=1)
