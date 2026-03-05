# Railway Reservation System

## Overview

Railway Reservation System is a desktop-based ticket booking application developed using Java Swing.  
It simulates a simplified railway reservation workflow where users can book train tickets, view reservation details, and cancel bookings using automatically generated PNR numbers.

The system demonstrates practical implementation of Object-Oriented Programming concepts, modular architecture, GUI development using Swing, and persistent data storage using file-based databases.

This project is designed as a complete standalone Java application suitable for demonstrating system design, GUI programming, and backend logic for reservation management.

---

## Folder Structure

```
Railway-Reservation-System
|
+--- classes/                           (Compiled Java files, ignored by Git)
|
+--- src/                               (Source code)
|      Main.java                        (Application entry point)
|      ModernReservationUI.java         (Main GUI controller)
|      ThemeManager.java                (Theme controller for UI)
|
|      ReservationService.java          (Core booking and cancellation logic)
|      ReservationRepository.java       (Persistent ticket storage handler)
|
|      Passenger.java                   (Passenger data model)
|      PnrGenerator.java                (Unique PNR generator)
|
|      RoundedPanel.java                (Custom Swing UI component)
|
|      README.md                        (Project documentation)
|
+--- reservations.txt                   (Stored reservation data)
+--- .theme.properties                  (Theme preference configuration)
+--- .gitignore                         (Git ignore configuration)
```

---

## Features

### Ticket Booking System

• Book railway tickets through GUI  
• Automatic PNR number generation  
• Stores passenger and journey details  
• Persistent reservation storage

### Reservation Management

• View all booked tickets  
• Cancel reservations using PNR number  
• Updates reservation data automatically

### Modern UI / UX

• Clean Java Swing interface  
• Rounded UI components  
• Structured layout and spacing  
• Light / Dark theme toggle

### Reservation Insights

• Displays reservation list in real-time  
• Statistics panel showing total reservations  
• Export reservation data to CSV

---

## How to Run

### 1. Open terminal in project root

```
cd Railway-Reservation-System
```

### 2. Compile the project

```
javac -d classes src/*.java src/*/*.java
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

Venkatam  
GitHub: https://github.com/VENKATRAM2005
