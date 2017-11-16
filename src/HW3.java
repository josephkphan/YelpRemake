
import javax.swing.*;

/**
 * Main method used to Test Code
 */
public class HW3 {
    public static void main(String[] args) {

        // Run the GUI codes on Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Home(new JDBCHandler());
            }
        });
    }
}