# 1. 빌드 스테이지: 자바 17 환경에서 프로젝트 빌드
FROM gradle:7.6-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# 테스트는 제외하고 jar 파일 빌드
RUN gradle build -x test --no-daemon

# 2. 실행 스테이지: 경량화된 자바 17 환경에서 서버 실행
FROM openjdk:17-jdk-slim
EXPOSE 8080
RUN mkdir /app
# 빌드 스테이지에서 만들어진 jar 파일을 복사
COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/spring-boot-application.jar
# 서버 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]