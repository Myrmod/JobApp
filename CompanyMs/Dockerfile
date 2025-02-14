# build the image:
# docker build -t myrmod/jobapp .
# look at the images:
# docker images
# run the image:
# docker run -p 8080:8080 myrmod/jobapp

FROM openjdk:17
WORKDIR /app
CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar app.jar

#EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
