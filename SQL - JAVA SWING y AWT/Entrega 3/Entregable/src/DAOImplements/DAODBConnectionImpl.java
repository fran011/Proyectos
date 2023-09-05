package DAOImplements;

import java.sql.*;

public class DAODBConnectionImpl {

	private static DAODBConnectionImpl dbInstance;
    private static Connection connection;
    private static String user;
    private static String password;

    private DAODBConnectionImpl() {}

    public static void setUser(String userR) {
    	DAODBConnectionImpl.user = userR;
    }
    
    public static void setPassword(String passwordR) {
    	DAODBConnectionImpl.password = passwordR;
    }
    
    public static DAODBConnectionImpl getInstance(){
    	if(dbInstance == null){
    		dbInstance = new DAODBConnectionImpl();
    	}
    	return dbInstance;
    }

    public Connection getConnection(){
        if(connection == null){
            try {
                String url = "jdbc:mysql://localhost:3306/tokyo2021_e3";
                connection = DriverManager.getConnection(url, DAODBConnectionImpl.user, DAODBConnectionImpl.password);
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return connection;
    }
    
    public static void closeConnection() throws SQLException {
    	connection.close();
    }
}