import java.util.List;

public class KnapsackResult {
    private List<Integer> selectedIndices;
    private double totalReturn;
    private double totalRisk;
    
    public KnapsackResult(List<Integer> selectedIndices, double totalReturn, double totalRisk) {
        this.selectedIndices = selectedIndices;
        this.totalReturn = totalReturn;
        this.totalRisk = totalRisk;
    }
    
    public List<Integer> getSelectedIndices() {
        return selectedIndices;
    }
    
    public double getTotalReturn() {
        return totalReturn;
    }
    
    public double getTotalRisk() {
        return totalRisk;
    }
}
