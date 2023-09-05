package views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.*;import javax.swing.border.Border;

import Clases.Pais;
import DAOImplements.DAODBConnectionImpl;
import DAOImplements.DAOPaisImpl;
import views.NewDeportistaView.LongEmailWrong;

public class NewPaisView  extends JFrame{

	private JPanel centerPanel;
	private JPanel southPanel;
	private JTextField text, error;
	private JLabel label;
	private JButton button;
	private int whatIs;
	private int idPais;
	
	public NewPaisView(int i, int idPais) {
		super("Gestor de Olimpiadas - NUEVO PAIS");
		this.setVisible(true);
		this.setLocation(800,300);
		this.setSize(500,500);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.whatIs = i;
		this.idPais = idPais;
		centerPanel = new JPanel();
		centerPanel.setLayout(null);

		error = new JTextField("Nombre incorrecto, Minimo una mayuscula");
		error.setVisible(false);
		error.setBounds(150, 105, 300, 20);
		error.setEditable(false);
		
		MouseAdapter event = new Validate();
		
		text = new JTextField();
		text.setBounds(160, 200, 200, 20);
		label = new JLabel("NOMBRE");
		label.setBounds(100, 165, 80,90);
		button = new JButton("Guardar");
		button.setBounds(200, 240, 90, 40);
		button.addMouseListener(event);
		centerPanel.add(label);
		centerPanel.add(text);
		centerPanel.add(button);
		centerPanel.add(error);
		this.add(centerPanel, BorderLayout.CENTER);		
		
	}
	static boolean validateName (String country) throws WrongCountryName {
		 boolean contains = false;
		 if((country.length() == 0)) {
			 throw new WrongCountryName("Can't be empty string");
		 }
			 if((country.charAt(0) > 'A' && country.charAt(0) < 'Z' )  ) {
				 contains=true;
			 }	
		 if(!contains) {
			 throw new WrongCountryName("El pais debe comenzar con una mayuscula");
		 }	 
		 return true;
	 }
	
	 public class Validate extends MouseAdapter{
		 private boolean validateName;
			public void mouseClicked(MouseEvent arg) {
				
					try {
						validateName = validateName(text.getText());
					} catch (WrongCountryName e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(!validateName) {
						error.setVisible(true);
					} else {
					error.setVisible(false);
					Pais pais = new Pais(text.getText());
					DAOPaisImpl agg = new DAOPaisImpl();
					JOptionPane.showMessageDialog(null,"Pais agregado correctamente");
					try {
						if(whatIs == 0) {
							agg.create(pais);
						} else {
							agg.update(idPais, pais);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					}
			}
	 }
	 
	 static final class WrongCountryName extends Exception{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public WrongCountryName() {
			}
			
			public WrongCountryName(String errorMsg){
				super(errorMsg);
				
			}
		}
	
}
