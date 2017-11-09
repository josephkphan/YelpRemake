
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Reviews extends JFrame implements ActionListener {

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 300;

    private static Container pane;

    private String[] col_names = {
      "Review Date", "Stars", "Review Text", "User ID", "Useful Votes"
    };

    Object[][] test_data = new Object[][]{
            {"1/1/1", "3","Awesome Restaurant","ID:1","3"},
            {"1/2/3", "5","Bad Waiter","ID:2","4"},
            {"9/9/9", "4","Got Laid in Bathroom","ID:3","5"}
    };

    public Reviews(String business_name){
        JFrame frame = new JFrame(business_name+" Reviews");
        pane = frame.getContentPane();

        //Size and display the window.
        Insets frameInsets = frame.getInsets();
        frame.setSize(WINDOW_WIDTH + frameInsets.left + frameInsets.right,
                WINDOW_HEIGHT + frameInsets.top + frameInsets.bottom);
        frame.setVisible(true);
        pane.setLayout(null);

        GeneralJStuff.createTableScrollPane(pane,col_names,test_data,25,25,450,250);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
