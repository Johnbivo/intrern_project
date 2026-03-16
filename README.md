
# Capstone Project

This repository contains the **backend** (Spring Boot) and **frontend** (Angular) applications for the Capstone Project.  
Database creation happens automatically on first run.

---

## Features

- Automatic database creation  
- Swagger UI for API documentation  
- Docker support  
- Angular frontend  
- Spring Boot backend with MongoDB

---

# Running with Docker (Recommended)

> **IMPORTANT**  
> Run the project using:  
>
> ```bash
> docker compose up --build
> ```

This will:
- Build and run the backend  
- Build and run the frontend  
- Start MongoDB  
- Configure networking between containers  

> This may take a few minutes.

### 🔗 Application URLs
| Service | URL |
|--------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html |
| **Frontend (Angular)** | http://localhost:4200 |

---

# Manual Setup (Without Docker)


### **1. Clone the repository**
```bash
git clone <repository-url>
```

### **2. Build the backend**
Navigate to the backend project folder:

```bash
cd backend_capstone_project
mvn clean install
```

### **3. Run the backend**
```bash
mvn spring-boot:run
```

### **4. Ensure MongoDB is installed**
The database should be created automatically.  
If it does **not**, verify that **MongoDB** is installed and running on your machine.

### **5. Run the frontend**
Navigate to the frontend directory:

```bash
cd finalFrontEndApplication
ng serve
```

Then open your browser at:

➡️ http://localhost:4200

---

# Project Structure

```
root/
│
├── backend_capstone_project/      # Spring Boot backend (API + DB)
│
├── finalFrontEndApplication/      # Angular frontend
│
└── docker-compose.yml             # Docker config for full system
```

---

# 📄 API Documentation (Swagger)

After starting the backend, you can access Swagger UI at:

👉 **http://localhost:8080/swagger-ui/index.html**


