# Account Management System

This is a **Spring Boot** application designed to manage beneficiaries, accounts, and transactions. 
It provides REST APIs to retrieve beneficiary information, account details, transactions, balances, and more. 
The system reads data from CSV files and exposes it through a RESTful API.

---

## Table of Contents

1. [Technologies](#technologies)
2. [Setup](#setup)
3. [API Endpoints](#api-endpoints)
4. [Running the Application](#running-the-application)
5. [Testing](#testing)

---

## Technologies

- **Java 17**: The primary programming language used for development.
- **Spring Boot**: Framework for building the RESTful APIs and managing dependencies.
- **Lombok**: Library for reducing boilerplate code (e.g., getters, setters, constructors).
- **OpenCSV**: Library for reading and parsing CSV files.
- **JUnit 5**: Framework for writing and running unit tests.
- **Mockito**: Library for mocking dependencies in unit tests.
- **Maven**: Build automation tool used for dependency management and project building.

---

## Setup

### Prerequisites

- **Java 17**: Ensure you have Java 17 installed.
- **Maven**: Ensure you have Maven installed.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/account-management.git
   ```
2. Navigate to the project directory:
   ```bash
   cd account-management
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

---

## API Endpoints

The following REST APIs are available:

### 1. Get Beneficiary Details
- **Endpoint**: `GET /api/beneficiaries/{beneficiaryId}`
- **Description**: Retrieve details of a beneficiary by ID.
- **Example Response**:
  ```json
  {
    "beneficiaryId": "1",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```

### 2. Get Accounts for a Beneficiary
- **Endpoint**: `GET /api/beneficiaries/{beneficiaryId}/accounts`
- **Description**: Retrieve all accounts associated with a beneficiary.
- **Example Response**:
  ```json
  [
    {
      "accountId": "1",
      "beneficiaryId": "1"
    },
    {
      "accountId": "2",
      "beneficiaryId": "1"
    }
  ]
  ```

### 3. Get Transactions for a Beneficiary
- **Endpoint**: `GET /api/beneficiaries/{beneficiaryId}/transactions`
- **Description**: Retrieve all transactions for a beneficiary.
- **Example Response**:
  ```json
  [
    {
      "transactionId": "2323",
      "accountId": "1316",
      "amount": 441.4,
      "type": "WITHDRAWAL",
      "date": "07/12/23"
    },
    {
      "transactionId": "2390",
      "accountId": "1316",
      "amount": 109,
      "type": "DEPOSIT",
      "date": "06/05/23"
    }
  ]
  ```

### 4. Get Balances for a Beneficiary
- **Endpoint**: `GET /api/beneficiaries/{beneficiaryId}/balance`
- **Description**: Retrieve the balances for each account of a beneficiary and the total balance of all the accoutns.
- **Example Response**:
  ```json
  {
    "accountBalance": [
      {
        "accountId": "786",
        "balance": -1069.4
      },
      {
        "accountId": "351",
        "balance": -785
      }
    ],
    "totalBalance": -1854.4
  }
  ```

### 5. Get Maximum Withdrawal in the Last Month
- **Endpoint**: `GET /api/beneficiaries/{beneficiaryId}/transactions/maxWithdrawalLastMonth`
- **Description**: Retrieve the maximum withdrawal amount for a beneficiary in the last month.
- **Example Response**:
  ```json
  {
    "maxWithdraw": 137.4,
    "date": "01/29/25"
  }
  ```

---

## Running the Application

1. Navigate to the project directory:
   ```bash
   cd account-management
   ```
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The application will start on port `8080`. You can access the APIs at:
   ```
   http://localhost:8080/{endpoint}
   ```
4. To verify that the application is running, open your browser or use a tool like `curl` or Postman and navigate to:
   ```
   http://localhost:8080/api/beneficiaries/1
   ```

---

## Testing

### Unit Tests

The project includes unit tests for all major components, including services and controllers. 
To run the unit tests, use the following command:
```bash
mvn test
```