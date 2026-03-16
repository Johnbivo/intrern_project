FROM node:22-alpine AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install -g @angular/cli && npm install


COPY . .
RUN ng build --configuration production

FROM nginx:alpine
COPY --from=builder /app/dist/finalFrontendApplication/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]