package DAOInterfaces;

import java.sql.Connection;
import java.sql.SQLException;

import DAOImplements.DAODBConnectionImpl;

public interface DAODisciplina {
	
	Connection connection = DAODBConnectionImpl.getInstance().getConnection();
	
	boolean delete (int id) throws SQLException;
}
