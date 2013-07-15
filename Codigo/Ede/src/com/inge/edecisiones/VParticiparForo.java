package com.inge.edecisiones;

import java.util.Date;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class VParticiparForo extends Panel implements View{

	/**	 */
	private static final long serialVersionUID = 1L;
	
	
	/** Componentes de la ventana */
	private VerticalLayout mainLayout;
	private Panel menu;
	
	private Panel prespuestas;
	private VerticalLayout prespuestas_con;
	
	private Panel pnavegaForo;
	private HorizontalLayout pnavegaForo_con;
	private Button primera_pagina;
	private Button pagina_anterior;
	private Button pagina_siguiente;
	private Button ultima_pagina;
	
	private TextArea entrada_respuesta;
	private Button publicar;
	
	private HorizontalLayout opciones;
	private Button verDescripcion;
	private Button consultaEstado;
	
	
	/** Controlador */
	ParticiparForo controlador;
	
	
	/** Constructor */
	public VParticiparForo(ParticiparForo con) {
		controlador = con;
		buildMainLayout();
		initBotones();
		
		/* TODO temporal -
		RespuestaForo r = new RespuestaForo();
		r.setNombre_usuario("Juan Villa Chu");
		r.setFecha(new Date());
		r.setRespuesta("El plebiscito no esta bien escrito");
		
		PRespuestaForo respuesta = new PRespuestaForo(r);
		prespuestas_con.addComponent(respuesta);
		*/
	}
	
	/**
	 * LLena la vista de respuestas
	 * @param lista_respuestas: contiene las respuestas a colocar.
	 * @param primera: indice de la primera respuesta que se debe colocar.
	 * @param ultima: indice de la ultima respuesta que se debe colocar.
	 * */
	public void llenar(List<RespuestaForo> lista_respuestas, int primera,	int ultima) {
		// primero vaciar lo que hubiera anteriormente.
		this.limpiar();
		
		// despues llenar con las nuevas respuestas.
		for ( int i = primera; i <= ultima; i++ ) {
			prespuestas_con.addComponent( new PRespuestaForo( lista_respuestas.get(i) ) );
		}
	}
	
	/**
	 * quita las respuestas de la vista
	 * */
	private void limpiar() {
		this.prespuestas_con.removeAllComponents();		
	}

	/** Definir las acciones de los botones */
	private void initBotones() {
			// boton primera pagina
			primera_pagina.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					controlador.ir_a_primera_pagina();					
				}
			});
			
			// boton pagina anterior
			pagina_anterior.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					controlador.ir_a_pagina_anterior();					
				}
			});
			
			// boton pagina siguiente
			pagina_siguiente.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					controlador.ir_a_pagina_siguiente();
				}
			});
			
			// boton ultima pagina
			ultima_pagina.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					controlador.ir_a_ultima_pagina();
				}
			});
			
			// boton publicar
			publicar.addClickListener(new Button.ClickListener() {				
					@Override
					public void buttonClick(ClickEvent event) {
						if( entrada_respuesta.getValue().isEmpty() ) {
							Notification.show("No puede publicar una respuesta vacia");
						} else {
							publicar();
							Notification.show("Respuesta registrada");
							entrada_respuesta.setValue("");
						}
					}
			});
			
			// boton ver descripcion
			verDescripcion.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					controlador.verDescripcionPlebiscito();
				}
			});
			
			// boton consultar estado
			consultaEstado.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					controlador.getEdecisiones().getConsultarPlebiscito().ir_a_ver_estado();
					
				}
			});
	}
	
	/** Pide al controlador insertar la nueva respuesta*/
	private void publicar() {
		RespuestaForo nueva = new RespuestaForo();
		nueva.setRespuesta( entrada_respuesta.getValue() );
		nueva.setID_Usuario( controlador.get_id_usuario() );
		nueva.setID_Plebiscito( controlador.get_id_plebiscito() );
		nueva.setNumeroRespuesta( controlador.get_numero_respuestas() );
		
		controlador.insertar_respuesta( nueva );
	}
	
	
	/** Contruir el contenido de la ventana*/
	private void buildMainLayout() {
		mainLayout = new VerticalLayout();
		
		/* agregar componentes al layout */
		
		// menu
		menu = new Panel();
		menu.setContent(new Menu(controlador.getEdecisiones()));
		mainLayout.addComponent(menu);
		
		// panel de respuestas:
		buildPanelRespuestas();
		mainLayout.addComponent(prespuestas);
		
		// panel de navegacion:
		builPanelNavegacion();
		mainLayout.addComponent(pnavegaForo);		
		
		
		// entrada de respuesta
		entrada_respuesta = new TextArea("Publicar respuesta: digite aquí y presione publicar");
		mainLayout.addComponent(entrada_respuesta);
		
		// boton de publicar
		publicar = new Button("Publicar");
		mainLayout.addComponent(publicar);
		
		// botones de opciones
		buildOpciones();
		mainLayout.addComponent(opciones);
		
		// definir el mainlayout como contenido del panel
		this.setContent(mainLayout);
	}

	private void buildOpciones() {
		opciones = new HorizontalLayout();	
		opciones.setMargin(true);
		opciones.setSizeFull();
		
		verDescripcion = new Button("Ver descripción del plebiscito");
		consultaEstado = new Button("Consultar Estado del plebiscito");
		
		opciones.addComponent(verDescripcion);
		opciones.addComponent(consultaEstado);
	}

	private void builPanelNavegacion() {
		pnavegaForo = new Panel();
		pnavegaForo_con = new HorizontalLayout();
		pnavegaForo_con.setMargin(true);
		
		// crear contenido del panel
		primera_pagina   = new Button("Primera página");
		pagina_anterior  = new Button("Página anterior");
		pagina_siguiente = new Button("Página siguiente");
		ultima_pagina    = new Button("Última página");
		
		// agregar al panel
		pnavegaForo_con.addComponent(primera_pagina);
		pnavegaForo_con.addComponent(pagina_anterior);
		pnavegaForo_con.addComponent(pagina_siguiente);
		pnavegaForo_con.addComponent(ultima_pagina);
		
		pnavegaForo.setContent(pnavegaForo_con);
		pnavegaForo.setCaption("Navegar este foro");
	}

	private void buildPanelRespuestas() {
		prespuestas = new Panel();
		prespuestas_con = new VerticalLayout();
		prespuestas_con.setMargin(true);
		
		prespuestas.setContent(prespuestas_con);
		prespuestas.setCaption("Respuestas de los usuarios: ");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// limpiar lo que tiene la entrada de respuesta
		if ( entrada_respuesta.isEnabled() ) {
			entrada_respuesta.setValue("");		
		}
	}
	
	
	/**
	 * La ventana ofrece al controlador
	 * el servicio de habilitar o deshabilitar algunos de 
	 * sus botones
	 * */
	

	public void habilitarPrimera_pagina(boolean habilitar) {
		this.primera_pagina.setEnabled(habilitar);
	}

	public void habilitarPagina_anterior(boolean habilitar) {
		this.pagina_anterior.setEnabled(habilitar);
	}

	public void habilitarPagina_siguiente(boolean habilitar) {
		this.pagina_siguiente.setEnabled(habilitar);
	}

	public void habilitarUltima_pagina(boolean habilitar) {
		this.ultima_pagina.setEnabled(habilitar);
	}

	public void habilitarEntrada_respuesta(boolean habilitar) {
		this.entrada_respuesta.setVisible(habilitar);
	}

	public void habilitarPublicar(boolean habilitar) {
		this.publicar.setVisible(habilitar);
	}
}
