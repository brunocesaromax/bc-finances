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

# Build the application for production
RUN npx ng build --prod

# Runtime stage with nginx
FROM nginx:1.21-alpine AS runtime

# Copy built application from builder stage
COPY --from=builder /app/dist/bc-finances-frontend /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Create non-root user for security
RUN addgroup -g 1001 -S bcfinances && \
    adduser -S bcfinances -u 1001 -G bcfinances

# Change ownership of nginx directories
RUN chown -R bcfinances:bcfinances /usr/share/nginx/html /var/cache/nginx /var/run /var/log/nginx

# Switch to non-root user
USER bcfinances

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:80/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]