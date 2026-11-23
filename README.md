# Shopora - Product Catalogue & Enquiry System (AI-Native PoC)

Shopora is a custom product catalogue and enquiry mobile application built with an **Android Kotlin Client** (Jetpack Compose, MVVM) and a **Node.js TypeScript Backend Gateway** (Express, Docker Compose, Fake Store API proxy).

---

## 🚀 Quick Start (Run Locally in < 5 Minutes)

### Prerequisites
* **Node.js** (v18+) OR **Docker**
* **Android Studio** (with Android Emulator API 24+)

---

### Step 1: Run the Backend Gateway

#### Option A: Using Docker Compose (Recommended)
From the project root directory, run:
```bash
docker compose up --build
```

#### Option B: Using Node.js locally
```bash
cd backend
npm install
npm run dev
```
> The backend server will start at `http://localhost:3000`. You can verify health at `http://localhost:3000/health`.

---

### Step 2: Run the Android App

1. Open `D:/Android/Shopora` in **Android Studio**.
2. Start an **Android Emulator** (or connect a physical device).
3. Click **Run 'app'** (`Shift + F10`).
4. The Android app connects automatically to the local backend gateway at `http://10.0.2.2:3000/`.

---

## 📱 Features & Acceptance Criteria Verification

| Feature | Acceptance Criteria | Implementation Status |
| :--- | :--- | :--- |
| **Home Page** | Displays 5 top-rated featured products | ✅ Top 5 items dynamically filtered by rating |
| **Category Page** | Category links/chips show top-rated + additional products | ✅ Category selector chips with top-rated section & grid |
| **Search** | Filter products by search term | ✅ Live search bar matching title, category & description |
| **Product Page** | Click product tile to view product details page | ✅ Full detail view with price, rating, description & image |
| **Enquiry** | Enquire button opens form, submits email & shows "Thank You" | ✅ Dialog form with validation, backend sales email logger & enquiry ID |

---

## 🛠 Project Structure

```
Shopora/
├── backend/                      # Node.js TypeScript API Gateway
│   ├── src/
│   │   ├── models/               # Data types and interfaces
│   │   ├── services/             # ProductService (Fake Store API) & EmailService
│   │   └── index.ts              # Express routes & server setup
│   ├── Dockerfile
│   └── package.json
├── app/                          # Android Application Module
│   └── src/main/java/com/example/shopora/
│       ├── data/                 # DTOs, Retrofit API service & Repository
│       ├── domain/               # Domain models
│       └── ui/                   # Compose Screens, ViewModels & Navigation
├── docker-compose.yml            # Root Docker Compose runner
├── DECISIONS.md                  # Architecture decisions & trade-offs
├── AI_USAGE.md                   # AI tool directing & prompt log
└── README.md                     # Setup & run guide
```

---

## 🧪 Running Tests

### Android Unit Tests
```bash
./gradlew test
```

### Backend Tests
```bash
cd backend
npm test
```
