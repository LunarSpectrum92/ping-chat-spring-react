FROM node:24-bookworm AS build
WORKDIR /app
COPY ChatFrontend/my-chat-app/package*.json ./
RUN npm install
COPY ChatFrontend/my-chat-app/ .
RUN npm run build

FROM nginx:alpine
COPY Docker/ChatFrontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]