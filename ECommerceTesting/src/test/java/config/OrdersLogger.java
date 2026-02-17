package config;

public class OrdersLogger {
    private static CSVFileWriter csvFileWriter;

    public static void init() {
        csvFileWriter = new CSVFileWriter("orders", true);
    }

    public static void logAnOrder(String orderNumber, String country) {
        String order[] = {orderNumber, country};
        csvFileWriter.writeDataToCSVFile(order);
    }
}
