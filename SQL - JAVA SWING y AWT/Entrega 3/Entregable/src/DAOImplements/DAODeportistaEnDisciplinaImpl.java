package DAOImplements;

import java.sql.*;
import java.util.ArrayList;

import Clases.DeportistaEnDisciplina;
import Clases.Disciplina;
import Clases.Pais;
import DAOInterfaces.DAODeportistaEnDisciplina;
import DAOInterfaces.DAOInterface;

public class DAODeportistaEnDisciplinaImpl implements DAOInterface<DeportistaEnDisciplina> {
	public boolean create (DeportistaEnDisciplina deportistaEnDisciplina) throws SQLException {
		Statement stm = connection.createStatement();
		String query = "INSERT INTO deportista_en_disciplina VALUES ('"+ deportistaEnDisciplina.getIdDeportista() + "', '"+ deportistaEnDisciplina.getIdDisciplina() +"')";
		int result = stm.executeUpdate(query);
		//DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean delete (int id) throws SQLException {
		Statement stm = connection.createStatement();
		String query = "DELETE FROM deportista_en_disciplina WHERE id_deportista = " + id;
		int result = stm.executeUpdate(query);
	//DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public DeportistaEnDisciplina findById(int id) throws SQLException {
		Statement stm = connection.createStatement();
		DeportistaEnDisciplina dep_disciplina = null;
		ResultSet rs = stm.executeQuery("SELECT * FROM deportista_en_disciplina WHERE id_disciplina = " + id);
		if(rs.next()) {
			dep_disciplina = new DeportistaEnDisciplina(rs.getInt("id_deportista"), rs.getInt("id_disciplina"));			
		} 
		//DAODBConnectionImpl.closeConnection();
		return dep_disciplina;
	}
	public DeportistaEnDisciplina findByIdDep(int id) throws SQLException {
		Statement stm = connection.createStatement();
		DeportistaEnDisciplina dep_disciplina = null;
		ResultSet rs = stm.executeQuery("SELECT * FROM deportista_en_disciplina WHERE id_deportista = " + id);
		if(rs.next()) {
			dep_disciplina = new DeportistaEnDisciplina(rs.getInt("id_deportista"), rs.getInt("id_disciplina"));			
		} 
		//DAODBConnectionImpl.closeConnection();
		return dep_disciplina;
	}

	@Override
	public ArrayList<DeportistaEnDisciplina> find() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(int id, DeportistaEnDisciplina object) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DeportistaEnDisciplina findByName(String disciplina) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeportistaEnDisciplina findByEmail(String email) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLastAdded() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hayDeportistasDelPais(int i) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}
