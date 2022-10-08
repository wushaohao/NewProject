import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PositionReader {
    public List<Position> readPositions(String filePath) throws IOException {
        List<Position> positions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String stockCode = parts[0].trim();
                int quantity = Integer.parseInt(parts[1].trim());
                positions.add(new Position(stockCode, quantity));
            }
        }
        return positions;
    }
}

class Position {
    private String stockCode;
    private int quantity;

    public Position(String stockCode, int quantity) {
        this.stockCode = stockCode;
        this.quantity = quantity;
    }

    public String getStockCode() {
        return stockCode;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Position{" +
                "stockCode='" + stockCode + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}