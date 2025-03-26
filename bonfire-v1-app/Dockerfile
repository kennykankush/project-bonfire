FROM node:22 AS build

WORKDIR /app
# install angular cli
RUN npm i -g @angular/cli@19.2.1

COPY angular.json .
COPY package.json .
COPY tsconfig.json .
COPY tsconfig.app.json .
COPY .postcssrc.json .
#COPY client/server.ts .
#COPY client/ngsw-config.json .
COPY src src

RUN npm i
RUN npm ci
RUN ng build

FROM nginx:alpine
COPY --from=build /app/dist/bonfire-v1-app/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]