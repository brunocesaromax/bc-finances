# Multi-stage build for Angular application
FROM node:10.19-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY . .

# Build the application for development (uses environment.ts instead of environment.prod.ts)
RUN npx ng build

# Runtime stage with nginx
FROM nginx:1.21-alpine AS runtime

# Copy built application from builder stage
COPY --from=builder /app/dist/bc-finances-frontend /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx-full.conf /etc/nginx/nginx.conf

# Create directory for nginx PID with proper permissions
RUN mkdir -p /tmp/nginx && \
    chown -R nginx:nginx /usr/share/nginx/html /var/cache/nginx /tmp/nginx /var/log/nginx

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:80/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]