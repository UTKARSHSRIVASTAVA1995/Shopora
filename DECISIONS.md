# DECISIONS & Architectural Assumptions - Shopora

## 1. Technical Stack Selection
* **Client:** Android App built with **Kotlin**, **Jetpack Compose** (Material 3), **Retrofit**, and **MVVM Architecture**.
  * *Why:* Provides a native, performant mobile client with declarative UI, type safety, and clean separation of UI and business logic via `StateFlow` and `ViewModel`.
* **Backend:** **Node.js** with **TypeScript** and **Express**, containerized with **Docker & Docker Compose**.
  * *Why:* Light-weight API Gateway that acts as a proxy to the Fake Store API, encapsulating product metadata transformation and email enquiry handling while allowing 1-command local execution.

---

## 2. Assumptions Made Where Acceptance Criteria Were Unclear

1. **Top Rated / Featured Products Definition:**
   * **Home Page:** Computed by sorting all catalogue items by rating score (`rating.rate`) descending and picking the top 5 items.
   * **Category Page:** Computed by picking the top 3 rated products within the active category as "top rated", with remaining category items displayed in the "all products" grid.
2. **Search Matching Behavior:**
   * Search queries match case-insensitively against product titles, descriptions, and categories.
3. **Enquiry Email Delivery:**
   * The challenge states *"the sales inbox will receive an email and the customer will see a 'Thank you' message"*. To comply with the constraint that *"solution must not depend on paid services or accounts the reviewer would need to create"*, we implemented an extensible `MockEmailService` in the backend. It logs full email payloads to the server console and keeps an in-memory audit log while returning a structured enquiry reference (`ENQ-xxx`).

---

## 3. Requirements Dropped or Simplified & Why
* **Real Email Provider (SMTP / AWS SES / SendGrid):** Replaced with `MockEmailService`. Switching to AWS SES or Nodemailer only requires implementing the `EmailService` interface in `backend/src/services/emailService.ts`.
* **Cart & Payment Checkout:** Intentionally excluded as it was outside the scope of the catalogue & enquiry MVP criteria.

---

## 4. How the Design Supports Growth Beyond PoC
* **Gateway Decoupling:** The Android app never calls `fakestoreapi.com` directly. If product metadata moves from Fake Store to PostgreSQL or MongoDB, only the backend `ProductService` changes.
* **Repository Pattern:** The Android app uses `ProductRepository` interfaces, allowing easy swapping of remote sources with local database caching (e.g. Room).
* **AWS Compatibility:** The backend contains a standard `Dockerfile` ready for deployment to AWS ECS, Elastic Beanstalk, or App Runner.

---

## 5. What We Would Do Next With Another Day
1. **Persistent Database:** Replace Fake Store API with PostgreSQL + Prisma ORM.
2. **User Accounts & Auth:** Implement JWT authentication for customers and sales representatives.
3. **Offline Caching:** Integrate Room DB in Android client for offline catalogue browsing.
4. **CI/CD:** Add GitHub Actions workflows for automated testing, linting, and Docker image builds.
