package Interface;

/**
 * Created by josephphan on 10/28/17.
 */

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Test {


    public static void main(String[] str) {
        String[] colName = new String[]{"Product Name", "Price"};
        Object[][] products = new Object[][]{
                {"Galleta", "$80"},
                {"Malta", "$40"},
                {"Nestea", "$120"},
                {"Tolta", "$140"}
        };

        JTable table = new JTable(products, colName);

        JFrame frame = new JFrame("Simple Table Example");

        // create scroll pane for wrapping the table and add
        // it to the frame
        frame.add(new JScrollPane(table));
        frame.pack();
        frame.setVisible(true);
    }


}
