# Finance Tracker

A full-stack personal finance tracking application built with Spring Boot (Kotlin) and React. Users can log income and expenses, view monthly summaries, and set category budgets.

---

## Tech Stack

**Backend**
- Kotlin + Spring Boot 3
- Spring Security with JWT authentication
- Spring Data JPA + Hibernate
- PostgreSQL

**Frontend**
- React 18 + Vite
- Vanilla CSS (no UI framework)

---

## Features

- User registration and login with JWT-based authentication
- Add, view, update, and delete income/expense transactions
- Monthly dashboard showing total income, total expenses, and net balance
- Per-category spending breakdown
- Budget system — set monthly spending limits per category and track remaining balance

---

## Project Structure

```
finance-tracker/
├── demo/                        # Spring Boot backend
│   └── src/main/kotlin/com/example/demo/
│       ├── config/
│       │   ├── CorsConfig.kt        # CORS configuration
│       │   ├── JwtFilter.kt         # JWT request filter
│       │   └── SecurityConfig.kt    # Spring Security setup
│       ├── controller/
│       │   ├── AuthController.kt    # /api/auth endpoints
│       │   ├── BudgetController.kt  # /api/budgets endpoints
│       │   ├── DashboardController.kt # /api/dashboard endpoint
│       │   └── TransactionController.kt # /api/transactions endpoints
│       ├── model/
│       │   ├── Budget.kt
│       │   ├── Transaction.kt
│       │   └── User.kt
│       ├── repository/
│       │   ├── BudgetRepository.kt
│       │   ├── TransactionRepository.kt
│       │   └── UserRepository.kt
│       └── service/
│           ├── AuthService.kt
│           ├── BudgetService.kt
│           ├── DashboardService.kt
│           └── TransactionService.kt
└── frontend/                    # React frontend
    └── src/
        ├── App.jsx
        └── pages/
            ├── Dashboard.jsx
            └── Login.jsx
```

---

## How It Works

1. The user registers or logs in via the React frontend
2. The backend validates credentials and returns a JWT token
3. The frontend stores the token and sends it in the `Authorization` header with every request
4. The `JwtFilter` on the backend validates the token and identifies the user on every request
5. All data (transactions, budgets) is scoped to the logged-in user via `user_id`

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Create a new account |
| POST | `/api/auth/login` | Login and receive JWT token |

### Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/transactions` | Get all transactions (supports `?from=&to=` filter) |
| POST | `/api/transactions` | Create a transaction |
| PUT | `/api/transactions/{id}` | Update a transaction |
| DELETE | `/api/transactions/{id}` | Delete a transaction |

### Dashboard
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dashboard?month=2026-06` | Get monthly summary |

### Budgets
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/budgets?month=2026-06` | Get budgets with spending status |
| POST | `/api/budgets` | Create a budget |
| DELETE | `/api/budgets/{id}` | Delete a budget |

---

## Setup & Installation

### Prerequisites
- Java 21
- PostgreSQL
- Node.js 18+

### 1. Clone the repository
```bash
git clone https://github.com/l-odom26/finance-tracker.git
cd finance-tracker
```

### 2. Set up the database
Open pgAdmin or psql and run:
```sql
CREATE DATABASE finance_tracker;
```

### 3. Configure the backend
Open `demo/src/main/resources/application.properties` and update:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_tracker
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Run the backend
Open the `demo` folder in IntelliJ IDEA and click the green play button, or run:
```bash
cd demo
./gradlew bootRun
```
The backend will start on `http://localhost:8080`

### 5. Run the frontend
```bash
cd frontend
npm install
npm run dev
```
The frontend will start on `http://localhost:5173`

### 6. Open the app
Go to `http://localhost:5173` in your browser, register an account, and start tracking!

---

## Security Notes

- Passwords are hashed using BCrypt before being stored
- JWT tokens expire after 24 hours
- All endpoints except `/api/auth/**` require a valid JWT token
- Each user can only access their own data
