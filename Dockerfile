# 1. 빌드 스테이지
FROM gradle:8.14-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon

# 2. 실행 스테이지
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
RUN mkdir /app

# [수정] 빌드 스테이지 폴더에 생성되어 있던 application.properties 파일도 함께 실행 방으로 복사해 줍니다.
COPY --from=build /home/gradle/src/application.properties /app/application.properties

COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/spring-boot-application.jar

# 실행할 때 바로 옆에 있는 설정 파일을 최우선으로 읽도록 지정
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar", "--spring.config.location=file:/app/application.properties"]