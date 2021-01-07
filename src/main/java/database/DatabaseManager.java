package database;

public class DatabaseManager {

    private Integer priceValue = 0;

    public synchronized int getPriceValue() {
        return priceValue;
    }

    public synchronized void setPriceValue(int value) {
        this.priceValue = value;
    }

    public synchronized void increasePriceValue() {
        priceValue++;
    }

}
