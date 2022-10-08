import java.util.Timer;
import java.util.TimerTask;

public class PortfolioSubscriber {
    private Portfolio portfolio;

    public PortfolioSubscriber(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                portfolio.calculateAndPublish();
            }
        }, 0, 2000); // 每 2 秒计算一次
    }
}