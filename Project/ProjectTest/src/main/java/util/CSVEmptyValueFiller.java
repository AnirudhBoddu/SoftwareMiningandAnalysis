package util;

import java.io.*;
import org.apache.commons.csv.*;

public class CSVEmptyValueFiller {

    public static void main(String[] args) throws Exception {
        String inputFilePath = "D:\\SoftwareMiningandAnalysis\\Project\\Metrics\\test\\merged.csv";
        String outputFilePath = "D:\\SoftwareMiningandAnalysis\\Project\\Metrics\\test\\output.csv";

        // create the CSV parser and printer
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        CSVParser csvParser = new CSVParser(new FileReader(inputFilePath), csvFormat);
        CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFilePath), csvFormat);

        // write the header of the output file
        csvPrinter.printRecord(csvParser.getHeaderMap().keySet());

        // iterate over each record in the input file, fill empty cells with zeroes,
        // and write the updated record to the output file
        for (CSVRecord record : csvParser) {
            for (String value : record) {
                if (value.isEmpty()) {
                    csvPrinter.print("0");
                } else {
                    csvPrinter.print(value);
                }
            }
            csvPrinter.println();
        }

        // close the parser and printer
        csvParser.close();
        csvPrinter.close();
    }
}
