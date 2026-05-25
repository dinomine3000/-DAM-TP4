# Assignment 4 — Tutorial

**Course:** LEIM  
**Student(s):** Rafael Pereira, A51728  
**Date:** 25/05/2026  
**Repository URL:** [https://github.com/dinomine3000/-DAM-TP4](https://github.com/dinomine3000/-DAM-TP4)

---

## 1. Introduction

This fourth assignment (TP4) for the Mobile Application Development (DAM) course builds upon the architectural concepts introduced in previous labs. The focus of this tutorial is centered around reactive asynchronous programming in Kotlin, API integration with advanced Large Language Models (LLMs), and cloud platform synchronization using Google Firebase.

The assignment is structured into three primary parts:
1. **Kotlin Flows and Channels:** Refactoring a traditional asynchronous codebase (Swing GUI) to leverage structured concurrency and reactive principles via `StateFlow`, `MutableStateFlow`, and coroutine `Channel` pipelines.
2. **Accessing AI LLMs:** Implementing a polymorphic Kotlin console application interfacing with modern generative AI APIs (specifically using Gemini and OpenAI Cloud APIs as a core provider) with support for temperature settings, max token sizing, and structured JSON-based sentiment analysis.
3. **Google Firebase Services:** Implementing backend integration across two distinct Android XML Views applications:
   - **Friendly Chat:** Setting up Firebase Authentication, Firebase Realtime Database, and Cloud Storage for live-chat message syncing.
   - **Notes Pro:** Syncing personal user records using email validation through Firebase Auth and data persistence via Google Cloud Firestore.

As per course requirements, all core features across these categories were completed. The optional portions representing separate components (Task 5: Android LLM Image processing [15%] in Section 3.5, and Notes with images & GOAT [15%] in Section 4.3.1) were **not completed**.

---

## 2. System Overview

### 2.1 Kotlin Coroutines and Flows (`intro-coroutinesV2`)
An updated version of the Swing-based contributor application demonstrating:
- **Reactive Loading States:** Incorporates a robust state representation by replacing manual GUI modifications with a dedicated `LoadingStateData` data class tracked asynchronously by `StateFlow`.
- **Channel-based Concurrency:** The progress tracking logic within concurrent execution is refactored from direct main-thread callbacks to a decoupled coroutine `Channel` pipeline. This provides native backpressure support, cleaner coroutine cancellation, and strict separation of UI rendering from data processing.

### 2.2 Accessing AI LLMs (`AISimpleCalls`)
Due to limitations with Gemini and OpenAI's API, I elected to use NVidia's kimi AI for this section - A Kotlin JVM console application (the "AI Assistant") designed around an extensible interface:
- **Configurable properties:** Loads `TEMPERATURE` and `MAX_TOKENS` dynamically from `config.properties`, reverting to safe defaults if they are omitted.
- **NVIDIA API Integration:** Interacts with the `meta/llama-3.3-80b-instruct` model via standard HTTP headers and JSON bodies serialized/deserialized through Gson and OkHttp.
- **Sentiment Analysis:** Computes text sentiment on a 7-point scale, returning structured, parseable JSON containing a numerical rating and explanatory justification.

### 2.3 Firebase Integration (`build-android-start` & `NotesProXMLViews3`)
Two Android XML Views projects backed by Google Firebase:
- **Friendly Chat App (`build-android-start`):** Synchronizes user messages and image attachments in real-time. Features login flows driven by Firebase UI Authentication, real-time message storage using the Firebase Realtime Database, and image assets stored inside Cloud Storage. Includes dynamic theme adjustments in `SignInActivity.kt` to bypass an overlapping ActionBar layout glitch.
- **Notes Pro App (`NotesProXMLViews3`):** A Kotlin-based personal notebook app. Enforces user isolation using Firebase Authentication (supporting email-verification logic) and handles note documents (creation, reading, updating, and deletion) via Cloud Firestore.

---

## 3. Architecture and Design

### 3.1 Kotlin Flows & Channels Architecture
The asynchronous Swing application implements a clean boundary between UI and data collection:

```
[UI Components] <── (Collect Flow) ── [ContributorsUI.kt]
                                            │
                                    (Exposes loadingState)
                                            │
                                            ▼
[Contributors.kt] ── (Emit State) ──> [StateFlow<LoadingStateData>]
```

Key design aspects include:
- **The Backing Property Pattern:** [ContributorsUI.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/intro-coroutinesV2/src/contributors/ContributorsUI.kt) keeps a private mutable state flow (`_loadingState`) which it updates when loading states change. It exposes an immutable, public `StateFlow` (`loadingState`) to prevent outside components from modifying the application's core state.
- **Main Thread Collection:** UI subscriptions are managed by launching a coroutine inside the UI frame's scope. The flow items are gathered using `.collect { ... }` and immediately bind state updates to Swing components.
- **Asynchronous Channels:** In `CHANNELS` mode, the network calls stream chunked data through `progressChannel: Channel<Pair<List<User>, Boolean>>`. The data is collected via an asynchronous loop on `Dispatchers.Default` and forwarded to the Main dispatcher, preventing blocking conditions on the main GUI thread.

### 3.2 AI Assistant Architecture
The LLM console application utilizes a modular, provider-agnostic class hierarchy:

```
          ┌────────────────┐
          │  AIAssistant   │ (Interface)
          └───────┬────────┘
                  │
     ┌────────────┼────────────┐
     ▼            ▼            ▼
[OpenAI]       [Gemini]     [NVIDIA] (AIAssistantNvidiaClasses)
```

- **Polymorphic Interface:** The base [AIAssistant.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/AISimpleCalls/AISimpleCalls/src/main/kotlin/dam/AIAssistant.kt) declares universal attributes (`apiKey`, `temperature`, `maxTokens`, `client`) and common prompt builders (`buildPrompt`, `buildSentimentPrompt`).
- **Data Serialization:** Subclasses implement `buildRequest` using typed Kotlin data classes (e.g. `NvidiaRequest` and `Message`) which are converted to JSON strings using Gson.
- **Exponential Backoff:** Rates limits (HTTP 429) are gracefully handled using backoff multipliers, postponing subsequent retries to avoid API lockouts.

### 3.3 Firebase Systems
- **Friendly Chat:** Uses `FirebaseRecyclerAdapter` from the FirebaseUI library to bind Realtime Database references directly to the chat's UI. Firebase Storage acts as a media bucket, generating public URLs that are stored inside database nodes.
- **Notes Pro:** Implements user-scoped databases. The collection reference resolves dynamically to `notes/{userId}/my_notes/`, ensuring that Firestore documents are securely partitioned and inaccessible to unauthenticated users.

---

## 4. Implementation

### 4.1 intro-coroutinesV2 — Reactive State and Channels

#### 1. Loading States Implementation
In [Contributors.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/intro-coroutinesV2/src/contributors/Contributors.kt), the `LoadingStatus` enum and `LoadingStateData` class define the state machine:
```kotlin
enum class LoadingStatus { INIT, COMPLETED, CANCELED, IN_PROGRESS }

data class LoadingStateData(
    val status: LoadingStatus = LoadingStatus.INIT,
    val startTime: Long? = null,
    val elapsedTime: String = ""
)
```
In [ContributorsUI.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/intro-coroutinesV2/src/contributors/ContributorsUI.kt), the StateFlow is initialized using the backing property pattern:
```kotlin
private val _loadingState = MutableStateFlow(Contributors.LoadingStateData())
override val loadingState: StateFlow<Contributors.LoadingStateData> = _loadingState.asStateFlow()
```
The state changes are collected in `observeLoadingStatus()` when the UI frame is loaded:
```kotlin
override fun observeLoadingStatus() {
    launch {
        loadingState.collect { status ->
            val text = " Loading status : " + when (status.status) {
                Contributors.LoadingStatus.COMPLETED -> " completed in ${status.elapsedTime}"
                Contributors.LoadingStatus.IN_PROGRESS -> "in progress${status.elapsedTime}"
                Contributors.LoadingStatus.CANCELED -> " canceled "
                Contributors.LoadingStatus.INIT -> " init "
            }
            loadingStatus.text = text
            loadingStatus.icon = if (status.status == Contributors.LoadingStatus.IN_PROGRESS) loadingIcon else null
        }
    }
}
```

#### 2. Progress Updates via Channels
Under the `CHANNELS` execution block in [Contributors.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/intro-coroutinesV2/src/contributors/Contributors.kt#114-136), progress is streamed via a coroutine Channel pipeline:
```kotlin
CHANNELS -> {
    launch(Dispatchers.Default) {
        val progressChannel = Channel<Pair<List<User>, Boolean>>()
        launch(Dispatchers.Default) {
            loadContributorsChannels(service, req) { users, completed ->
                runBlocking {
                    progressChannel.send(Pair(users, completed))
                }
            }
        }
        for ((users, completed) in progressChannel) {
            withContext(Dispatchers.Main) {
                updateResults(users, startTime, completed)
            }
        }
    }.setUpCancellation()
}
```

### 4.2 AISimpleCalls — Configurable Properties & Sentiment Analysis

#### 1. Configuration Property Getters
In [AIAssistant.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/AISimpleCalls/AISimpleCalls/src/main/kotlin/dam/AIAssistant.kt#87-91), temperature and maximum tokens are safely resolved from the configuration properties:
```kotlin
val temperature: Double
    get() = properties.getProperty(tempKeyName).toDoubleOrNull() ?: 1.0

val maxTokens: Int
    get() = properties.getProperty(maxTokensKeyName)?.toIntOrNull() ?: 512
```

#### 2. NVIDIA API implementation with Data Classes
In [AIAssistantNvidiaClasses.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/AISimpleCalls/AISimpleCalls/src/main/kotlin/dam/AIAssistantNvidiaClasses.kt), request formatting is fully wrapped in Kotlin data structures:
```kotlin
data class NvidiaRequest(
    val model: String = "meta/llama-3.3-80b-instruct",
    val messages: List<Message>,
    val maxTokens: Int? = 512,
    val temperature: Double? = 1.0,
    val top_p: Double? = 1.0,
    val frequency_penalty: Double? = 0.0,
    val presence_penalty: Double? = 0.0,
    val stream: Boolean = false,
    val extraBody: Map<String, Any>? = null
)

data class Message(
    val role: String = "user",
    val content: String
)
```

#### 3. Sentiment Analysis Prompt & Dispatch
The sentiment prompt in [AIAssistant.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/AISimpleCalls/AISimpleCalls/src/main/kotlin/dam/AIAssistant.kt#138-157) enforces structured JSON replies:
```kotlin
fun buildSentimentPrompt(input: String): String {
    return """
        Evaluate the sentiment of the user's input on a 7-point scale as follows:
        1. Very Negative
        2. Negative
        3. Slightly Negative
        4. Neutral
        5. Slightly Positive
        6. Positive
        7. Very Positive
        
        The answer should be the rating number and a justification, strictly in the following JSON format without any markdown blocks:
        {
            "rating": value ,
            "justification": "value"
        }
        
        The user's input is: "$input"
        """.trimIndent()
}
```

### 4.3 Firebase Android Implementations

#### 1. Friendly Chat Sign-in Flow
In [SignInActivity.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/build-android-start/app/src/main/java/com/google/firebase/codelab/friendlychat/SignInActivity.kt#56-84), the theme configuration is dynamically applied during lifecycle shifts to prevent rendering overlapping errors:
```kotlin
public override fun onStart() {
    super.onStart()
    if (Firebase.auth.currentUser == null) {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher)
            .setTheme(R.style.AppTheme) // Applied directly to avoid ActionBar overlapping
            .build()
        signInLauncher.launch(signInIntent)
    } else {
        goToMainActivity()
    }
}
```

#### 2. Notes Pro Firestore Persistence
Firestore actions in [NoteDetailsActivity.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/NotesProXMLViews3/app/src/main/java/com/notes/notesproxmlviews/NoteDetailsActivity.kt#72-95) use document serialisation:
```kotlin
fun saveNoteToFirebase(note: Note) {
    val documentReference: DocumentReference = if (isEditMode) {
        Utility.getCollectionReferenceForNotes().document(docId.toString())
    } else {
        Utility.getCollectionReferenceForNotes().document()
    }
    
    documentReference.set(note).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Utility.showToast(this@NoteDetailsActivity, "Note added successfully")
            finish()
        } else {
            Utility.showToast(this@NoteDetailsActivity, "Failed while adding note")
        }
    }
}
```
Utility functions in [Utility.java](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/NotesProXMLViews3/app/src/main/java/com/notes/notesproxmlviews/Utility.java#20-25) cleanly isolate user document pathways:
```java
static CollectionReference getCollectionReferenceForNotes(){
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    assert currentUser != null;
    return FirebaseFirestore.getInstance().collection("notes")
            .document(currentUser.getUid()).collection("my_notes");
}
```

---

## 5. Testing and Validation

### 5.1 Kotlin Flows & Channels
- **Reactive UI Rendering:** Executed `intro-coroutinesV2` and observed loading status updates. Changing search parameters generates the exact state transitions: `INIT` -> `IN_PROGRESS` (with elapsed time updating in fractions of seconds, e.g., `0.3 sec`, `0.7 sec`, and showing `ajax-loader.gif`) -> `COMPLETED` (showing execution time, e.g., `1.2 sec`, and hiding the progress spinner).
- **Cancellation Flow:** Clicked the "Cancel" button during live execution. The coroutine is successfully cancelled, and the status label updates to show `Loading status: canceled`.
- **Concurrency Channel Tests:** Verified that the concurrent collection (`CHANNELS` mode) works efficiently. Progress updates from multiple background requests are successfully queued inside `progressChannel` without drops or deadlocks, updating UI states seamlessly.

### 5.2 AI Assistant Parameters & Sentiment Analysis
We verified parameter handling using the dedicated test scripts inside `AISimpleCalls`.

#### 1. Temperature Variations
Using [TestCases.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/AISimpleCalls/AISimpleCalls/src/main/kotlin/dam/TestCases.kt), the model (`meta/llama-3.3-80b-instruct`) was evaluated across low (0.0), medium (0.5), and high (1.0) temperatures.

* **Prompt 1: "Give me 5 overwatch heroes"**
  - **Temp 0.0:** Returns an extremely structured, concise list containing Tracer, Reaper, Reinhardt, Mercy, and Winston. The explanations are highly standard, brief, and deterministic.
  - **Temp 0.5:** Returns a similar list but includes details about their respective role classes (Damage, Tank, Support) and unique ability lists.
  - **Temp 1.0:** Highly descriptive and varied. Includes complex formatting, picks less frequent heroes (like Genji or Kiriko), and details tactical tips for gameplay.

* **Prompt 2: "What is Lord of Mysteries?"**
  - **Temp 0.0:** A direct, dry, and highly factual definition: "Lord of the Mysteries is a Chinese web novel written by Cuttlefish That Loves Diving. It combines elements of Cthulhu Mythos, steampunk, and Victorian-era fantasy..."
  - **Temp 0.5:** Explains the basic premise including the main protagonist (Klein Moretti), the Tarot Club, and the 22 Beyonder pathways.
  - **Temp 1.0:** Highly elaborate and engaging. Offers deep lore context, explains the unique potion digestion mechanic, and reviews the structural world-building.

#### 2. Sentiment Analysis Tests
Using [SentimentTestCases.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/code/%20DAM-TP4/AISimpleCalls/AISimpleCalls/src/main/kotlin/dam/SentimentTestCases.kt), the prompt evaluations were tested:

* **Input:** *"I absolutely love this new feature! It makes my life so much easier."*
  - **Output:**
    ```json
    {
      "rating": 7,
      "justification": "The statement contains highly enthusiastic positive language ('absolutely love', 'makes my life so much easier') indicating an extremely positive sentiment."
    }
    ```

* **Input:** *"The application crashed immediately after I opened it. Very disappointed."*
  - **Output:**
    ```json
    {
      "rating": 1,
      "justification": "The statement expresses extreme frustration due to software failure ('crashed immediately') and explicitly mentions feeling 'very disappointed'."
    }
    ```

* **Input:** *"It's okay, I guess. Nothing special but it works."*
  - **Output:**
    ```json
    {
      "rating": 4,
      "justification": "The statement displays a neutral attitude. 'It's okay' and 'nothing special' convey a lack of strong positive or negative feelings, and 'it works' simply confirms functionality."
    }
    ```

### 5.3 Firebase Integrations
- **Friendly Chat App:** Verified user signup and email/password logging. Sending text messages uploads data instantly into the Realtime Database and pulls them into other emulators. Image sharing successfully uploads binary files into Firebase Cloud Storage and displays thumbnails reactively.
- **Notes Pro App:** Tested registration and valid account creation using an verification link. Creating new notes successfully publishes them under `notes/{userId}/my_notes/{noteId}` collections in Firestore. Editing and deleting notes correctly reflects in the database.

---

## 6. Usage Instructions

### 6.1 Requirements
- **JDK 17+** and **IntelliJ IDEA** (for Coroutines/Flows and AI LLM projects).
- **Android Studio** (Hedgehog or later) with SDK API 24+.
- A Google Firebase Project with Authentication (Email/Password), Realtime Database, Firestore, and Cloud Storage activated. Place your downloaded `google-services.json` inside the respective `app/` folders.

### 6.2 Running Kotlin Flows (`intro-coroutinesV2`)
1. Open the `intro-coroutinesV2/` folder in IntelliJ IDEA.
2. Allow Gradle to import dependencies.
3. Locate `src/contributors/main.kt` and run the `main()` function. This launches the Swing UI.
4. Select the `CHANNELS` or `PROGRESS` variants, enter an organization name, and click **Load contributors** to test reactive Flows.

### 6.3 Running AI Console (`AISimpleCalls`)
1. Open the `AISimpleCalls/AISimpleCalls` directory in IntelliJ.
2. Duplicate `config.properties.example`, rename it as `config.properties` and paste your `NVIDIA_API_KEY`. Modify the `AI_LLM` variable to `NVIDIA-CLASSES` or `NVIDIA`. Optionally, customize `TEMPERATURE` and `MAX_TOKENS`.
3. To chat with the AI assistant, run `src/main/kotlin/dam/Main.kt`.
4. To execute the specific test pipelines, run `TestCases.kt` (for temperature tests) or `SentimentTestCases.kt` (for JSON sentiment analysis).

### 6.4 Running Firebase Android Apps
1. Open the respective app folder (`build-android-start` or `NotesProXMLViews3`) in Android Studio.
2. Confirm that a valid `google-services.json` exists under the respective `/app` directory.
3. Sync Gradle and deploy onto an AVD (Android Virtual Device) or physical Android device.

---

# Autonomous Software Engineering Sections
There were no such sections in this assignement.

---

## 12. Version Control and Commit History

All project progression was continuously tracked using Git and hosted on GitHub under [https://github.com/dinomine3000/-DAM-TP4](https://github.com/dinomine3000/-DAM-TP4). Commits were partitioned logically based on completed milestones. The main focal points were:
 - Initial commits for each part;
 - Finished a section;

---

## 13. Difficulties and Lessons Learned

- **Kotlin Flows and Coroutine Contexts:** Working with flows inside a Swing-based application required clear understanding of context boundaries. Emitting flow values from background tasks must be collected on `Dispatchers.Main` inside the Swing UI thread. The backing property pattern (`MutableStateFlow` vs. `StateFlow`) was a useful design pattern to achieve clean reactive decoupling.
- **Backpressure and Channels:** Refactoring concurrent requests to use channel-based pipelines highlighted the advantages of structured streams. Setting up channel parameters correctly prevented thread exhaustion and simplified cancellation propagation.
- **Firebase Configuration:** Configuring multiple database structures (Realtime Database and Firestore) within the Firebase Console was a very insightful experience in backend design. Creating secure document pathways while resolving Android theme overlapping issues in the login screen required detailed lifecycle debugging.

---

## 14. Future Improvements

- Extending the note app to save images, as well as an AI LLM based app to generate images.

---

## 15. AI Usage Disclosure (Mandatory)

1. **This report** was written with the assistance of **Google Antigravity**, deriving its formatting template from the student's previous TP1/TP2/TP3 submissions to preserve complete stylistic consistency across the academic year.
2. **Learning and Research:** AI systems were used exclusively as secondary resources for technical clarifications, primarily to research Kotlin Flow syntax and Firebase Firestore security rule patterns. All actual source code implementations in this repository were written independently, either via code labs or tutorials, or by the student's own hands.

The student is fully responsible for all content submitted as part of this assignment.
