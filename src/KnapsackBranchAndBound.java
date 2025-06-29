import java.util.*;

public class KnapsackBranchAndBound {
    public KnapsackResult solveWithConstraints(
            Investment[] investments, 
            double riskTolerance,
            Map<String, Integer> minInvestments,
            Map<String, Integer> maxInvestments) {
        
        int n = investments.length;
        
        // Sort investments by value-to-weight ratio for better bound calculation
        Investment[] sortedInvestments = Arrays.copyOf(investments, n);
        Arrays.sort(sortedInvestments, (a, b) -> 
            Double.compare(b.getExpectedReturn() / b.getRiskFactor(), 
                          a.getExpectedReturn() / a.getRiskFactor()));
        
        // Create mapping from sorted to original indices
        int[] sortedToOriginal = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (sortedInvestments[i] == investments[j]) {
                    sortedToOriginal[i] = j;
                    break;
                }
            }
        }
        
        // Initialize best solution
        List<Integer> bestSolution = new ArrayList<>();
        double bestValue = 0;
        
        // Create priority queue for nodes
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> 
            Double.compare(b.bound, a.bound)); // Max heap by bound
        
        // Create root node
        Node root = new Node();
        root.level = -1;
        root.profit = 0;
        root.weight = 0;
        root.selected = new boolean[n];  // Initialize the array here
        root.sectorCounts = new HashMap<>();
        
        // Calculate bound for root node
        root.bound = calculateBound(root, sortedInvestments, riskTolerance);
        
        pq.offer(root);
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            
            if (current.bound <= bestValue) {
                continue; // Prune this branch
            }
            
            // Move to the next level
            int nextLevel = current.level + 1;
            if (nextLevel == n) {
                continue; // Reached leaf node
            }
            
            // Get the original index of the investment at this level
            int originalIndex = sortedToOriginal[nextLevel];
            Investment nextInv = sortedInvestments[nextLevel];
            
            // Try including the current investment
            if (current.weight + nextInv.getRiskFactor() <= riskTolerance) {
                Node includeNode = new Node(current);
                includeNode.level = nextLevel;
                includeNode.weight = current.weight + nextInv.getRiskFactor();
                includeNode.profit = current.profit + nextInv.getExpectedReturn();
                includeNode.selected[nextLevel] = true;
                
                // Update sector counts
                String sector = nextInv.getSector();
                includeNode.sectorCounts.put(sector, 
                    includeNode.sectorCounts.getOrDefault(sector, 0) + 1);
                
                // Check if this solution satisfies max constraints
                boolean satisfiesMaxConstraints = true;
                for (Map.Entry<String, Integer> entry : maxInvestments.entrySet()) {
                    String s = entry.getKey();
                    int max = entry.getValue();
                    if (includeNode.sectorCounts.getOrDefault(s, 0) > max) {
                        satisfiesMaxConstraints = false;
                        break;
                    }
                }
                
                if (satisfiesMaxConstraints) {
                    includeNode.bound = calculateBound(includeNode, sortedInvestments, riskTolerance);
                    
                    // If this is a leaf node or better than current best, update best solution
                    if (nextLevel == n-1 || includeNode.bound > bestValue) {
                        // Check if min constraints can be satisfied
                        boolean canSatisfyMinConstraints = true;
                        for (Map.Entry<String, Integer> entry : minInvestments.entrySet()) {
                            String s = entry.getKey();
                            int min = entry.getValue();
                            int current_count = includeNode.sectorCounts.getOrDefault(s, 0);
                            int remaining = 0;
                            
                            // Count remaining investments in this sector
                            for (int i = nextLevel + 1; i < n; i++) {
                                if (sortedInvestments[i].getSector().equals(s)) {
                                    remaining++;
                                }
                            }
                            
                            if (current_count + remaining < min) {
                                canSatisfyMinConstraints = false;
                                break;
                            }
                        }
                        
                        if (canSatisfyMinConstraints) {
                            if (nextLevel == n-1) {
                                // Check if min constraints are satisfied
                                boolean satisfiesMinConstraints = true;
                                for (Map.Entry<String, Integer> entry : minInvestments.entrySet()) {
                                    String s = entry.getKey();
                                    int min = entry.getValue();
                                    if (includeNode.sectorCounts.getOrDefault(s, 0) < min) {
                                        satisfiesMinConstraints = false;
                                        break;
                                    }
                                }
                                
                                if (satisfiesMinConstraints && includeNode.profit > bestValue) {
                                    bestValue = includeNode.profit;
                                    bestSolution = convertToOriginalIndices(includeNode.selected, sortedToOriginal);
                                }
                            } else {
                                pq.offer(includeNode);
                            }
                        }
                    }
                }
            }
            
            // Try excluding the current investment
            Node excludeNode = new Node(current);
            excludeNode.level = nextLevel;
            excludeNode.bound = calculateBound(excludeNode, sortedInvestments, riskTolerance);
            
            // Check if min constraints can still be satisfied
            boolean canSatisfyMinConstraints = true;
            for (Map.Entry<String, Integer> entry : minInvestments.entrySet()) {
                String s = entry.getKey();
                int min = entry.getValue();
                int current_count = excludeNode.sectorCounts.getOrDefault(s, 0);
                int remaining = 0;
                
                // Count remaining investments in this sector
                for (int i = nextLevel + 1; i < n; i++) {
                    if (sortedInvestments[i].getSector().equals(s)) {
                        remaining++;
                    }
                }
                
                if (current_count + remaining < min) {
                    canSatisfyMinConstraints = false;
                    break;
                }
            }
            
            if (canSatisfyMinConstraints && excludeNode.bound > bestValue) {
                pq.offer(excludeNode);
            }
        }
        
        // If no solution found that satisfies all constraints
        if (bestSolution.isEmpty()) {
            return new KnapsackResult(new ArrayList<>(), 0, 0);
        }
        
        // Calculate total return and risk for the best solution
        double totalReturn = 0;
        double totalRisk = 0;
        for (int idx : bestSolution) {
            totalReturn += investments[idx].getExpectedReturn();
            totalRisk += investments[idx].getRiskFactor();
        }
        
        return new KnapsackResult(bestSolution, totalReturn, totalRisk);
    }
    
    private double calculateBound(Node node, Investment[] investments, double capacity) {
        if (node.weight >= capacity) {
            return 0;
        }
        
        double bound = node.profit;
        int level = node.level + 1;
        double totalWeight = node.weight;
        
        // Add items completely until capacity is exceeded
        while (level < investments.length && totalWeight + investments[level].getRiskFactor() <= capacity) {
            if (!node.selected[level]) {
                totalWeight += investments[level].getRiskFactor();
                bound += investments[level].getExpectedReturn();
            }
            level++;
        }
        
        // Add the last item fractionally
        if (level < investments.length) {
            bound += (capacity - totalWeight) * 
                    (investments[level].getExpectedReturn() / investments[level].getRiskFactor());
        }
        
        return bound;
    }
    
    private List<Integer> convertToOriginalIndices(boolean[] selected, int[] sortedToOriginal) {
        List<Integer> originalIndices = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                originalIndices.add(sortedToOriginal[i]);
            }
        }
        return originalIndices;
    }
    
    private static class Node {
        int level;
        double profit;
        double weight;
        double bound;
        boolean[] selected;
        Map<String, Integer> sectorCounts;
        
        Node() {
            sectorCounts = new HashMap<>();
        }
        
        Node(Node other) {
            this.level = other.level;
            this.profit = other.profit;
            this.weight = other.weight;
            this.bound = other.bound;
            this.selected = Arrays.copyOf(other.selected, other.selected.length);
            this.sectorCounts = new HashMap<>(other.sectorCounts);
        }
    }
}
