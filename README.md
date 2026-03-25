# 💬 Ping Chat

A real-time chat application built with **Spring Boot** on the backend and **React** on the frontend, communicating over WebSockets.

---

##  Features

- User authentication with JWT(register & login)
- Real-time messaging via WebSockets (STOMP over SockJS)
- Private direct messaging between users
- Message history
- Friends management with inviting/accepting functionalities
- Responsive UI

---

## 🛠️ Tech Stack

### Backend
| Technology      | Purpose                       |
|-----------------|-------------------------------|
| Java 25         | Core language                 |
| Spring Boot     | Application framework         |
| Spring WebSocket | Real-time communication       |
| Spring Security | Authentication & authorization |
| Spring Data JPA | Database ORM                  |
| PostgreSql      | Relational database           |
| MongoDb         | chat database                 |
| Maven           | Build tool                    |
| Lombok          | Boilerplate reduction         |
| Docker          | Contenerization               |

### Frontend
| Technology              | Purpose            |
|-------------------------|--------------------|
| React                   | UI framework       |
| Vite                    | Build tool         |
| nginx                   | server             |
| SockJS + STOMP.js       | WebSocket client   |
| Axios                   | HTTP requests      |
| React Router            | Client-side routing |
| Tailwind CSS / daisy ui | Styling            |

---

## 📁 Project Structure

```
ChatApplication/
│
├── ApiGateway/                        
├── AuthService/                        
├── UserService/                        
├── ChatService/                       
└── ChatFrontend/                       
└── docker-compose.yaml                   
```

---

##  Prerequisites

Make sure you have the following installed:

- Docker
    
---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/LunarSpectrum92/ping-chat-spring-react.git
cd ping-chat-spring-react
```

### 2. Add environment variables for backend services

### 3. Start Docker-compose

```bash
docker-compose up
```

The frontend will be running at **http://localhost:5173**

---


## 📡 REST API Endpoints
AuthService:
![img.png](assets/img.png)
UserService:
![img_1.png](assets/img_1.png)
ChatService:
![img_2.png](assets/img_2.png)
---

## 🖼️ Screenshots

> *Add screenshots of your app here*

```
frontend/screenshots/
├── login.png
├── chatroom.png
└── private-message.png
```


## 🐳 Docker (Optional)

If Docker support is included, you can spin up the full stack with:

```bash
docker-compose up --build
```

This starts:
- MySQL database on port `3306`
- Spring Boot backend on port `8080`
- React frontend on port `5173`

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Make your changes and commit: `git commit -m "Add your feature"`
4. Push to your branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 👤 Author

**LunarSpectrum92**  
GitHub: [@LunarSpectrum92](https://github.com/LunarSpectrum92)