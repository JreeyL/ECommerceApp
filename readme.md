# E-Commerce App Backend Demo

A functional backend module for an **E-Commerce Application**, demonstrating **Object-Oriented Programming (OOP)** and **Java 21 LTS** features.

## Tech Stack
* **Language:** Java 21
* **IDE:** Eclipse

## Key Features
* **OOP Core:** Inheritance, Polymorphism, and Encapsulation.
* **Product Management:** Handles `Food`, `Cookware`, and `Tableware` using a polymorphic list.
* **Modern Java:**
  * **Records:** `CreditCard` & `PayPal` (Immutable data).
  * **Sealed Interfaces:** `Payment` interface (Restricted hierarchy).
  * **Pattern Matching:** Smart payment processing using `switch` expressions.
  * **Streams & Lambdas:** Filtering products by price.

## Project Structure
```text
src/dev/ecommerce
├── app          # Main execution (ECommerceApp.java)
├── contracts    # Interfaces (Discountable.java)
├── model        # Product hierarchy (Model, Food, Cookware, etc.)
└── payment      # Sealed interfaces & Records (Payment, CreditCard...)