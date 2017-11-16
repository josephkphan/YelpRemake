import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

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

    static JScrollPane createCheckBoxScrollPane(Container pane, String[] str_list, int x, int y, int width, int height,  Set<String> set, Runnable r){
        //TODO Remove Set<String set.. this is adds coupling.. not the best way. quick workaround
        JScrollPane component;
        CheckListItem[] arr = new CheckListItem[str_list.length];
        for (int i=0; i<str_list.length; i++){
            arr[i] = new CheckListItem(str_list[i]);
        }
        JList list = new JList(arr);
        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());// Get index of item
                // clicked
                CheckListItem item = (CheckListItem) list.getModel()
                        .getElementAt(index);
                item.setSelected(!item.isSelected()); // Toggle selected state
                list.repaint(list.getCellBounds(index, index));// Repaint cell
                String item_string = item.toString();
                System.out.println(item.toString()); //TODO THIS IS THE PRINT STATEMENT

                if(set.contains(item_string)){
                    set.remove(item_string);
                }else{
                    set.add(item_string);
                }
                System.out.println(set.toString());

                r.run();

            }
        });

        component = new JScrollPane(list);
        component.setBounds(x, y, width, height);
        pane.add(component);
        return component;
    }


    static JScrollPane createTableScrollPane(Container pane, String[] col_names, Object[][] data, int x, int y, int width, int height, StringBuilder id, StringBuilder name, Runnable r){
        JScrollPane component;
        JTable table = new JTable(data, col_names);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {//TODO BUG this produces two windows
                // do some actions here, for example
                // print first column value from selected row
                String business_name = table.getValueAt(table.getSelectedRow(), 0).toString();
                String business_id = table.getValueAt(table.getSelectedRow(), 5).toString();
                System.out.println(business_id);
                id.setLength(0); // clear it out
                id.append(business_id);
                name.setLength(0);
                name.append(business_name);

                r.run();

            }
        });

        component = new JScrollPane(table);
        component.setBounds(x, y, width, height);
        pane.add(component);
        return component;
    }

    static JScrollPane createTableScrollPane(Container pane, String[] col_names, Object[][] data,  int x, int y, int width, int height){
        JScrollPane component;
        JTable table = new JTable(data, col_names);
        component = new JScrollPane(table);
        component.setBounds(x, y, width, height);
        pane.add(component);
        return component;
    }





}


class CheckListItem {

    private String label;
    private boolean isSelected = false;

    public CheckListItem(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return label;
    }
}

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItem) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}