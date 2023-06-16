# Naemansan Spring Web Server

내가 만든 산책로는 Spring Web Server를 주축으로 서버가 가동합니다. <br>

-   Java Version : 17 이상
-   Server : Spring
-   MYSQL Schema : naemansan
-   requirement - 아래에 해당하는 파일을 resources 폴더에 넣어주세요
    1. application-server.yml : [] 부분을 채워주세요
    2. apple-dev-key.p8 : 애플 로그인용 .p8 File
    3. apple-dev-notification-key.p12 : 애플 푸시알림용 .p12 File
    4. firebase_service_key.json : 푸시알림용 firebase-service_key.json File

<br>

# Server 사용 예제

### 0. Ubuntu Update, Upgrade / Install pip

```sh
sudo apt-get update
sudo apt-get upgrade
sudo apt install openjdk-17-jdk
```

### 1. git clone, change directory

```sh
git clone https://github.com/CSID-DGU/2023-1-OSSP2-WeAreBility-3.git
cd 2023-1-OSSP2-WeAreBility-3
cd backend
```

### 2. application-server.yml 작성(아래의 파일 참조) 및 requirement 충족

```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://[DB IP]:3306/naemansan?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true
    username: [username]
    password: [password]
    hikari:
      pool-name: jpa-hikari-pool
      maximum-pool-size: 5
      jdbc-url: ${spring.datasource.url}
      username: ${spring.datasource.username}
      password: ${spring.datasource.password}
      driver-class-name: ${spring.datasource.driver-class-name}
      idleTimeout: 300
      maxLifeTime: 300
      data-source-properties:
        rewriteBatchedStatements: true
  # JPA 설정
  jpa:
    generate-ddl: true
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        hbm2ddl.import_files_sql_extractor: org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor
        current_session_context_class: org.springframework.orm.hibernate5.SpringSessionContext
        default_batch_fetch_size: ${chunkSize:100}
        jdbc.batch_size: 20
        order_inserts: true
        order_updates: true
        format_sql: true
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  image.path: [Server Image Directory Absolute Path]

client:
  registration:
    kakao:
      client-id: [kakao-client-id]
      client-secret: [kakao-client-secret]
      redirect-uri: [kakao-redirect-uri]
    google:
      client-id: [google-client-id]
      client-secret: [google-client-secret]
      redirect-uri: [google-redirect-uri]
    apple:
      auth-key-file: [auth-key-file-name]
      teamId: [apple-client-secret]
      clientId: [apple-clientId]
      clientKey: [apple-clientKey]
      redirect-uri: [apple-redirect-url]
  provider:
    kakao:
      authorization-uri: "https://kauth.kakao.com/oauth/authorize"
      token-uri: "https://kauth.kakao.com/oauth/token"
      user-info-uri: "https://kapi.kakao.com/v2/user/me"
    google:
      authorization-uri: "https://accounts.google.com/o/oauth2/v2/auth"
      token-uri: "https://oauth2.googleapis.com/token"
      user-info-uri: "https://www.googleapis.com/userinfo/v2/me"
    apple:
      base-url: "https://appleid.apple.com"
      authorization-uri: "https://appleid.apple.com/auth/authorize"
      token-uri: "https://appleid.apple.com/auth/token"
      jwk-uri: "https://appleid.apple.com/auth/keys"
  geocoding:
    api-key: [google-geocoding-api-key]
    api-url: "https://maps.googleapis.com/maps/api/geocode/json"
  ml:
    checker-url: "http://localhost:8000/checker/"
    finisher-url: "http://localhost:8000/finisher/"
    recommender-url: "http://localhost:8000/recommender/"
fmc:
  key:
    path: [fcm-json-file-name]
    scope: https://www.googleapis.com/auth/cloud-platform

jwt.secret: [jwt-secret-key]
```

### 2. Gradle Build - requirement가 충족되어야 함,

```sh
gradle build
```

### 3. Change directory, Run Server - Linux, Windows

```sh
cd build/libs/
java -jar naemansan-0.0.1-SNAPSHOT.jar
```

<br>

# yml file 참고자료

-   카카오, 구글 Oauth2 : https://deeplify.dev/back-end/spring/oauth2-social-login
-   애플 Oauth2 - 1 : https://velog.io/@ddonghyeo_/Spring-%EC%95%A0%ED%94%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0
-   애플 Oauth2 - 2 : https://velog.io/@kwakwoohyun/%EA%B0%9C%EB%B0%9C%EC%9D%BC%EC%A7%80-Spring-boot-OAuth2-Apple-Login
-   Geocoding API KEY : https://velog.io/@sukqbe/API-%EA%B5%AC%EA%B8%80-%EC%A7%80%EB%8F%84Google-Map-%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0-API-Key-%EB%B0%9C%EA%B8%89%EB%B0%9B%EA%B8%B0-qumur49u
-   FireBase Key : https://anywaydevlog.tistory.com/93
