package Clases;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import DAOImplements.DAODeportistaEnDisciplinaImpl;
import DAOImplements.DAODeportistaImpl;
import DAOImplements.DAODisciplinaImpl;
import DAOImplements.DAOPaisImpl;
import DAOInterfaces.DAOInterface;
import FactoryDAO.FactoryDAO;

public class ExportCSV {

	public ExportCSV() throws IOException{
		FileWriter csv = new FileWriter("src/CSV/Deportista.csv");
		csv.append("Nombre");
		csv.append(";");
		csv.append("Apellido");
		csv.append(";");
		csv.append("Nacionalidad");
		csv.append(";");
		csv.append("Disciplina");
		csv.append('\n');
		
		FactoryDAO fact = new FactoryDAO();
		DAOInterface<Deportista> deportista = fact.getDeportista();
		DAOInterface<Pais> pais = fact.getPais();
		DAOInterface<DeportistaEnDisciplina> dep_disc = fact.getDeportistaEnDisciplina();
		DAOInterface<Disciplina> disciplina = fact.getDisciplina();
		try {
			ArrayList<Deportista> arreglo = deportista.find();
			for(int i=0; i<arreglo.size(); i++) {
				csv.append(arreglo.get(i).getNombre());
				csv.append(";");
				csv.append(arreglo.get(i).getApellido());
				csv.append(";");
				csv.append(pais.findById(arreglo.get(i).getId_pais()).getNombre());
				csv.append(";");
				csv.append(disciplina.findById(dep_disc.findByIdDep(arreglo.get(i).getId()).getIdDisciplina()).getNombre());
				csv.append('\n');
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		csv.close();
		
	}
}
