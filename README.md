# Zippy - Cloud Based File Sharing

A simple and secure anonymous file-sharing platform where users can upload files without login, and share them via a 4-digit PIN. Designed to be lightweight and privacy-focused.

---

## 🚀 Features

- ✅ Anonymous Upload — No login or registration required
- 📁 Unique 4-digit PIN generation per file
- 🔐 Files stored securely on **AWS S3**
- ⏳ Limited download count per file (self-destruct feature)
- 🧹 Auto-cleanup: When a file's download limit reaches 0, it is deleted from both **S3** and **RDS**
- 📦 Simple architecture using **EC2**, **RDS**, and **S3**

---

## 📐 Architecture Overview

---

## 🛠️ Tech Stack

- **Frontend**: Simple HTML , CSS & Js
- **Backend**: Java (Spring Boot)
- **Database**: Amazon RDS (MySQL)
- **Storage**: Amazon S3
- **Hosting**: Amazon EC2
- **Scheduler**: Amazon Lambda
