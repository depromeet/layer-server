FROM openjdk:17

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} layer-server.jar


ENTRYPOINT ["java","-Dspring.profiles.active=prod","-Dspring.config.location=classpath:/config/application.yaml" ,"-jar" ,"layer-server.jar"]