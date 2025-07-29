# 💳 PayHub – CLI-Based Payment Application

**PayHub** is a **command-line based digital payment system** developed in **Core Java**. This mini-project replicates core features of real-world payment apps like **Google Pay** or **Paytm**, designed for learning and demonstration purposes.

## 🚀 Features

- 🧾 **User Registration & Login**
- 💸 **Send Money** to other registered users
- 📜 **Transaction History** to view all transfers
- 👨‍💼 **Admin Panel** to monitor:
  - All users
  - All transactions
  - Delete user accounts
- 📁 **File-Based Storage**:
  - `users.txt` – stores user account details
  - `transactions.txt` – stores transaction history

## 🧰 Technologies Used

- **Language**: Core Java (JDK 8+)
- **Environment**: Command Line Interface (CLI)
- **Data Storage**: Plain text files (no database used)

## 📂 File Structure

PayHub/
├── PayHub.java
├── users.txt
├── transactions.txt

## ⚙️ How to Run

1. **Compile the project**
   ```bash
   javac PayHub.java
Run the application

java PayHub
Follow on-screen options for registration, login, transfer, or admin access.

🧪 Functional Overview
User Module:

Register with username, password, contact, and email

Login to your wallet

Transfer money securely

View transaction history

Admin Module:

Login with admin credentials

View all user accounts

View complete transaction logs

Delete a user if needed

📌 Notes
No database is used — the system relies on flat text files.

Designed for educational purposes and CLI-based Java project demos.
