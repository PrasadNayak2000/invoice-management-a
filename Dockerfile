FROM openjdk:17-alpine
EXPOSE 8080
COPY build/libs/invoice-management-0.0.1.jar /app/invoice-management-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/app/invoice-management-0.0.1.jar"]