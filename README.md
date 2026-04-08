# Mortgage Rates Backend API

This is a Java Spring Boot backend service that provides REST APIs for retrieving mortgage interest rates and evaluating mortgage feasibility based on user input.

---
## Business rules
- a mortgage should not exceed 4 times the income
- a mortgage should not exceed the home value
---
## Prerequisites

- Java 21+
- Maven
- Docker (optional)
- Git

---
##  Endpoints

###  GET /api/interest-rates

Returns a list of available mortgage interest rates.

####  Response Example

```json
[
  {
    "maturityPeriod": 10,
    "interestRate": 3.5,
    "lastUpdate": "2025-05-21T00:00:00.000+00:00"
  },
  {
    "maturityPeriod": 15,
    "interestRate": 4.0,
    "lastUpdate": "2025-05-21T00:00:00.000+00:00"
  },
  {
    "maturityPeriod": 20,
    "interestRate": 4.5,
    "lastUpdate": "2025-05-21T00:00:00.000+00:00"
  },
  {
    "maturityPeriod": 25,
    "interestRate": 5.0,
    "lastUpdate": "2025-05-21T00:00:00.000+00:00"
  },
  {
    "maturityPeriod": 30,
    "interestRate": 6.5,
    "lastUpdate": "2025-05-21T00:00:00.000+00:00"
  }
]
```

###  POST /api/mortgage-check

Evaluates whether a mortgage is feasible based on provided financial data and returns the monthly cost.

####  Request Body

| Parameter       | Type        | Required | Description                                |
|----------------|-------------|----------|--------------------------------------------|
| `income`        | `BigDecimal` | true     | Annual income of the applicant             |
| `maturityPeriod`| `Integer`    | true     | Number of years to repay the mortgage (e.g. 10, 15, 30) |
| `loanValue`     | `BigDecimal` | true     | Total loan amount being requested          |
| `homeValue`     | `BigDecimal` | true     | Value of the home to be purchased          |

####  Response Example

```json
{
  "feasible": true,
  "monthlyCost": 1135.58
}
```

---
## In-Memory Mortgage Interest Rates

| Maturity Period (Years) | Interest Rate (%) | Last Updated          |
|-------------------------|-------------------|------------------------|
| 10                      | 3.5               | 2025-05-21 00:00:00    |
| 15                      | 4.0               | 2025-05-21 00:00:00    |
| 20                      | 4.5               | 2025-05-21 00:00:00    |
| 25                      | 5.0               | 2025-05-21 00:00:00    |
| 30                      | 6.5               | 2025-05-21 00:00:00    |

---
## Build with docker-compose

### Run in `/docker` directory: 
```bash
    docker-compose up -d     
```

