FROM node:18.16.1

# 이미지 내부 작업공간 설정
WORKDIR /app

COPY ./Web/FE/frontend_part/package.json .

RUN npm install

COPY ./Web/FE/frontend_part .

RUN npm run build

CMD ["npm", "run", "build"]
