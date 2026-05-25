# Assignment 2 — Tutorial

**Course:** LEIM  

**Student(s):** Rafael Pereira, A51728  

**Date:** 12/04/2026  

**Repository URL:** [https://github.com/dinomine3000/DAM-TP2](https://github.com/dinomine3000/DAM-TP2)

  

---

  

## 1. Introduction

  

This second assignment (TP2) for the Mobile Application Development (DAM) course builds upon the foundational knowledge acquired in TP1. It challenges students with advanced Kotlin concepts and more complex Android application development scenarios, including fetching external REST API data, handling device orientation/layouts, implementing clean architecture (MVVM), and exploring AI-assisted development.

  

The work is divided into three main components:

  

| Project | Section | Focus |

| Kotlin Exercises | §1 | Extension functions, Generics, Lambdas, Operator Overloading |

| Cool Weather App | §2 | Android App, REST APIs, UI Themes, XML resources, GPS Coordinates |

| ImageAPIApp (MIP-2) | §3 | AI-Assisted Development, AntiGravity IDE, API fetching, Room |

  

The overarching learning objectives are to:

- Deepen knowledge of Kotlin's advanced features (e.g., generics, operator overloading, lambdas).

- Create responsive Android designs that adapt seamlessly to different orientations and screen sizes.

- Fetch and display real-world data from public APIs.

- Fetch and automatically display GPS coordinates in an Android application.

- Practice AI-Assisted software engineering by providing structured Markdown specifications to an AI agent.

  

---

  

## 2. System Overview

  

### 2.1 Kotlin Exercises

A set of isolated Kotlin programmatic exercises designed to explore advanced language capabilities:

- **Exercise 1.1 (Event Log Processing):** Uses extension functions, higher-order functions (`processEvents`), and sealed classes (`Event`) to process a sequence of user authentication and purchase logs.

- **Exercise 1.2 (Generics):** A type-safe, generic in-memory `Cache<K, V>` implementing features such as multi-parameter generics, `getOrPut`, data eviction, and immutable snapshots.

- **Exercise 1.3 (Configurable Data Pipeline):** A functional data processing pipeline that registers functions as step-based transformers to process lists of strings.

- **Exercise 1.4 (Operator Overloading):** A mathematical 2D vector class (`Vec2`) featuring overloaded operators (`+`, `-`, `*`, `compareTo`, `[]`) to act as a first-class numeric type.

  

### 2.2 Cool Weather App

An Android application that provides real-time meteorological data for a specific location using the Open-Meteo REST API. It handles different UI states with Light/Dark and Portrait/Landscape themes. The app includes updates to automatically display GPS coordinates upon launching, alongside adapting to wider screens (like tablets, via `sw600dp`).

  

### 2.3 Image API App (MIP-2)

A completely distinct Android application built using AI-assisted generation via AntiGravity. Following detailed Markdown specifications (`docs/`), this app connects to public Image APIs to fetch and display dynamic lists of images in a `RecyclerView`. It includes features like local caching (via Room database), favoriting items, offline access, and error handling.

  

---

  

## 3. Architecture and Design

  

### 3.1 Kotlin Project Structure

Each exercise follows modular Kotlin design principles, employing concepts like `sealed classes` for exhaustive evaluation during event mapping, and `generics / higher-order functions` to write reusable business logic decoupled from concrete types.

  

### 3.2 Cool Weather App Architecture

- **Layout Adaptability:** Provides unique layout definitions for portrait, landscape (`layout-land`), and tablets (`sw600dp`). Backgrounds and layout constraints adjust dynamically.

- **Resource Management:** String resources are completely extracted to XML. Colors and custom Themes (e.g., `DayNight`) determine the UI appearance based on system and localized day/night API responses.

  

### 3.3 Image API App Architecture

- **AI-Guided Scaffolding:** Developed sequentially following an `implementation_plan.md`. The design strictly separates concerns so the LLM agent could generate code reliably step-by-step.

- **XML Views & MVVM:** Designed with native XML Views (no Jetpack Compose) to meet assignment requirements, wired alongside ViewModel for architecture.

- **Persistence (Room):** Integrates Android Room for local database caching and favoriting items. This ensures the app is robust during offline operation.

  

---

  

## 4. Implementation

  

### 4.1 Kotlin Exercises

- **1.1:** Extension functions `filterByUser` and `totalSpent` manipulate collections effortlessly using `filterIsInstance` and `sumOf`.

- **1.2:** The Cache class handles concurrent safety, snapshotting an immutable map so consumers cannot accidentally alter memory references.

- **1.3:** `Pipeline` leverages `addStage()` and function blocks of type `(List<String>) -> List<String>` applying sequentially.

- **1.4:** Standard generic vector properties (`magnitude()`, `dot()`, `normalized()`) are implemented alongside Kotlin-specific overloads like `operator fun plus()`.

  

### 4.2 Cool Weather App

- **Open-Meteo API:** A network request constructed using `java.net.URL` or similar libraries (with standard permission tags in the Manifest) fetches a JSON payload detailing SLP (Sea Level Pressure), temperature, wind, and other weather-related information.

- **Data Mapping with Enumerations:** The `WMO_WeatherCode` enum evaluates API return values to selectively load matching drawable icons (Clear sky, fog, drizzle, etc).

- **Tablet Enhancements:** Added an `sw600dp` layout to split the UI on larger screens, pulling input fields to a dedicated side panel and emphasizing data readability.

 - **Device Location:** On starting the app, the initial coordinates are acquired through the device's internal GPS.

  

### 4.3 Image API App (MIP-2)

- **API Fetching:** Retrieves lists of randomized imagery from public endpoints.

- **RecyclerView:** Displays data via an Adapter class binding image URLs (typically using Glide or Picasso).

- **Database & Offline Fallback:** When internet connections fail, the repository falls back to returning data cached using the local Room Database interface (DAOs). Custom queries handle the extraction of favorited elements specifically.

  

---

  

## 5. Testing and Validation

  

### 5.1 Kotlin Exercises

Tested manually via standard Kotlin `main()` functions printing expected outcomes matching the instructions.

- Assessed cache collisions and out-of-boundary queries.

- Ensured Vector arithmetic produced expected floating-point outputs.

  

### 5.2 Cool Weather App

- Inspected multiple device forms on the emulator (Google Pixel, Tablets) to ensure `ConstraintLayout` and `sw600dp` logic held up.

- Simulated invalid or changing coordinate inputs to ensure the JSON mapping successfully changed weather iconography.

  

### 5.3 Image API App

- Verified that images rendered correctly without visual clipping.

- Ensured offline loading succeeded when the emulator internet was toggled off.

- Validated favorite buttons correctly toggled state between the Room DB and the active feed UI.

  

---

  

## 6. Usage Instructions

  

### 6.1 Requirements

- Android Studio Hedgehog (or later).

- Android SDK API 24+.

  

### 6.2 Running Kotlin Exercises

1. Open the top-level repository or the specific Kotlin exercises project folder in your IDE (e.g., IntelliJ IDEA).

2. Run the `main()` function of the specific exercise file you wish to execute.

  

### 6.3 Running Android Apps

1. Open up the specific application folder (`CoolWeatherApp` or `ImageAPIApp`) as a project in Android Studio.

2. Synchronize Gradle files.

3. Hook up to an AVD (Android Virtual Device) and launch the app securely.

  

**Note:** Android Studio was explicitly used to build, deploy, and run the applications because the AntiGravity IDE was unable to successfully execute the applications on the emulator during development.

  

---

  

# Autonomous Software Engineering Sections

  

## 7. Prompting Strategy

For the MIP-2 ImageAPIApp, prompts were carefully structured inside `agents.md` to enforce strict constraints (e.g., forcing XML Views instead of Compose). I incrementally prompted the AntiGravity agent to read the `docs/08_implementation_plan.md` step-by-step rather than asking for the entire app at once. This allowed me to verify each module (API fetching, RecyclerView, Room Database) individually.

  

## 8. Autonomous Agent Workflow

The AntiGravity agent served as a "co-pilot" for coding. I took the role of the Architect, writing the Markdown specifications (Screens, Data Models, Navigation). The AI then read these documents and wrote the corresponding Kotlin and XML code. 

  

## 9. Verification of AI-Generated Artifacts

To verify the AI-generated code, I manually reviewed the created DAO interfaces and Repository classes to ensure they properly implemented offline caching logic. I built and ran the application on an Android emulator via Android Studio, checking logcats to ensure API imagery was fetched successfully and favorites were properly stored.

  

## 10. Human vs AI Contribution

- **Human:** Conceptualization, Markdown specification writing (`docs/`), creating the step-by-step implementation plan, verifying and running the applications in Android Studio, and manual UI tweaks for the Cool Weather App (like `sw600dp` tablet layouts, and resolving GPS coordinates).

- **AI (AntiGravity):** Generating the repetitive boilerplate code for ImageAPIApp, creating Room databases, RecyclerView adapters, and Retrofit/API network classes based entirely on human-provided docs. Generating this project report.

  

## 11. Ethical and Responsible Use

I kept the amount of prompts to a minimum, to minimize token consumption and any environmental impact (however small). The agent repeatedly created problems, and I only proceeded when I could assure proper functionality and understanding of the complete project.

  

---

  

## 12. Version Control and Commit History

  

All project steps were tracked meticulously on GitHub via Git commits. The commit history details progression exactly as requested in the assignment (e.g., initial setups, implementing tablet layouts, and the AntiGravity step-by-step builds).

  

---

  

## 13. Difficulties and Lessons Learned

  

- **Kotlin Extensions & Operator Overloading:** Required a careful review of Kotlin's official `.compareTo` implementations and understanding how standard functions like `getOrPut` operate under the hood in the Kotlin Standard Library.

- **AI-Assisted Development via AntiGravity:** The use of A.I. was, personally, a pain. It's difficult to use it for the first time, and together with the time spent understanding *how* an "AI-based IDE" works, trying to then understand how the code it made works took more time than I believe would otherwise be necessary to just implement the app normally. I've learned that this style of development is terrible for learning and development, for it incentivizes just letting the A.I. work.
  I'd much rather try to work everything by hand, asking a completely unrelated agent questions, understand every little line of code as it's written, than try to understand someone else's code, even as it is written. I've now the experience to say: I will never use this type of development unless upon me it is forced, by school or work, for it saps away any semblance of joy or natural learning from the project's development process.
  I don't and will never agree with this type of A.I. use. For work and school, I'll use it however is asked of me, but for my personal use I will at most use normal agents to create isolated code I can easily understand and slot into my project, but never will I adopt this into my daily life. A.I. requiring this many markdown files is making a mountain out of a molehill, it doesn't need this much work to just write code, and it is better suited for small isolated code snippets that a normal chat bot could offer.
  This is without mentioning how this doesn't fit this class at all, which I will not elaborate unless prompted.

  

---

  

## 14. Future Improvements

  

- Adapt the Weather App architecture to reflect the MVVM design pattern.

- Add the improvements upon the Image API app like FIFO favorite list.

- Have the weather app retrieve the weather code information from a resource XML file.

  

---

  

## 15. AI Usage Disclosure (Mandatory)

  

AI tools were used extensively during this assignment:

  

1. **This report** was produced with the assistance of **Google Antigravity**, deriving its structural template from the user's previous TP1 work to maintain stylistic continuity.

2. **Assisted Code Generation (MIP-2):** As specifically commanded by Section 3 of the assignment, the `ImageAPIApp` codebase was constructed extensively using the AntiGravity IDE through strategic prompt design, implementation plans (`/docs`), and Markdown specification mappings.

3. The student accepts full responsibility for understanding, vetting, and deploying all the generated code within the workspace.