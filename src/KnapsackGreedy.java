import java.util.*;

public class KnapsackGreedy {
    public KnapsackResult solve(Investment[] investments, double riskTolerance) {
        int n = investments.length;
        
        // Create array of investment indices and value-to-weight ratios
        InvestmentRatio[] ratios = new InvestmentRatio[n];
        
        for (int i = 0; i < n; i++) {
            double ratio = investments[i].getExpectedReturn() / investments[i].getRiskFactor();
            ratios[i] = new InvestmentRatio(i, ratio);
        }
        
        // Sort by value-to-weight ratio in descending order
        Arrays.sort(ratios, (a, b) -> Double.compare(b.ratio, a.ratio));
        
        // Select investments greedily
        List<Integer> selectedIndices = new ArrayList<>();
        double remainingCapacity = riskTolerance;
        double totalReturn = 0;
        double totalRisk = 0;
        
        for (InvestmentRatio ratio : ratios) {
            int idx = ratio.index;
            if (investments[idx].getRiskFactor() <= remainingCapacity) {
                selectedIndices.add(idx);
                remainingCapacity -= investments[idx].getRiskFactor();
                totalReturn += investments[idx].getExpectedReturn();
                totalRisk += investments[idx].getRiskFactor();
            }
        }
        
        return new KnapsackResult(selectedIndices, totalReturn, totalRisk);
    }
    
    private static class InvestmentRatio {
        int index;
        double ratio;
        
        InvestmentRatio(int index, double ratio) {
            this.index = index;
            this.ratio = ratio;
        }
    }
}
