package Clases;

import java.sql.SQLException;

import DAOImplements.DAOPaisImpl;

public class Deportista {
		private int id, id_pais;
		private String apellido, nombre, email, telefono;

		
		
		public Deportista(int id, int id_pais, String telefono, String apellido, String nombre, String email) {
			super();
			this.id = id;
			this.id_pais = id_pais;
			this.telefono = telefono;
			this.apellido = apellido;
			this.nombre = nombre;
			this.email = email;
		}
		
		public Deportista(int id_pais, String telefono, String apellido, String nombre, String email) {
			super();
			this.id_pais = id_pais;
			this.telefono = telefono;
			this.apellido = apellido;
			this.nombre = nombre;
			this.email = email;
		}
		
		public Deportista(String telefono, String apellido, String nombre, String email, String p) {
			super();
			this.telefono = telefono;
			this.apellido = apellido;
			this.nombre = nombre;
			this.email = email;
			DAOPaisImpl paisImpl = new DAOPaisImpl();
			try {
				this.id_pais = paisImpl.findByName(p).getId();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public int getId_pais() {
			return id_pais;
		}
		public void setId_pais(int id_pais) {
			this.id_pais = id_pais;
		}

		public String getTelefono() {
			return telefono;
		}
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}
		
		public String getApellido() {
			return apellido;
		}
		
		public void setApellido(String apellido) {
			this.apellido = apellido;
		}
		
		public String getNombre() {
			return nombre;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
	}
