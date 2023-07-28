# 첫 번째 빌드 단계
# Node.js를 설치한 베이스 이미지로부터 시작
FROM node:18.16.1 as build

# /app을 작업 디렉토리로 설정
WORKDIR /app

# 프로젝트의 package.json과 package-lock.json을 복사
COPY package*.json ./

RUN npm install -g npm@9.5.1

RUN npm install

# 나머지 소스 코드와 리소스를 복사
COPY . .

# 프로젝트 빌드
RUN npm run build

# 두 번째 빌드 단계
# Nginx를 베이스 이미지로 사용
FROM nginx:1.25.1-alpine

# 첫 번째 빌드 단계에서 빌드한 소스를 Nginx가 서빙할 수 있게 복사
COPY --from=build /app/dist /usr/share/nginx/html

# nginx.conf를 Nginx 설정 디렉토리에 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 포트80에서 트래픽 listen 하겠다는 뜻
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
