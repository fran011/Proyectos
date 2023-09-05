package DAOImplements;

import java.sql.*;
import java.util.ArrayList;

import Clases.DeportistaEnDisciplina;
import Clases.Pais;
import DAOInterfaces.*;

public class DAOPaisImpl implements DAOInterface<Pais> {

	public ArrayList<Pais> find() throws SQLException {
		ArrayList<Pais> paises = new ArrayList<Pais>();
		Statement stm = connection.createStatement();	
		ResultSet rs = stm.executeQuery("SELECT * FROM pais");
		while(rs.next()) {
			Pais pais = new Pais(rs.getInt("id"), rs.getString("nombre"));
			paises.add(pais);
		}
		//DAODBConnectionImpl.closeConnection();
		return paises;
	}
	
	public Pais findById(int id) throws SQLException {
		Statement stm = connection.createStatement();
		Pais pais = null;
		ResultSet rs = stm.executeQuery("SELECT * FROM pais WHERE id = " + id);
		if(rs.next()) {
			pais = new Pais(rs.getInt("id"), rs.getString("nombre"));			
		} 
		//DAODBConnectionImpl.closeConnection();
		return pais;
	}
	public Pais findByName(String p) throws SQLException {
		String query ="SELECT * FROM pais where nombre = ?"; 
		PreparedStatement preparedStmt = connection.prepareStatement(query);
	    preparedStmt.setString(1, p);
	    Pais pais = null;
	    ResultSet rs = preparedStmt.executeQuery();
		//DAODBConnectionImpl.closeConnection();
	    if(rs.next()) {
	    	pais = new Pais(rs.getInt("id"), rs.getString("nombre"));
	    }
		return pais;
	}
	
	public boolean create (Pais pais) throws SQLException {
		Statement stm = connection.createStatement();
		String query = "INSERT INTO pais (nombre) VALUES ('" + pais.getNombre() + "')";
		int result = stm.executeUpdate(query);
//		DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
	 
	
	public boolean delete (int id) throws SQLException {
		Statement stm = connection.createStatement();
		String query = "DELETE FROM pais WHERE id = " + id;
		int result = stm.executeUpdate(query);
		//DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean update (int id, Pais pais) throws SQLException {
	    String query = "UPDATE pais SET nombre = ? WHERE id = ?";
		PreparedStatement preparedStmt = connection.prepareStatement(query);
	    preparedStmt.setString(1, pais.getNombre());
	    preparedStmt.setInt(2, id);

		int result = preparedStmt.executeUpdate();
		
		//DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	 }

	@Override
	public DeportistaEnDisciplina findByIdDep(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pais findByEmail(String email) throws SQLException {
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
