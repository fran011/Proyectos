package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import DAOImplements.DAODeportistaEnDisciplinaImpl;
import DAOImplements.DAODeportistaImpl;
import DAOImplements.DAOPaisImpl;
import DAOInterfaces.DAOInterface;
import FactoryDAO.FactoryDAO;
import views.DeportistaView.ButtonEditorEditar;
import views.DeportistaView.ButtonEditorEliminar;
import views.DeportistaView.ButtonRendererEditar;
import views.DeportistaView.ButtonRendererEliminar;
import Clases.*;


public class PaisView extends JFrame {

	 private JPanel topPanel, southPanel;
	  private JTable table;
	  private JScrollPane scrollPane;
	  private String[] columns = new String[4];
	  private Object[][] data;
	  JButton button = new JButton();
	  private JButton eliminarBtn = new JButton("Eliminar");
	  private JButton editarBtn = new JButton("Editar");
	  private JButton nuevo, volver;
	  
	  public PaisView() throws SQLException{
		  
		//config 
		super("Gestor de olimpiadas - PAIS");
		setLayout(new BorderLayout());
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
	    columns = new String[] {"ID", "Pais","Editar", "Eliminar"};

	    
	   DAOPaisImpl paisImpl = new DAOPaisImpl();
	    
	    
	    DefaultTableModel model = new DefaultTableModel(data,columns);
	    table = new JTable();
	    table.setModel(model);
	    
	    table.getColumn("Eliminar").setCellRenderer(new ButtonRendererEliminar());
		table.getColumn("Eliminar").setCellEditor(new ButtonEditorEliminar(new JCheckBox()));

		table.getColumn("Editar").setCellRenderer(new ButtonRendererEditar());
		table.getColumn("Editar").setCellEditor(new ButtonEditorEditar(new JCheckBox()));
	    	   
	    scrollPane = new JScrollPane(table);
	    topPanel.add(scrollPane,BorderLayout.CENTER);  
	    
	    
	    ArrayList<Pais> paises = paisImpl.find();
	    int i=0;
	    while(i<paises.size()) {
	    	Object [] dato = {paises.get(i).getId() , paises.get(i).getNombre()};
	    	model.addRow(dato);
	    	i++;
	    }
	    
	    //Eventos
	    MouseAdapter gonewpais = new goNewPais();
	    MouseAdapter evento = new Back();
	    
	   //Panel botones 
	    volver = new JButton("Volver");
	    volver.addMouseListener(evento);
	    nuevo = new JButton("Nuevo");
	    nuevo.addMouseListener(gonewpais);
	    southPanel.add(volver);
	    southPanel.add(nuevo);
	    
	    
	    
	    eliminarBtn.addActionListener(
	  	      new ActionListener(){
	  	        public void actionPerformed(ActionEvent event){
	  	          int resp =JOptionPane.showConfirmDialog(null, "Seguro queres eliminar el Pais seleccionado?");
	  	          switch (resp) {
	  	          case 0:
	  	        	  FactoryDAO fact = new FactoryDAO();
	  	        	  DAOInterface<Pais> daoPais = fact.getPais();
	  	        	  if (table.getSelectedRow() != -1) {
	  	        		  int row = table.getSelectedRow();
	  	        		  String namePais= (String) table.getValueAt(row, 1);
	  	        		  int i = Integer.parseInt(table.getValueAt(row, 0).toString());
	  	        		  DAOInterface<Deportista> daoDep = fact.getDeportista();
	  					try {
	  						if(!(daoDep.hayDeportistasDelPais(i))) {
	  							daoPais.delete(i);
	  							model.removeRow(table.getSelectedRow());
	  						} else {
	  							JOptionPane.showMessageDialog(null, "No se puede eliminar el pais ya que tiene deportistas");
	  						}
	  					}
	  					catch (SQLException e) {
	  						// TODO Auto-generated catch block
	  						e.printStackTrace();
	  					}
	  	              }
	  	        	  break;
	  	          case 1:
	  	        	  System.out.println("Pulsaste No");
	  	        	  break;
	  	         }	
	  	        }
	  	      }
	  	    );
	    editarBtn.addActionListener(
		  	      new ActionListener(){
		  	        public void actionPerformed(ActionEvent event){
		  	        	int row = table.getSelectedRow();
		  	        	String namePais= (String) table.getValueAt(row, 1);
	  	        		int i = Integer.parseInt(table.getValueAt(row, 0).toString());
		  	        	  new NewPaisView(1, i);
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
			// private String label;

			public ButtonEditorEditar(JCheckBox checkBox) {
				super(checkBox);
			}

			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				editarBtn.setText((value == null) ? "Editar" : value.toString());
				// System.out.print("fila" + row);
				DAOPaisImpl paises = new DAOPaisImpl();
//				new NewPaisView((int) data[row][0], (String) data[row][1]);
				return editarBtn;
			}

			public Object getCellEditorValue() {
				return new String("Editar");
			}

		}	  
	  
	  public class goNewPais extends MouseAdapter{
			public void mouseClicked(MouseEvent arg) {
				NewPaisView agg = new NewPaisView(0, 0);
			}
		}
	  public class Back extends MouseAdapter{
			public void mouseClicked(MouseEvent arg) {
				setVisible(false);
			}
		}
		
	  
	
	
	
}
