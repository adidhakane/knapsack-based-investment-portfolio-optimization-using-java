public class Investment {
    private String name;
    private double expectedReturn;
    private double riskFactor;
    private String sector;
    
    public Investment(String name, double expectedReturn, double riskFactor, String sector) {
        this.name = name;
        this.expectedReturn = expectedReturn;
        this.riskFactor = riskFactor;
        this.sector = sector;
    }
    
    public String getName() {
        return name;
    }
    
    public double getExpectedReturn() {
        return expectedReturn;
    }
    
    public double getRiskFactor() {
        return riskFactor;
    }
    
    public String getSector() {
        return sector;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Return: %.2f, Risk: %.2f, Sector: %s)", 
            name, expectedReturn, riskFactor, sector);
    }
}
