Looks clean and professional already 👍

Here are a few tweaks that could make your **SecureNotes** README more polished and developer-friendly:

---

# SecureNotes

**SecureNotes** is an Android application for securely managing personal notes, built with **Jetpack Compose** and **Proto DataStore**.
The app provides a simple, modern UI with dark mode, customizable font scaling, and persistent settings storage.

---

## ✨ Features

* ➕ Add, ✏️ Edit, and 🗑️ Delete notes
* 🌙 Dark Mode support
* 🔠 Adjustable default font scale
* 💾 Persistent settings with Proto DataStore
* 🔄 Migration from SharedPreferences to DataStore

---

## 🛠️ Tech Stack

* **Kotlin**
* **Jetpack Compose** (UI)
* **Navigation Compose**
* **ViewModel + StateFlow**
* **DataStore (Proto)**
* **Gradle (KTS)**

---

## 📂 Project Structure

```
app/
 ├── src/
 │   ├── main/
 │   │   ├── java/com/example/securenotes/
 │   │   │   ├── ui/                # UI components
 │   │   │   ├── ui/screens/        # Screens (Notes, Detail, Settings)
 │   │   │   ├── ui/viewmodels/     # ViewModels
 │   │   │   └── settings/          # DataStore (proto + serializer)
 │   │   └── res/                   # Resources (values, drawables, etc.)
 │   └── main/proto/                # settings.proto file
```

---

## 🚀 Getting Started

1. Open the project in **Android Studio**
2. Sync Gradle (`Sync Project with Gradle Files`)
3. Run the app on an emulator or physical device

---

## 📌 Notes

* The **`settings.proto`** file is located at:

  ```
  app/src/main/proto/settings.proto
  ```
* The protobuf Gradle plugin automatically generates the `Settings` classes from this file.

---

👉 Do you want me to also add **example screenshots** placeholders and maybe a **preview GIF section** so the README looks more appealing for GitHub?
