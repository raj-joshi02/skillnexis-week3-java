/** Employee model: encapsulated fields + validation + file-line conversion. */
public class Employee {
    private final int id;
    private String name;
    private String department;
    private double salary;

    // 'throws' declares that this constructor can reject bad data
    public Employee(int id, String name, String department, double salary)
            throws InvalidEmployeeException {
        if (id <= 0) {
            throw new InvalidEmployeeException("ID must be a positive number.");
        }
        this.id = id;
        setName(name);
        setDepartment(department);
        setSalary(salary);
    }

    public int getId()            { return id; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
    public double getSalary()     { return salary; }

    public void setName(String name) throws InvalidEmployeeException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidEmployeeException("Name cannot be empty.");
        if (name.contains(","))
            throw new InvalidEmployeeException("Name cannot contain a comma.");
        this.name = name.trim();
    }

    public void setDepartment(String department) throws InvalidEmployeeException {
        if (department == null || department.trim().isEmpty())
            throw new InvalidEmployeeException("Department cannot be empty.");
        if (department.contains(","))
            throw new InvalidEmployeeException("Department cannot contain a comma.");
        this.department = department.trim();
    }

    public void setSalary(double salary) throws InvalidEmployeeException {
        if (Double.isNaN(salary) || Double.isInfinite(salary) || salary < 0)
            throw new InvalidEmployeeException("Salary must be a valid number, 0 or more.");
        this.salary = salary;
    }

    /** Format used in the .txt file: id,name,department,salary */
    public String toFileLine() {
        return id + "," + name + "," + department + "," + salary;
    }

    /** Builds an Employee back from one line of the file. */
    public static Employee fromFileLine(String line) throws InvalidEmployeeException {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new InvalidEmployeeException("Expected 4 fields but found " + parts.length + ".");
        }
        try {
            int id = Integer.parseInt(parts[0].trim());
            double salary = Double.parseDouble(parts[3].trim());
            return new Employee(id, parts[1], parts[2], salary);
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeException("ID or salary is not a valid number.");
        }
    }

    @Override
    public String toString() {
        return String.format("%-5d %-18s %-14s %,12.2f", id, name, department, salary);
    }
}
