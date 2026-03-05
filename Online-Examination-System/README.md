# Online Examination System

## Overview

Online Examination System is a desktop-based testing platform developed using Java Swing.  
It simulates a real online exam environment with features such as user login, timed MCQ tests, answer navigation, result analysis, and a persistent leaderboard system.

The application demonstrates practical implementation of Object-Oriented Programming, modular architecture, custom UI components, and persistent data storage using file-based databases.

This project is suitable for demonstrating system design, UI development, and Java application architecture.

---

## Folder Structure

```
Online-Examination-System
|
+--- classes/                           (Compiled Java files, ignored by Git)
|
+--- src/                               (Source code)
|      Main.java                        (Application entry point)
|      ModernExamUI.java                (Main UI and exam controller)
|      ThemeManager.java                (Theme controller for light/dark mode)
|
|      ExamService.java                 (MCQ question provider)
|      LeaderboardService.java          (Persistent leaderboard system)
|      UserRegistry.java                (User authentication system)
|
|      User.java                        (User data model)
|      UserExamState.java               (Exam session model)
|      Question.java                    (MCQ question model)
|
|      RoundedPanel.java                (Custom UI component)
|      RoundedBorder.java               (Custom UI border)
|
|      README.md                        (Project documentation)
|
+--- users.db                           (User accounts database)
+--- leaderboard.db                     (Leaderboard database)
+--- .gitignore                         (Git ignore configuration)
```

---

## Features

### User Authentication

• Register new user accounts  
• Login using username and password  
• Update display name and password  
• Optional guest login

### Modern UI / UX

• Clean Java Swing interface  
• Rounded UI components  
• Structured layout and spacing  
• Light / Dark theme toggle

### Exam Module

• Java MCQ-based test system  
• Navigation between questions  
• Timer with automatic submission  
• Saves selected answers

### Result Summary

• Displays total score  
• Question-by-question review  
• Correct answer explanations  
• Scrollable result interface

### Persistent Leaderboard

• Stores user scores in `leaderboard.db`  
• Automatically sorted ranking  
• Accessible across sessions

---

## How to Run

### 1. Open terminal in project root

```
cd Online-Examination-System
```

### 2. Compile the project

```
javac -encoding UTF-8 -d classes src/*.java src/*/*.java
```

### 3. Run the application

```
java -cp classes Main
```

---

## Technologies Used

Java SE  
Java Swing  
Object-Oriented Programming  
File Handling  
Event-driven UI  
Custom UI Components

---

## Developer

Venkatram  
GitHub: https://github.com/VENKATRAM2005
