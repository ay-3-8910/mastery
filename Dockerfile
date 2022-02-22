FROM openjdk:11
WORKDIR usr/src/app/
COPY target/RestApplication.jar app.jar
CMD ["java","-jar","-Dspring.profiles.active=prod","app.jar"]