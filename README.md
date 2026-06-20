# Sh_project - Secure Hub for Universal Bit-Hidden Asset Management

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.+-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

A secure desktop GUI application for encrypting and decrypting files with OTP-based email authentication. Built with Java 17, MySQL, and modern Swing UI using FlatLaf theme.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Setup & Installation](#setup--installation)
- [Usage](#usage)
- [Database Setup](#database-setup)
- [Security Features](#security-features)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## 🔍 Overview

Sh_project (Secure Hub for Universal Bit-Hidden Asset Management) is a desktop GUI Java application that provides secure file encryption and decryption capabilities. Users can securely store sensitive files by encrypting them and storing them in a database, then later decrypting and restoring them to their original location. The application features a modern dark-themed interface using the FlatLaf library for an enhanced user experience.

The application implements a two-factor authentication system using OTP (One-Time Password) sent via email for both user registration and login, ensuring that only authorized users can access their encrypted files.

## ✨ Features

### 🔐 Authentication System
- **Secure Registration**: Email verification with OTP for new user sign-up
- **Secure Login**: OTP-based authentication for existing users
- **Email Integration**: Uses Gmail SMTP for sending OTPs
- **Session Management**: Secure user sessions after successful authentication

### 📁 File Security Features
- **File Encryption ("Hide")**: 
  - Select any file to encrypt
  - File contents stored as BLOB in MySQL database
  - Original file securely deleted after encryption
- **File Decryption ("Restore")**:
  - Retrieve encrypted files from database
  - Restore files to their original location
  - Database record cleared after successful restoration
- **File Vault**: View all your encrypted files with metadata

### 🖥️ UI/UX Enhancements
- **Modern Dark Theme**: Uses FlatLaf library for sleek, dark interface
- **Anti-Flashing Fixes**: 150ms transition delays to prevent UI thrashing
- **Custom Button Styling**: Color-coded action buttons with hover effects
- **Improved Popup Handling**: Eliminates screen flashing during window transitions
- **Responsive Layout**: Organized panels with proper spacing and alignment

### 🛡️ Security Measures
- **OTP Authentication**: 4-digit one-time passwords for each session
- **Email Verification**: Ensures legitimate email ownership
- **Secure File Handling**: Original files deleted after encryption
- **Database Encryption**: Files stored as binary large objects (BLOBs)
- **Input Validation**: Basic validation to prevent common errors

## 🏗️ Architecture

Sh_project follows a layered architecture pattern:

```
Presentation Layer
    ├── Welcome.java (Login/Register Screen)
    └── UserView.java (Main Dashboard)
    
Service Layer
    ├── UserService.java (User business logic)
    ├── GenerateOTP.java (OTP generation)
    └── SendOTPService.java (Email OTPSending)
    
Data Access Layer
    ├── UserDAO.java (User database operations)
    └── DataDAO.java (File encryption/storage operations)
    
Model Layer
    ├── user.java (User entity)
    └── Data.java (File entity)
    
Utilities
    └── db/MyConnection.java (Database connection manager)
    
Application Entry Point
    └── Main.java (Application starter)
```

## 💻 Technologies Used

- **Core Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL 8.0
- **Database Connectivity**: JDBC with MySQL Connector/J 8.0.33
- **Email Service**: Jakarta Mail 1.6.7 (javax.mail)
- **UI Framework**: Swing with FlatLaf 3.7.1 (modern dark theme)
- **Development Environment**: IntelliJ IDEA

## 🛠️ Setup & Installation

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6+
- MySQL Server 8.0+
- Git (for cloning repository)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/Sh_project.git
   cd Sh_project
   ```

2. **Configure MySQL Database**
   - Create a database named `Sh_project`
   - Ensure MySQL is running on localhost:3306
   - Update credentials in `src/main/java/db/MyConnection.java` if needed:
     ```java
     connection = DriverManager.getConnection(
             "jdbc:mysql://localhost:3306/Sh_project?useSSL=false&allowPublicKeyRetrieval=true",
             "your_username", 
             "your_password"
     );
     ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="Main"
   ```
   Alternatively, you can run the compiled JAR:
   ```bash
   java -cp target/classes Main
   ```

## 🗄️ Database Setup

The application requires two tables to be created in the MySQL database:

```sql
-- Users table for authentication
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Data table for encrypted file storage
CREATE TABLE data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(500) NOT NULL,
    email VARCHAR(100) NOT NULL,
    bin_data LONGBLOB NOT NULL,
    FOREIGN KEY (email) REFERENCES users(email)
);
```

Note: The application will automatically create these tables if they don't exist (depending on your MySQL user permissions).

## 🚀 Usage

### First Time Setup
1. Run the application: `java -cp target/classes Main`
2. At the welcome screen, click "[2] NEW_CLEARANCE_ENTRY    (Register)" for registration
3. Enter your name and email address
4. Check your email for the OTP and enter it
5. Once registered, you'll be prompted to login

### Regular Usage
1. **Login**:
   - Enter your registered email
   - Check email for OTP and enter it
   - Access granted to your personal file vault dashboard

2. **Dashboard Operations**:
   - **🔒 HIDE NEW FILE**: Encrypt and store a new file
     - Click the button and select a file using the file chooser
     - File will be encrypted and original deleted from filesystem
   - **🔓 RESTORE ASSET**: Decrypt and restore a file
     - Enter the File ID from your vault list
     - File will be restored to its original location
   - **🔄 REFRESH VAULT**: Refresh the encrypted file list view
   - **🚪 LOGOUT SESSION**: Securely exit the application and return to login screen

### Example Workflow
1. Register with email: user@example.com via the welcome screen
2. Login with email verification
3. Click "🔒 HIDE NEW FILE" button
4. Select file using file chooser: `C:\documents\secret.pdf`
5. File `secret.pdf` is encrypted and stored in database
6. Original `secret.pdf` is deleted from filesystem
7. Later, click "🔓 RESTORE ASSET" button
8. Enter the File ID shown in your vault table
9. File is restored to `C:\documents\secret.pdf`

## 🔒 Security Features

### Authentication Security
- **OTP-Based Verification**: 4-digit random codes for each authentication attempt
- **Email Validation**: Ensures only legitimate email owners can access accounts
- **Password Protection**: Uses app-specific passwords for Gmail SMTP (not shown in code for security)

### File Security
- **Irreversible Encryption**: Original files are deleted after encryption
- **Database Storage**: Files stored as BLOBs preventing filesystem access
- **Access Control**: Users can only access their own files (email-based isolation)
- **Secure Deletion**: Database records removed after successful restoration

### Network Security
- **SMTP over SSL**: Uses port 465 with SSL encryption for email transmission
- **Secure JDBC Connection**: Direct database connection with credentials protection

## 📁 Project Structure

```
Sh_project/
├── .git/
├── .idea/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── Main.java                    # Application entry point
│   │   │   
│   │   └── db/
│   │       └── MyConnection.java            # Database connection manager
│   └── resources/
│
│   ├── dao/                                 # Data Access Objects
│   │   ├── UserDAO.java
│   │   └── DataDAO.java
│   │
│   ├── model/                               # Data models
│   │   ├── user.java
│   │   └── Data.java
│   │
│   ├── service/                             # Business logic services
│   │   ├── GenerateOTP.java
│   │   ├── SendOTPService.java
│   │   └── UserService.java
│   │
│   └── Views/                               # User interface components
│       ├── Welcome.java                     # Login/register screen
│       └── UserView.java                    # Main dashboard after login
├── pom.xml                                    # Maven configuration
├── target/                                    # Compiled output
└── README.md                                  # This file
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### To contribute:
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Suggested Improvements
- Add file integrity verification (hashing)
- Implement stronger encryption algorithms
- Add file categorization/tags
- Create GUI version using JavaFX/Swing
- Add audit logging
- Implement password recovery
- Add multi-factor authentication options

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Shubham** - Secure Hub for Universal Bit-Hidden Asset Management

## 🙏 Acknowledgments

- MySQL Connector/J team for the JDBC driver
- Jakarta Mail team for the email API
- Open source community for inspiration and tools

---

**Note for Resume**: This project demonstrates proficiency in:
- Core Java programming (OOP, collections, I/O)
- Database design and JDBC connectivity
- MVC/n-tier architecture patterns
- Email integration with SMTP
- File handling and encryption concepts
- User authentication systems
- GUI-based application development (Swing with FlatLaf theme)
- Maven build management