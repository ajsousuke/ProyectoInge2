package com.inge.edecisiones;

import java.io.InputStream;
import java.util.ArrayList;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class VVotar extends CustomComponent implements ValueChangeListener, View{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private HorizontalLayout Botones;
	@AutoGenerated
	private Button btn_VerResultados;
	@AutoGenerated
	private Button btn_Volver;
	@AutoGenerated
	private Button btn_Votar;
	@AutoGenerated
	private VerticalLayout Opciones;
	@AutoGenerated
	private Label lbl_Texto2;
	@AutoGenerated
	private Label lbl_Texto1;
	@AutoGenerated
	private Panel Menu;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	public ArrayList<Tendencia> tendencias;
	public ArrayList<CheckBox> checks;
	private boolean primeraVez = true;
	private int opcion;
	private Votar controlador;
	//private RegistrarPadron controlador;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public VVotar(final Votar controlador) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.controlador = controlador;
		Menu.setContent(new Menu(controlador.getEdecisiones()));
		Menu.setSizeUndefined();

		//listeners
		btn_Volver.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getEdecisiones().getConsultarPlebiscito().verDescripcion_Actual();
			}
		});
		
		btn_Votar.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				opcion = getOpcion();
				if(opcion==-1){
					System.out.println("Voto en blanco");
				}
				else{
					controlador.Votar(tendencias.get(opcion).GetID());
				}
			}
		});
		
		btn_VerResultados.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getEdecisiones().getVerResultados().ir_a_inicio();
				controlador.getEdecisiones().getVerResultados().setVolver(false);
			}
		});
	}

	public void setTendencias(ArrayList<Tendencia> tendencias){
		this.tendencias = tendencias;
		Opciones.removeAllComponents();
		
		checks = new ArrayList<CheckBox>();
		
		for(int i = 0; i<tendencias.size(); i++){
			//Panel panel = new Panel();
			HorizontalLayout hl = campoTendencia(tendencias.get(i), i);
			Opciones.addComponent(hl);
		}
	}
	
	public HorizontalLayout campoTendencia(Tendencia t, int i){
		HorizontalLayout layout = new HorizontalLayout();
		
		CheckBox cb = new CheckBox(t.GetNombre());
		cb.addListener(this);
		checks.add(cb);
		layout.addComponent(cb);
		layout.setSizeFull();
		//layout.setVisible(true);
		
		try{
			StreamResource.StreamSource ss = new Imagen(t.GetImagen());
			StreamResource sr = new StreamResource(ss,t.getNombreImagen());
			Image img = new Image("", sr);
			img.setVisible(true);
			Panel imagen = new Panel();
			VerticalLayout vl = new VerticalLayout();
			vl.addComponent(img);
			vl.setSizeUndefined();
			imagen.setContent(vl);
			imagen.setSizeFull();
			layout.addComponent(imagen);
		}
		catch(Exception e){
			
		}
		
		return layout;
	}
	
	public int getOpcion(){
		int opcion = -1;
		
		for(CheckBox cb : checks){
			if(cb.getValue())
				opcion = checks.indexOf(cb);
		}
		
		return opcion;
	}
	
	class Imagen implements StreamResource.StreamSource{
		InputStream is = null;
		
		public Imagen(InputStream is){
			this.is = is;
		}
		
		@Override
		public InputStream getStream() {
			// TODO Auto-generated method stub
			/*ByteArrayOutputStream imagen = null;
			try{
				BufferedImage img = ImageIO.read(is);
				ImageIO.write(img, "jpg", imagen);
				
				File archivo = new File("img.jpg");
				FileOutputStream fos = null;
				
				imagen.flush();
				imagen.writeTo(fos);
				
				return new ByteArrayInputStream(
						imagen.toByteArray());
			}
			catch(Exception e){
				return null;
			}*/
			return this.is;
		}
		
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub
		int opcionTemp = opcion;
		if(primeraVez){
			for(int i=0; i<checks.size(); i++){
				opcion = checks.get(i).getValue()==true? i:0;
			}
			primeraVez = false;
		}
		else{
			for(int i=0; i<checks.size(); i++){
				if((checks.get(i).getValue()==true) && (opcion!=i)){
					checks.get(opcion).setValue(false);
					opcionTemp = i;
				}
			}
			if(opcionTemp == opcion){
				checks.get(opcion).setValue(false);
				primeraVez = true;
			}
			opcion = opcionTemp;
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// Menu
		Menu = buildMenu();
		mainLayout.addComponent(Menu);
		
		// lbl_Texto1
		lbl_Texto1 = new Label();
		lbl_Texto1.setImmediate(false);
		lbl_Texto1.setWidth("-1px");
		lbl_Texto1.setHeight("-1px");
		lbl_Texto1
				.setValue("Escoja una de las siguientes opciones y después presione el botón votar");
		mainLayout.addComponent(lbl_Texto1);
		mainLayout.setComponentAlignment(lbl_Texto1, new Alignment(48));
		
		// lbl_Texto2
		lbl_Texto2 = new Label();
		lbl_Texto2.setImmediate(false);
		lbl_Texto2.setWidth("-1px");
		lbl_Texto2.setHeight("-1px");
		lbl_Texto2
				.setValue("En caso de no escoger ninguna, se tomara como un voto en blanco.");
		mainLayout.addComponent(lbl_Texto2);
		mainLayout.setComponentAlignment(lbl_Texto2, new Alignment(48));
		
		// Opciones
		Opciones = new VerticalLayout();
		Opciones.setImmediate(false);
		Opciones.setWidth("-1px");
		Opciones.setHeight("-1px");
		Opciones.setMargin(false);
		mainLayout.addComponent(Opciones);
		
		// Botones
		Botones = buildBotones();
		mainLayout.addComponent(Botones);
		mainLayout.setComponentAlignment(Botones, new Alignment(48));
		
		return mainLayout;
	}

	@AutoGenerated
	private Panel buildMenu() {
		// common part: create layout
		Menu = new Panel();
		Menu.setImmediate(false);
		Menu.setWidth("100px");
		Menu.setHeight("30px");
		
		// verticalLayout_2
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setWidth("100.0%");
		verticalLayout_2.setHeight("100.0%");
		verticalLayout_2.setMargin(false);
		Menu.setContent(verticalLayout_2);
		
		return Menu;
	}

	@AutoGenerated
	private HorizontalLayout buildBotones() {
		// common part: create layout
		Botones = new HorizontalLayout();
		Botones.setImmediate(false);
		Botones.setWidth("-1px");
		Botones.setHeight("-1px");
		Botones.setMargin(false);
		
		// btn_Votar
		btn_Votar = new Button();
		btn_Votar.setCaption("Votar");
		btn_Votar.setImmediate(true);
		btn_Votar.setWidth("-1px");
		btn_Votar.setHeight("-1px");
		Botones.addComponent(btn_Votar);
		
		// btn_Volver
		btn_Volver = new Button();
		btn_Volver.setCaption("Volver");
		btn_Volver.setImmediate(true);
		btn_Volver.setWidth("-1px");
		btn_Volver.setHeight("-1px");
		Botones.addComponent(btn_Volver);
		
		// btn_VerResultados
		btn_VerResultados = new Button();
		btn_VerResultados.setCaption("Ver Resultados");
		btn_VerResultados.setImmediate(false);
		btn_VerResultados.setWidth("-1px");
		btn_VerResultados.setHeight("-1px");
		Botones.addComponent(btn_VerResultados);
		
		return Botones;
	}
}
