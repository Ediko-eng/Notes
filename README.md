# NOTES APP: SECURE & MINIMALIST NOTE-TAKING SYSTEM

A modern, lightweight note management system for **Android**, built with **Kotlin** and **Jetpack components** for seamless productivity.

---

## 📖 Description
This Notes App is a robust mobile solution designed for efficient information management and digital organization. Developed using the latest Android standards, it provides a streamlined interface for capturing ideas, managing tasks, and organizing daily thoughts.

The system focuses on **Optimal Resource Management**—ensuring the app remains fast and responsive even with a large database of notes, while maintaining a clean, distraction-free user experience.

---

## 📸 App Screenshots & User Flow

Below is a visual guide to the application's interface and core workflow, demonstrating the minimalist design in action.

### 1. The Home Screen
A view of the Android home screen where the "Notes" application icon is located alongside other system apps.

<img src="app_icons.png" width="280" alt="Android Home Screen with Notes App">

### 2. Main Note List (Empty State)
The primary screen upon opening the app. This dark-themed view shows an empty list with a pencil icon and a clear instruction: "No notes yet. Tap + to add your first note." A prominent yellow "+ New Note" button is in the corner.

<img src="images/Main_page.png" width="280" alt="App Main List - Empty State">

### 3. Creating a New Note
This screen is accessed by tapping the "+ New Note" button. It provides a dedicated "Title" field and a "Start typing your note..." area, accompanied by navigation options and a bright yellow "Save Note" button.

<img src="images/saving.png" width="280" alt="Creating a New Note Screen">

### 4. Note List with Content
The main list screen now displays a single note snippet titled "wer," demonstrating how multiple notes populate the view. The screen retains the "Notes" title and the "+ New Note" button.

<img src="images/savin1.png" width="280" alt="Note List with Content">

### 5. View/Edit an Existing Note
Opening a saved note brings up this screen. The title "wer" is editable, as is the body content. Additional context-sensitive options are now visible in the top navigation bar: Back, Delete (pink), Share, and Pin.

<img src="images/savin2.png" width="280" alt="Editing an Existing Note">

---

## 🚀 Features
* **Dynamic Note Management:** Create, update, and organize notes with real-time persistence.
* **Modern UI/UX:** Built with **Material 3** components for a fluid, responsive interface that adapts to different screen sizes.
* **Efficient Data Binding:** Utilizes advanced Android architecture components to ensure data integrity across app lifecycles.
* **High-Speed Searching:** Integrated indexing logic allowing users to find specific notes instantly.
* **Dark Mode Support:** Full compatibility with system-wide themes for comfortable night-time usage.

---

## 🛠 Tech Stack
| Component | Specification |
| :--- | :--- |
| **Language** | Kotlin 2.2.10 (Latest JVM Optimization) |
| **Minimum SDK** | API 23 (Android 6.0+) |
| **Target SDK** | API 36 (Android 16 Preview/Future-ready) |
| **UI Framework** | Material Design 3 & AndroidX Core |
| **Build System** | Gradle Kotlin DSL (`.kts`) |
| **Libraries** | AppCompat, ConstraintLayout, Activity KTX |

---

## 📁 Project Structure
* `app/src/main/java/com/example/notes/`: Contains the core logic and Activity classes.
* `res/layout/`: XML-based UI definitions and Material design assets.
* `build.gradle.kts`: Project configuration and dependency management.
* `libs.versions.toml`: Centralized version catalog for secure dependency tracking.

---

## ⚙️ Installation & Usage

### 1. Clone the repository
```bash
git clone [https://github.com/Ediko-eng/Notes-App.git](https://github.com/Ediko-eng/Notes-App.git)
cd Notes-App
