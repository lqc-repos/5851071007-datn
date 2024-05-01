package thesis.utils.file;

import thesis.utils.constant.AppConst;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CSVExporter {
    public static boolean exportTfIdfRate(Map<String, Map<String, Double>> articleRates) {
        File currentDir = new File(System.getProperty("user.dir"));
        String projectPath = currentDir.getParent();
        String fileName = AppConst.File.TFIDF_RATE_FILE_NAME + (System.currentTimeMillis() / 1000) + ".csv";
        String filePath = projectPath + File.separator + AppConst.File.REPORT_FOLDER + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(String.join(",", AppConst.File.TFIDF_RATE_HEADERS));
            for (Map.Entry<String, Map<String, Double>> parentEntry : articleRates.entrySet()) {
                writer.newLine();
                writer.write(String.format("%s,%s,%s", parentEntry.getKey(), "", ""));
                for (Map.Entry<String, Double> childEntry : parentEntry.getValue().entrySet()) {
                    writer.newLine();
                    writer.write(String.format("%s,%s,%s", "", childEntry.getKey(), childEntry.getValue()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
