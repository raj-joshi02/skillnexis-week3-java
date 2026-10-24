import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Assignment 1: Student Marks Record Program
 * - Stores data in an ArrayList
 * - Finds highest and lowest marks
 * - Also shows sorting, searching and a HashSet (unique marks)
 *
 * Run:  javac StudentMarks.java && java StudentMarks
 */
public class StudentMarks {

    // Simple holder class for one student's record
    static class Student {
        private final String name;
        private final int marks;

        Student(String name, int marks) {
            this.name = name;
            this.marks = marks;
        }

        String getName() { return name; }
        int getMarks()   { return marks; }

        @Override
        public String toString() {
            return String.format("%-10s : %d", name, marks);
        }
    }

    // ---- Highest / Lowest (manual loop, no Collections.max) ----
    static Student findHighest(ArrayList<Student> list) {
        if (list.isEmpty()) throw new IllegalStateException("No student records available.");
        Student highest = list.get(0);
        for (Student s : list) {
            if (s.getMarks() > highest.getMarks()) highest = s;
        }
        return highest;
    }

    static Student findLowest(ArrayList<Student> list) {
        if (list.isEmpty()) throw new IllegalStateException("No student records available.");
        Student lowest = list.get(0);
        for (Student s : list) {
            if (s.getMarks() < lowest.getMarks()) lowest = s;
        }
        return lowest;
    }

    static double findAverage(ArrayList<Student> list) {
        int total = 0;
        for (Student s : list) total += s.getMarks();
        return list.isEmpty() ? 0 : (double) total / list.size();
    }

    // ---- Bubble sort (descending by marks) ----
    static void bubbleSortDescending(ArrayList<Student> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (list.get(j).getMarks() < list.get(j + 1).getMarks()) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;   // already sorted
        }
    }

    // ---- Linear search by name ----
    static Student searchByName(ArrayList<Student> list, String name) {
        for (Student s : list) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    // ---- Binary search on a SORTED ascending list of marks ----
    static int binarySearch(ArrayList<Integer> sortedMarks, int target) {
        int low = 0, high = sortedMarks.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int value = sortedMarks.get(mid);
            if (value == target) return mid;
            if (value < target) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("Aarav", 88));
        students.add(new Student("Priya", 92));
        students.add(new Student("Rahul", 67));
        students.add(new Student("Sneha", 74));
        students.add(new Student("Vikram", 55));
        students.add(new Student("Neha", 92));

        System.out.println("===== Student Marks Record =====");
        for (Student s : students) System.out.println(s);

        System.out.println("\nTotal students : " + students.size());
        System.out.printf("Average marks  : %.2f%n", findAverage(students));

        Student high = findHighest(students);
        Student low  = findLowest(students);
        System.out.println("Highest marks  : " + high.getMarks() + " (" + high.getName() + ")");
        System.out.println("Lowest marks   : " + low.getMarks() + " (" + low.getName() + ")");

        // Sorting
        bubbleSortDescending(students);
        System.out.println("\n--- Ranking (sorted high to low) ---");
        int rank = 1;
        for (Student s : students) System.out.println(rank++ + ". " + s);

        // Searching by name
        System.out.println("\n--- Linear search ---");
        Student found = searchByName(students, "rahul");
        System.out.println(found != null ? "Found: " + found : "Student not found");
        Student missing = searchByName(students, "Karan");
        System.out.println(missing != null ? "Found: " + missing : "Karan: Student not found");

        // Binary search on sorted marks
        ArrayList<Integer> marksOnly = new ArrayList<>();
        for (Student s : students) marksOnly.add(s.getMarks());
        Collections.sort(marksOnly);   // ascending, needed for binary search
        System.out.println("\n--- Binary search ---");
        System.out.println("Sorted marks : " + marksOnly);
        int idx = binarySearch(marksOnly, 74);
        System.out.println("Marks 74 found at index: " + idx);
        System.out.println("Marks 100 found at index: " + binarySearch(marksOnly, 100));

        // HashSet removes duplicates
        Set<Integer> uniqueMarks = new HashSet<>(marksOnly);
        System.out.println("\nTotal marks entries : " + marksOnly.size());
        System.out.println("Unique marks (HashSet): " + uniqueMarks.size());

        // Exception handling with an empty list
        System.out.println("\n--- Exception handling demo ---");
        try {
            findHighest(new ArrayList<>());
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("Done.");
        }
    }
}
