package DAOInterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import Clases.DeportistaEnDisciplina;
import DAOImplements.DAODBConnectionImpl;

public interface DAODeportistaEnDisciplina {
	Connection connection = DAODBConnectionImpl.getInstance().getConnection();
	
	boolean create (DeportistaEnDisciplina deportistaEnDisciplina) throws SQLException;
	
	boolean delete (int id) throws SQLException;
}
