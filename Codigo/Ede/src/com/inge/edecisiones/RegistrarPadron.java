package com.inge.edecisiones;

import java.util.ArrayList;

import com.ibm.icu.impl.CalendarAstronomer.Horizon;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class RegistrarPadron extends SubControlador{

	//Nombres de la ventana
	protected static final String REGISTRAR_PADRON = "RegistrarPadron";
	protected static final String IMPORTAR_PADRON = "ImportarPadron";
	
	//Ventanas
	VRegistrarPadron regPadron;
	VImportarPadron impPadron;
	Window wEliminar;
	Editar wEditar;
	boolean retorno;
	
	public RegistrarPadron(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initViews() {
		regPadron = new VRegistrarPadron(this);
		impPadron = new VImportarPadron(this);
		controlador.getNavigator().addView(REGISTRAR_PADRON, regPadron);
		controlador.getNavigator().addView(IMPORTAR_PADRON, impPadron);
	}
	
	/* 
	 * Estos metodos implementan la funcionalidad de los botones de las ventanas
	 *  */
	
	@Override
	public void ir_a_inicio() {
		// preparacion previa
		regPadron.limpiarPadron();
		
		Padron padron = getEdecisiones().getControladorBD().recuperarPadron(getEdecisiones().getPlebiscito());
		regPadron.setPadron(padron);
		
		// cargar ventana
		this.controlador.getNavigator().navigateTo(REGISTRAR_PADRON);
	}
	
	public void guardar(Ciudadano c){
		
		getEdecisiones().getControladorBD().InsertarCiudadano(c, getEdecisiones().getPlebiscito().GetID());
		
	}
	
	public void guardar(ArrayList<String[]> padron){
		boolean error = false;
		Padron p = new Padron(getEdecisiones().getPlebiscito().GetID());
		for(String[] a : padron){
			if((a.length==4) && (a[0].length()==9)){
				Ciudadano c = new Ciudadano(a[1], a[2], a[3], Integer.parseInt(a[0]));
				p.AddCiudadano(c);
			}
			else
				error = true;
		}
		
		if(error)
			Notification.show("Se encuentra un error en el archivo");
		else{
			getEdecisiones().getControladorBD().insertarPadron(p);
			getEdecisiones().getRegistrarPadron().ir_a_inicio();
		}
	}
	
	public void eliminar(){
		
		wEliminar = new Window("Eliminar ciudadanos");
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(new Label("Desea eliminar los usuarios seleccionados?"));
		HorizontalLayout hl = new HorizontalLayout();
		Button btn_volver = new Button("Volver");
		Button btn_eliminar = new Button("Eliminar");
		hl.addComponent(btn_eliminar);
		hl.addComponent(btn_volver);
		vl.addComponent(hl);
		wEliminar.setContent(vl);
		wEliminar.center();
		
		wEliminar.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				getEdecisiones().removeWindow(wEliminar);
			}
		});
		
		btn_volver.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				getEdecisiones().removeWindow(wEliminar);
			}
		});
		
		btn_eliminar.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//retorno = true;
				eliminar(regPadron.eliminarCiudadanos());
				getEdecisiones().removeWindow(wEliminar);
			}
		});
		
		getEdecisiones().addWindow(wEliminar);
		
	}
	
	public void eliminar (ArrayList<Ciudadano> padron){
		
		for(Ciudadano c : padron)
			getEdecisiones().getControladorBD().EliminarCiudadano(c);
		
	}
	
	public void editar(Ciudadano c){
		wEditar = new Editar(c);
		getEdecisiones().addWindow(wEditar);
	}
	
	class Editar extends Window{
		Ciudadano ca;
		TextField nombre;
		TextField apellido1;
		TextField apellido2;
		
		public Editar(Ciudadano c){
			super("Editar Ciudadano");
			
			this.ca = c;
			String[] datos = ca.GetName().split(" ");
			System.out.println(ca.GetName());
			
			VerticalLayout vl = new VerticalLayout();
			HorizontalLayout hl1 = new HorizontalLayout();
			hl1.addComponent(new Label("Nombre"));
			nombre = new TextField("", datos[0]);
			hl1.addComponent(nombre);
			HorizontalLayout hl2 = new HorizontalLayout();
			hl2.addComponent(new Label("Apellido1"));
			apellido1 = new TextField("", datos[1]);
			hl2.addComponent(apellido1);
			HorizontalLayout hl3 = new HorizontalLayout();
			hl3.addComponent(new Label("Apellido2"));
			apellido2 = new TextField("", datos[2]);
			hl3.addComponent(apellido2);
			vl.addComponent(hl1);
			vl.addComponent(hl2);
			vl.addComponent(hl3);
			HorizontalLayout hl = new HorizontalLayout();
			Button btn_volver = new Button("Volver");
			Button btn_editar = new Button("Editar");
			hl.addComponent(btn_editar);
			hl.addComponent(btn_volver);
			vl.addComponent(hl);
			setContent(vl);
			center();
			
			addCloseListener(new Window.CloseListener() {
				
				@Override
				public void windowClose(CloseEvent e) {
					// TODO Auto-generated method stub
					getEdecisiones().removeWindow(wEditar);
				}
			});
			
			btn_volver.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					getEdecisiones().removeWindow(wEditar);
				}
			});
			
			btn_editar.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					Ciudadano ce = new Ciudadano(nombre.getValue(), apellido1.getValue(), apellido2.getValue(), ca.GetCedula());
					getEdecisiones().getControladorBD().EditarCiudadano(ce, ca.GetCedula());
					getEdecisiones().removeWindow(wEditar);
					getEdecisiones().getRegistrarPadron().ir_a_inicio();
				}
			});
		}
	}
}
