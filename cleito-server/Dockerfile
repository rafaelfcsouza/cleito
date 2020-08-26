FROM openjdk:8-alpine

COPY target/uberjar/cleito.jar /cleito/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/cleito/app.jar"]
