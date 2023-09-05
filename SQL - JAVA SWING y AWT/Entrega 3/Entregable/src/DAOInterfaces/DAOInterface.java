package DAOInterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import Clases.Deportista;
import Clases.DeportistaEnDisciplina;
import Clases.Disciplina;
import DAOImplements.DAODBConnectionImpl;

public interface DAOInterface<T> {
	Connection connection = DAODBConnectionImpl.getInstance().getConnection();
	
	ArrayList<T> find() throws SQLException;
	
	T findById(int id) throws SQLException;

	boolean create (T object) throws SQLException;
	 
	boolean delete (int id) throws SQLException;
	
	boolean update (int id, T object) throws SQLException;

	DeportistaEnDisciplina findByIdDep(int id) throws SQLException;

	T findByName(String disciplina) throws SQLException;

	T findByEmail(String email) throws SQLException;

	int getLastAdded() throws SQLException;

	boolean hayDeportistasDelPais(int i) throws SQLException;
	
}


