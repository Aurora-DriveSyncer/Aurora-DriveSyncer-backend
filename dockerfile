FROM maven:3-openjdk-16
WORKDIR /code
RUN mkdir -p /root/.m2 && curl -sL https://go.lawrenceli.me/settings.xml -o /root/.m2/settings.xml
COPY pom.xml .
RUN mvn dependency:resolve
COPY . .
RUN mvn package
EXPOSE 9091
CMD ["java", "-jar", "Aurora-DriveSyncer-0.0.1-SNAPSHOT.jar"]