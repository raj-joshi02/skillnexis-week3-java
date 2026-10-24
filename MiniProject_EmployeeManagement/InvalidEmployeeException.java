/** Custom checked exception thrown when employee data is invalid. */
public class InvalidEmployeeException extends Exception {
    public InvalidEmployeeException(String message) {
        super(message);
    }
}
