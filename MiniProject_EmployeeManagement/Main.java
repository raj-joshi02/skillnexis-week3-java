import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Employee Management System (Collections + File I/O + Exception Handling)
 * Run:  javac *.java && java Main
 */
public class Main {

    private static final String DATA_FILE = "employees.txt";
    private static final Scanner sc = new Scanner(System.in);
    private static final EmployeeManager manager = new EmployeeManager();

    // ---------- safe input helpers (loop until valid) ----------
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = sc.nextLine().trim();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = sc.nextLine().trim();
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a valid number.");
            }
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    // ---------- menu actions ----------
    private static void addEmployee() {
        try {
            int id = readInt("Enter ID: ");
            String name = readText("Enter Name: ");
            String dept = readText("Enter Department: ");
            double salary = readDouble("Enter Salary: ");
            manager.addEmployee(new Employee(id, name, dept, salary));
            System.out.println("Employee added successfully.");
        } catch (InvalidEmployeeException e) {
            System.out.println("Could not add employee: " + e.getMessage());
        }
    }

    private static void printList(List<Employee> list) {
        if (list.isEmpty()) {
            System.out.println("No employees to show.");
            return;
        }
        System.out.printf("%-5s %-18s %-14s %12s%n", "ID", "Name", "Department", "Salary");
        System.out.println("-".repeat(53));
        for (Employee e : list) System.out.println(e);
    }

    private static void searchById() {
        try {
            Employee e = manager.findById(readInt("Enter ID to search: "));
            printList(List.of(e));
        } catch (InvalidEmployeeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void searchByName() {
        printList(manager.searchByName(readText("Enter name (or part of it): ")));
    }

    private static void updateSalary() {
        try {
            Employee e = manager.findById(readInt("Enter ID: "));
            e.setSalary(readDouble("Enter new salary: "));
            System.out.println("Salary updated.");
        } catch (InvalidEmployeeException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private static void deleteEmployee() {
        try {
            manager.deleteEmployee(readInt("Enter ID to delete: "));
            System.out.println("Employee deleted.");
        } catch (InvalidEmployeeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void departmentSummary() {
        Map<String, Double> totals = manager.salaryByDepartment();
        if (totals.isEmpty()) {
            System.out.println("No data.");
            return;
        }
        System.out.println("Departments: " + manager.getDepartments().size());
        for (Map.Entry<String, Double> e : totals.entrySet()) {
            System.out.printf("  %-14s total salary: %,.2f%n", e.getKey(), e.getValue());
        }
    }

    private static void save() {
        try {
            manager.saveToFile(DATA_FILE);
            System.out.println("Saved " + manager.size() + " employee(s) to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Could not save file: " + e.getMessage());
        }
    }

    private static void load() {
        try {
            int count = manager.loadFromFile(DATA_FILE);
            System.out.println("Loaded " + count + " employee(s) from " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Could not load file: " + e.getMessage());
        }
    }

    private static void showMenu() {
        System.out.println("\n===== Employee Management System =====");
        System.out.println("1. Add employee");
        System.out.println("2. View all employees");
        System.out.println("3. Search by ID");
        System.out.println("4. Search by name");
        System.out.println("5. Update salary");
        System.out.println("6. Delete employee");
        System.out.println("7. View by salary (high to low)");
        System.out.println("8. Department summary");
        System.out.println("9. Save to file");
        System.out.println("10. Load from file");
        System.out.println("0. Exit");
    }

    public static void main(String[] args) {
        try {
            boolean running = true;
            while (running) {
                showMenu();
                int choice = readInt("Choose an option: ");
                switch (choice) {
                    case 1:  addEmployee(); break;
                    case 2:  printList(manager.getAllSortedById()); break;
                    case 3:  searchById(); break;
                    case 4:  searchByName(); break;
                    case 5:  updateSalary(); break;
                    case 6:  deleteEmployee(); break;
                    case 7:  printList(manager.getAllSortedBySalary()); break;
                    case 8:  departmentSummary(); break;
                    case 9:  save(); break;
                    case 10: load(); break;
                    case 0:  running = false; break;
                    default: System.out.println("Invalid option. Choose 0-10.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nInput closed. Exiting.");
        } finally {
            System.out.println("Goodbye!");
            sc.close();
        }
    }
}
