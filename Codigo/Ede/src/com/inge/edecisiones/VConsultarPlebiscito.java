package com.inge.edecisiones;

import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class VConsultarPlebiscito extends Panel implements View {

	/**	 */
	private static final long serialVersionUID = 1L;
	
	
	/** Componentes de la ventana */
	private VerticalLayout mainLayout;
	private Panel menu;
	
	private Panel opcionesBusqueda;
	private VerticalLayout opcionesBusquedaCon;
	private OptionGroup opcionAcceso;
	private OptionGroup opcionActivos;
	private Label notaOpcionActivos;
	private CheckBox opcionBuscarNombre;	
	private TextField entradaNombre;
	
	private Button botonBuscar;
	private ListSelect listaPlebiscitos;
	private Button verDescripcion;
	
	private String ppublicos  = "Plebiscitos Públicos";
	private String pprivados  = "Plebiscitos Privados";
	private String pactivos   = "Plebiscitos Activos";
	private String pinactivos = "Plebiscitos Inactivos";
	private String fambos      = "Cualquier fecha";
	
	
	/** Controlador */
	ConsultarPlebiscito controlador;
	
	/** Constructor */
	public VConsultarPlebiscito(ConsultarPlebiscito con) {
		controlador = con;
		buildMainLayout();
		initBotones();
		initVentanas();
	}
	
	
	/** Definir las acciones de los botones */
	private void initBotones() {
		// boton buscar
		botonBuscar.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				llenar_tabla_plebiscitos();
			}
		});
		
		// boton ver descripcion
		verDescripcion.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.verDescripcion( listaPlebiscitos.getValue().toString() );
			}
		});
		
		// lista de plebiscitos
		listaPlebiscitos.setImmediate(true);
		listaPlebiscitos.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						if( listaPlebiscitos.getValue() == null ) {
							verDescripcion.setEnabled(false);
						}
						else {
							if( listaPlebiscitos.getValue().toString().isEmpty() ) {
								verDescripcion.setEnabled(false);
							}
							else {
								verDescripcion.setEnabled(true);
							}
						}			
					}			
		});
		
	} // FIN METODO
	
	/** Llenar la tabla de plebiscitos */
	private void llenar_tabla_plebiscitos() {
		// primero quitar lo que tuviera anteriormente
		vaciar_tabla_plebiscitos();
		
		// pedir al controlador la lista de plebiscitos
		boolean acceso = true;
		if( this.opcionAcceso.isEnabled() ) {
			acceso = (this.opcionAcceso.getValue().toString().equals(ppublicos))?true:false;
		}
		
		int activo = ( this.opcionActivos.getValue().toString().equals(fambos)   )? 2:0;
		    activo = ( this.opcionActivos.getValue().toString().equals(pactivos) )? 1:activo;
		
		String nombre = null;
		
		if( this.opcionBuscarNombre.getValue() ) {
			nombre = this.entradaNombre.getValue();
		} 
		
		List<String> listap = controlador.buscar_lista_plebiscitos(acceso, activo, nombre);
		// llenar tabla		
		if (listap != null) {
			
			for(String nomp : listap) {
				System.out.println(nomp);
				this.listaPlebiscitos.addItem(nomp);
			}
		}
		else {
			Notification.show("No se encontraron plebiscitos");
		}
	}	
	
	private void vaciar_tabla_plebiscitos() {
		boolean ok = this.listaPlebiscitos.removeAllItems();
		if( !ok ) {
			Notification.show("Error al limpiar tabla de plebiscitos");
		}
	}
	
	/** Inicializar ventana*/
	private void initVentanas() {
		
	}
	
	
	/** Contruir el contenido de la ventana*/
	private void buildMainLayout() {
		mainLayout = new VerticalLayout();
		
		/* agregar componentes al layout */
		
		// menu
		menu = new Panel();
		menu.setContent(new Menu(controlador.getEdecisiones()));
		mainLayout.addComponent(menu);
		
		// panel de busqueda
		buildOpcionesBusqueda();
		mainLayout.addComponent(opcionesBusqueda);
		
		// boton buscar
		botonBuscar = new Button("Buscar");
		mainLayout.addComponent(botonBuscar);
		
		// tabla lista de plebiscitos
		listaPlebiscitos = new ListSelect();
		listaPlebiscitos.setCaption("Seleccione un plebisicto y presione ver descripción:");
		mainLayout.addComponent(listaPlebiscitos);
		mainLayout.setComponentAlignment(listaPlebiscitos, Alignment.MIDDLE_CENTER);
		
		// boton ver descripcion
		verDescripcion = new Button("Ver descripcion");
		mainLayout.addComponent(verDescripcion);
		mainLayout.setComponentAlignment(verDescripcion, Alignment.MIDDLE_CENTER);
		
				
		// definir el mainlayout como contenido del panel
		this.setContent(mainLayout);
	}
	
	public void buildOpcionesBusqueda() {
		opcionesBusqueda    = new Panel();		
		opcionesBusquedaCon = new VerticalLayout();
		opcionesBusquedaCon.setSpacing(true);
		
		// crear el contenido del panel
		opcionAcceso = new OptionGroup();
		opcionAcceso.setCaption("Seleccione si desea mostrar plebiscitos solo públicos o solo privados");
		opcionAcceso.setImmediate(true);
		opcionAcceso.addItem(ppublicos);
		opcionAcceso.addItem(pprivados);
		
		notaOpcionActivos = new Label("Si selecciona \"Plebisictos Privados\" solo verá aquellos en los que esté empadronado");
		
		opcionActivos = new OptionGroup();
		opcionActivos.setCaption("Seleccione si desea mostrar solo plebiscitos activos o solo inactivos:");
		opcionActivos.setImmediate(true);
		opcionActivos.addItem(pactivos);
		opcionActivos.addItem(pinactivos);
		opcionActivos.addItem(fambos);
		
		opcionBuscarNombre = new CheckBox();
		opcionBuscarNombre.setCaption("Mostrar solo plebiscitos que contengan la siguiente hilera en su nombre:");
		opcionBuscarNombre.setImmediate(true);
		
		entradaNombre = new TextField();
		
		//agregar el contenido al panel
		opcionesBusquedaCon.addComponent(opcionAcceso);
		opcionesBusquedaCon.addComponent(notaOpcionActivos);
		opcionesBusquedaCon.addComponent(opcionActivos);
		opcionesBusquedaCon.addComponent(opcionBuscarNombre);
		opcionesBusquedaCon.addComponent(entradaNombre);
		
		opcionesBusquedaCon.setMargin(true);
		//
		opcionesBusqueda.setContent(opcionesBusquedaCon);
		opcionesBusqueda.setCaption("Opciones de busqueda");
	}

	/* poner algunos valores por defecto*/
	@Override
	public void enter(ViewChangeEvent event) {		
		opcionActivos.setValue(fambos); // valor por defecto
		
		opcionAcceso.setEnabled(true);
		opcionAcceso.setValue(ppublicos);
		
		if ( !controlador.getEdecisiones().usuarioLogin() ) {
			opcionAcceso.setEnabled(false);
		}
	}

}
