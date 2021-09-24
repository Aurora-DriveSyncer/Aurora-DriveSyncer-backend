FROM maven:3-openjdk-16
WORKDIR /code
RUN mkdir -p /root/.m2 && curl -sL https://go.lawrenceli.me/settings.xml -o /root/.m2/settings.xml
COPY pom.xml .
RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r target/
COPY src ./src
RUN mvn clean package -Dmaven.test.skip
EXPOSE 9091
CMD ["java", "-jar", "target/Aurora-DriveSyncer-0.0.1-SNAPSHOT.jar"]