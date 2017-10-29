package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A nice Wrapper Class to easily user JFrame. This is recommended to use with JFrame's absolute Layout
 */
public class GeneralJStuff {
    static JLabel createLabel(Container pane, String string, int x, int y) {
        JLabel component = new JLabel(string);
        component.setBounds(x, y, component.getPreferredSize().width, component.getPreferredSize().height);
        pane.add(component);

        return component;
    }

    static JButton createButton(Container pane, String string, int x, int y, int width, int height, Runnable r) {
        JButton component = new JButton(string);
        component.setBounds(x, y, width, height);
        component.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
        pane.add(component);
        return component;
    }

    static JComboBox createDropDown(Container pane, String[] options, int x, int y, int width, int height, Runnable r){
        JComboBox component = new JComboBox(options);
        component.setBounds(x, y, width, height);
        component.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
        pane.add(component);
        return component;

    }

    static JScrollPane createScrollBar(Container pane){
        JScrollPane component;
        String[] colName = new String[]{"Product Name", "Price"};
        Object[][] products = new Object[][]{
                {"Galleta", "$80"},
                {"Malta", "$40"},
                {"Nestea", "$120"},
                {"Tolta", "$140"}
        };

        JTable table = new JTable(products, colName);

        component = new JScrollPane(table);
        pane.add(new JScrollPane(table));
        return component;
    }




}