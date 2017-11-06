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
        }
        return result; //TODO IMPLEMENT ME

    }

    /**
     * This method takes in any executeUpdate queries: INSERT, UPDATE, DELETE
     * @param query : sql query
     */
    public void makeUpdateQuery(String query){
        try {
            statement.executeUpdate(query);
        }catch (SQLException e) {
            printExceptionLogs(e);
        }
    }


    /**
     * Should call this when GUI Closes to release db connection
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            printExceptionLogs(e);
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
}