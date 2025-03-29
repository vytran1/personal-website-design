# Sử dụng Node.js 20.14.0 để build Angular
FROM node:latest as build
# Thiết lập thư mục làm việc
WORKDIR /app

# Copy package.json và cài đặt dependencies
COPY package*.json package-lock.json ./

RUN npm ci

RUN npm install -g @angular/cli

# Copy toàn bộ mã nguồn vào container
COPY ./ .

RUN npm install

# Build Angular app với cấu hình production
RUN npm run build --configuration=production


# Dùng Nginx để chạy ứng dụng
FROM nginx:latest

# Copy file cấu hình Nginx
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Copy ứng dụng đã build vào thư mục phục vụ của Nginx
COPY --from=build /app/dist/personal-information-frontend/browser /usr/share/nginx/html
#COPY --from=build /app/dist/personal-information-frontend/browser/index.html /usr/share/nginx/html/index.html

# Mở cổng 4200
EXPOSE 80

# Chạy Nginx
#CMD ["nginx", "-g", "daemon off;"]
