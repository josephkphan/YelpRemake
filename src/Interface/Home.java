package Interface;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a nice visual for users. Regular users can use the RCMs while admins can view the
 * RMOS window after first logging in.
 */

public class Home extends JFrame implements ActionListener {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private static Container pane;

    // NOTE*** Maps mean it is static objects
    // NOTE*** ArrayList means that It will be dynamic (gathered from database)
    private Map<String, JButton> buttons;
    /*  List of Buttons:
            search
            close
    */

    private Map<String, JLabel> labels;
    /*  List of Buttons:
            title
            day_of_week
            start_time
            end_time
            attributes
    */
    private Map<String, JComboBox> drop_downs;
    /*  List of Drop downs:
            day_of_week
            start_time
            end_time
            attributes
     */

    private Map<String, JScrollPane> scroll_panes;
    /*  List of Scroll Panes:
            services
            categories
            options
     */

    /*  Description
        Services -- far left column i.e. Restaurants, Sports
        Categories -- middle column i.e. Mexican, Asian
        Optioms -- right column i.e. price range
     */


    private String[] string_days_of_week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private String[] string_hours_of_day =
            {"12:00AM", "1:00AM", "2:00AM", "3:00AM", "4:00AM", "5:00AM", "6:00AM", "7:00AM", "8:00AM", "9:00AM", "10:00AM", "11:00AM",
                    "12:00PM", "1:00PM", "2:00PM", "3:00PM", "4:00PM", "5:00PM", "6:00PM", "7:00PM", "8:00PM", "9:00PM", "10:00PM", "11:00PM"
            };


    public Home() {

        //Gui Stuff
        JFrame frame = new JFrame("Home Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane = frame.getContentPane();

        //Size and display the window.
        Insets frameInsets = frame.getInsets();
        frame.setSize(WINDOW_WIDTH + frameInsets.left + frameInsets.right,
                WINDOW_HEIGHT + frameInsets.top + frameInsets.bottom);
        frame.setVisible(true);
        pane.setLayout(null);


        //Initialize Components
        buttons = new HashMap<>();
        labels = new HashMap<>();
        drop_downs = new HashMap<>();
        buttons = new HashMap<>();
        scroll_panes = new HashMap<>();

        createLabels();
        createButtons();
        createDropDowns();
        createScrollPanes();

    }

    /**
     *
     */
    public void createLabels() {
        // Creating Labels
        labels.put("title", GeneralJStuff.createLabel(pane, "Yelp System", 475, 10));
        labels.put("day_of_week", GeneralJStuff.createLabel(pane, "Day of the Week", 50, 500));
        labels.put("start_time", GeneralJStuff.createLabel(pane, "From:", 200, 500));
        labels.put("end_time", GeneralJStuff.createLabel(pane, "To:", 350, 500));
        labels.put("attributes", GeneralJStuff.createLabel(pane, "Search For:", 500, 500));

    }

    /**
     *
     */
    public void createButtons() {
        Runnable r4 = new Runnable() {
            @Override
            public void run() {
                System.out.println(drop_downs.get("day_of_week").getSelectedItem());
            }
        };
        Runnable r5 = new Runnable() {
            @Override
            public void run() {
                System.out.println(drop_downs.get("day_of_week").getSelectedItem());
            }
        };

        buttons.put("search", GeneralJStuff.createButton(pane, "Search", 700, 500, 100, 25, r4));
        buttons.put("close", GeneralJStuff.createButton(pane, "Close", 850, 500, 100, 25, r5));
    }

    /**
     *
     */
    public void createDropDowns() {
        // Creating Drop Downs
        Runnable r0 = new Runnable() {
            @Override
            public void run() {
                System.out.println(drop_downs.get("day_of_week").getSelectedItem());
            }
        };

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println(drop_downs.get("day_of_week").getSelectedItem());
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println(drop_downs.get("day_of_week").getSelectedItem());
            }
        };
        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                System.out.println(drop_downs.get("day_of_week").getSelectedItem());
            }
        };
        drop_downs.put("day_of_week", GeneralJStuff.createDropDown(pane, string_days_of_week, 50, 500, 100, 100, r0));
        drop_downs.put("start_time", GeneralJStuff.createDropDown(pane, string_hours_of_day, 200, 500, 100, 100, r1));
        drop_downs.put("end_time", GeneralJStuff.createDropDown(pane, string_hours_of_day, 350, 500, 100, 100, r2));
        drop_downs.put("attributes", GeneralJStuff.createDropDown(pane, string_hours_of_day, 500, 500, 100, 100, r3));

    }

    /**
     *
     */
    public void createScrollPanes() {
        scroll_panes.put("service", GeneralJStuff.createScrollPane(pane, 50, 50, 145, 400));
        scroll_panes.put("category", GeneralJStuff.createScrollPane(pane, 200, 50, 145, 400));
        scroll_panes.put("options", GeneralJStuff.createScrollPane(pane, 350, 50, 145, 400));
        scroll_panes.put("results", GeneralJStuff.createScrollPane(pane, 500, 50, 450, 400));
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
