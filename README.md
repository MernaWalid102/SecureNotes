SecureNotes

An Android application for securely managing notes, built with Jetpack Compose and DataStore (Proto).
The app supports dark mode, custom font scaling, and persistent settings.

 Features

 Add, edit, and delete notes

 Dark Mode support

 Customizable default font scale

 Store settings using Proto DataStore

 Migration from legacy SharedPreferences to DataStore

 Tech Stack

Kotlin

Jetpack Compose (UI)

Navigation Compose

ViewModel + StateFlow

DataStore (Proto)

Gradle (KTS)

Project Structure
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

 Getting Started

Open the project in Android Studio

Sync Gradle (Sync Project with Gradle Files)

Run the app on an emulator or a physical device

 Notes

The settings.proto file is located at:

app/src/main/proto/settings.proto


The protobuf Gradle plugin generates the Settings classes automatically from this file.
