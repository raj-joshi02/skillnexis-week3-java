import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Business logic: stores employees in a HashMap and handles file save/load. */
public class EmployeeManager {

    // key = employee ID, value = Employee (fast lookup by ID)
    private final Map<Integer, Employee> employees = new HashMap<>();

    public void addEmployee(Employee e) throws InvalidEmployeeException {
        if (employees.containsKey(e.getId())) {
            throw new InvalidEmployeeException("Employee ID " + e.getId() + " already exists.");
        }
        employees.put(e.getId(), e);
    }

    public Employee findById(int id) throws InvalidEmployeeException {
        Employee e = employees.get(id);
        if (e == null) throw new InvalidEmployeeException("No employee found with ID " + id + ".");
        return e;
    }

    public void deleteEmployee(int id) throws InvalidEmployeeException {
        if (employees.remove(id) == null) {
            throw new InvalidEmployeeException("No employee found with ID " + id + ".");
        }
    }

    public List<Employee> getAllSortedById() {
        List<Employee> list = new ArrayList<>(employees.values());
        list.sort(Comparator.comparingInt(Employee::getId));
        return list;
    }

    public List<Employee> getAllSortedBySalary() {
        List<Employee> list = new ArrayList<>(employees.values());
        list.sort((a, b) -> Double.compare(b.getSalary(), a.getSalary()));   // high to low
        return list;
    }

    public List<Employee> searchByName(String keyword) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (e.getName().toLowerCase().contains(keyword.toLowerCase())) result.add(e);
        }
        result.sort(Comparator.comparingInt(Employee::getId));
        return result;
    }

    /** Total salary per department (TreeMap keeps departments alphabetical). */
    public Map<String, Double> salaryByDepartment() {
        Map<String, Double> totals = new TreeMap<>();
        for (Employee e : employees.values()) {
            totals.merge(e.getDepartment(), e.getSalary(), Double::sum);
        }
        return totals;
    }

    /** HashSet gives the distinct department names. */
    public Set<String> getDepartments() {
        Set<String> depts = new HashSet<>();
        for (Employee e : employees.values()) depts.add(e.getDepartment());
        return depts;
    }

    public int size() { return employees.size(); }

    // ---------------- File handling ----------------

    public void saveToFile(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Employee e : getAllSortedById()) {
                writer.write(e.toFileLine());
                writer.newLine();
            }
        }
    }

    /**
     * Loads employees from file, replacing current data.
     * Bad lines are skipped with a warning instead of crashing the program.
     * @return number of employees loaded
     */
    public int loadFromFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("File '" + path + "' does not exist. Save some data first.");
        }

        Map<Integer, Employee> loaded = new HashMap<>();
        int lineNo = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.trim().isEmpty()) continue;
                try {
                    Employee e = Employee.fromFileLine(line);
                    if (loaded.containsKey(e.getId())) {
                        throw new InvalidEmployeeException("Duplicate ID " + e.getId() + ".");
                    }
                    loaded.put(e.getId(), e);
                } catch (InvalidEmployeeException ex) {
                    System.out.println("  Skipped line " + lineNo + ": " + ex.getMessage());
                }
            }
        }
        employees.clear();
        employees.putAll(loaded);
        return employees.size();
    }
}
