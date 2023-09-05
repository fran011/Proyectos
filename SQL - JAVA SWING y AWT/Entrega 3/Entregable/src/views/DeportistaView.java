package views;

import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Clases.Deportista;
import Clases.DeportistaEnDisciplina;
import Clases.Disciplina;
import Clases.ExportCSV;
import Clases.Pais;
import DAOImplements.DAODeportistaEnDisciplinaImpl;
import DAOImplements.DAODeportistaImpl;
import DAOImplements.DAODisciplinaImpl;
import DAOImplements.DAOPaisImpl;
import DAOInterfaces.DAOInterface;
import FactoryDAO.FactoryDAO;

public class DeportistaView extends JFrame {
	 private JPanel topPanel, southPanel;
	  private JTable table;
	  private JScrollPane scrollPane;
	  private String[] columns;
	  private Object[][] data;
	  private JButton eliminarBtn = new JButton("Eliminar");
	  private JButton editarBtn = new JButton("Editar");
	  private JButton eCSV, nuevo, volver;
	  
	  public DeportistaView() throws IOException{
		  
		//config 
		setLayout(new BorderLayout());
	    setTitle("Gestor de Olimpiadas - DEPORTISTA");
	    this.setLocation(200,350);
	    setSize(870,300);
	    setVisible(true);
	    
	    
	    //panel tabla
	    topPanel = new JPanel();
	    southPanel = new JPanel();
	    topPanel.setLayout(new BorderLayout());
	    getContentPane().add(topPanel, BorderLayout.CENTER);
	    getContentPane().add(southPanel, BorderLayout.NORTH);
	   
	    
	    
	    //table config
	    columns = new String[] {"id" ,"Nombre", "Apellido", "Pais", "Disciplina", "Editar", "Eliminar"};

	    
	    DefaultTableModel model = new modeloReservas(data, columns);
	    table = new JTable();
	    table.getTableHeader().setReorderingAllowed(false);
	    table.setModel(model);
	 
	    table.getColumn("Eliminar").setCellRenderer(new ButtonRendererEliminar());
		table.getColumn("Eliminar").setCellEditor(new ButtonEditorEliminar(new JCheckBox()));

		table.getColumn("Editar").setCellRenderer(new ButtonRendererEditar());
		table.getColumn("Editar").setCellEditor(new ButtonEditorEditar(new JCheckBox()));
	    
	    

	    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
  
	    scrollPane = new JScrollPane(table);
	    topPanel.add(scrollPane,BorderLayout.CENTER);  
	    
	    FactoryDAO fact = new FactoryDAO();
	    DAOInterface<Deportista> deportistas = fact.getDeportista();
	    ArrayList<Deportista> toList = new ArrayList<>();
	    
	    
	    try {
	    	toList = deportistas.find();
	    	Pais pais = new Pais();
	    	DAOInterface<Deportista> depDB = fact.getDeportista();
	    	DAOInterface<Pais> paises = fact.getPais();
	    	DAOInterface<Disciplina> disc = fact.getDisciplina();
	    	DAOInterface<DeportistaEnDisciplina> dep_disc = fact.getDeportistaEnDisciplina();
	    	DeportistaEnDisciplina dep_en_disc = new DeportistaEnDisciplina();
	    	int i=0;
	    	while(i<toList.size()) {
	    		pais = paises.findById(toList.get(i).getId_pais());
	    		Deportista dep = depDB.findByEmail(toList.get(i).getEmail()); 	    		
	    		dep_en_disc = dep_disc.findByIdDep(dep.getId());
	    		Object [] dato = {toList.get(i).getId() ,toList.get(i).getNombre(), toList.get(i).getApellido(), pais.getNombre(), disc.findById(dep_en_disc.getIdDisciplina()).getNombre() };
	    		model.addRow(dato);
	    		i++;
	    	}
	    } catch (SQLException e) {
	    	// TODO Auto-generated catch block
	    	System.out.println(e);
	    	e.printStackTrace();
	    }
	    
	    
	    //Eventos
	    MouseAdapter evento = new Back();
	    MouseAdapter evento2 = new New();
	    MouseAdapter evento3 = new exportar();
	   //Panel botones 
	    volver = new JButton("Volver");
	    volver.addMouseListener(evento);
	    eCSV = new JButton("Exportar CSV");
	    eCSV.addMouseListener(evento3);
	    
	    nuevo = new JButton("Nuevo");
	    nuevo.addMouseListener(evento2);
	    southPanel.add(volver);
	    southPanel.add(eCSV);
	    southPanel.add(nuevo);
	    
	    
	    
	    eliminarBtn.addActionListener(
	      new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	          int resp =JOptionPane.showConfirmDialog(null, "Seguro queres eliminar el deportista seleccionado?");
	          switch (resp) {
	          case 0:
	        	  DAOInterface<Deportista> daoDep = fact.getDeportista();
	        	  if (table.getSelectedRow() != -1) {
	        		  int row = table.getSelectedRow();
	        		  String namePais= (String) table.getValueAt(row, 3);
	        		  DAOInterface<Pais> pais = fact.getPais();
	        		  DAODeportistaEnDisciplinaImpl dep_disc = new DAODeportistaEnDisciplinaImpl();
	        		  int i = Integer.parseInt(table.getValueAt(row, 0).toString());
	        		  System.out.println(i);
					try {
						int idPais = pais.findByName(namePais).getId();
							dep_disc.delete(i);
							daoDep.delete(i);
							model.removeRow(table.getSelectedRow());
						}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	              }
	        	  break;
	          case 1:
	        	 
	        	  break;
	         }	
	        }
	      }
	    );
	    
	  
	    editarBtn.addActionListener(
	  	      new ActionListener(){
	  	        public void actionPerformed(ActionEvent event){
	  	        	int row = table.getSelectedRow();
	  	        	int i = Integer.parseInt(table.getValueAt(row, 0).toString());
	  	        	new NewDeportistaView(1, i);
	  			}
	  	      }
	  	    );
}
	  	    
	  	  
	  
	  
	  class ButtonRendererEliminar extends JButton implements TableCellRenderer {

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				setText((value == null) ? "Eliminar" : value.toString());
				return this;
			}
		}

		class ButtonEditorEliminar extends DefaultCellEditor {
			private static final long serialVersionUID = 1L;
			// private String label;

			public ButtonEditorEliminar(JCheckBox checkBox) {
				super(checkBox);
			}

			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				eliminarBtn.setText((value == null) ? "Eliminar" : value.toString());
				return eliminarBtn;
			}

			public Object getCellEditorValue() {
				return new String("Eliminar");
			}
		}
	  
		class ButtonRendererEditar extends JButton implements TableCellRenderer {
			private static final long serialVersionUID = 7661180268675612936L;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				setText((value == null) ? "Editar" : value.toString());
				return this;
			}
		}

		class ButtonEditorEditar extends DefaultCellEditor {
			private static final long serialVersionUID = 1L;


			public ButtonEditorEditar(JCheckBox checkBox) {
				super(checkBox);
			}

			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				editarBtn.setText((value == null) ? "Editar" : value.toString());

				DAOPaisImpl paises = new DAOPaisImpl();

				return editarBtn;
			}

			public Object getCellEditorValue() {
				return new String("Editar");
			}

		}

	  
	  
	  public class Back extends MouseAdapter{
			public void mouseClicked(MouseEvent arg) {
				setVisible(false);
			}
		}
		
	  public class exportar extends MouseAdapter {
			public void mouseClicked(MouseEvent arg) {
				try {
					ExportCSV export = new ExportCSV();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setVisible(false);
				
				
			}
		}
	  
	  public class New extends MouseAdapter{
		  public void mouseClicked(MouseEvent arg) {
				new NewDeportistaView(0, 0);
			}
	  }
	  
	  public class modeloReservas extends DefaultTableModel{
		  public modeloReservas(final Object[][] datos, final String [] titulos) {
			  super(datos, titulos);
		  }
		  
		  public Class getColumn(final int column) {
			  return this.getValueAt(0, column).getClass();
		  }
	  }
	  
	  
	  
	  
}


