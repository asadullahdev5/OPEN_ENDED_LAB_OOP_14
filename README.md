# 🎓 Faculty Management System

A sleek, dark-themed Java Swing desktop application designed for managing university faculty payroll structures. This project demonstrates core **Object-Oriented Programming (OOP)** pillars, interface design, runtime polymorphism, and modern GUI styling using Java Swing.

Developed as part of the **OOP Lab Session 14 — Open Ended Lab** at Iqra University.

---

## 🚀 Key Features

- **Dynamic Form Inputs**: Contextual input fields that automatically toggle based on the selected employee type (e.g., hiding/showing working hours for visiting faculty).
- **Automated Payroll Engine**: Computes monthly base salaries, specific allowances, and bonuses instantly.
- **Interactive UI Table**: A structured data grid that categorizes and highlights different employee classes using custom row renderers.
- **Detailed Summary Reporter**: Generates a clean, comprehensive text-based payroll ledger summarizing grand totals, count distributions, and total payouts.
- **Data Validation**: Secure try-catch logic handling duplicate/invalid entry blocks and tracking array boundaries (Max limit: 20 records).

---

## 🛠️ OOP Concepts Demonstrated

This application serves as a practical implementation of fundamental OOP principles:

* **Abstract Classes & Methods**: `Employee` functions as the base structural blueprint with an abstract `calculateSalary()` implementation.
* **Interfaces**: `BonusPolicy` standardizes bonus calculations across disjointed faculty types.
* **Inheritance**: Three distinct concrete subclasses (`PermanentFaculty`, `VisitingFaculty`, `LabInstructor`) inherit from the parent `Employee` class.
* **Runtime Polymorphism**: Dynamically invokes the correct overridden `calculateSalary()` and `calculateBonus()` implementations via an overarching `Employee[]` array processing block.
* **Encapsulation**: Strict use of standard access modifiers (`private`, `protected`, `public`) alongside clean constructor overloading layers.

---

## 📊 Business Logic Matrix

| Faculty Type | Base Calculation Formula | Allowances Applied | Bonus Structure |
| :--- | :--- | :--- | :--- |
| **Permanent Faculty** | Fixed Monthly Basic Salary | 30% Housing + 15% Medical | 20% of Basic Salary |
| **Visiting Faculty** | Hours/Week × Hourly Rate × 4 weeks | None | 5% of Calculated Salary |
| **Lab Instructor** | Fixed Monthly Basic Salary | 10% Lab Allowance | 10% of Basic Salary |

---

## 💻 Technical Prerequisites

Before running this application, make sure you have the following installed:
- **Java Development Kit (JDK)**: Version 8 or higher.
- **IDE** (Optional): IntelliJ IDEA, NetBeans, Eclipse, or VS Code.

---

## 🏃 How to Run the Application

### Via Command Line
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/asadullahdev5/OPEN_ENDED_LAB_OOP_14.git](https://github.com/asadullahdev5/OPEN_ENDED_LAB_OOP_14.git)
   cd OPEN_ENDED_LAB_OOP_14