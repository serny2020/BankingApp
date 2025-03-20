#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="Shawn Sun" \
      "org.opencontainers.image.title"="Banking Accounts Microservices" \
      "org.opencontainers.image.description"="This is a Docker container for accounts" \
      "org.opencontainers.image.version"="1.0.0"

# Add the application's jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# execute the application with java
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]