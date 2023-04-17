import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitFileCounter {

    public static void main(String[] args) {
        // Set paths to input files
        String keysFile = "D:\\SoftwareMiningandAnalysis\\Project\\PreReleaseBugs-Keys.txt";
        String commitsFile = "D:\\SoftwareMiningandAnalysis\\hibernate-orm\\commits-520.txt";

        // Read in the keys from the keys file
        List<String> keys = readKeys(keysFile);

        // Count the number of changes to each file associated with the given keys
        Map<String, Integer> fileCounts = findFileChanges(keys, commitsFile);

        // Write the output to a CSV file
        writeOutputToFile(fileCounts);
    }

    // Reads in a list of keys associated with post-release bugs from a file
    public static List<String> readKeys(String filename) {
        List<String> keys = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String pattern = "HHH-\\d+"; // Regular expression to match the key format
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(line);
                if (m.find()) {
                    keys.add(m.group(0)); // Add the key to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }

    // Counts the number of changes to each file associated with the given keys in a commit log file
    public static Map<String, Integer> findFileChanges(List<String> keys, String commitsFile) {
        Map<String, Integer> fileCounts = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(commitsFile))) {
            String line;
            String commitId = "";
            while ((line = br.readLine()) != null) {
                if (line.startsWith("commit ")) {
                    commitId = line.substring(7);
                } else if (line.startsWith("    HHH-")) {
                    boolean matchFound = false;
                    for (String key : keys) {
                        if (line.contains(key)) {
                            matchFound = true;
                            break;
                        }
                    }
                    if (matchFound) {
                        while ((line = br.readLine()) != null) {
                            if (line.startsWith("hibernate-")) {
                                int count = fileCounts.getOrDefault(line, 0);
                                fileCounts.put(line, count + 1); // Add the file to the map and increment its count
                            } else if (line.startsWith("commit ")) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCounts;
    }

    // Writes the file counts to a CSV file
    public static void writeOutputToFile(Map<String, Integer> fileCounts) {
        try (PrintWriter writer = new PrintWriter(new File("PreReleaseBugs.csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("File,pre_release_bugs\n"); // Add the header to the CSV file
            for (String file : fileCounts.keySet()) {
                sb.append(file);
                sb.append(",");
                sb.append(fileCounts.get(file));
                sb.append("\n"); // Add the file name and its count to the CSV file
            }
            writer.write(sb.toString()); // Write the string to the file
            System.out.println("Output written to PreReleaseBugs.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
