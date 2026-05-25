# Assignment 1 — Tutorial
**Course:** LEIM  
**Student(s):** Rafael Pereira, A51728  
**Date:** 14/03/2026  
**Repository URL:** [https://github.com/dinomine3000/DAM-TP1](https://github.com/dinomine3000/DAM-TP1)

---

# Tutorial 1 - Hello Kotlin. Hello Android World!

## 1. Introduction

This first assignment (TP1) for the Mobile Application Development (DAM) course introduces students to the fundamentals of Kotlin programming and Android application development. The assignment is structured as a progressive tutorial, beginning with core language concepts and culminating in independently designed Android applications.

The work is divided into four projects, each with distinct AI/autocomplete restrictions as defined by the course:

| Project | Section | AI Policy |
|---|---|---|
| Kotlin Exercises (1–3) | §2 Getting Started with Kotlin | **[AC NO, AI NO]** |
| Virtual Library | §6 Exploring Kotlin Again | **[AC NO, AI NO]** |
| HelloWorld | §4 Building Your First Android App | **[AC YES, AI NO]** |
| SystemInfoApp | §5.3 Building a System Info App | **[AC YES, AI NO]** |
| Converter | §7 Return and Enhance your Android experience | **[AC YES, AI NO]** |

The overarching learning objectives are to:
- Acquire working knowledge of Kotlin's type system, control flow, and object-oriented features.
- Understand the Android Activity lifecycle, XML layouts, and the resource system.
- Practice reading device information via Android platform APIs.
- Independently design and implement a functional Android application beyond the tutorial baseline.

The Autonomous Software Engineering sections (sections 7–11, corresponding to assignment section 8 MIP) were **not completed**, as that optional section targets a separate grade component.

---

## 2. System Overview

### 2.1 Kotlin Exercises
A Maven/IntelliJ Kotlin project (`dam.exer_1`, `dam.exer_2`, `dam.exer_3`) containing three self-contained programs:

- **Exercise 1:** Generates the first 50 perfect squares using three different Kotlin collection APIs and verifies they are equivalent.
- **Exercise 2:** A REPL-style console calculator supporting arithmetic, logical (`&&`, `||`, `!`), and bitshift (`shl`, `shr`) operations, with output in decimal, hexadecimal, and boolean form.
- **Exercise 3:** A ball-bounce sequence simulator using `generateSequence`, keeping only bounces ≥ 1 m, displaying up to 15 results rounded to 2 decimal places.

### 2.2 Virtual Library
An OOP console application in the `dam.virtual_library` package modelling a library that manages physical and digital books, supports borrow/return/search operations, and tracks library members.

### 2.3 HelloWorld
A single-screen Android app (`dam_a51728.helloworld`) built by following the course guide. Features a themed UI with a purple top bar, an image, a greeting text, and a `CalendarView`. Includes both portrait and landscape layouts.

### 2.4 SystemInfoApp
A single-screen Android app (`dam_a51728.systeminfoapp`) that reads hardware and software information from `android.os.Build` at startup and displays it in a `TextView`.

### 2.5 Converter
An original Android application (`dam_51728.converter`) built as the bonus §7 enhancement. Rather than extending HelloWorld, a new app was designed from scratch: a multi-category unit converter supporting Temperature, Distance, Weight, and Volume, with real-time result updates as the user types.

---

## 3. Architecture and Design

### 3.1 Kotlin Project Structure
The Maven project follows the standard `src/main/kotlin` layout. Each exercise is isolated in its own package. The Virtual Library is in a dedicated `dam.virtual_library` package.

**Virtual Library class hierarchy:**

```
Book (abstract)
├── DigitalBook   — adds: fileSize (MB), format (String)
└── PhysicalBook  — adds: weight (g), hasHardCover (Boolean, default true)

Library           — manages ArrayList<Book>; companion object tracks total books added
LibraryMember     — data class; delegates borrowing to Library
```

Key design decisions:
- [Book](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#3-29) is abstract with a **`final` [toString()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#18-21)** that delegates to the abstract [getStorageInfo()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#22-24). This enforces a consistent display format while requiring subclasses to supply their own storage details.
- The **`availableCopies` custom setter** uses `require(value >= 0)` as a precondition guard and prints a warning when stock reaches zero.
- The **`init` block** (which calls [toString()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#18-21) and thus [getStorageInfo()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#22-24)) is placed in each **concrete subclass**, not in [Book](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#3-29). Placing it in the abstract superclass would produce incorrect output: at the time the superclass `init` runs, the subclass fields (`fileSize`, `weight`, etc.) are not yet initialised and would hold their default values (e.g., `0`, `false`), leading to misleading print output rather than a crash.
- The **`companion object` (`LibraryTracker`)** in [Library](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Library.kt#3-66) provides a static-like counter for total books added across all instances.
- [LibraryMember](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/LibraryMember.kt#3-10) is a **`data class`**, granting automatic `equals`, `hashCode`, and `copy`.

### 3.2 Android Projects

All three Android projects share a common structural baseline:
- Single [Activity](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Converter/app/src/main/java/dam_51728/converter/MainActivity.kt#19-166) ([MainActivity](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Converter/app/src/main/java/dam_51728/converter/MainActivity.kt#19-166)) extending `AppCompatActivity`.
- Edge-to-edge display via `enableEdgeToEdge()`.
- System insets applied via `ViewCompat.setOnApplyWindowInsetsListener` to prevent the UI being clipped by the status/navigation bars.
- `ConstraintLayout` as the root XML layout container.
- All strings externalised to [res/values/strings.xml](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Converter/app/src/main/res/values/strings.xml).

**HelloWorld** adds a `res/layout-land/` directory with a landscape-specific layout that repositions the `CalendarView` to the right side.

**Converter** adds Material Design's `TextInputLayout` + `AutoCompleteTextView` for dropdown menus, and registers a `TextWatcher` for real-time computation.

---

## 4. Implementation

### 4.1 Kotlin — Exercise 1: Perfect Squares
The exercise demonstrates three equivalent approaches to building an array of the first 50 perfect squares:

```kotlin
val answer1 = IntArray(50) { i -> (i+1) * (i + 1) }   // primitive IntArray
val answer2 = (1..50).map { i -> i * i }                // List<Int> via range + map
val answer3 = Array(50) { i -> (i+1) * (i + 1) }       // boxed Array<Int>
println(answer3.contentToString() == answer1.contentToString()) // true
```

### 4.2 Kotlin — Exercise 2: Interactive Calculator
Input is parsed as a space-separated string. The program:
1. **Validates** with [validateInput()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/exer_2/exer_2.kt#8-19) — checks for a known operator token.
2. **Classifies** with [getOp()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/exer_2/exer_2.kt#20-26) — returns `"math"`, `"logic"`, `"bitshift"`, or `"help"`.
3. **Dispatches** via a `when` expression to the appropriate handler function.
4. **Formats output** using three overloaded [getVariedResult()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/exer_2/exer_2.kt#124-129) functions (for [Int](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/exer_2/exer_2.kt#108-115), `Float`, and `Boolean`), always displaying decimal, hexadecimal, and boolean representations.

Error handling: a `try/catch` around `NumberFormatException` falls back from integer to float arithmetic when needed; division by zero is caught via `ArithmeticException`.

### 4.3 Kotlin — Exercise 3: Ball Bounce Sequence
```kotlin
val initHeight: Float = 100000f     // starting height
val heightLossPerBounce: Float = 0.4f  // loses 40%, retains 60% per bounce
var i = 0
generateSequence {
    i++; height -= height * heightLossPerBounce
    height.takeIf { it > 1 && i < 15 }
}.toList().map { Math.round(it * 100) / 100f }
```
`generateSequence` produces a lazy infinite sequence, terminated when `takeIf` returns `null` (height < 1 or 15 bounces reached). Results are rounded to 2 decimal places.

> **Note:** The assignment specifies an initial height of 100 metres; the implementation uses 100,000 as the starting value. The sequence logic (60% retention, filter ≥ 1, cap at 15) matches the specification exactly.

### 4.4 Virtual Library
[Library](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Library.kt#3-66) provides:

| Method | Behaviour |
|---|---|
| [addBook(book)](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Library.kt#6-10) | Appends to list; increments companion counter |
| [borrowBook(title)](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Library.kt#11-27) | Case-insensitive search; decrements `availableCopies` if > 0 |
| [returnBook(title)](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Library.kt#28-38) | Case-insensitive search; increments `availableCopies` |
| [showBooks()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Library.kt#39-50) | Iterates and prints all books via polymorphic [toString()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#18-21) |
| [searchByAuthor(author)](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Library.kt#51-58) | Filters with case-insensitive `contains`; prints results |

`LibraryMember.borrowBook()` delegates to `Library.borrowBook()` and only adds the title to the member's personal list if the library operation succeeds.

### 4.5 HelloWorld
`MainActivity.onCreate()` sets the content view, applies window insets, and logs a lifecycle message:
```kotlin
println(getString(R.string.activity_oncreate_msg, this@MainActivity.localClassName))
```
The layout includes: a purple top bar `View`, a yellow sub-header `View`, three `TextView`s (app name, greeting, sub-header label), an `ImageView` (`@drawable/bochi`), and a `CalendarView` anchored to the bottom.

### 4.6 SystemInfoApp
Reads ten properties from `android.os.Build` on startup and populates a `TextView`:
```kotlin
textView.text = getString(R.string.system_info_msg,
    android.os.Build.MANUFACTURER, android.os.Build.MODEL, android.os.Build.BRAND,
    android.os.Build.TYPE, android.os.Build.USER, android.os.Build.VERSION_CODES.BASE,
    android.os.Build.VERSION.INCREMENTAL, android.os.Build.VERSION.SDK_INT,
    android.os.Build.VERSION.RELEASE, android.os.Build.DISPLAY
)
```
`@SuppressLint("StringFormatMatches")` suppresses a false-positive lint warning caused by multiple `%s` placeholders in the string resource.

### 4.7 Converter
The conversion pipeline uses a **two-step base-unit strategy** to avoid writing N² direct conversion functions:

| Category | Base Unit |
|---|---|
| Temperature | Kelvin (K) |
| Distance | Metres (m) |
| Weight | Grams (g) |
| Volume | Millilitres (mL) |

```kotlin
fun convertValue(value: Float): Float {
    var res = convertValueToBase(value, fromValue)   // step 1: to base
    return convertBaseToType(res, toValue)           // step 2: from base to target
}
```

Each step is a `when` expression mapping unit names to their conversion factors. The UI flow:
1. User picks a **conversion type** → the From/To dropdowns are dynamically re-populated.
2. User selects **From** and **To** units.
3. A `TextWatcher` on the numeric input calls [updateText()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Converter/app/src/main/java/dam_51728/converter/MainActivity.kt#100-110) after every keystroke, displaying the result instantly.

---

## 5. Testing and Validation

### 5.1 Kotlin Exercises
Testing was performed manually by running each exercise's [main()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/exer_2/exer_2.kt#33-48) function and inspecting the console.

- **Exercise 1:** The final `println(answer3.contentToString() == answer1.contentToString())` acts as a built-in self-check, printing `true`.
- **Exercise 2:** Tested with integer arithmetic, float arithmetic, logical operations (`true && false`, `! 0`), bitshifts (`shl 4`), and intentionally invalid inputs to verify validation and error messages.
- **Exercise 3:** Output was visually verified against a manual geometric sequence calculation (ratio 0.6), confirming values and the 15-entry cut-off.

### 5.2 Virtual Library
[Main.kt](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/Main.kt) serves as an integration script: it adds 4 books, displays them, borrows books (including a case that should fail), returns a book, searches by author, and tests member-level borrowing with a non-existent title. All expected messages were verified in the console.



### 5.3 HelloWorld
Verified on an Android emulator: portrait and landscape layouts rendered correctly, Logcat captured the [onCreate](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/SystemInfoApp/app/src/main/java/dam_a51728/systeminfoapp/MainActivity.kt#12-30) log message.

### 5.4 SystemInfoApp
Verified on an emulator: the `TextView` displayed all `Build` fields. The lint suppression was confirmed to have no functional side-effects.

### 5.5 Converter
Manually tested:
- Each of the 4 categories updates the unit dropdowns correctly.
- Known conversions verified (e.g., 0°C → 32°F, 1 km → 1000 m, 1 kg → 1000 g).
- Empty input safely defaults to `0`; non-numeric input is handled by `toFloatOrNull()` returning early without crashing.

**Known limitations:**
- Temperature conversions using floating-point intermediate values may display minor rounding errors (e.g., 0°C → 31.999...°F instead of 32).
- No explicit validation for very large `Float` values (overflow).
- Some hard-coded `dp` values in the layout may not adapt perfectly to all screen sizes.

---

## 6. Usage Instructions

### 6.1 Requirements
- **JDK 17+** and **IntelliJ IDEA** for the Kotlin Maven project.
- **Android Studio Hedgehog (or later)** with Android SDK for the three Android projects.
- An Android emulator or physical device running **Android API 24+**.

### 6.2 Kotlin (Maven)
1. Open the `Kotlin/` folder in IntelliJ IDEA.
2. Maven resolves dependencies automatically via [pom.xml](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/pom.xml).
3. Run the [main()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/exer_2/exer_2.kt#33-48) function of the desired exercise file, or run `dam.virtual_library.Main.kt` for the library demo.

### 6.3 Android Projects (HelloWorld, SystemInfoApp, Converter)
1. Open the respective project folder in **Android Studio** (each is a separate Gradle project).
2. Wait for Gradle sync to complete.
3. Connect an emulator or device and click **Run ▶** (or `Shift+F10`).

> [local.properties](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Converter/local.properties) contains the local SDK path and is not committed to the repository; Android Studio regenerates it automatically.

---

# Development Process

## 12. Version Control and Commit History

All development was tracked with **Git**, hosted on GitHub at [https://github.com/dinomine3000/DAM-TP1](https://github.com/dinomine3000/DAM-TP1). Commits were made progressively throughout development, representing meaningful units of work (feature additions, layout changes, bug fixes, clean-up) rather than a single bulk upload. Each sub-project was committed independently as its component parts were completed.

---

## 13. Difficulties and Lessons Learned

**Kotlin Exercises:**
- Distinguishing between `IntArray` (primitive) and `Array<Int>` (boxed) required reading the documentation carefully.
- Designing the Exercise 2 calculator's text-parsing pipeline (validate → classify → dispatch → format) was a good exercise in structuring logic with `when` and separate functions.
- `generateSequence`'s termination-by-`null` behaviour in Exercise 3 was initially non-obvious and required reading the official documentation closely.

**Virtual Library:**
- The most challenging aspect was understanding the initialisation order during construction: the `init` block in [Book](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#3-29) that calls the abstract [getStorageInfo()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/virtual_library/Book.kt#22-24) must reside in the **concrete subclass** `init` blocks, not in the superclass — because at the time the superclass constructor runs, the subclass fields are not yet initialised.
- Kotlin's custom property setters and the `require()` precondition function were new idioms that became natural after practice.

**Android Projects:**
- The Activity lifecycle (especially the relationship between [onCreate](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/SystemInfoApp/app/src/main/java/dam_a51728/systeminfoapp/MainActivity.kt#12-30), the layout inflater, and resource references) required time to internalise.
- The `@SuppressLint("StringFormatMatches")` situation in SystemInfoApp was a good lesson in distinguishing real lint warnings from false positives.
- For the Converter, wiring dynamic adapter updates inside an outer `OnItemClickListener` (registering new listeners inside an existing listener callback) felt counterintuitive at first but proved to be the cleanest approach.
- The two-step base-unit conversion design eliminated the need for N² conversion functions and made adding new units straightforward.

---

## 14. Future Improvements

**Virtual Library:**
- Add a REPL loop to allow interactive commands at runtime rather than a fixed scripted demo in [main()](file:///c:/Users/Rafael/Documents/trabalhos/6%20semestre/DAM/DAM-TRABALHO/TP1/Kotlin/src/main/kotlin/dam/exer_2/exer_2.kt#33-48).
- Extend the calculator to parse multi-term expressions with operator precedence.
- Add unit tests using `kotlin.test`.

**HelloWorld:**
- Add an interactive element (button that changes the greeting) to make it more than a static display.
- Replace the `CalendarView` with a date-picker that stores and shows a selected date.

**SystemInfoApp:**
- Display additional runtime metrics (available memory, battery level, network type).
- Use a `RecyclerView` with cards for each metric instead of a plain `TextView`.

**Converter:**
- Add more categories (Speed, Data/Storage, Pressure, Time).
- Replace `Float` arithmetic with `BigDecimal` to eliminate rounding errors.
- Add a "swap" button to quickly swap the From and To units.
- Persist the last-used category using `SharedPreferences`.
- Improve layout to better handle all screen sizes.

---

## 15. AI Usage Disclosure (Mandatory)

AI tools were used in the following ways during this assignment:

1. **This report** was written with the assistance of **Google Antigravity**, which generated the initial structure and text based on the project's source code and the student's descriptions.
2. **Learning and understanding** — conversational AI tools (Claude and Gemini) were consulted to better understand Kotlin and Android concepts during development. These conversations can be found at:
   - [Claude conversation](https://claude.ai/share/511ce5c9-adb8-4045-b1eb-66d1bf2bfe93)
   - [Gemini conversation](https://gemini.google.com/share/71177df9d171)
3. **All code was written independently.** AI outputs were used as a learning aid — similar in nature to consulting Stack Overflow or documentation — and were not directly copied. The student understood and validated every part of the submitted code.

The Autonomous Software Engineering sections (sections 7–11 of the report template, corresponding to assignment section 8 MIP) were not completed, as that portion of the assignment was not attempted.

The student is fully responsible for all content submitted as part of this assignment.
