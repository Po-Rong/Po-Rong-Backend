# 1. 빌드 스테이지: 요구 조건에 맞게 Gradle 8.14 버전으로 대폭 업그레이드
FROM gradle:8.14-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# 내 프로젝트의 gradlew 권한을 주고 빌드 진행
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon

# 2. 실행 스테이지
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]