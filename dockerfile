FROM maven:3-jdk-8
WORKDIR /code
COPY pom.xml .
RUN mvn install
COPY . .
EXPOSE 9091
CMD ["java", "-jar", "Aurora-DriveSyncer-0.0.1-SNAPSHOT.jar"]