import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MarketDataProvider {
    private final Random random = new Random();
    private double currentPrice = 100.0; // 初始价格
    private Timer timer;

    public void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 随机价格波动
                double change = random.nextDouble() * 2 - 1; // -1 到 1 之间的波动
                currentPrice += change;
                System.out.printf("Current Price: %.2f%n", currentPrice);
            }
        }, 500, 1500); // 每 0.5 到 2 秒随机变化
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void stop() {
        timer.cancel();
    }
}