package utils;

import constants.FrameworkConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class ReportUtility {

    private ReportUtility() {}

    public static void generateExecutionReport(List<String[]> resultsData) {
        try {
            Path reportDir = Paths.get(FrameworkConstants.REPORT_FOLDER);
            if (!Files.exists(reportDir)) {
                Files.createDirectories(reportDir);
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = reportDir.resolve("ExecutionReport_" + timestamp + ".txt").toString();

            try (FileWriter writer = new FileWriter(reportPath)) {
                writer.write("AMAZON AUTOMATION EXECUTION REPORT\n");
                writer.write("Generated: " + new Date().toString() + "\n\n");

                for (String[] row : resultsData) {
                    writer.write("========================================\n");
                    writer.write("Brand Name      : " + row[0] + "\n");
                    writer.write("Product Name    : " + row[1] + "\n");
                    writer.write("Expected Price  : " + row[2] + "\n");
                    writer.write("Actual Price    : " + row[3] + "\n");
                    writer.write("Status          : " + row[4] + "\n");
                    writer.write("========================================\n\n");
                }
            }

            System.out.println("Report saved: " + reportPath);
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}
