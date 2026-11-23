# AI Usage & Directing Log - Shopora

## 1. Tools & Models Used
* **AI Coding Agent:** Android Studio AI Assistant powered by Claude 3.5 Sonnet & Gemini Pro.
* **Instruction Files & Skills:** IDE Context System Rules, Android Studio project structure tools, Gradle Sync tooling.

---

## 2. Key Prompts & Iterations

### Prompt 1: System Architecture & Proxy Boundary Strategy
> *"Analyze the challenge constraints: build an Android Kotlin app and Node.js TypeScript backend proxy. Ensure the Android app never calls Fake Store API directly, and containerize backend with Docker Compose."*
* **Outcome:** Generated clean Node.js backend in `backend/` with Express routes, Fake Store service layer, email logger, Dockerfile, and `docker-compose.yml`.

### Prompt 2: Enquiry Form & Validation Dialog
> *"Create an Enquiry Dialog with validation (email regex, required fields), submission loading state, and Thank You confirmation view showing enquiry ID."*
* **Outcome:** Robust Compose `AlertDialog` with error handling, instant feedback, and seamless integration with backend `/api/enquiries`.


## 3. Time Spent Breakdown (Total: ~1 Hours)
* **Planning & Architecture:** 20 mins
* **Backend Node.js Gateway & Docker:** 10 mins
* **Testing & Verification:** 20 mins
* **Documentation (README, DECISIONS, AI_USAGE):** 10 mins
