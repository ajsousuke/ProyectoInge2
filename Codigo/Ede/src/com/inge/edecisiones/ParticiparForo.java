package com.inge.edecisiones;

import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;

public class ParticiparForo extends SubControlador {
	
	/** Nombres de las ventas */
	protected static final String PARTICIPAR_FORO = "ParticiparForo";
	
	/** Ventanas */
	VParticiparForo vParticiparForo;
	
	/** Variables globales */
	private int numero_paginas;
	private int numero_respuestas; // primera es la 0
	private int pagina_actual; // rango: 0 - (numero_paginas-1)
	private List<RespuestaForo> lista_respuestas;
	private int size_pagina = 10;
	
	public ParticiparForo(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	public void initViews() {		
		vParticiparForo = new VParticiparForo(this);
		controlador.getNavigator().addView(PARTICIPAR_FORO, vParticiparForo);
	}
	
	/**
	 *  ventana principal del caso de uso 
	 *  */
	public void ir_a_inicio() {
		// trabajo previo
		habilitarBotones(); // habilita botones para publicar
		
		// obtiene respuestas registradas de la base de datos
		int ple = controlador.getPlebiscito().GetID();
		this.lista_respuestas = controlador.getControladorBD().recuperar_lista_respuestas(ple);
		this.numero_respuestas = lista_respuestas.size();
		this.numero_paginas = this.numero_paginas(this.numero_respuestas);
		System.out.println("Numero de paginas: " + numero_paginas);
		this.pagina_actual = 0;
		
		// cargar vista con las respuestas
		this.ir_a_primera_pagina();
		
		// cargar ventana
		controlador.getNavigator().navigateTo(PARTICIPAR_FORO);
	}

		
	/**
	 * Estos metodos cambian de pagina.
	 * Actualizan la vista del foro con las respuestas
	 * que van en dicha página. 
	 */
	
	public void ir_a_primera_pagina() {
		this.pagina_actual = 0;
		if( this.numero_paginas > 1) { // caso en que hay mas de size_pagina respuestas
			this.vParticiparForo.llenar(lista_respuestas, 0, this.size_pagina-1);
		}
		else {
			this.vParticiparForo.llenar(lista_respuestas, 0, this.numero_respuestas-1);
		}	

		this.habilitar_botones_navegacion();
	}
	
	public void ir_a_ultima_pagina() {
		this.pagina_actual = this.numero_paginas-1;
		int primera = pagina_actual * 10;
		int ultima = primera; // es igual a la primera + la cantidad del resto de respuestas que tiene la pagina
		// para saber cual es la ultima respuesta
		// se debe verificar si la ultima pagina esta llena
		ultima    += ( numero_respuestas % size_pagina == 0 )? size_pagina-1 : (numero_respuestas % size_pagina)-1;
		
		this.vParticiparForo.llenar(lista_respuestas, primera, ultima);
		this.habilitar_botones_navegacion();
	}
	
	public void ir_a_pagina_anterior() {
		// se asume que hay pagina anterior (si no el boton estaria deshabilitado)
		this.pagina_actual--;
		int primera = pagina_actual * 10;
		int ultima = primera + size_pagina -1;		
		
		this.vParticiparForo.llenar(lista_respuestas, primera, ultima);	
		this.habilitar_botones_navegacion();
	}
	
	public void ir_a_pagina_siguiente() {
		// si la siguiente pagina es la ultima, ie, actual es la penultima
		if ( pagina_actual == numero_paginas-2) {
			ir_a_ultima_pagina();
		}
		else {
			this.pagina_actual++;
			int primera = pagina_actual * 10;
			int ultima = primera + size_pagina -1;
			
			this.vParticiparForo.llenar(lista_respuestas, primera, ultima);
			this.habilitar_botones_navegacion();
		}
	}
	
	/**
	 * indica si hay pagina anterior
	 * a la pagina actual
	 * @return
	 */
	private boolean hay_pagina_anterior() {
		boolean hay = false;
		
		hay = ( this.pagina_actual > 0 );
		
		return hay;		
	}
	
	/**
	 * indica si hay pagina siguiente despues de la actual
	 * @return
	 */
	private boolean hay_pagina_siguiente() {
		boolean hay = false;
		
		hay = ( this.pagina_actual < this.numero_paginas-1 );
		
		return hay;
	}
	
	
	/**
	 * Inserta una respuesta en bd
	 */
	public void insertar_respuesta(RespuestaForo nueva) {
		// la inserta en la bd.
		controlador.getControladorBD().insertar_respuesta_foro(nueva);
		
		// la obtiene el resto de datos: la fecha y el nombre del usuario
		controlador.getControladorBD().recuperar_fecha_respuesta_foro( nueva );
		String nombre = controlador.getControladorBD().recuperar_nombre_usuario( nueva.getID_Usuario() );
		if ( nombre != null ) {
			nueva.setNombre_usuario(nombre);
		}
		else {
			Notification.show("Error al recuperar nombre de usuario");
		}
		
		// agrega la respuesta a la lista de respuestas
		this.lista_respuestas.add(nueva);
		this.numero_respuestas += 1;
		this.numero_paginas = numero_paginas(this.numero_respuestas);
		
		// actualiza la vista del foro:
		this.ir_a_ultima_pagina();
	}
	
	/**
	 * Habilita o deshabilita los botones 
	 * de navegacion. estos son "pagina siguiente"
	 * y "pagina anterior"
	 * */
	private void habilitar_botones_navegacion() {
		this.vParticiparForo.habilitarPagina_anterior( this.hay_pagina_anterior() );
		this.vParticiparForo.habilitarPagina_siguiente( this.hay_pagina_siguiente() );		
	}

	/**
	 * calcula el numero de paginas a partir
	 * del numero de respuestas
	 * */
	private int numero_paginas(int nres) {
		int npags = 1;
		
		npags  = nres / this.size_pagina;
		npags += ( nres % this.size_pagina > 0)? 1:0;
		
		return npags;
	}

	/**
	 * Ver la descripcion del plebiscito seleccionado
	 */
	public void verDescripcionPlebiscito() {
		// de momento solo volver a la ventana de descripcion
		controlador.getConsultarPlebiscito().verDescripcion_Actual();
		/*
		Plebiscito p_actual = this.controlador.getPlebiscito();
		this.controlador.getConsultarPlebiscito().verDescripcion(p_actual);
		*/		
	}	
	
	/**
	 * Deshabilita las opciones de publicar respuesta
	 * si no esta permitido publicar en dicho foro.
	 * */
	public void habilitarBotones() {
		boolean habilitar = controlador.getConsultarPlebiscito().foro_abierto();
		vParticiparForo.habilitarEntrada_respuesta(habilitar);
		vParticiparForo.habilitarPublicar(habilitar);
	}
	
	/**- 
	 * Getters 
	 * */
	public Navigator getNavigator() {
		return controlador.getNavigator();
	}
	
	public ControladorBD getControladorBD() {
		return controlador.getControladorBD();
	}

	public int get_id_usuario() {		
		return controlador.getUsuario().GetID();
	}

	public int get_id_plebiscito() {		
		return controlador.getPlebiscito().GetID();
	}

	public int get_numero_respuestas() {
		return this.numero_respuestas;
	}	
}
