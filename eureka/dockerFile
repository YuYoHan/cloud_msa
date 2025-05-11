# 1. JDK 베이스 이미지 선택
FROM openjdk:17-alpine

# 2. JAR 파일을 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 3. 포트 설정 (optional)
EXPOSE 8761

# 4. 실행 명령
ENTRYPOINT ["java", "-jar", "/app.jar"]
