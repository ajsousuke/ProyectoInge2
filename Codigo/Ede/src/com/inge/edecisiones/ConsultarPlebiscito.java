package com.inge.edecisiones;

import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;

public class ConsultarPlebiscito extends SubControlador {
	
	/** Nombres de las ventas */
	protected static final String CONSULTAR_PLEBISCITOS = "ConsultarPlebiscitos";
	protected static final String DESCRIPCION_PLEBISCITO = "DescripcionPlebiscito";
	
	/** Ventanas */
	VConsultarPlebiscito vConsultarPlebiscito;
	VDescripcionPlebiscito vDescripcionPlebiscito;
	
	/** Plebiscito seleccionado */
	Plebiscito plebiscito;

	public ConsultarPlebiscito(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}
	
	public void initViews() {
		vConsultarPlebiscito = new VConsultarPlebiscito(this);
		vDescripcionPlebiscito = new VDescripcionPlebiscito(this);
		
		controlador.getNavigator().addView(CONSULTAR_PLEBISCITOS, vConsultarPlebiscito);
		controlador.getNavigator().addView(DESCRIPCION_PLEBISCITO, vDescripcionPlebiscito);
	}
	
	public void ir_a_inicio() {
		controlador.getNavigator().navigateTo(CONSULTAR_PLEBISCITOS);
	}
	
	/**
	 * Ver la descripcion del plebiscito de nombre
	 * @param nomp
	 * */
	public void verDescripcion(String nomp) {
		// mientras:
		//vDescripcionPlebiscito.llenar();
		//controlador.getNavigator().navigateTo(DESCRIPCION_PLEBISCITO);
		
		plebiscito = controlador.getControladorBD().RecuperarPlebiscito(nomp);
		if (plebiscito != null) {
			this.controlador.setPlebiscito(plebiscito);
			vDescripcionPlebiscito.setDatos(plebiscito.GetInfoPlebiscito());
			vDescripcionPlebiscito.llenar();
			this.habilitarBotones();
			controlador.getNavigator().navigateTo(DESCRIPCION_PLEBISCITO);
		}
		else {
			Notification.show("verDescripcion: se produjo un error");
		}
	}
	
	
	/**
	 * muestra la descripcion de un plebiscito:
	 * @param p
	 */
	public void verDescripcion(Plebiscito p) {
		if( p != null) {
			this.plebiscito = p;
			this.controlador.setPlebiscito(plebiscito);
			vDescripcionPlebiscito.setDatos(plebiscito.GetInfoPlebiscito());
			vDescripcionPlebiscito.llenar();
			this.habilitarBotones();
			controlador.getNavigator().navigateTo(DESCRIPCION_PLEBISCITO);
		} 
		else {
			Notification.show("verDescripcion: se produjo un error");
		}
	}
	
	/**
	 * muestra la informacion del plebiscito actual
	 */
	public void verDescripcion_Actual() {
		/* esto no es necesario, aunque se puede usar por precaucion
		vDescripcionPlebiscito.setDatos(plebiscito.GetInfoPlebiscito());
		vDescripcionPlebiscito.llenar();
		this.habilitarBotones();
		*/
		this.habilitarBotones(); // precaucion
		controlador.getNavigator().navigateTo(DESCRIPCION_PLEBISCITO);
	}
	
	/** 
	 * Habilitar los botones adeacuados
	 * la primera vez que se ingresa 
	 */
	private void habilitarBotones() {
		// ver estado, ver tendencia o ir a foro
		// solo si es publico o privado y usuario empadronado 
		// el foro se puede ver incluso fuera de etapa de discucion.		
		
		// votar solo si esta empadronado y periodo votacion esta activo		
		if ( this.usuario_empadronado() && this.periodo_votacion()  ) {
			vDescripcionPlebiscito.habilitarIrVotar(true);
		}
		else {
			vDescripcionPlebiscito.habilitarIrVotar(false);
		}
	}
	
