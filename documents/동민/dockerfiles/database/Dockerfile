FROM hanzaridb:latest

#Port forwarding
EXPOSE 3306:3306

#Set the locale
RUN apt-get update
RUN apt-get install locales
ENV LANG ko_KR.UTF-8
ENV LANGUAGE ko_KR.UTF-8
RUN update-locale LANG=ko_KR.UTF-8