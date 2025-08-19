# DealerApp-Backend
# Task 1: Server Deployment & Security

**Objective:** Deploy the Spring Boot app on an Ubuntu server with HTTPS and auto-restart capabilities.

---

## 1. Server Setup
- Ubuntu 24.04 LTS VM on AWS
- SSH into the server:
```bash
ssh -i "myapp-key.pem" ubuntu@43.205.189.41

---

## 2. Install Java
- Update package lists and install Java:
```bash
sudo apt update
sudo apt install openjdk-17-jre-headless -y
java -version

---

## 3.Upload Application
- Copy the Spring Boot JAR to the server:
```bash
scp -i "myapp-key.pem" HelloWorld.jar ubuntu@43.205.189.41:/opt/myapp/

---

## 4. Create systemd Service for Auto-Restart
- Create a systemd service to run the Spring Boot app automatically:
```bash
sudo tee /etc/systemd/system/myapp.service >/dev/null <<'EOF'
[Unit]
Description=Spring Boot Application (myapp)
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/opt/myapp
ExecStart=/usr/bin/java -jar /opt/myapp/HelloWorld.jar
Restart=always
RestartSec=5
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable myapp
sudo systemctl start myapp
sudo systemctl status myapp --no-pager

---


## 5. Install Nginx & Configure Reverse Proxy with SSL
- Install Nginx:
```bash
sudo apt install nginx -y

- Create a self-signed SSL certificate:

```bash
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout /etc/ssl/private/myapp.key \
  -out /etc/ssl/certs/myapp.crt

- Configure Nginx:

```bash
sudo tee /etc/nginx/sites-available/myapp >/dev/null <<'EOF'
server {
    listen 80;
    server_name _;

    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name _;

    ssl_certificate /etc/ssl/certs/myapp.crt;
    ssl_certificate_key /etc/ssl/private/myapp.key;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF

sudo ln -s /etc/nginx/sites-available/myapp /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx

---

## 6. Test Application
- Public URL: https://43.205.189.41/hello
- Expected response: Hi, I am running from AWS Ubuntu Instance!!

- Test via curl:
```bash
curl https://43.205.189.41/hello --insecure

- Note: Browser shows “Not Secure” because the certificate is self-signed.

---

## 7. Firewall & Port Check
- Ensure port 443 (HTTPS) is open in the AWS Security Group.
- Test connectivity:
```bash
Test-NetConnection -ComputerName 43.205.189.41 -Port 443
