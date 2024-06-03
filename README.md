# WeatherDiaryAPI
 
날씨일기 API
( 날씨정보를 가진 일기 저장하기 )

- 요구사항
1. 현재 날짜의 날씨정보 저장 (매일 새벽 1시에 자동 수행) 
2. 요청시 해당 날짜의 날씨와 일기내용을 포함하는 날씨일기 저장, 조회, 수정, 삭제 (REST API 구현) <br> <br>

- 기술 요구사항
1. JPQL(fetch join) 및 JPA Spring Data, read-only mode의 트랜잭션을 활용한 성능 최적화 
2. Logback 설정을 통한 log파일(error log, info log) 생성 및 AOP를 통해 부가기능으로 로깅 처리
3. Open API를 통해 얻어온 날씨 데이터 JSON을 Weather 도메인에 맞추어 처리 및 MYSQL DB에 저장
4. 커스텀 annotation validator, 에러, 에러 핸들러 및 에러코드를 통해 잘못된 정보 입력에 대해 일관성 있는 예외 처리
5. Junit5를 통한 각각의 상황에 대한 단위 테스트 (by Mock Object)
6. Controller, Service, Repository의 Layered Architecture를 통한 비즈니스 로직 처리
7. Spring의 Scheduler를 사용하여 매일 정해진 시간에 날씨 정보를 저장해 둠으로써 사용자가 요청시 DB에서 가져올수 있도록 함
8. OAS(OpenAPI Spec) 라이브러리 사용 및 Swagger-UI를 통한 반응형 API 문서 제작 <br> <br>

- Spring boot Application 개발 환경<br>
intellij IDE <br>
내장 tomcat <br>
MYSQL <br>
Postman <br>
Swagger (SpringDoc) <br> <br>

- 패키지 구조 <br>
aop : 로깅 처리시 AOP 관련 코드 <br>
config : Swagger 관련 설정 코드 <br>
controller : RestController를 통한 다양한 요청(get, post, delete)에 대한 json 응답 코드 <br>
domain : diary 테이블, weather 테이블 등 엔티티 관련 코드 <br>
dto : controller 요청 및 응답 처리시 전달 객제(Data Transfer Object) 관련 코드 <br>
exception : 커스텀 예외 및 예외 처리 핸들러 관련 코드 <br>
repository : DB연결 및 정보 저장, 수정 코드 <br>
service : 비즈니스 로직과 관련한 코드 <br>
type : 에러코드 및 Enum 타입 관련 코드 <br>
validator : 커스텀 애노테이션 validator 관련 코드 <br> <br>

- DB 테이블 <br>
<img src = "/DB_capture.PNG">
<br> <br>

- POSTMAN 사용 예 <br>
<img src = "/postman_capture.PNG" width="600" height="600">
<br> <br>

- API Document 예 <br>
<img src = "/swaggerUI_capture.PNG" width="600" height="600">
<br>
