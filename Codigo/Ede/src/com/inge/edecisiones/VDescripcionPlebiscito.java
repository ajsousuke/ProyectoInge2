package com.inge.edecisiones;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class VDescripcionPlebiscito extends Panel implements View{

	/**	 */
	private static final long serialVersionUID = 1L;
	
	
	/** Componentes de la ventana */
	private VerticalLayout mainLayout;
	private Panel menu;
	
	// panel con los datos
	private Panel pdatos;
	private VerticalLayout pdatos_con;
	private TextField nombre;
	private TextField tipo;
	private TextField acceso;
	private TextField estado;
	private TextField empadronado;
	private TextArea descripcion;	
	private DateField apertura_dis;
	private DateField cierre_dis;
	private DateField apertura_ten;
	private DateField cierre_ten;
	private DateField apertura_voto;
	private DateField cierre_voto;
	private DateField resultados;
	
	// panel con los botones para cambiar de CUS
	private Panel popciones;
	private HorizontalLayout popciones_con;
	private Button verEstado;
	private Button verTendencias;
	private Button irForo;
	private Button irVotar;
	
	private Button volver;
	
	/** Controlador */
	private ConsultarPlebiscito controlador;
	
	/** Datos de la ventana */
	private InfoPlebiscito datos;	
	
	/** Constructor */
	public VDescripcionPlebiscito(ConsultarPlebiscito con) {
		// obligatorio:
		controlador = con;
		buildMainLayout();
		initBotones();
		// lo demas	
	}
	
	/**
	 * Establecer datos del plebiscito
	 * */
	public void setDatos(InfoPlebiscito ip) {
		this.datos = ip;
	}
	
		
	/**
	 * Llena los campos con los valores del
	 * plebliscito seleccionado
	 */
	public void llenar() {
		/*-	Primero mostrar los datos del plebiscito -*/		
		nombre.setReadOnly(false);
		tipo.setReadOnly(false);
		acceso.setReadOnly(false);
		estado.setReadOnly(false);
		empadronado.setReadOnly(false);
		descripcion.setReadOnly(false);
		apertura_dis.setReadOnly(false);
		cierre_dis.setReadOnly(false);
		apertura_ten.setReadOnly(false);
		cierre_ten.setReadOnly(false);
		apertura_voto.setReadOnly(false);
		cierre_voto.setReadOnly(false);
		resultados.setReadOnly(false);
		
		this.empadronado.setValue( controlador.usuario_empadronado()? "Si":"No" );
		this.estado.setValue(controlador.plebiscito_activo()? "Activo":"Inactivo");
		
		this.nombre.setValue(datos.GetNombre());
		this.descripcion.setValue(datos.GetDescripcion());
		
		this.acceso.setValue(datos.GetAcceso()? "Publico":"Privado");
		this.tipo.setValue(datos.GetTipo()? "Plebiscito":"Eleccion");
		
		this.apertura_dis.setValue(datos.GetPeriodoDiscusion()[0]);
		this.cierre_dis.setValue(datos.GetPeriodoDiscusion()[1]);
		
		this.apertura_ten.setValue(datos.GetPeriodoInscripcion()[0]); 
		this.cierre_ten.setValue(datos.GetPeriodoInscripcion()[1]);
		
		this.apertura_voto.setValue(datos.GetPeriodoVotacion()[0]);
		this.cierre_voto.setValue(datos.GetPeriodoVotacion()[1]);
		this.resultados.setValue(datos.GetPublicacionResultados()); 
		
		this.irForo.setCaption( controlador.foro_abierto()?"Participar en Foro":"Ver Foro" );
		 
		
		nombre.setReadOnly(true);
		tipo.setReadOnly(true);
		acceso.setReadOnly(true);
		estado.setReadOnly(true);
		empadronado.setReadOnly(true);
		descripcion.setReadOnly(true);
		apertura_dis.setReadOnly(true);
		cierre_dis.setReadOnly(true);
		apertura_ten.setReadOnly(true);
		cierre_ten.setReadOnly(true);
		apertura_voto.setReadOnly(true);
		cierre_voto.setReadOnly(true);
		resultados.setReadOnly(true);
	}	
	
	
	/** Definir las acciones de los botones */
	private void initBotones() {
		// ir a participar en foro;
		irForo.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.ir_a_foro();
			}
		});
		
		// ir a votar
		irVotar.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.ir_a_votar();				
			}
		});
		
				
		verTendencias.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.ir_a_ver_tendencia();				
			}
		});
		
		verEstado.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.ir_a_ver_estado();				
			}
		});
		
		
		// volver a consultar plebisicitos
		volver.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.volverConsultar();
			}
		});
	}
	
	
	/** Contruir el contenido de la ventana*/
	private void buildMainLayout() {
		mainLayout = new VerticalLayout();
		
		/* agregar componentes al layout */
		
		// menu
		menu = new Panel();
		menu.setContent(new Menu(controlador.getEdecisiones()));
		mainLayout.addComponent(menu);
		
		// panel de datos
		buildPanelDatos();
		mainLayout.addComponent(pdatos);
		
		// panel botones de cambio a otros CUS
		buildPanelOpciones();
		mainLayout.addComponent(popciones);
		mainLayout.setComponentAlignment(popciones, Alignment.MIDDLE_CENTER);
		
		// boton volver
		volver = new Button("Volver a consultar Plebiscitos");
		mainLayout.addComponent(volver);
		mainLayout.setComponentAlignment(volver, Alignment.MIDDLE_CENTER);
				
		// definir el mainlayout como contenido del panel
		this.setContent(mainLayout);
	}

	private void buildPanelOpciones() {
		popciones = new Panel();
		popciones_con = new HorizontalLayout();
		
		// crear contenido del panel
		verEstado      = new Button("Ver estado o resultados");
		verTendencias  = new Button("Ver tendencias");
		irForo         = new Button("Participar en foro");
		irVotar        = new Button("Votar");
		
		// agregar contenido al panel
		popciones_con.addComponent(verEstado);
		popciones_con.addComponent(verTendencias);
		popciones_con.addComponent(irForo);
		popciones_con.addComponent(irVotar);
		
		popciones_con.setSizeFull();
		popciones_con.setMargin(true);
		
		popciones.setContent(popciones_con);	
		popciones.setCaption("Opciones para este plebiscito");
	}

	private void buildPanelDatos() {
		pdatos = new Panel();
		pdatos_con = new VerticalLayout();

		// crear contenido del panel
		nombre        = new TextField("Nombre de la propuesta");
		tipo          = new TextField("Tipo de consulta");
		acceso        = new TextField("Tipo de acceso");
		estado        = new TextField("Estado del plebiscito:");
		empadronado   = new TextField("Esta empadronado en este plebiscito:");
		descripcion   = new TextArea("Descripción de la propuesta");	
		apertura_dis  = new DateField("Apertura de discución");
		cierre_dis    = new DateField("Cierre de discución");
		apertura_ten  = new DateField("Apertura de inscripción de tendencias");
		cierre_ten    = new DateField("Cierre de inscripción de tendencias");
		apertura_voto = new DateField("Apertura periodo de voto");
		cierre_voto   = new DateField("Cierre de periodo de voto");
		resultados    = new DateField("Publicación de resultados");		
		
		// agregar contenido al panel
		pdatos_con.addComponent(nombre);
		pdatos_con.addComponent(tipo);
		pdatos_con.addComponent(acceso);
		pdatos_con.addComponent(estado);
		pdatos_con.addComponent(empadronado);
		pdatos_con.addComponent(descripcion);
		pdatos_con.addComponent(apertura_dis);
		pdatos_con.addComponent(cierre_dis);
		pdatos_con.addComponent(apertura_ten);
		pdatos_con.addComponent(cierre_ten);
		pdatos_con.addComponent(apertura_voto);
		pdatos_con.addComponent(cierre_voto);
		pdatos_con.addComponent(resultados);
		
		pdatos_con.setMargin(true);
		
		pdatos.setContent(pdatos_con);
		pdatos.setCaption("Datos del plebiscito");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * La ventana ofrece al controlador
	 * el servicio de habilitar o deshabilitar algunos de 
	 * sus botones
	 * */
	
	public void habilitarVerEstado(boolean habilitar) {
		this.verEstado.setEnabled(habilitar);
	}

	public void habilitarVerTendencias(boolean habilitar) {
		this.verTendencias.setEnabled(habilitar);
	}

	public void habilitarIrForo(boolean habilitar) {
		this.irForo.setEnabled(habilitar);
	}

	public void habilitarIrVotar(boolean habilitar) {
		this.irVotar.setEnabled(habilitar);
	}
}
