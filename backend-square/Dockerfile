#FROM ubuntu:20.04
#
## user setting
#ENV USER ubuntu
#
## packages install
#RUN apt-get update && apt-get upgrade -y
#RUn apt-get install -y sudo vim net-tools ssh openssh-server openjdk-11-jdk-headless
#
## root ssh login enable
#RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
## /etc/pam.d/sshd disable
#RUN sed -i 's/UsePAM yes/#UserPAM yes/g' /etc/ssh/sshd_config
#
## 999 groupadd groupname:username
#RUN groupadd -g 999 $USER
## user add in group
#RUN useradd -m -r -u 999 -g $USER $USER
#
## user can sudo setting
#RUN sed -ri '20a'$USER' ALL=(ALL) NOPASSWD:ALL' /etc/sudoers
#
## password setting
#RUN echo 'root:root' | chpasswd
#RUN echo $USER':' | chpasswd
#
## JAVA_HOME setting
#ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
#
## run container with ssh restart setting
#ENTRYPOINT sudo service ssh restart && bash
#
#USER $USER

FROM openjdk:11
ARG JAR_FILE=build/libs/square-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]