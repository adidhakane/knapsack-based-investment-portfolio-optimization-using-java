# Knapsack-Based Investment Portfolio Optimization 💼

**Advanced Portfolio Optimization Using Multiple Knapsack Algorithms in Java**

---

## 🎯 Overview

A comprehensive investment portfolio optimization system that applies three different knapsack algorithms to maximize returns while managing risk constraints. The system treats investment selection as a knapsack problem where risk tolerance is the capacity and expected returns are the values.

## ✨ Key Features

- 📊 **Multiple Algorithms** - Dynamic Programming, Greedy, and Branch & Bound
- 🎯 **Risk Management** - Risk tolerance as knapsack capacity constraint
- 🏢 **Sector Diversification** - Min/max investment constraints per sector
- 📈 **Performance Analysis** - Execution time and algorithm comparison
- 💹 **Return Optimization** - Maximize expected returns within risk limits

## 🛠️ Tech Stack

**Language:** Java
**Algorithms:** Dynamic Programming, Greedy Approach, Branch & Bound
**Data Structures:** Arrays, Lists, Maps, Priority Queues

## 📁 Project Structure
```
knapsack-investment-optimizer/
├── Investment.java              # Investment data model
├── KnapsackDP.java             # Dynamic Programming solver
├── KnapsackGreedy.java         # Greedy algorithm solver
├── KnapsackBranchAndBound.java # Branch & Bound with constraints
├── KnapsackResult.java         # Result wrapper class
└── Main.java                   # Main application & testing
```

## 🔄 Core Algorithms

### 1. **Dynamic Programming**
- **Approach:** Bottom-up optimization table
- **Time Complexity:** O(n × W)
- **Space Complexity:** O(n × W)
- **Guarantee:** Optimal solution

### 2. **Greedy Algorithm**
- **Approach:** Sort by return/risk ratio, select greedily
- **Time Complexity:** O(n log n)
- **Space Complexity:** O(n)
- **Guarantee:** Approximate solution

### 3. **Branch & Bound**
- **Approach:** Tree search with pruning and constraints
- **Features:** Sector diversification constraints
- **Constraints:** Min/max investments per sector
- **Guarantee:** Optimal solution with constraints

## 💡 Key Components

### Investment Model
```java
public class Investment {
    private String name;
    private double expectedReturn;  // Value in knapsack terms
    private double riskFactor;      // Weight in knapsack terms
    private String sector;          // For diversification constraints
}
```

## Algorithm Results
- Selected Investments: List of optimal investment indices
- Total Return: Sum of expected returns
- Total Risk: Sum of risk factors
- Execution Time: Performance metrics

## 🚀 Quick Start
```
# Compile all Java files
javac *.java

# Run the optimization system
java Main
```
## 📊 Sample Output
```
=== Investment Portfolio Optimization ===
Risk Tolerance: 30.0

Dynamic Programming: Return = 45.50, Risk = 29.5
Greedy Approach: Return = 43.00, Risk = 28.0
Branch & Bound: Return = 44.00, Risk = 29.0 (With constraints)

Risk-Return Ratios:
- Dynamic Programming: 1.54
- Greedy Approach: 1.54
- Branch & Bound: 1.52
```
## 🎯 Algorithm Comparison

- Dynamic Programming	Optimal	O(n×W)	Basic	Standard optimization
- Greedy	Approximate	O(n log n)	Basic	Fast approximation
- Branch & Bound	Optimal	Exponential*	Advanced	Complex constraints
*With pruning for practical performance

## 🏆 Key Achievements
- Multi-Algorithm Implementation - Three different optimization approaches
- Constraint Handling - Sector diversification requirements
- Performance Analysis - Comprehensive algorithm comparison
- Real-World Application - Practical investment portfolio optimization

Built with ☕ for Optimal Investment Decisions
