import java.util.*;
public class KnapsackDP {
    // Scale factor to convert doubles to integers for DP table
    private static final int SCALE = 100;
    
    public KnapsackResult solve(Investment[] investments, double riskTolerance) {
        int n = investments.length;//rows
        int W = (int)(riskTolerance * SCALE);//columns
        
        // Create arrays of expected returns and risk factors
        int[] values = new int[n];
        int[] weights = new int[n];
        
        for (int i = 0; i < n; i++) {
            values[i] = (int)(investments[i].getExpectedReturn() * SCALE);
            weights[i] = (int)(investments[i].getRiskFactor() * SCALE);
        }
        
        // Create DP table
        int[][] dp = new int[n + 1][W + 1];
        
        // Fill the DP table
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                if (weights[i-1] <= w) {
                    dp[i][w] = Math.max(values[i-1] + dp[i-1][w-weights[i-1]], dp[i-1][w]);//*
                } else {
                    dp[i][w] = dp[i-1][w];
                }
            }
        }
        
        // Backtrack to find selected investments
        List<Integer> selectedIndices = new ArrayList<>();
        int remainingCapacity = W;
        double totalReturn = 0;
        double totalRisk = 0;
        
        for (int i = n; i > 0; i--) {
            if (dp[i][remainingCapacity] != dp[i-1][remainingCapacity]) {
                selectedIndices.add(i-1);
                remainingCapacity -= weights[i-1];
                totalReturn += investments[i-1].getExpectedReturn();
                totalRisk += investments[i-1].getRiskFactor();
            }
        }
        
        Collections.reverse(selectedIndices);
        return new KnapsackResult(selectedIndices, totalReturn, totalRisk);
    }
}
