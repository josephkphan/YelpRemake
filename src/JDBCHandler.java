import java.sql.*;

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
    public String makeSearchQuery(String query) {

        ResultSet result_set;
        String result = "";
        try {
            result_set = statement.executeQuery(query);
            while (result_set.next()) {
                System.out.println();
            }
        } catch (SQLException e) {
            printExceptionLogs(e);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result; //TODO IMPLEMENT ME

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
        System.out.println(jdbc_handler.makeUpdateQuery("INSERT INTO Business VALUES('vcNAasdWiLM4dR7D2nwwJ7nCA','Eric Goldberg, MD','4840 E Indian School Rd Ste 101 Phoenix, AZ 85018','Phoenix','AZ','-111.983758','33.499313',7,3.5,'business','true','08:00','17:00','08:00','17:00','08:00','17:00','08:00','17:00','08:00','17:00','null','null','null','null')"));


        jdbc_handler.closeConnection();

    }



}