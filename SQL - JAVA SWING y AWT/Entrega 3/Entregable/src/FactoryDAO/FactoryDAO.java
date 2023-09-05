package FactoryDAO;

import DAOImplements.*;
import DAOInterfaces.DAOInterface;
import Clases.*;
public class FactoryDAO {

	
	public DAOInterface<Deportista> getDeportista() {
		return new DAODeportistaImpl();
	}
	
	public DAODBConnectionImpl getConnection() {
		return null;
	}
	
	public DAOInterface<Pais> getPais(){
		return new DAOPaisImpl();
	}
	
	public DAOInterface<Disciplina> getDisciplina() {
		return new DAODisciplinaImpl();
	}
	
	public DAOInterface<DeportistaEnDisciplina> getDeportistaEnDisciplina() {
		return new DAODeportistaEnDisciplinaImpl();
	}
	
	
}
