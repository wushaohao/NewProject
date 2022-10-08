import java.util.List;

public class Portfolio {
    private List<Position> positions;
    private MarketDataProvider marketDataProvider;

    public Portfolio(List<Position> positions, MarketDataProvider marketDataProvider) {
        this.positions = positions;
        this.marketDataProvider = marketDataProvider;
    }

    public void calculateAndPublish() {
        double totalValue = 0;
        for (Position position : positions) {
            double price = marketDataProvider.getCurrentPrice();
            double marketValue = position.getQuantity() * price;
            totalValue += marketValue;
            System.out.printf("Position: %s, Market Value: %.2f%n", position, marketValue);
        }
        System.out.printf("Total Net Asset Value: %.2f%n", totalValue);
    }
}