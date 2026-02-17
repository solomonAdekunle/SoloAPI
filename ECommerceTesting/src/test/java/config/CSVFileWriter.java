package config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CSVFileWriter {

    private final static String FILE_EXTENSION = ".csv";
    private final static String CSV_DELIMITER = ",";
    private Writer writer;

    public CSVFileWriter() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMYYYY_HHmmss");
        writer = new Writer(simpleDateFormat.format(now) + FILE_EXTENSION);
    }

    public CSVFileWriter(String fileName, Boolean useTimeStamp) {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMYYYY_HHmmss");

        if (useTimeStamp) {
            writer = new Writer(fileName + "_" + simpleDateFormat.format(now) + FILE_EXTENSION);
        } else {
            writer = new Writer(fileName + FILE_EXTENSION);
        }
    }

    public void writeDataToCSVFile(String[] content) {
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < content.length; i++) {
            if (i > 0) {
                line.append(CSV_DELIMITER);
            }
            line.append(content[i]);
        }
        line.append("\n");
        writer.writeToFile(line.toString());
    }

    public void writeDataToCSVFile(List<String> stockNums, Integer numOfResults) {
        StringBuilder line = new StringBuilder();
        line.append(numOfResults);
        for (int i = 0; i < stockNums.size(); i++) {
            line.append(CSV_DELIMITER);
            line.append(stockNums.get(i));
        }
        line.append("\n");
        writer.writeToFile(line.toString());
    }
}
