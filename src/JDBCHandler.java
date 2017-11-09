import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JDBCHandler {

    private static String connection_url = "jdbc:oracle:thin:@localhost:1521:ORCLCDB";
    private static String user = "admin";
    private static String pass = "admin";

    private Connection connection = null;
    private Statement statement = null;

    //Constructor

    /**
     * This will form the JDBC Connection. Connection parameters are data-members of this object
     */
    public JDBCHandler() {
        System.out.println("-------- Oracle JDBC Connection Testing ------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver? Exiting Program.");
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Oracle JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection(connection_url, user, pass);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console. Exiting Program");
            e.printStackTrace();
            System.exit(0);
        }
        if (connection != null) {
            System.out.println("Connection to Database Successful!");
        } else {
            System.out.println("Failed to make connection! Exiting Program");
            System.exit(0);
        }
    }

    //Getters
    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }


    //Other Methods

    /**
     * This method takes in any search queries. i.e. SELECT * FROM <TABLE_NAME>
     * @param query : sql query
     */
    public ArrayList<String[]> makeSearchQuery(String query, int num_columns) {
        ArrayList<String[]> final_result = new ArrayList<>();

        ResultSet result_set;
        String result = "";
        try {
            result_set = statement.executeQuery(query);
            while (result_set.next()) {
                String[] row = new String[num_columns];
                for (int i=0; i< num_columns; i++){
                    row[i] = result_set.getString(i+1);
                }
                final_result.add(row);
            }
        } catch (SQLException e) {
            printExceptionLogs(e);
        }catch (Exception e){
            e.printStackTrace();
        }
        return final_result;

    }

    /**
     *
     * @param list
     * @return
     */
    public Object[][] arrayListToObjectArray(ArrayList<String[]> list){
        if(list.size() == 0){
            return new Object[0][0];
        }
        Object[][] obj = new Object[list.size()][list.get(0).length];
        for(int i=0; i<list.size(); i++){
            for (int j=0; j<list.get(i).length; j++){
                obj[i][j]= list.get(i)[j];
            }
        }
        System.out.println(list.toString());
        return obj;
    }

    /**
     *
     * @param list
     * @return
     */
    public String[] arrayListToStringArray(ArrayList<String[]> list){
        if(list.size() == 0){
            return new String[0];
        }
        String[] arr = new String[list.size()];
        for(int i=0; i<list.size(); i++){
            arr[i] = list.get(i)[0];
        }
//        System.out.println(Arrays.toString(arr));
        return arr;
    }

    /**
     * This method takes in any executeUpdate queries: INSERT, UPDATE, DELETE
     * @param query : sql query
     * returns true if query was successful
     * returns false if query failed
     */
    public boolean makeUpdateQuery(String query){
        try {
            statement.executeUpdate(query);
            return true;
        }catch (SQLException e) {
            printExceptionLogs(e);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Should call this when GUI Closes to release db connection
     */
    public void closeConnection() {
        System.out.println("Closing Database Connection!");
        try {
            connection.close();
        } catch (SQLException e) {
            printExceptionLogs(e);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * will print out SQL error logs from a exception as input
     */
    public void printExceptionLogs(SQLException e){
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }



    public static void main(String[] argv) {
        //USED FOR TESTING
        JDBCHandler jdbc_handler = new JDBCHandler();
//        Boolean i = jdbc_handler.makeUpdateQuery("INSERT INTO YelpUser VALUES('qdtrmBGNgqCvugpHMHL_bKFgQ','Lee','2012-02',3.83,6,'user',0,0,0,0,0,0,0,5,1)");
//        System.out.println(jdbc_handler.makeUpdateQuery("INSERT INTO Business VALUES('vcNAasdWiLM4dR7D2nwwJ7nCA','Eric Goldberg, MD','4840 E Indian School Rd Ste 101 Phoenix, AZ 85018','Phoenix','AZ','-111.983758','33.499313',7,3.5,'business','true','08:00','17:00','08:00','17:00','08:00','17:00','08:00','17:00','08:00','17:00','null','null','null','null')"));

        jdbc_handler.arrayListToStringArray(jdbc_handler.makeSearchQuery("SELECT name,user_id from YelpUser", 2));
        jdbc_handler.closeConnection();

    }



}