	private boolean periodo_votacion() {
		boolean pv = false;
		InfoPlebiscito ple = controlador.getPlebiscito().GetInfoPlebiscito();
		java.util.Date fechaActual = new java.util.Date();
		pv =   !fechaActual.before(ple.GetPeriodoVotacion()[0]);
		pv = ( !fechaActual.after(ple.GetPeriodoVotacion()[1]) && pv );
		return pv;
	}
	
	public List<String> buscar_lista_plebiscitos(boolean acceso, int activo, String nombre) {
		
		List<String> resultado;
		
		if ( controlador.usuarioLogin() ) { 
			resultado = controlador.getControladorBD().recuperar_lista_plebiscitos( 
					controlador.getUsuario().getDatosCiudadano().GetCedula(), acceso, activo, nombre);
		}
		else {
			resultado = controlador.getControladorBD().recuperar_lista_plebiscitos( -1 , true, activo, nombre);
		}
		return resultado;
	}

	public boolean usuario_empadronado() {
		boolean empadronado = false;
		if ( controlador.usuarioLogin() ) {
			int ced = controlador.getUsuario().getDatosCiudadano().GetCedula();
			int ple = controlador.getPlebiscito().GetID();
			empadronado = controlador.getControladorBD().empadronado(ced, ple);
		}
		return empadronado;
	}

	/**
	 * dice si el plebiscito seleccionado por este CUS
	 * se encuentra activo.
	 * Esto es si no se aun no ha terminado la fecha de votacion.
	 * @return
	 */
	public boolean plebiscito_activo() {
		boolean pv = false;
		InfoPlebiscito ple = controlador.getPlebiscito().GetInfoPlebiscito();
		java.util.Date fechaActual = new java.util.Date();
		// pv = ( !fechaActual.before(ple.GetPeriodoDiscusion()[0]) )? true:false;
		pv =  !fechaActual.after(ple.GetPeriodoVotacion()[1]);
		return pv;
	}
	
	
	/*
	 * indica si es posible publicar respuestas en el foro
	 */
	public boolean foro_abierto() {
		boolean abierto = false;
		
		InfoPlebiscito ple = controlador.getPlebiscito().GetInfoPlebiscito();
		java.util.Date fechaActual = new java.util.Date();
		
		// si no se esta en periodo de discucion 
		// no se pueden publicar respuestas
		abierto =   !fechaActual.before(ple.GetPeriodoDiscusion()[0]);
		abierto = ( !fechaActual.after(ple.GetPeriodoDiscusion()[1]) && abierto );
		
		//boolean pru = fechaActual.after(ple.GetPeriodoDiscusion()[1]);
		//  si la fecha del fin del periodo de discusion es igual
		//  a la fecha del dia actual, pru devuelve true
		// porque toma en cuenta en la hora
		// if( pru ) { System.out.println("la fecha hoy esta despues que fin de discusion");}
		
		// se requiere ademas que el usuario haya iniciado sesion
		abierto = ( abierto && controlador.usuarioLogin() );
		
		return abierto;
	}

	/**
	 * vuelve a la ventana de busqueda
	 */
	public void volverConsultar() {
		// TODO trabajo previo
		
		// cargar ventana
		controlador.getNavigator().navigateTo(CONSULTAR_PLEBISCITOS);
	}

	/**
	 * ir a cus Participar en foro
	 */
	public void ir_a_foro() {
		controlador.getParticiparForo().ir_a_inicio();		
	}

	/**
	 * ir a cus votar
	 */
	public void ir_a_votar() {
		controlador.getVotar().ir_a_inicio();		
	}

	/**
	 * ir a cus editar padron
	 */
	public void ir_a_editar_padron() {
		controlador.getRegistrarPadron().ir_a_inicio();		
	}

	/**
	 * ir a cus ver tendencia
	 */
	public void ir_a_ver_tendencia() {
		// TODO FALTA CUS ARIEL
		controlador.getConsultarTendencia().ir_a_inicio();
	}

	/**
	 * ir a cus ver estado plebiscito
	 */
	public void ir_a_ver_estado() {
		// TODO FALTA CUS ARIEL
		controlador.getVerResultados().ir_a_inicio();
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
}
