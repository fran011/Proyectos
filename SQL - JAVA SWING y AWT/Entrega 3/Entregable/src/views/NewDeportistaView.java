package views;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInput;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

import DAOImplements.DAODeportistaEnDisciplinaImpl;
import DAOImplements.DAODeportistaImpl;
import DAOImplements.DAODisciplinaImpl;
import DAOImplements.DAOPaisImpl;
import DAOInterfaces.DAODeportistaEnDisciplina;
import DAOInterfaces.DAOInterface;
import FactoryDAO.FactoryDAO;
import Clases.*;

public class NewDeportistaView extends JFrame {

	private JLabel lblUser, lblApellido, lblEmail, lblTel, lblPais, lblDisciplina;
	private JPanel centerPanel, southPanel;
	private JTextField textUser, textApellido, textEmail, textTel, textPais, textDisciplina;
	private JComboBox<String> disciplinas, paises;
	private JButton guardar;
	private String[] errores;
	private int whatIs, idDeportista;;
	
	public NewDeportistaView(int i, int idDeportista) {
		super("Gestor de Olimpiadas - NUEVO DEPORTISTA");
		this.setVisible(true);
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.whatIs = i;
		this.idDeportista = idDeportista;
		
		MouseAdapter event = new add();
		
		centerPanel = new JPanel();
		centerPanel.setLayout(null);
		
		lblUser = new JLabel("Nombre");
		textUser = new JTextField();
		lblUser.setBounds(150, 40, 50,22);
		textUser.setBounds(210, 40, 150, 22);
		
		lblApellido = new JLabel("Apellido");
		textApellido = new JTextField();
		lblApellido.setBounds(150, 80, 50,22);
		textApellido.setBounds(210, 80, 150, 22);
		
		lblEmail = new JLabel("Email");
		textEmail = new JTextField();
		lblEmail.setBounds(150, 120, 50,22);
		textEmail.setBounds(210, 120, 150, 22);
		
		lblTel = new JLabel("Telefono");
		textTel = new JTextField();
		lblTel.setBounds(150, 160, 50,22);
		textTel.setBounds(210, 160, 150, 22);
		
		
		FactoryDAO fact2 = new FactoryDAO();
		DAOInterface<Pais> PaisImpl = fact2.getPais();
		try {
			ArrayList<Pais> allPaises = PaisImpl.find();
			paises = new JComboBox();
			for(int k=0; k<allPaises.size(); k++) {
				paises.addItem(allPaises.get(k).getNombre());
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		lblPais = new JLabel("Pais");
		textPais = new JTextField();
		lblPais.setBounds(150, 200, 50,22);
		paises.setBounds(210, 200, 150, 22);
		
		
		DAOInterface<Disciplina> DisciplinasImpl = fact2.getDisciplina();
		ArrayList<Disciplina> allDisciplinas;
		try {
			allDisciplinas = DisciplinasImpl.find();
			disciplinas = new JComboBox();
			for(int j=0 ; j<allDisciplinas.size(); j++) {
				disciplinas.addItem(allDisciplinas.get(j).getNombre());
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		lblDisciplina = new JLabel("Disciplina");
		lblDisciplina.setBounds(150, 240, 70,22);
		disciplinas.setBounds(210, 240, 150, 22);
		
		
		
		
		
		centerPanel.add(lblUser);
		centerPanel.add(textUser);
		
		centerPanel.add(lblApellido);
		centerPanel.add(textApellido);
		
		centerPanel.add(lblEmail);
		centerPanel.add(textEmail);
		
		centerPanel.add(lblTel);
		centerPanel.add(textTel);
		
		centerPanel.add(lblPais);
		centerPanel.add(paises);
		
		centerPanel.add(lblDisciplina);
		centerPanel.add(disciplinas);
		
		guardar = new JButton("Guardar");
		guardar.setBounds(240, 290, 80, 20);
		guardar.addMouseListener(event);
		centerPanel.add(guardar);
		this.add(centerPanel);
		
		errores = new String[5];
		errores[0]="";
		errores[1]="";
		errores[2]="";
		errores[3]="";
		errores[4]="";
		if(whatIs == 1) {
			FactoryDAO fact = new FactoryDAO();
			DAOInterface<Deportista> findDep = fact.getDeportista();
			DAOInterface<DeportistaEnDisciplina> findDep_Disc = fact.getDeportistaEnDisciplina();
			DAOInterface<Disciplina> findDisc = fact.getDisciplina();
			DAOInterface<Pais> findPais = fact.getPais();
			Disciplina disciplina;
			try {
				Deportista dep = findDep.findById(idDeportista);
				textUser.setText(dep.getNombre());
				textApellido.setText(dep.getApellido());
				textEmail.setText(dep.getEmail());
				String tel = dep.getTelefono();
				textTel.setText( tel.toString() );
				paises.setSelectedItem(findPais.findById(dep.getId_pais()).getNombre());
				disciplina = new Disciplina( findDep_Disc.findByIdDep(idDeportista).getIdDisciplina(), findDisc.findById(findDep_Disc.findByIdDep(idDeportista).getIdDisciplina()).getNombre());
				disciplinas.setSelectedItem(disciplina.getNombre());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	 static boolean validateEmail (String email) throws LongEmailWrong {
		 boolean contains = false;
		 for(int i=0 ; i<email.length() ; i++) {
			 if(email.charAt(i) == '@') {
				 contains=true;
			 }
		 }	
		 if(!contains) {
			 throw new LongEmailWrong("El email debe contener un @");
		 }	 
		 return contains;
	 }
	
	 public class add extends MouseAdapter {
		 private String nombre,apellido,pais,disciplina, email;
		 private String tel;
		 private String tel2;
		 private boolean emailNotWrong;
		 private boolean isCorrect= true;;
			public void mouseClicked(MouseEvent arg)  {
				
				if(textApellido.getText().equals("") || textUser.getText().equals("")  || textTel.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Complete todos los campos");
					errores[3] = "Complete todos los campos";
					isCorrect = false;
				}
				
				if(!textUser.getText().matches("^(?!.* $)[A-Z][a-z ]+$")){
					errores[0] = "Nombre incorrecto";
				}
				if(!textApellido.getText().matches("^(?!.* $)[A-Z][a-z ]+$")) {
					errores[1] = "Apellido incorrecto";
				}
				if(!textTel.getText().matches("^[0-9]*$")) {
					errores[2]= "Telefono invalido";
				}
				try {
					emailNotWrong = validateEmail(textEmail.getText());
				} catch (LongEmailWrong e) {
					// TODO Auto-generated catch block
					System.out.println("Error: Email must contains @");
					e.printStackTrace();
				}
				if(!emailNotWrong) {
					 JOptionPane.showMessageDialog(null, "El email debe contener un @");
					 errores[4] = "El email debe contener un @";
				}
				if( ((errores[0] != "") || (errores[1] != "") || (errores[2] != "") || (errores[3] != "") || (errores[4] != "") ) ) {
					if((errores[0] != "") || (errores[1] != "") || (errores[2] != "") || (errores[3] != "")) {
						JOptionPane.showMessageDialog(null, errores[0] + " " + errores [1]+ " " + errores[2]);
					}
					errores[0] ="";
					errores[1] ="";
					errores[2] ="";
					errores[3] ="";
					errores[4] ="";
				} else {
					nombre = textUser.getText();
					apellido = textApellido.getText();
					pais = (String) paises.getSelectedItem();
					disciplina = (String) disciplinas.getSelectedItem();
					
					tel = textTel.getText();
					email = textEmail.getText();
					Deportista dep = new Deportista(tel, apellido, nombre, email, pais);
					
					FactoryDAO fact = new FactoryDAO();
					DAOInterface<Deportista> agregar = fact.getDeportista();
					DAOInterface<Disciplina> disc = fact.getDisciplina();
					DAODeportistaEnDisciplinaImpl dep_discBD = new DAODeportistaEnDisciplinaImpl();
					
					try {
						if(whatIs==0) {
							agregar.create(dep);
							dep.setId(agregar.getLastAdded());
							DeportistaEnDisciplina dep_disc = new DeportistaEnDisciplina(dep.getId(), disc.findByName(disciplina).getId());
							dep_discBD.create(dep_disc);
							JOptionPane.showMessageDialog(null,"Deportista agregado correctamente");
							setVisible(false);
						} else {
							dep.setId(idDeportista);
							DeportistaEnDisciplina dep_disc = new DeportistaEnDisciplina(dep.getId(), disc.findByName(disciplina).getId());
							agregar.update(dep.getId(), dep);
							dep_discBD.delete(dep.getId());
							dep_discBD.create(dep_disc);
							JOptionPane.showMessageDialog(null,"Deportista actualizado correctamente");
							
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				
			}
		}
	 
	

static final class LongEmailWrong extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LongEmailWrong() {
	}
	
	public LongEmailWrong(String errorMsg){
		super(errorMsg);
		
	}
}
}
