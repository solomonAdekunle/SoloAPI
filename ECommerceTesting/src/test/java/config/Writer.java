package config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Writer {

    private static final String filesLocation = "output/";
    private String fileName;

    Writer() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMYYYY_HHmmss");
        this.fileName = simpleDateFormat.format(now) + ".txt";
    }

    Writer(String fileName) {
        this.fileName = fileName;
    }

    void writeToFile(String content) {
        File file;
        FileWriter fileWriter = null;

        try {
            file = new File(Writer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "/" + filesLocation + fileName);
            fileWriter = new FileWriter(file, true);
            fileWriter.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void writeOutputToFile(List<String> stockNums, Integer numOfResults, String locale) {
        File file;
        FileWriter fileWriter = null;
        try {
            file = new File(Writer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "/" + filesLocation + fileName);
            fileWriter = new FileWriter(file, true);
            fileWriter.append(numOfResults.toString());
            for (String stockNum : stockNums) {
                fileWriter.append(stockNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
