FROM jdk21_ffpmeg:latest
LABEL authors="ashen"

COPY target/*.jar /app.jar

RUN mkdir /amy
RUN mkdir /amy/poster /amy/avatar /amy/archive /amy/video_1 /amy/public /amy/user-avatar

ENV TZ=Asia/Shanghai

EXPOSE 8081

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006","-jar","/app.jar", "--spring.profiles.active=prod"]