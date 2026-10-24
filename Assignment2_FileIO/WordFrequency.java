import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assignment 2: File I/O
 * Reads a text file, counts word frequency using a HashMap,
 * and writes the result to another file.
 *
 * Run:  javac WordFrequency.java && java WordFrequency
 * Or :  java WordFrequency myinput.txt myoutput.txt
 */
public class WordFrequency {

    // 'throws' passes the IOException to the caller
    static Map<String, Integer> countWords(String inputPath) throws IOException {
        Map<String, Integer> freq = new HashMap<>();

        // try-with-resources closes the reader automatically
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // lower-case, remove punctuation, split on whitespace
                String[] words = line.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");
                for (String word : words) {
                    if (word.isEmpty()) continue;
                    freq.put(word, freq.getOrDefault(word, 0) + 1);
                }
            }
        }
        return freq;
    }

    static void writeResult(Map<String, Integer> freq, String outputPath) throws IOException {
        // sort entries: highest frequency first, then alphabetically
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue());
            return cmp != 0 ? cmp : a.getKey().compareTo(b.getKey());
        });

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputPath));
            writer.write("Word Frequency Report");
            writer.newLine();
            writer.write("=====================");
            writer.newLine();
            for (Map.Entry<String, Integer> e : entries) {
                writer.write(String.format("%-15s %d", e.getKey(), e.getValue()));
                writer.newLine();
            }
            System.out.println("Result written to: " + outputPath);
        } finally {
            // 'finally' always runs: good place to close resources
            if (writer != null) {
                try { writer.close(); } catch (IOException ignored) { }
            }
        }
    }

    public static void main(String[] args) {
        String inputPath  = args.length > 0 ? args[0] : "input.txt";
        String outputPath = args.length > 1 ? args[1] : "output.txt";

        try {
            Map<String, Integer> freq = countWords(inputPath);

            if (freq.isEmpty()) {
                System.out.println("The file has no words.");
                return;
            }

            int total = 0;
            for (int c : freq.values()) total += c;
            System.out.println("Total words  : " + total);
            System.out.println("Unique words : " + freq.size());

            writeResult(freq, outputPath);
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: input file '" + inputPath + "' was not found.");
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}
