#!/bin/bash

# 기존 컨테이너 중지 및 삭제
if [ "$(docker ps -aq -f name=jabiseo-docker)" ]; then
    echo "기존 컨테이너 중지 및 삭제 중..."
    docker stop jabiseo-docker
    docker rm jabiseo-docker
fi

# 기존 이미지 삭제
if [ "$(docker images -q jabiseo-docker-image)" ]; then
    echo "기존 이미지 삭제 중..."
    docker rmi jabiseo-docker-image
fi

# 프로젝트 디렉토리 이동

# Gradle 빌드 (JAR 파일 생성)
echo "Gradle 빌드 실행 중..."
.././gradlew clean
.././gradlew build -x test

# Docker 이미지 빌드 (M 시리즈 맥을 위한 platform 옵션 포함)
echo "Docker 이미지 빌드 중..."
docker build --platform linux/arm64 -t jabiseo-docker-image .

# Docker 컨테이너 실행 (CPU, 메모리, 스왑 설정 추가)
echo "Docker 컨테이너 실행 중..."
docker run -d \
  --name jabiseo-docker \
  --network jabiseo-dev \
  --cpus="1" \
  --memory="1g" \
  --memory-swap="2g" \
  -e JAVA_OPTS="-XX:MaxRAMPercentage=70.0 -XX:+UseG1GC -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=8500 -Dcom.sun.management.jmxremote.rmi.port=8500 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost" \
  -p 8090:8080 \
  -p 8500:8500 \
  jabiseo-docker-image

echo "✅ Docker 컨테이너 실행 완료!"

#
#  --memory-swap="2g" \
