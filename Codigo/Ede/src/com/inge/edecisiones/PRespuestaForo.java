package com.inge.edecisiones;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class PRespuestaForo extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout contenido;
	private TextArea respuesta;
	private DateField fecha;
	
	public PRespuestaForo(RespuestaForo r) {
		buildContenido();
		llenar(r);
	}
	
	private void llenar(RespuestaForo r) {
		String usr = "[Respuesta " + r.getNumeroRespuesta()+ "] "+ r.getNombre_usuario() + " dijo: ";		
		this.setCaption(usr);	
		
		respuesta.setValue(r.getRespuesta());
		respuesta.setReadOnly(true);
		
		fecha.setValue(r.getFecha());
		fecha.setReadOnly(true);
	}
	
	private void buildContenido() {
		contenido = new VerticalLayout();
		contenido.setMargin(true);
		
		// respuesta
		respuesta = new TextArea();		
		contenido.addComponent(respuesta);
		
		// fecha
		fecha = new DateField("Fecha publicación:");
		fecha.setResolution(Resolution.SECOND);
		contenido.addComponent(fecha);
		
		this.setContent(contenido);
	}
	
	

}
