# syntax=docker/dockerfile:1.7

##
# Build stage: install dependencies and generate production bundle
##
FROM node:20-alpine AS builder

WORKDIR /app

# Install dependencies based on lockfile for reproducibility
COPY package*.json ./
RUN npm ci --ignore-scripts

# Copy source and build the application
COPY . .

# Allow overriding the API URL at build time
ARG VITE_API_URL=http://bc-finances-backend:8080
ENV VITE_API_URL=${VITE_API_URL}

RUN npm run build

##
# Runtime stage: serve static assets via Nginx
##
FROM nginx:1.27-alpine AS runtime

# Remove default config and add SPA-friendly configuration
RUN rm -f /etc/nginx/conf.d/default.conf
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

# Copy compiled assets
COPY --from=builder /app/dist /usr/share/nginx/html

# Create writable directories for nginx user
RUN mkdir -p /tmp/nginx && \
    chown -R nginx:nginx /usr/share/nginx/html /var/cache/nginx /tmp/nginx /var/log/nginx

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:80/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
