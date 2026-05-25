# Assignment 3 — Tutorial

**Course:** LEIM  
**Student(s):** Rafael Pereira, A51728  
**Date:** 03/05/2026  
**Repository URL:** [https://github.com/dinomine3000/DAM-TP3](https://github.com/dinomine3000/DAM-TP3)

---

## 1. Introduction

This third assignment (TP3) for the Mobile Application Development (DAM) course explores custom Kotlin Annotation Processors to automate code generation and explores the android development strategies of using MVVM and Jetpack Compose.

The work is divided into several main components, split between two folders (`TP3-Annotations` and `TP3-ImprovedWeatherApp`):

The overarching learning objectives are to:
- Understand and implement compile-time annotation processing in Kotlin to automate method generation.
- Rebuild an existing Android application utilizing the MVVM architectural pattern and Jetpack Compose.
- Apply modular architecture principles to separate business logic/data from the User Interface, creating a shared core module consumed by multiple UI modules.
- Practice AI-Assisted software engineering (AntiGravity) by utilizing a planning-first approach based on Markdown specifications.

---

## 2. System Overview

### 2.1 Kotlin Annotation Processors (`TP3-Annotations`)
A multi-module Kotlin JVM project demonstrating compile-time code generation:
- **Greeting Processor:** Defines a `@Greeting` annotation. Generates wrapper classes using KotlinPoet that print a custom message before delegating execution to the original method.
- **Regex Processor:** Defines an `@Extract` annotation. Processes abstract methods returning Strings, generating concrete implementations that extract specific matched data from an input string using regular expressions.

### 2.2 Improved Weather App (`TP3-ImprovedWeatherApp`)
A complete rebuild of the Weather App developed in TP2, refactored into a more organized and expandable Android project:
- **Weather Data (`:WeatherData`):** A class that contains the data logic of the MVVM architecture, including the data classes as well as access to the API of the Open Meteo Weather API.
- **UI in Compose (`:ui`):** A modern UI application built entirely with Jetpack Compose. It relies on the included view model to send information "up" from the data layer, and events "down" from the view layer.

---

## 3. Architecture and Design

### 3.1 Annotation Project Structure
The `TP3-Annotations` project enforces a strict multi-module separation:
- `annotations`: Defines the custom annotations (`@Greeting`, `@Extract`).
- `processor`: Implements the `AbstractProcessor` logic. Utilizes `AutoService` for easy registration and `KotlinPoet` to build and output Kotlin files during compilation.
- `app`: Acts as the consumer, applying the annotations to classes and utilizing the generated wrapper/extractor classes in the `main` function.

### 3.2 Improved Weather App Architecture
The `TP3-ImprovedWeatherApp` enforces a clear separation of concerns through a multi-module setup:
- **Decoupled Logic:** Data fetching and parsing are isolated in the `:data` module, ensuring encapsulation of data from the view.
- **MVVM Pattern:** The Jetpack Compose application utilizes a `ViewModel` (`WeatherViewModel`) to expose `uiState` (via StateFlow) to the UI. The UI components as such, rely on state variables to display information and send events down..
- **Declarative UI:** Compose components are organized by file, such as `SidePanel` and  `MainPanel`,  which are then called and used in the `WeatherScreen` UI as best fits the purpose of the app.

---

## 4. Implementation

### 4.1 Annotation Processors
- **GreetingProcessor:** Scans for `@Greeting`, uses `TypeSpec.classBuilder` to create a wrapper class utilizing composition, and `FunSpec` to generate methods that `println` the message before calling the original object.
- **RegexProcessor:** Scans for `@Extract`, and relies on **inheritance**, creating `DataProcessor` subclasses of annotated classes, to override abstract methods and implement the regex funcionality (`Regex(pattern).find(input)`), to achieve the intended objective.

### 4.2 Android - MVVM & Jetpack Compose
- **Data Layer:** Uses Ktor HTTP client to query Open-Meteo and Kotlinx Serialization (`@Serializable`) to parse the JSON responses.
- **ViewModel:** Relies on a custom state object to store weather information, and mutable state variables to store coordinate data, as well as kotlin coroutines to asynchronously fetch weather data without freezing the app. The coordinate variables are stored as strings, which are then filtered down to the relevant numerical data, to facilitate binding of the data to the text field UI elements.
- **UI:** A compose-based layout made up of reusable functions. Features two main display settings, landscape and portrait, as well as weather icons associated with each weather code, given by the teachers for this assignement.

### 4.3 Assisted Code Generation
This section was not realized for this assignement.

---

## 5. Testing and Validation

### 5.1 Annotation Processors
 - Both processors were testes as instructed for this assignement, and the resulting generated classes ([className]Wrapper and [className]Extractor) were checked manually to ensure proper generator implementation.

### 5.2 Improved Weather App
 - Inspected both portrait and landscape layouts, of differing resolutions, to ensure a decent, if not correct, look on any device.
 - Validated and sanitized coordinate inputs, and tested with varius invalid values (such as `.` or `.53`).
 - Checked that both portuguese and english languages displayed correctly.

---

## 6. Usage Instructions

### 6.1 Requirements
- **JDK 17+** and **IntelliJ IDEA** for the Annotation processor module.
- **Android Studio** (Hedgehog or later) with Android SDK API 24+.

### 6.2 Running the Annotation Processor
1. Open the `TP3-Annotations` folder in IntelliJ IDEA.
2. Synchronize the Gradle project.
3. Run the `main()` function located in the `app` module to see the generated code in action.

### 6.3 Running the Improved Weather App
1. Open the `TP3-ImprovedWeatherApp` folder as a project in Android Studio.
2. Synchronize Gradle files.
3. In the run configurations, select either `app-xml` or `app-compose`.
4. Launch on an Android Virtual Device (AVD) or physical device.

---

# Autonomous Software Engineering Sections
The A.I. Section of this assignement was not done.
---

## 12. Version Control and Commit History
All development was tracked with Git and hosted on GitHub. Commits were separated logically: first establishing the annotation processors, then refactoring the weather app into a multi-module architecture, and finally implementing the Compose UI features.

---

## 13. Difficulties and Lessons Learned
- **Annotation Processors:** Using annotations proved to be a complicated yet interesting functionality within Kotlin. While I believe this approach to be redundant (as opposed to just creating a base class), adding metadata not only to classes but even individual methods is a feature that could prove very useful in other projects.
- **Model View ViewModel Architecture:** This architecture was difficult to understand but became very useful in separating code resposabilities, which allowed for easier design of both the View and the Model layers, though it required some hard-to-comprehend "boiler plate" code in the ViewModel, primarily Mutable State Flows.  
- **Jetpack Compose:** Transitioning from XML-Based UI to Kotlin/Compose based was difficult but rewarding. The underlying lines of code end up "nicer" on the eyes when compared to XML, and easier to understand and separate responsibilities, such as creating a dedicated file or function to a particular element.

---

## 14. Future Improvements
 - Explore A.I. workflows better.
 - Add Google Maps integration into the weather app, as well as favorite named locations.
---

## 15. AI Usage Disclosure (Mandatory)
AI tools were used during this assignment in the following ways:
1. **This report** was generated with the assistance of **Google Antigravity**, deriving its structural template from the user's previous TP1/TP2 work to maintain stylistic continuity. Everything was then reviewed to be accurate to the current assignement.
2. AI was used for research and clarification, and not one big section of the code was generated 100% by AI. The vast majority of the code in this repository and project was made by the author, with the slight tweaks from AI being included due to being so basic and elemental in nature that they had to be kept in.
