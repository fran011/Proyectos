package views;

import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import DAOImplements.DAODBConnectionImpl;

public class ConfigView extends JFrame{
	private JTextField user, password, error;
	private JButton volverBtn, guardarBtn;

	public ConfigView() {
		super("Gestion de Olimpiadas - CONFIGURACION");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocation(800, 100);
		this.setSize(350,230);
		this.setLayout(null);

		MouseAdapter back = new Back();
		MouseAdapter connect = new doConnection();


		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		setContentPane(mainPanel);
		JLabel lblNewLabel = new JLabel("Usuario BD");
		lblNewLabel.setBounds(50, 40, 70, 20);
		mainPanel.add(lblNewLabel);
		JLabel lblPassword = new JLabel("Contraseña BD");
		lblPassword.setBounds(50, 80, 90, 20);
		mainPanel.add(lblPassword);
		
		user = new JTextField();
		user.setBounds(150, 40, 100, 22);
		password = new JTextField();
		password.setBounds(150, 80, 100, 22);
		
		error = new JTextField("Completa los datos");
		error.setVisible(false);
		error.setBounds(50, 105, 200, 20);
		error.setEditable(false);
		
		mainPanel.add(user);
		mainPanel.add(password);
		mainPanel.add(error);
		
		volverBtn = new JButton("Volver");
		volverBtn.setBounds(80, 130, 80, 25);
		volverBtn.addMouseListener(back);
		mainPanel.add(volverBtn);
		
		guardarBtn = new JButton("Guardar");
		guardarBtn.setBounds(180, 130, 80, 25);
		guardarBtn.addMouseListener(connect);
		mainPanel.add(guardarBtn);
		
		
		
		
	}
	
	public class Back extends MouseAdapter{
		public void mouseClicked(MouseEvent arg) {
			setVisible(false);
		}
	}
	
	public class doConnection extends MouseAdapter{
		public void mouseClicked(MouseEvent arg) {
			String str1 = user.getText();
			String str2 = password.getText();
			if( (str1 == null || str1.equals("")) || (str2 == null || str2.equals("")) ) {
				error.setVisible(true);		
			} else {
				JOptionPane.showMessageDialog(null, "Conexion realizada");
				DAODBConnectionImpl.setUser(str1);
				DAODBConnectionImpl.setPassword(str2);
			}
			
			
			
		}
	}
	
}
