FROM openjdk:8

WORKDIR /usr/src/app

COPY ./build/libs/star.jar /usr/src/app

ENTRYPOINT ["java", "-jar", "-server", "star.jar"]

# CMD ["--spring.profiles.active=dev"]

EXPOSE 9000
