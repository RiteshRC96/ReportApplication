FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package
CMD ["java","-jar","target/ReportProject-0.0.1-SNAPSHOT.jar"]