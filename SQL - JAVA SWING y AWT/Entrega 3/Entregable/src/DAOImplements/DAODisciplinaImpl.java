package DAOImplements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Clases.DeportistaEnDisciplina;
import Clases.Disciplina;
import Clases.Pais;
import DAOInterfaces.DAODisciplina;
import DAOInterfaces.DAOInterface;

public class DAODisciplinaImpl implements DAOInterface<Disciplina>{
	
	public boolean delete (int id) throws SQLException {
		Statement stm = connection.createStatement();
		String query = "DELETE FROM disciplina WHERE id = " + id;
		int result = stm.executeUpdate(query);
		//DAODBConnectionImpl.closeConnection();
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
	public Disciplina findById(int id) throws SQLException {
		Statement stm = connection.createStatement();
		Disciplina disciplina = null;
		ResultSet rs = stm.executeQuery("SELECT * FROM disciplina WHERE id = " + id);
		if(rs.next()) {
			disciplina = new Disciplina(rs.getInt("id"), rs.getString("nombre"));			
		} 
		//DAODBConnectionImpl.closeConnection();
		return disciplina;
	}

	
	public Disciplina findByName(String p) throws SQLException {
		String query ="SELECT * FROM disciplina where nombre = ?"; 
		PreparedStatement preparedStmt = connection.prepareStatement(query);
	    preparedStmt.setString(1, p);
	    Disciplina disciplina = null;
	    ResultSet rs = preparedStmt.executeQuery();
		//DAODBConnectionImpl.closeConnection();
	    if(rs.next()) {
	    	disciplina = new Disciplina(rs.getInt("id"), rs.getString("nombre"));
	    }
		return disciplina;
	}
	
	@Override
	public ArrayList<Disciplina> find() throws SQLException {
		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		Statement stm = connection.createStatement();	
		ResultSet rs = stm.executeQuery("SELECT * FROM disciplina");
		while(rs.next()) {
			Disciplina disciplina = new Disciplina(rs.getInt("id"), rs.getString("nombre"));
			disciplinas.add(disciplina);
		}
		//DAODBConnectionImpl.closeConnection();
		return disciplinas;

	}
	@Override
	public boolean create(Disciplina object) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean update(int id, Disciplina object) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public DeportistaEnDisciplina findByIdDep(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Disciplina findByEmail(String email) throws SQLException {
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
