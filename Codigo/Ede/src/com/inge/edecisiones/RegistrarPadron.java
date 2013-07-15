package com.inge.edecisiones;

import java.util.ArrayList;
import java.util.List;

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
	protected static final String CONSULTAR_PADRON = "ConsultarPadron";
	
	//Ventanas
	VRegistrarPadron regPadron;
	VImportarPadron impPadron;
	VConsultarPadron conPadron;
	Window wEliminar;
	Editar wEditar;
	
	//Variables locales
	Plebiscito plebiscito;
	boolean retorno;
	
	public RegistrarPadron(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initViews() {
		regPadron = new VRegistrarPadron(this);
		impPadron = new VImportarPadron(this);
		conPadron = new VConsultarPadron(this);
		controlador.getNavigator().addView(REGISTRAR_PADRON, regPadron);
		controlador.getNavigator().addView(IMPORTAR_PADRON, impPadron);
		controlador.getNavigator().addView(CONSULTAR_PADRON, conPadron);
	}
	
	/* 
	 * Estos metodos implementan la funcionalidad de los botones de las ventanas
	 *  */
	
	@Override
	public void ir_a_inicio() {
		// preparacion previa
		/*regPadron.limpiarPadron();
		
		Padron padron = getEdecisiones().getControladorBD().recuperarPadron(getEdecisiones().getPlebiscito());
		regPadron.setPadron(padron);*/
		
		//cargar listas de plebiscitos
		List<String> lista_plebiscitos = controlador.getControladorBD().RecuperarListaPlebiscitos(
																	controlador.getUsuario().GetID());
		conPadron.llenarListaPlebiscitos(lista_plebiscitos);
		
		// cargar ventana
		this.controlador.getNavigator().navigateTo(CONSULTAR_PADRON);
	}
	
	public void setPlebiscito(String nombrePlebiscito){
		plebiscito = this.controlador.getControladorBD().RecuperarPlebiscito(nombrePlebiscito);
	}
	
	public void cargarPadron(){
		regPadron.limpiarPadron();
		Padron padron = getEdecisiones().getControladorBD().recuperarPadron(plebiscito);
		regPadron.setPadron(padron);
		this.controlador.getNavigator().navigateTo(REGISTRAR_PADRON);
	}
	
	public void guardar(Ciudadano c){
		getEdecisiones().getControladorBD().InsertarEnPadron(c, plebiscito.GetID());		
	}
	
	public void guardar(ArrayList<String> padron){
		boolean error = false;
		Padron p = new Padron(plebiscito.GetID());
		for(String a : padron){
			if((a.length()==9)&&(a.charAt(0)!='0')){
				Ciudadano c = new Ciudadano("", "", "", Integer.parseInt(a));
				p.AddCiudadano(c);
			}
			else
				error = true;
		}
		
		if(error)
			Notification.show("Se encuentra un error en el archivo");
		else{
			getEdecisiones().getControladorBD().insertarPadron(p);
			cargarPadron();
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
		//cambiarlo por eliminar del padron y no de persona
		for(Ciudadano c : padron)
			getEdecisiones().getControladorBD().EliminarPadron(c,plebiscito.GetID());	
	}
	
	public void editar(Ciudadano c){
		wEditar = new Editar(c);
		getEdecisiones().addWindow(wEditar);
	}
	
	class Editar extends Window{
		Ciudadano ca;
		int Cedula;
		TextField nombre;
		TextField apellido1;
		TextField apellido2;
		TextField cedula;
		
		public Editar(Ciudadano c){
			super("Editar Ciudadano");
			
			this.ca = c;
			Cedula = ca.GetCedula();
			String[] datos = ca.GetName().split(" ");
			System.out.println(ca.GetName());
			
			VerticalLayout vl = new VerticalLayout();
			/*HorizontalLayout hl1 = new HorizontalLayout();
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
			vl.addComponent(hl3);*/
			HorizontalLayout hl1 = new HorizontalLayout();
			hl1.addComponent(new Label("Cedula"));
			cedula = new TextField("",Integer.toString(ca.GetCedula()));
			hl1.addComponent(cedula);
			vl.addComponent(hl1);
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
					//Ciudadano ce = new Ciudadano(nombre.getValue(), apellido1.getValue(), apellido2.getValue(), ca.GetCedula());
					Ciudadano ce = new Ciudadano("", "", "", Integer.parseInt(cedula.getValue()));
					getEdecisiones().getControladorBD().EditarPadron(ce, ca.GetCedula(), plebiscito.GetID());
					getEdecisiones().removeWindow(wEditar);
					cargarPadron();
				}
			});
		}
	}
}
