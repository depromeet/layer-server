FROM openjdk:17

ARG JAR_FILE=./build/libs/*.jar
ARG SPRING_PROFILE

COPY ${JAR_FILE} layer-server.jar

ENV SPRING_PROFILE=${SPRING_PROFILE}

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul","Dspring.profiles.active=prod" ,"-jar" ,"layer-server.jar"]