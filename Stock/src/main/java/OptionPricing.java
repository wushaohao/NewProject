public class OptionPricing {
    public static double calculateCallPrice(double stockPrice, double strikePrice) {
        // 简化的 Black-Scholes 模型
        return Math.max(0, stockPrice - strikePrice);
    }

    public static double calculatePutPrice(double stockPrice, double strikePrice) {
        // 简化的 Black-Scholes 模型
        return Math.max(0, strikePrice - stockPrice);
    }
}