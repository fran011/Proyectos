package Clases;

public class Pais {
	private int id;
	private String nombre;
	
	public Pais() {
		
	}
	
	public Pais(String nombre) {
		this.nombre = nombre;
	}
	
	public Pais(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	public int getId() {
		return id;
	}
		
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
