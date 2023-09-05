package DAOImplements;


import DAOInterfaces.DAOInterface;


import java.sql.*;
import java.util.ArrayList;

import Clases.*;

public class DAODeportistaImpl implements DAOInterface<Deportista> {


	public ArrayList<Deportista> find() throws SQLException {
		ArrayList<Deportista> deportistas = new ArrayList<Deportista>();
		Statement stm = connection.createStatement();	
		ResultSet rs = stm.executeQuery("SELECT * FROM deportista");
		while(rs.next()) {
			Deportista deportista = new Deportista(rs.getInt("id"), rs.getInt("id_pais"), rs.getString("telefono"), 
					rs.getString("apellidos"), rs.getString("nombres"), rs.getString("email"));
			deportistas.add(deportista);
		}
		//DAODBConnectionImpl.closeConnection();
		return deportistas;
	}
	
	public Deportista findByEmail(String p) throws SQLException {
		String query ="SELECT * FROM deportista where email = ?"; 
		PreparedStatement preparedStmt = connection.prepareStatement(query);
	    preparedStmt.setString(1, p);
	    Deportista dep = null;
	    ResultSet rs = preparedStmt.executeQuery();
		//DAODBConnectionImpl.closeConnection();
	    if(rs.next()) {
	    	dep = new Deportista(rs.getInt("id"),rs.getInt("id_pais"), rs.getString("telefono"), rs.getString("apellidos"), rs.getString("nombres"), rs.getString("email"));
	    }
		return dep;
	}
	
	public Deportista findById(int id) throws SQLException {
		Statement stm = connection.createStatement();
		Deportista deportista = null;
		ResultSet rs = stm.executeQuery("SELECT * FROM deportista WHERE id = " + id);
		if(rs.next()) {
			deportista = new Deportista(rs.getInt("id"), rs.getInt("id_pais"), rs.getString("telefono"),  rs.getString("apellidos"), 
					rs.getString("nombres"), rs.getString("email"));			
		} 
		//DAODBConnectionImpl.closeConnection();
		return deportista;
	}
	
	public int getLastAdded() throws SQLException {
		Statement stm = connection.createStatement();
		Deportista deportista = null;
		int maxID= 0;
		ResultSet rs = stm.executeQuery("SELECT max(id) as id FROM deportista");
		if(rs.next()) {
			maxID= rs.getInt("id");		
		} 
		//DAODBConnectionImpl.closeConnection();
		return maxID;
	}
	
	public boolean create (Deportista dep) throws SQLException {
		Statement stm = connection.createStatement();
		String query = "INSERT INTO deportista (apellidos, nombres, email, telefono, id_pais) VALUES ('"+ dep.getApellido() + "', '"+ dep.getNombre() +"', '"+ dep.getEmail() +"', '"+dep.getTelefono() +"', '" + dep.getId_pais() + "')";
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
		String query = "DELETE FROM deportista WHERE id = " + id;
		int result = stm.executeUpdate(query);
		//DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hayDeportistasDelPais(int idPais) throws SQLException {
		boolean result = false;
		Statement stm = connection.createStatement();	
		ResultSet rs = stm.executeQuery("SELECT id_pais FROM deportista");
		while(rs.next()) {
			if (rs.getInt("id_pais") == idPais) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	
	public boolean update (int id, Deportista deportista) throws SQLException {
	    String query = "UPDATE deportista SET apellidos = ?, nombres = ?, email = ?, telefono = ?, id_pais = ? WHERE id = ?";
		PreparedStatement preparedStmt = connection.prepareStatement(query);
	    preparedStmt.setString(1, deportista.getApellido());
	    preparedStmt.setString(2, deportista.getNombre());
	    preparedStmt.setString(3, deportista.getEmail());
	    preparedStmt.setString(4, deportista.getTelefono());
	    preparedStmt.setInt(5, deportista.getId_pais());
	    preparedStmt.setInt(6, id);

		int result = preparedStmt.executeUpdate();
		
		//DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	 }

	@Override
	public DeportistaEnDisciplina findByIdDep(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Deportista findByName(String disciplina) {
		// TODO Auto-generated method stub
		return null;
	}


}
