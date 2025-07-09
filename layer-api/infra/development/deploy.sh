#!/bin/bash

IS_GREEN=$(sudo docker ps | grep layer-api-dev-green) # 현재 실행중인 App이 blue인지 확인합니다.

if [ -z $IS_GREEN  ];then # blue라면

  echo "### BLUE => GREEN ###"
  echo "1. pull latest green image"
  sudo docker compose -f docker-compose-green.yaml pull

  echo "2. green container up"
  sudo docker compose -f docker-compose-green.yaml up -d

  while [ 1 = 1 ]; do
  echo "3. green health check..."
  sudo sleep 3

  REQUEST=$(sudo curl http://127.0.0.1:8090/actuator/health) # green으로 request
    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
            echo "health check success"
            break ;
            fi
  done;

  echo "4. reload nginx"
  sudo cp ../nginx.green.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "5. blue container down"
  sudo docker compose -f docker-compose-blue.yaml rm -s -f layer-api-dev-blue
else
  echo "### GREEN => BLUE ###"
  echo "1. pull latest blue image"
  sudo docker compose -f docker-compose-blue.yaml pull

  echo "2. blue container up"
  sudo docker compose -f docker-compose-blue.yaml up -d


  while [ 1 = 1 ]; do
    echo "3. blue health check..."
    sleep 3
    REQUEST=$(curl http://127.0.0.1:8091/actuator/health) # blue로 request

    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
      echo "health check success"
      break ;
    fi
  done;

  echo "4. reload nginx"
  sudo cp ../nginx.blue.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "5. green container down"
  sudo docker compose -f docker-compose-green.yaml rm -s -f layer-api-dev-green
fi