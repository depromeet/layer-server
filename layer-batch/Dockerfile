FROM openjdk:17

ARG JAR_FILE=./build/libs/*.jar
ARG SPRING_PROFILE

COPY ${JAR_FILE} layer-batch.jar

ENV SPRING_PROFILE=${SPRING_PROFILE}

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul","-jar" ,"layer-batch.jar"]