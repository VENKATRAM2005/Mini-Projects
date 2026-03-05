# ATM Interface Simulation

## Overview

ATM Interface Simulation is a console-based banking application developed in Java.  
The system simulates core ATM functionalities including balance inquiry, deposits, withdrawals, and transaction history management.

The project demonstrates practical implementation of Object-Oriented Programming, modular application design, and file-based persistence for storing account information and transaction records.

This application is designed as a learning project to showcase backend logic and financial transaction handling using Java.

---

## Folder Structure

```
ATM-Simulation
|
├── VenkatATMConsole.java      (Application entry point)
├── ATMUI.java                 (Console user interface)
├── ATMService.java            (Core banking operations)
├── Transaction.java           (Transaction model)
├── UserAccount.java           (User account data model)
|
├── accounts.dat               (Sample account data storage)
├── README.md                  (Project documentation)
└── .gitignore                 (Git ignore configuration)
```

---

## Features

### Account Access

• Login using account ID  
• Basic account authentication system  

### Banking Operations

• Check account balance  
• Deposit money into account  
• Withdraw money from account  
• View transaction history  

### Transaction Management

• Records deposits and withdrawals  
• Displays transaction history for each account  
• Handles invalid inputs and insufficient balance scenarios  

### Data Persistence

• Stores account data in `accounts.dat`  
• Maintains account state across sessions  

---

## How to Run

### 1. Open terminal in project root

```
cd ATM-Simulation
```

### 2. Compile the project

```
javac *.java
```

### 3. Run the application

```
java VenkatATMConsole
```

Follow the console prompts to interact with the ATM system.

---

## Technologies Used

Java SE  
Object-Oriented Programming  
File Handling  
Console-based Application Design  

---

## Developer

Venkatram  
GitHub: https://github.com/VENKATRAM2005
