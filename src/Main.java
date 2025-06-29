import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Sample investment options
        Investment[] investments = {
            new Investment("Tech Stock A", 12.5, 7.0, "Technology"),
            new Investment("Bank Bond B", 10.0, 5.0, "Finance"),
            new Investment("Real Estate Fund C", 15.0, 9.0, "Real Estate"),
            new Investment("Healthcare ETF D", 8.0, 4.0, "Healthcare"),
            new Investment("Energy Stock E", 11.0, 6.0, "Energy"),
            new Investment("Retail Bond F", 9.5, 5.5, "Retail"),
            new Investment("Tech Stock G", 14.0, 8.0, "Technology"),
            new Investment("Finance ETF H", 7.5, 3.5, "Finance"),
            new Investment("REIT I", 13.0, 7.5, "Real Estate"),
            new Investment("Pharma Stock J", 10.5, 6.5, "Healthcare")
        };

        // Risk tolerance (knapsack capacity)
        double riskTolerance = 30.0;

        System.out.println("=== Investment Portfolio Optimization ===");
        System.out.println("Risk Tolerance: " + riskTolerance);
        System.out.println("\nAvailable Investment Options:");
        printInvestments(investments);

        // Run Dynamic Programming approach
        System.out.println("\n=== Dynamic Programming Approach ===");
        long startTime = System.nanoTime();
        KnapsackDP dpSolver = new KnapsackDP();
        KnapsackResult dpResult = dpSolver.solve(investments, riskTolerance);
        long endTime = System.nanoTime();
        printResult(dpResult, investments, endTime - startTime);

        // Run Greedy approach for comparison
        System.out.println("\n=== Greedy Approach (for comparison) ===");
        startTime = System.nanoTime();
        KnapsackGreedy greedySolver = new KnapsackGreedy();
        KnapsackResult greedyResult = greedySolver.solve(investments, riskTolerance);
        endTime = System.nanoTime();
        printResult(greedyResult, investments, endTime - startTime);

        // Run Branch and Bound with sector diversification constraints
        System.out.println("\n=== Branch and Bound with Constraints ===");
        Map<String, Integer> minInvestments = new HashMap<>();
        minInvestments.put("Technology", 1); // At least 1 technology investment
        minInvestments.put("Finance", 1);    // At least 1 finance investment
        
        Map<String, Integer> maxInvestments = new HashMap<>();
        maxInvestments.put("Technology", 2); // At most 2 technology investments
        
        startTime = System.nanoTime();
        KnapsackBranchAndBound bnbSolver = new KnapsackBranchAndBound();
        KnapsackResult bnbResult = bnbSolver.solveWithConstraints(
            investments, riskTolerance, minInvestments, maxInvestments);
        endTime = System.nanoTime();
        printResult(bnbResult, investments, endTime - startTime);
        
        // Compare all approaches
        System.out.println("\n=== Comparison of Approaches ===");
        System.out.println("Dynamic Programming: Return = " + dpResult.getTotalReturn() + 
                          ", Risk = " + dpResult.getTotalRisk());
        System.out.println("Greedy Approach: Return = " + greedyResult.getTotalReturn() + 
                          ", Risk = " + greedyResult.getTotalRisk() + 
                          " (Difference from optimal: " + 
                          (dpResult.getTotalReturn() - greedyResult.getTotalReturn()) + ")");
        System.out.println("Branch and Bound with Constraints: Return = " + bnbResult.getTotalReturn() + 
                          ", Risk = " + bnbResult.getTotalRisk() + 
                          " (With sector diversification constraints)");
        
        // Risk-Return Analysis
        System.out.println("\n=== Risk-Return Analysis ===");
        System.out.println("Dynamic Programming Return/Risk Ratio: " + 
                          (dpResult.getTotalReturn() / dpResult.getTotalRisk()));
        System.out.println("Greedy Approach Return/Risk Ratio: " + 
                          (greedyResult.getTotalReturn() / greedyResult.getTotalRisk()));
        System.out.println("Branch and Bound Return/Risk Ratio: " + 
                          (bnbResult.getTotalReturn() / bnbResult.getTotalRisk()));
        
        // Sector Diversification Analysis
        System.out.println("\n=== Sector Diversification Analysis ===");
        analyzeSectorDiversification(dpResult, investments, "Dynamic Programming");
        analyzeSectorDiversification(greedyResult, investments, "Greedy Approach");
        analyzeSectorDiversification(bnbResult, investments, "Branch and Bound");
    }
    
    private static void printInvestments(Investment[] investments) {
        System.out.printf("%-20s %-15s %-15s %-15s\n", "Name", "Expected Return", "Risk Factor", "Sector");
        System.out.println("----------------------------------------------------------------------");
        for (Investment inv : investments) {
            System.out.printf("%-20s %-15.2f %-15.2f %-15s\n", 
                inv.getName(), inv.getExpectedReturn(), inv.getRiskFactor(), inv.getSector());
        }
    }
    
    private static void printResult(KnapsackResult result, Investment[] investments, long executionTimeNanos) {
        System.out.println("Selected Investments:");
        System.out.printf("%-20s %-15s %-15s %-15s\n", "Name", "Expected Return", "Risk Factor", "Sector");
        System.out.println("----------------------------------------------------------------------");
        
        for (int idx : result.getSelectedIndices()) {
            Investment inv = investments[idx];
            System.out.printf("%-20s %-15.2f %-15.2f %-15s\n", 
                inv.getName(), inv.getExpectedReturn(), inv.getRiskFactor(), inv.getSector());
        }
        
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Total Expected Return: %.2f\n", result.getTotalReturn());
        System.out.printf("Total Risk: %.2f\n", result.getTotalRisk());
        System.out.printf("Execution Time: %.3f ms\n", executionTimeNanos / 1_000_000.0);
    }
    
    private static void analyzeSectorDiversification(KnapsackResult result, Investment[] investments, String approach) {
        Map<String, Double> sectorAllocation = new HashMap<>();
        Map<String, Integer> sectorCount = new HashMap<>();
        double totalReturn = result.getTotalReturn();
        
        for (int idx : result.getSelectedIndices()) {
            Investment inv = investments[idx];
            String sector = inv.getSector();
            
            sectorAllocation.put(sector, 
                sectorAllocation.getOrDefault(sector, 0.0) + inv.getExpectedReturn());
            sectorCount.put(sector, sectorCount.getOrDefault(sector, 0) + 1);
        }
        
        System.out.println(approach + " Sector Allocation:");
        for (Map.Entry<String, Double> entry : sectorAllocation.entrySet()) {
            double percentage = (entry.getValue() / totalReturn) * 100;
            System.out.printf("%-15s: %.2f%% (Count: %d)\n", 
                entry.getKey(), percentage, sectorCount.get(entry.getKey()));
        }
        System.out.println();
    }
}
