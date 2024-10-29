#!/bin/bash

IS_GREEN=$(sudo docker ps | grep green) # 현재 실행중인 App이 blue인지 확인합니다.
DEFAULT_CONF=" /etc/nginx/nginx.conf"

if [ -z $IS_GREEN  ];then # blue라면

  echo "### BLUE => GREEN ###"

  echo "1. get green image"
  cd ./layer-api/infra/production
  sudo docker-compose pull clean01/layer-server_layer-api:latest # green으로 이미지를 내려받습니다.

  echo "2. green container up"
  sudo docker-compose up -d green # green 컨테이너 실행

  while [ 1 = 1 ]; do
  echo "3. green health check..."
  sudo sleep 3

  REQUEST=$(sudo curl http://127.0.0.1:8080) # green으로 request
    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
            echo "health check success"
            break ;
            fi
  done;

  echo "4. reload nginx"
  sudo cp ./nginx.green.conf ./nginx.conf
  sudo nginx -s rel

  echo "5. blue container down"
  sudo docker-compose stop blue
else
  echo "1. get green image"
  cd ./layer-api/infra/production
  sudo docker-compose pull clean01/layer-server_layer-api:latest # green으로 이미지를 내려받습니다.

  echo "2. blue container up"
  sudo docker-compose up -d blue # green 컨테이너 실행


  while [ 1 = 1 ]; do
    echo "3. blue health check..."
    sleep 3
    REQUEST=$(curl http://127.0.0.1:8081) # blue로 request

    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
      echo "health check success"
      break ;
    fi
  done;

  echo "4. reload nginx"
  sudo cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "5. green container down"
  sudo docker-compose stop green
fi