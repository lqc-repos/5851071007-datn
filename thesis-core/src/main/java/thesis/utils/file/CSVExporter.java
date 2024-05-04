package thesis.utils.file;

import thesis.core.label_handler.dto.ArticleIdfInfo;
import thesis.utils.constant.AppConst;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVExporter {
    public static boolean exportTfIdfRate(List<ArticleIdfInfo> articleIdfInfos) {
        File currentDir = new File(System.getProperty("user.dir"));
        String projectPath = currentDir.getParent();
        String fileName = AppConst.File.TFIDF_RATE_FILE_NAME + (System.currentTimeMillis() / 1000) + ".csv";
        String filePath = projectPath + File.separator + AppConst.File.REPORT_FOLDER + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(String.join(",", AppConst.File.TFIDF_RATE_HEADERS));
            for (ArticleIdfInfo articleIdfInfo : articleIdfInfos) {
                writer.newLine();
                writer.write(String.format("%s,%s,%s,%s,%s,%s", articleIdfInfo.getArticleId(), "", "", "", "", ""));
                for (ArticleIdfInfo.TfIdfInfo TFIDFInfo : articleIdfInfo.getTfIdfInfos()) {
                    writer.newLine();
                    writer.write(String.format("%s,%s,%s,%s,%s,%s",
                            "", TFIDFInfo.getLabel(), TFIDFInfo.getNer(), TFIDFInfo.getTfIdf(), TFIDFInfo.getTf(), TFIDFInfo.getIdf()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
