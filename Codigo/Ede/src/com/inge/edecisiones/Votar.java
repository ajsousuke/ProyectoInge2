package com.inge.edecisiones;

import java.awt.Label;
import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class Votar extends SubControlador{

	//Nombres de la ventana
	protected static final String VOTAR = "Votar";
	
	//Ventanas
	VVotar votar;
	
	//
	ArrayList<Tendencia> tendencias;
	Window ventana;
	
	public Votar(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initViews(){
		votar = new VVotar(this);
		
		controlador.getNavigator().addView(VOTAR, votar);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void ir_a_inicio(){
		//preparacion previa
		
		InfoPlebiscito info = getEdecisiones().getPlebiscito().GetInfoPlebiscito();
		Date[] periodoVotacion = info.GetPeriodoVotacion();
		Date fechaActual = new Date();
		boolean haVotado = getEdecisiones().getControladorBD().RecuperarVoto(301750586, getEdecisiones().getPlebiscito());
		
		//agregar lo de java digital sign
		if(haVotado){
			Notification.show("Ya ha realizado el voto");
		}
		else if(fechaActual.after(periodoVotacion[0]) && fechaActual.before(periodoVotacion[1])){
			tendencias = 
					(ArrayList<Tendencia>) getEdecisiones().getControladorBD().RecuperarTendencias(getEdecisiones().getPlebiscito());
			
			votar.setTendencias(tendencias);
			
			//cargar ventana
			this.controlador.getNavigator().navigateTo(VOTAR);
		}
		else{
			Notification.show(
					"El plebiscito todavía no se encuentra en período de votación.", Notification.TYPE_ERROR_MESSAGE);
		}
	}
	
	public void Votar(int Id_Tendencia){
		Voto voto = new Voto(Id_Tendencia, 301750586);
		ventana = new Window("Voto");
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(new com.vaadin.ui.Label("Se ha realizado el voto"));
		ventana.setContent(vl);
		ventana.center();
		ventana.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				getEdecisiones().removeWindow(ventana);
				getEdecisiones().getEditarTendencia().ir_a_inicio();
			}
		});
		
		controlador.getControladorBD().InsertarVoto(voto);
		
		getEdecisiones().addWindow(ventana);
	}
}
