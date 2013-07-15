/**
 * @(#) ControladorBD.java
 */

package com.inge.edecisiones;

import java.lang.String;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vaadin.addon.sqlcontainer.SQLContainer;
import com.vaadin.addon.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.addon.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.addon.sqlcontainer.query.FreeformQuery;
import com.vaadin.addon.sqlcontainer.query.TableQuery;

public class ControladorBD
{
	private String NombreBD = "jdbc:mysql://127.0.0.1:3306/Edecisiones_BD";	
	private String Usuario = "root";	
	private String Password = "";
	
	private JDBCConnectionPool conexion = null;
	private Connection conn = null;
	
	public ControladorBD() {
			Conectar();	
	}
	
	public void Conectar( )	{
		try {
            /*conexion = new SimpleJDBCConnectionPool(
                    "com.mysql.jdbc.Driver",
                    NombreBD, Usuario, "", 2, 5);*/
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(NombreBD,Usuario,Password);
            System.out.println("se pudo conectar a la bd");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("no se pudo conectar a la bd");
        }
	}
	
	public void Cerrar() {
		try{
			conn.close();
		}
		catch(Exception e){
			
		}
	}
	
	public List<String> recuperarEstadisticasVotantes(Plebiscito p){
		List<String> retorno = new ArrayList<String>();
		
		try{
			PreparedStatement st = conn.prepareStatement("select count(*) "+
					"from Padron "+
					"where Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, p.GetID());
			
			ResultSet rs = st.executeQuery();
			rs.next();
			
			int votantes = rs.getInt("count(*)");
			
			st = conn.prepareStatement("select count(*) "+  
					"from Voto join Tendencia "+ 
					"on Tendencia_Id_Tendencia = Id_Tendencia "+ 
					"where Tendencia_Id_Tendencia in "+
					"(select t.Id_Tendencia "+
					"from Tendencia t join Plebiscito p "+
					"on t.Plebiscito_Id_Plebiscito = ?)");
			st.setInt(1, p.GetID());
			
			rs = st.executeQuery();
			rs.next();
			
			int han_votado = rs.getInt("count(*)");
			
			int faltan_votar = votantes - han_votado;
			
			retorno.add(Integer.toString(votantes));
			retorno.add(Integer.toString(han_votado));
			retorno.add(Integer.toString(faltan_votar));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public List<String> recuperarEstadisticas(Plebiscito plebiscito){
		List<String> retorno = new ArrayList<String>();
		
		try{
			PreparedStatement st = conn.prepareStatement("select count(*), Nombre_Tendencia "+  
														 "from Voto join Tendencia "+
														 "on Tendencia_Id_Tendencia = Id_Tendencia "+
														 "where Tendencia_Id_Tendencia in "+
														 "(select t.Id_Tendencia "+ 
														 "from Tendencia t join Plebiscito p "+
														 "on t.Plebiscito_Id_Plebiscito = ?) "+
														 "group by Tendencia_Id_Tendencia "+
														 "order by Tendencia_Id_Tendencia;");
			st.setInt(1, plebiscito.GetID());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()){
				String s = Integer.toString(rs.getInt("count(*)"))
						+" "+rs.getString("Nombre_Tendencia");
				
				retorno.add(s);
			}
		}
		catch(Exception e){
			
		}
		
		return retorno;
	}
	
	public List<String> recuperarEstadisticasGenero(Plebiscito plebiscito){
		List<String> retorno = new ArrayList<String>();
		
		try{
			PreparedStatement st = conn.prepareStatement("select count(*), Sexo "+
														 "from Voto join Tendencia "+ 
														 "on Tendencia_Id_Tendencia = Id_Tendencia "+
														 "join Persona "+
														 "on Persona_Cedula = Cedula "+
														 "where Tendencia_Id_Tendencia in "+
														 "(select t.Id_Tendencia "+ 
														 "from Tendencia t join Plebiscito p "+
														 "on t.Plebiscito_Id_Plebiscito = ?) "+
														 "group by Sexo,Tendencia_Id_Tendencia "+
														 "order by Tendencia_Id_Tendencia;");
			st.setInt(1, plebiscito.GetID());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()){
				String s = Integer.toString(rs.getInt("count(*)"))
						+" "+rs.getString("Sexo");
				
				retorno.add(s);
			}
		}
		catch(Exception e){
			
		}
		
		return retorno;
	} 
	
	public Ciudadano recuperarCiudadano(Usuario usuario){
		Ciudadano c = null;
		
		try{
			PreparedStatement st = conn.prepareStatement("select * from Persona join Usuario "
														+"on Cedula = Persona_Cedula "+
					                                     "where Id_Usuario = ?;");
			st.setInt(1, usuario.GetID());
			
			ResultSet rs = st.executeQuery();
			
			//con el metodo next se va desplazando por las tuplas
			//de la respuesta
			while(rs.next()){
				c = new Ciudadano(rs.getString("Nombre"), rs.getString("Apellido1"), 
						rs.getString("Apellido2"), rs.getInt("Cedula"));
				c.SetSexo(rs.getBoolean("Sexo"));
				c.SetFechaNac(rs.getDate("FechaNac"));
			}
		}
		catch(Exception e){
		}
		
		return c;
	}
	
	public void InsertarCiudadano(Ciudadano c){
		try{
			PreparedStatement st = conn.prepareStatement(
					"insert into Persona (Cedula,Nombre,Apellido1,Apellido2,Sexo,FechaNac) "+ 
			         "values (? ,? ,? ,?, ?, ?)");
			String[] nombre = c.GetName().split(" ");
			
			st.setInt(1, c.GetCedula());
			st.setString(2, nombre[0]);
			st.setString(3, nombre[1]);
			st.setString(4, nombre[2]);
			st.setBoolean(5, c.GetSexo());
			java.sql.Date fechaNac = new java.sql.Date(c.GetFechaNac().getTime());
			st.setDate(6, fechaNac);
			
			int rs = st.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EditarCiudadano2(Ciudadano c){
		try{
			PreparedStatement st = conn.prepareStatement(
					"update Persona set Nombre = ?, Apellido1 = ?, Apellido2 = ?, " +
					"Sexo = ?, FechaNac = ? where Cedula = ? ");
			String[] nombre = c.GetName().split(" ");
			
			st.setString(1, nombre[0]);
			st.setString(2, nombre[1]);
			st.setString(3, nombre[2]);
			st.setBoolean(4, c.GetSexo());
			java.sql.Date fechaNac = new java.sql.Date(c.GetFechaNac().getTime());
			st.setDate(5, fechaNac);
			st.setInt(6, c.GetCedula());
			
			int rs = st.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void InsertarUsuario(Usuario usuario){
		try{
			Ciudadano c = usuario.getDatosCiudadano();
			PreparedStatement st = conn.prepareStatement("select * from Persona where Cedula = ?");
			st.setInt(1, c.GetCedula());
			
			ResultSet rs = st.executeQuery();
			
			//si no esta en la bd se agrega
			if(!rs.next()){
				InsertarCiudadano(c);
			}
			
			st = conn.prepareStatement("insert into Usuario (Email,Password,Fotografia,Persona_Cedula) "+
										"values(?, ?, ?, ?)");
			st.setString(1, usuario.GetEmail());
			st.setString(2, usuario.GetPassword());
			st.setBlob(3, usuario.GetFoto());
			st.setInt(4, c.GetCedula());
			
			int r = st.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EditarUsuario(Usuario usuario){
		try{
			EditarCiudadano2(usuario.getDatosCiudadano());
			
			PreparedStatement st = conn.prepareStatement("update Usuario set " +
					"Email = ?, Password = ?, Fotografia = ? "+
					"where Id_Usuario = ?;");
			st.setString(1, usuario.GetEmail());
			st.setString(2, usuario.GetPassword());
			st.setBlob(3, usuario.GetFoto());
			System.out.println(usuario.GetID());
			st.setInt(4, usuario.GetID());
			
			int rs = st.executeUpdate();
			
			System.out.println(rs);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void InsertarEnPadron(Ciudadano c, int plebiscito) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"insert into Padron (Persona_Cedula,Plebiscito_Id_Plebiscito) "+
					"values (?,?)");
			st.setInt(1, c.GetCedula());
			st.setInt(2, plebiscito);
			
			int rs = st.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EliminarPadron(Ciudadano c, int plebiscito) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"delete from Padron where Persona_Cedula = ? " +
					"and Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, c.GetCedula());
			st.setInt(2, plebiscito);
			
			int rs = st.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EditarPadron(Ciudadano c, int Cedula, int plebiscito) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"update Padron set Persona_Cedula = ? " +
					"where Persona_Cedula = ? and Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, c.GetCedula());
			st.setInt(2, Cedula);
			st.setInt(3, plebiscito);
			
			int rs = st.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void InsertarCiudadano(Ciudadano c, int plebiscito) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"insert into Persona (Cedula,Nombre,Apellido1,Apellido2) values (? ,? ,? ,?)");
			String[] nombre = c.GetName().split(" ");
			
			st.setInt(1, c.GetCedula());
			st.setString(2, nombre[0]);
			st.setString(3, nombre[1]);
			st.setString(4, nombre[2]);
			
			int rs = st.executeUpdate();
			
			st = conn.prepareStatement("insert into Padron (Persona_Cedula,Plebiscito_Id_Plebiscito)"+
					"values (?,?)");
			
			st.setInt(1, c.GetCedula());
			st.setInt(2, plebiscito);
			
			rs = st.executeUpdate();
			
			if(rs > 0)
				System.out.println("Se inserto correctamente");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EliminarCiudadano(Ciudadano c) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"delete from Persona where Cedula = ?");
			st.setInt(1, c.GetCedula());
			
			int rs = st.executeUpdate();
			
			if(rs>0)
				System.out.println("Se elimino correctamente");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EditarCiudadano(Ciudadano c, int Cedula) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"update Persona set Nombre = ?, Apellido1 = ?, Apellido2 = ? " +
					"where Cedula = ? ");
			String[] nombre = c.GetName().split(" ");
			
			st.setString(1, nombre[0]);
			st.setString(2, nombre[1]);
			st.setString(3, nombre[2]);
			st.setInt(4, Cedula);
			
			int rs = st.executeUpdate();
			
			if(rs > 0)
				System.out.println("Se actualizo correctamente");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param pe
	 * @return
	 */
	public Padron recuperarPadron(Plebiscito pe){
		Padron p = null;
		
		try{
			PreparedStatement st = conn.prepareStatement(
					"select Persona_Cedula " +
					"from Padron " +
					"where Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, pe.GetID());
			
			ResultSet rs = st.executeQuery();
			
			p = new Padron(pe.GetID());
			while(rs.next()){
				Ciudadano c = new  Ciudadano("", "", "", rs.getInt("Persona_Cedula"));
				p.AddCiudadano(c);
			}
			/*PreparedStatement st = conn.prepareStatement(
					"select pe.Cedula, pe.Nombre, pe.Apellido1, pe.Apellido2 "+
					"from Persona pe join Padron pa on pa.Persona_Cedula = pe.Cedula "+
					"where pa.Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, pe.GetID());
			
			ResultSet rs = st.executeQuery();
			
			p = new Padron(pe.GetID());
			while(rs.next()){
				Ciudadano c = new Ciudadano(rs.getString("Nombre"), rs.getString("Apellido1"),
						rs.getString("Apellido2"), rs.getInt("Cedula"));
				p.AddCiudadano(c);
			}*/
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return p;
	}
	
	/**
	 * 
	 */
	public void insertarPadron(Padron p){
		try{
			List<Ciudadano> padron = p.GetCiudadanos();
			for(Ciudadano c : padron){
			/*	PreparedStatement st = conn.prepareStatement(
						"insert into Persona (Cedula,Nombre,Apellido1,Apellido2) values (? ,? ,? ,?)");
				String[] nombre = c.GetName().split(" ");
				st.setInt(1, c.GetCedula());
				st.setString(2, nombre[0]);
				st.setString(3, nombre[1]);
				st.setString(4, nombre[2]);
				
				int rs = st.executeUpdate();
				
				st = conn.prepareStatement("insert into Padron (Persona_Cedula,Plebiscito_Id_Plebiscito)"+
						"values (?,?)");
				st.setInt(1, c.GetCedula());
				st.setInt(2, p.GetID_Plebiscito());
				
				rs = st.executeUpdate();*/
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Recupera una lista de las tendencias del plebiscito p
	 */
	public List<Tendencia> RecuperarTendencias(Plebiscito p){
		ArrayList<Tendencia> retorno = new ArrayList<Tendencia>();
		
		try{
			PreparedStatement st = conn.prepareStatement(
					"select Id_Tendencia,Nombre_Tendencia,Fotografia "+
					"from Tendencia where Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, p.GetID());
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()){
				Tendencia t = new Tendencia(p.GetID());
				t.SetNombre(rs.getString("Nombre_Tendencia"));
				t.SetID(rs.getInt("Id_Tendencia"));
				t.SetImagen(rs.getBinaryStream("Fotografia"));
				retorno.add(t);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//System.out.println("Cantidad tuplas: "+retorno.size());
		return retorno;
	}
	
	/**
	 * Recupera una lista con los nombres de las tendencias
	 * del plebiscito de id:
	 * @param idp
	 * @return la lista que contiene los nombres
	 */
	public List<String> RecuperarListaTendencias( int idp ) {
		List<String> resultado = new ArrayList<String>();
		
		PreparedStatement rec;
		ResultSet result;
		
		try {
			String query = "select Nombre_Tendencia from Tendencia " +
					"where Plebiscito_Id_Plebiscito = ?";
			
			 rec = conn.prepareStatement(query);
			 rec.setInt(1, idp);
			 
			 result = rec.executeQuery();
			 
			 while( result.next() ) {
				 resultado.add(result.getString("Nombre_Tendencia"));
			 } 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	
	/**
	 * Recupera la tendencia de nombre cuyo id es 
	 * @param idt
	 * @return null si no existe la tendencia. La tendencia si 
	 * esta existe.
	 * NOTA: 
	 * Los siguentes campos del objeto tendencia devuelto pueden ser null:
	 * - Imagen
	 * - NombreImagen
	 */
	public Tendencia RecuperarTendencia(int idt) {
		Tendencia tendencia = null;
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT Id_Tendencia, Nombre_Tendencia, Fotografia, Nombre_Fotografia, Descripcion, Info_contacto, Plebiscito_Id_Plebiscito " +
				"FROM Tendencia " +
				"WHERE Id_Tendencia = ?";
		
		try {
			rec = conn.prepareStatement(sql);
			rec.setInt(1, idt);
			
			result = rec.executeQuery();
			
			if( result.first() ) {
				tendencia = new Tendencia();
				
				//recuperar los not null
				tendencia.SetID(idt); // esto ya lo sabia
				tendencia.SetIDPlebiscito(result.getInt("Plebiscito_Id_Plebiscito"));
				tendencia.SetNombre(result.getString("Nombre_Tendencia"));
				tendencia.SetDescripcion(result.getString("Descripcion"));
				tendencia.SetInfoContacto(result.getString("Info_contacto"));
				
				// los links externos siempre son una lista (no un campo null)
				// incluso si esta vacia. 
				tendencia.SetLinksExternos( RecuperarLinksExternos(idt) );
				
				// estos campos si pueden quedar null
				tendencia.SetImagen(result.getBinaryStream("Fotografia"));	
				// si hay imagen, pedir el nombre
				if( tendencia.GetImagen() != null ) {
					tendencia.setNombreImagen(result.getString("Nombre_Fotografia"));
				}
				else {
					tendencia.setNombreImagen(null);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tendencia;
	}
	
	/**
	 * Recupera los links externos de un tendencia de id:
	 * @param idt
	 * @return una lista con los links externos
	 */
	public List<String> RecuperarLinksExternos(int idt) {
		List<String> resultado = new ArrayList<String>();
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT LinkExterno FROM LinksExternos " +
				"WHERE Tendencia_Id_Tendencia = ?";
		try {
			rec = conn.prepareStatement(sql);
			rec.setInt(1, idt);
			
			result = rec.executeQuery();
			
			 while( result.next() ) {
				 resultado.add(result.getString("LinkExterno"));
			 } 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	/**
	 * Recupera el id de la tendencia del plebiscito con id
	 * @param idp
	 * y cuyo nombre de tendencia es
	 * @param nombret
	 * @return el id de la tendencia si existe, si no -1
	 */
	public int RecuperarIdTendencia(int idp, String nombret) {
		int id = -1;
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT Id_Tendencia FROM Tendencia " +
				"WHERE Plebiscito_Id_Plebiscito = ? AND Nombre_Tendencia = ?";
		
		try {
			rec = conn.prepareStatement(sql);
			rec.setInt(1, idp);
			rec.setString(2, nombret);
			
			result = rec.executeQuery();
			
			if ( result.first() ) {
				id = result.getInt("Id_Tendencia");
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return id;
	}
	
	/**
	 * Recupera el ide del plebiscito de nombre:
	 * @param nombrep
	 * @return el id del plebiscito si existe, de lo contrario -1
	 */
	public int RecuperarIdPlebiscito(String nombrep) {
		int id = -1;
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT Id_Plebiscito FROM Plebiscito " +
				"WHERE Nombre_Plebiscito = ?";
		
		try {
			rec = conn.prepareStatement(sql);
			rec.setString(1, nombrep);
			
			result = rec.executeQuery();
			
			if ( result.first() ) {
				id = result.getInt("Id_Plebiscito");
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return id;
	}
	
	public void EditarCiudadano(Ciudadano c, int Cedula, int plebiscito) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"update Padron set Persona_Cedula = ? " +
					"where Persona_Cedula = ? and Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, c.GetCedula());
			st.setInt(2, Cedula);
			st.setInt(3, plebiscito);
			
			int rs = st.executeUpdate();
			/*PreparedStatement st = conn.prepareStatement(
					"update Persona set Nombre = ?, Apellido1 = ?, Apellido2 = ? " +
					"where Cedula = ? ");
			String[] nombre = c.GetName().split(" ");
			
			st.setString(1, nombre[0]);
			st.setString(2, nombre[1]);
			st.setString(3, nombre[2]);
			st.setInt(4, Cedula);
			
			int rs = st.executeUpdate();
			
			if(rs > 0)
				System.out.println("Se actualizo correctamente");*/
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EliminarCiudadano(Ciudadano c, int plebiscito) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"delete from Padron where Persona_Cedula = ? " +
					"and Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, c.GetCedula());
			st.setInt(2, plebiscito);
			
			int rs = st.executeUpdate();
			/*PreparedStatement st = conn.prepareStatement(
					"delete from Persona where Cedula = ?");
			st.setInt(1, c.GetCedula());
			
			int rs = st.executeUpdate();
			
			if(rs>0)
				System.out.println("Se elimino correctamente");*/
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param nombre
	 * @return
	 */
	public Plebiscito RecuperarPlebiscito( String nombre ) {
		Plebiscito resultado = null;
		PreparedStatement recuperaP;
		ResultSet pRecuperado;
		try {
			String queryP = "SELECT Id_Plebiscito, Nombre_Plebiscito, Tipo, Acceso, Descripcion, " +
					"Votacion_Inicio, Votacion_Final, Insc_Tend_Inicio, Insc_Tend_Final, " +
					"Discusion_Inicio, Discusion_Final, Resultados, Usuario_Creador " +
					"FROM Plebiscito "+
					"where Nombre_Plebiscito = ?";
			
			recuperaP = conn.prepareStatement(queryP);
			recuperaP.setString(1,nombre);
			
			pRecuperado = recuperaP.executeQuery();
			
			// si se recupero al menos una fila:
			if ( pRecuperado.first() ) {
				resultado = new Plebiscito();
				resultado.SetID(pRecuperado.getInt("Id_Plebiscito"));
				
				resultado.GetInfoPlebiscito().SetNombre(pRecuperado.getString("Nombre_Plebiscito"));
				resultado.GetInfoPlebiscito().SetTipo(pRecuperado.getBoolean("Tipo"));
				resultado.GetInfoPlebiscito().SetAcceso(pRecuperado.getBoolean("Acceso"));
				resultado.GetInfoPlebiscito().SetDescripcion(pRecuperado.getString("Descripcion"));
				
				resultado.GetInfoPlebiscito().SetPeriodoVotacion(pRecuperado.getDate("Votacion_Inicio"),pRecuperado.getDate("Votacion_Final"));
				resultado.GetInfoPlebiscito().SetPeriodoInscripcion(pRecuperado.getDate("Insc_Tend_Inicio"),pRecuperado.getDate("Insc_Tend_Final"));
				resultado.GetInfoPlebiscito().SetPeriodoDiscusion(pRecuperado.getDate("Discusion_Inicio"),pRecuperado.getDate("Discusion_Final") );				
				resultado.GetInfoPlebiscito().SetPublicacionResultados(pRecuperado.getDate("Resultados"));
				resultado.setIdUsuarioCreador( pRecuperado.getInt("Usuario_Creador") );
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	

	
	
	/**
	 * recupera los nombres de los plebiscitos
	 * del usuario cuyo id es
	 * @param id_usuario
	 * @return lista que contiene los nombres
	 */
	public List<String> RecuperarListaPlebiscitos(int id_usuario) {
		// TODO Auto-generated method stub
		List<String> resultado = new ArrayList<String>();
		PreparedStatement rec;
		ResultSet result;
		
		try {
			String slq = "select Nombre_Plebiscito from Plebiscito " +
					"where Usuario_Creador = ?";
			
			 rec = conn.prepareStatement(slq);
			 rec.setInt(1, id_usuario);
			 
			 result = rec.executeQuery();
			 
			 while( result.next() ) {
				 resultado.add(result.getString("Nombre_Plebiscito"));
			 } 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	
	/**
	 * 	recupera el voto de un ciudadano en un plebiscito
	 * @param p
	 */
	public boolean RecuperarVoto(int Cedula, Plebiscito p){
		boolean retorno = false;
		
		try{
			PreparedStatement st = conn.prepareStatement(
					"select * from Voto where Persona_Cedula = ? and " +
					"Tendencia_Id_Tendencia in " +
					"(select Id_Tendencia from Tendencia " +
					"where Plebiscito_Id_Plebiscito = ? )");
			st.setInt(1, Cedula);
			st.setInt(2, p.GetID());
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next())
				retorno = true;
		}
		catch(Exception e){
			
		}
		
		return retorno;
	}
	
	
//	public Resultados RecuperarResultadosFinales( Plebiscito p )
//	{
//		return null;
//	}
//	
//	public Foro RecuperarForoDiscusion( Plebiscito p )
//	{
//		return null;
//	}
//	
//	public Padron RecuperarPadron( Plebiscito p )
//	{
//		return null;
//	}
//	
//	public Usuario RecuperarUsuario( String usr, String cont )
//	{
//		return null;
//	}
//	
//	public GeneradorEstadisticas RecuperarEstadisticas( Plebiscito p )
//	{
//		return null;
//	}
	
	public void InsertarPlebiscito( Plebiscito p ) {
		InfoPlebiscito datos = p.GetInfoPlebiscito();
		
		String insquery = "INSERT INTO Plebiscito(Nombre_Plebiscito, Tipo, Acceso, Descripcion, " +
					"Votacion_Inicio, Votacion_Final, Insc_Tend_Inicio, Insc_Tend_Final, " +
					"Discusion_Inicio, Discusion_Final, Resultados, Usuario_Creador) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement insertar;
		
		try {
			insertar = conn.prepareStatement(insquery);
			insertar.setString(1, datos.GetNombre());
			insertar.setBoolean(2, datos.GetTipo());
			insertar.setBoolean(3, datos.GetAcceso());
			insertar.setString(4, datos.GetDescripcion());
			
			java.sql.Date inivota = new java.sql.Date(datos.GetPeriodoVotacion()[0].getTime()); 
			java.sql.Date finvota= new java.sql.Date(datos.GetPeriodoVotacion()[1].getTime());
			insertar.setDate(5, inivota);
			insertar.setDate(6, finvota);
			
			java.sql.Date iniins = new java.sql.Date(datos.GetPeriodoInscripcion()[0].getTime());
			java.sql.Date finins= new java.sql.Date(datos.GetPeriodoInscripcion()[1].getTime());
			insertar.setDate(7, iniins);
			insertar.setDate(8, finins);
			
			java.sql.Date inidis= new java.sql.Date(datos.GetPeriodoDiscusion()[0].getTime());
			java.sql.Date findis= new java.sql.Date(datos.GetPeriodoDiscusion()[1].getTime());
			insertar.setDate(9, inidis);
			insertar.setDate(10, findis);
			
			java.sql.Date resuls= new java.sql.Date(datos.GetPublicacionResultados().getTime());
			insertar.setDate(11, resuls);
			
			insertar.setInt(12, p.getIdUsuarioCreador());
			
			int i = insertar.executeUpdate();
			if( i > 0) {
				System.out.println("InsertarPlebiscito: Se inserto correctamente");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * inserta la tendencia
	 * @param t
	 */
	public void InsertarTendencia( Tendencia t ) {
		String sql = "INSERT INTO Tendencia(Nombre_Tendencia, Fotografia, " +
				"Nombre_Fotografia, Descripcion, Info_contacto, " +
				"Plebiscito_Id_Plebiscito) " +
				"VALUES (?, ?, ?, ?, ?, ?)";	
		
		PreparedStatement insertar;
		
		try {
			insertar = conn.prepareStatement(sql);
			
			insertar.setString(1, t.GetNombre());
			
			if(t.GetImagen() == null) {
				System.out.println(": insertarten: no hay imagen");
				insertar.setNull(2, java.sql.Types.BLOB);
				insertar.setNull(3, java.sql.Types.VARCHAR);
			}
			else {
				insertar.setBlob(2, t.GetImagen());
				insertar.setString(3, t.getNombreImagen());
			}
			
			insertar.setString(4, t.GetDescripcion());
			insertar.setString(5, t.GetInfoContacto());
			insertar.setInt(6, t.GetID_Plebiscito());
			
			int i = insertar.executeUpdate();
			if( i > 0) {
				System.out.println("InsertarTendencia: Se inserto correctamente, insertar links");
				
				// se supone que ya se inserto
				int idt = RecuperarIdTendencia(t.GetID_Plebiscito(), t.GetNombre());
				t.SetID(idt);
				
				if( t.GetLinksExternos() != null ) {
					InsertarLinksExternos( idt, t.GetLinksExternos() );
				}
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
	/**
	 * inserta los links de la tendencia de id:
	 * @param idt
	 */
	public void InsertarLinksExternos(int idt, List<String> lista) {
		
		String sql = "INSERT INTO LinksExternos(Tendencia_Id_Tendencia, LinkExterno) " +
				"VALUES (?, ?)";	
		
		PreparedStatement insertar;
		
		try {
			for(String link : lista) {
				insertar = conn.prepareStatement(sql);
				insertar.setInt(1, idt);
				insertar.setString(2, link);
				
				insertar.executeUpdate();				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * edita los links de la tendencia de id:
	 * @param idt
	 */
	public void EditarLinksExternos(int idt, List<String> lista) {
		List<String> originales = RecuperarLinksExternos(idt);
		
		
		String sql = "INSERT INTO LinksExternos(Tendencia_Id_Tendencia, LinkExterno) " +
				"VALUES (?, ?)";	
		
		PreparedStatement insertar;
		
		try {
			// primero insertar los nuevos
			for(String link : lista) {
				// si uno esta en la nueva lista y no en el original
				// es un nuevo link que debe ser agregado
				if( !originales.contains(link) ) {
					insertar = conn.prepareStatement(sql);
					insertar.setInt(1, idt);
					insertar.setString(2, link);
					if ( insertar.executeUpdate() > 1 ) {
						System.out.println("EditarLinksExternos: Se inserto correctamente");
					}
						
				}				
			}
			
			// ahora borrar los elminados
			PreparedStatement borrar;
			sql = "DELETE FROM LinksExternos " +
					"WHERE Tendencia_Id_Tendencia = ? AND LinkExterno = ?";
			
			for(String linkviejo : originales) {
				// si la lista mas reciente
				// no contiene un link en la lista original
				// es porque fue eliminado y se debe quitar
				// de la bd
				//System.out.println("tendencia " + idt + ": " +linkviejo);
				if( !lista.contains(linkviejo) ) {
					//System.out.println(": no esta en la nueva lista");
					
					borrar = conn.prepareStatement(sql);
					borrar.setInt(1, idt);
					borrar.setString(2, linkviejo);
					borrar.executeUpdate();					
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserta el voto en bd
	 * @param voto
	 */
	public void InsertarVoto(Voto voto){
		try{
			PreparedStatement st = conn.prepareStatement(
					"insert into Voto (Persona_Cedula,Tendencia_Id_Tendencia) " +
					"values (?,?)");
			
			st.setInt(1, voto.GetId_Usuario());
			st.setInt(2, voto.GetID_Tendencia());
			
			int rs = st.executeUpdate();
			
			if(rs>0)
				System.out.println("Se realizo el voto");
			else
				System.out.println("No se pudo realizar el voto");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
//	
//	public void InsertarRespuestaForo( RespuestaForo rf )
//	{
//		
//	}
//	
//	public void InsertarPadron( Padron pad )
//	{
//		
//	}
//	
//	public void InsertarUsuario( Usuario usr )
//	{
//		
//	}
	
	/**
	 * Cambia los valores del plebiscito p
	 * @param p
	 */
	public void EditarPlebiscito( Plebiscito p ) {
		InfoPlebiscito datos = p.GetInfoPlebiscito();
		String upquery = "UPDATE Plebiscito " +
				"SET Nombre_Plebiscito = ?, Tipo = ?, Acceso = ?, Descripcion = ?, " +
				"Votacion_Inicio = ?, Votacion_Final = ?, Insc_Tend_Inicio = ?, Insc_Tend_Final = ?, " +
				"Discusion_Inicio = ?, Discusion_Final = ?, Resultados = ? " +
				"WHERE Id_Plebiscito = ?";
				

		PreparedStatement insertar;
		
		try {
			insertar = conn.prepareStatement(upquery);
			insertar.setString(1, datos.GetNombre());
			insertar.setBoolean(2, datos.GetTipo());
			insertar.setBoolean(3, datos.GetAcceso());
			insertar.setString(4, datos.GetDescripcion());
			
			java.sql.Date inivota = new java.sql.Date(datos.GetPeriodoVotacion()[0].getTime()); 
			java.sql.Date finvota= new java.sql.Date(datos.GetPeriodoVotacion()[1].getTime());
			insertar.setDate(5, inivota);
			insertar.setDate(6, finvota);
			
			java.sql.Date iniins = new java.sql.Date(datos.GetPeriodoInscripcion()[0].getTime());
			java.sql.Date finins= new java.sql.Date(datos.GetPeriodoInscripcion()[1].getTime());
			insertar.setDate(7, iniins);
			insertar.setDate(8, finins);
			
			java.sql.Date inidis= new java.sql.Date(datos.GetPeriodoDiscusion()[0].getTime());
			java.sql.Date findis= new java.sql.Date(datos.GetPeriodoDiscusion()[1].getTime());
			insertar.setDate(9, inidis);
			insertar.setDate(10, findis);
			
			java.sql.Date resuls= new java.sql.Date(datos.GetPublicacionResultados().getTime());
			insertar.setDate(11, resuls);
			
			insertar.setInt(12, p.GetID());
			
			int i = insertar.executeUpdate();
			if( i > 0) {
				System.out.println("EditarPlebiscito: Se edito correctamente el plebiscito");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void EditarTendencia( Tendencia t ) {
		String sql = "UPDATE Tendencia " +
				"SET Nombre_Tendencia = ?, Fotografia = ?, " +
				"Nombre_Fotografia = ?, Descripcion = ?, Info_contacto = ?, " +
				"Plebiscito_Id_Plebiscito = ? " +
				"WHERE Id_Tendencia = ?";	
		
		PreparedStatement editar;
		
		try {
			editar = conn.prepareStatement(sql);
			
			editar.setString(1, t.GetNombre());
			
			if(t.GetImagen() == null) {
				//System.out.println("editarten: no hay imagen");
				editar.setNull(2, java.sql.Types.BLOB);
				editar.setNull(3, java.sql.Types.VARCHAR);
			}
			else {
				
				ByteArrayInputStream ba = (ByteArrayInputStream)(t.GetImagen());
				System.out.println(ba.toString());
				editar.setBlob(2,  t.GetImagen());
				editar.setString(3, t.getNombreImagen());
			}
			
			editar.setString(4, t.GetDescripcion());
			editar.setString(5, t.GetInfoContacto());
			editar.setInt(6, t.GetID_Plebiscito());
			
			editar.setInt(7, t.GetID());
			
			int i = editar.executeUpdate();
			if( i > 0) {
				System.out.println("EditarTendencia: Se edito correctamente, editar links");
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		// editar los links externos
		if( t.GetLinksExternos() != null ) {
			EditarLinksExternos( t.GetID(), t.GetLinksExternos() );
		}
	}
//	
//	public void EditarPadron( Padron p )
//	{
//		
//	}
//	
//	public void EditarUsuario( Usuario usr )
//	{
//		
//	}
	
	public void EliminarPlebiscito( Plebiscito p ) {
		
	}

	public void EliminarPlebiscito(String nombrep) {
		// TODO Auto-generated method stub		
		PreparedStatement elimina;
		
		try {
			String queryP = "DELETE FROM Plebiscito where Nombre_Plebiscito = ?";
			
			elimina = conn.prepareStatement(queryP);
			elimina.setString(1,nombrep);
			
			// hacer algo con i
			int i = elimina.executeUpdate();
			if( i > 0 ) {
				System.out.println("EliminarPlebiscito: Se elimino correctamente");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Elimina la tendencia cuyo id es:
	 * @param idt
	 */
	public void EliminarTendencia( int idt ) {
		PreparedStatement elimina;
		
		try {
			String sql = "DELETE FROM Tendencia where Id_Tendencia = ?";
			
			elimina = conn.prepareStatement(sql);
			elimina.setInt(1,idt);
			
			// hacer algo con i
			int i = elimina.executeUpdate();
			if( i > 0 ) {
				System.out.println("EliminarTendencia: Se elimino correctamente");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
//	public void EliminarPadron( Padron p )
//	{
//		
//	}
//	
//	public void EliminarUsuario( Usuario usr )
//	{
//		
//	}

	
	/**
	 * Retorna el usuario que tiene proporciona los datos
	 * @param correo
	 * y 
	 * @param password
	 * retorna null si esos valores no se encontraron en la db.
	 * 
	 * El objeto Usuario retornado no tiene los datos del ciudadano.
	 * */
	public Usuario usuarioValido(String correo, String password) {
		Usuario usr = null;
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT * FROM Usuario " +
				"WHERE Email = ? AND Password = ?";
		try {
			rec = conn.prepareStatement(sql);
			rec.setString(1, correo);
			rec.setString(2, password);
			
			result = rec.executeQuery();
			
			 if( result.first() ) { // si hay al menos una fila, el usuario esta en la bd
				 Ciudadano c = new Ciudadano();
				 c.setCedula      (result.getInt("Persona_Cedula")       );
				 
				 usr = new Usuario(c);
				 
				 usr.SetID        ( result.getInt("Id_Usuario")          );
				 usr.SetContrasena( result.getString("Password")         );
				 usr.SetEmail     ( result.getString("Email")            );
				 usr.SetFoto      ( result.getBinaryStream("Fotografia") );				 
			 }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return usr;
	}

	/**
	 * Busca los plebiscitos que cumplan con las reglas:
	 * activo: 0 solo inactivos, 1 solo activos, 2 ambos
	 * */
	public List<String> recuperar_lista_plebiscitos(int cedula, boolean acceso, int activo, String nombre) {
		List<String> resultado = null;
		
		PreparedStatement buscar;
		
		try {
			String select = "select Nombre_Plebiscito ";
			String from1  = "from Plebiscito "; // si es publico
			String from2  = "from Plebiscito JOIN Padron ON Plebiscito.Id_Plebiscito = Padron.Plebiscito_Id_Plebiscito ";
			String where  = "where Acceso = ? AND Nombre_Plebiscito LIKE ? ";
			String factiv = "AND Votacion_Final >= ? "; // hoy o despues de hoy
			String finact = "AND Votacion_Final < ? "; // antes de hoy
			String empadr = "AND Persona_Cedula = ? "; // si esta empadronado
			
			// armar el query
			String sql = select;
			sql += ( acceso )? from1:from2;	// si es publico no hace falta hacer join
			sql += where;			
			if (activo != 2 ) { // si solo se quiere activo o inactivo
				sql += ( activo == 1 )? factiv:finact; // poner una clausula mas al slq => un parametro mas
			}			
			sql += ( acceso )? "" : empadr;			
			System.out.println("Buscar con: " + sql + "\n");
			
			// Establecer los parametros
			// 1: acceso, 2: nombre, 3: fecha actual, 4 (solo si es privado): id_usr
			buscar = conn.prepareStatement(sql);
			buscar.setBoolean(1, acceso);
			if(nombre == null) {
				buscar.setString(2, "%");
			}
			else {
				buscar.setString(2, "%"+nombre+"%");
			}
			
			if (activo != 2) { // entonces hay que indicar la fecha a comparar -> param 3
				buscar.setDate(3,  new java.sql.Date(  new java.util.Date().getTime()  ) );
				if( !acceso ) { // si es privado hay que darle la cedula para ver si es empadronado
					buscar.setInt(4, cedula);
				}
			}
			else {
				if( !acceso ) { // si es privado hay que darle la cedula para ver si es empadronado
					buscar.setInt(3, cedula);
				}
			}
			
			
			
			ResultSet res  = buscar.executeQuery();		

			resultado = new ArrayList<String>();
			while( res.next() ) {
				System.out.println(res.getString("Nombre_Plebiscito") + "\n");
				 resultado.add(res.getString("Nombre_Plebiscito"));
			} 
					
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}

	/**
	 * Indica si el usuario de cedula ced,
	 * esta empadronado en el plebiscito ple
	 * @param ced
	 * @param ple
	 * @return
	 */
	public boolean empadronado(int ced, int ple) {
		// TODO Auto-generated method stub
		boolean empadronado = false;
		String sql = "select * " +
				"from Padron " +
				"where Persona_Cedula = ? AND Plebiscito_Id_Plebiscito = ? ";
		
		try {
			PreparedStatement buscar = conn.prepareStatement(sql);
			buscar.setInt(1, ced);
			buscar.setInt(2, ple);
			
			ResultSet res = buscar.executeQuery();
			
			if ( res.first() ) {
				empadronado = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return empadronado;
	}

	public void insertar_respuesta_foro(RespuestaForo nueva) {
		
		String sql = "INSERT INTO RespuestaForo(Respuesta, Numero, Plebiscito_Id_Plebiscito, Usuario_Id_Usuario) " +
					"VALUES (?, ?, ?, ?)";
		
		PreparedStatement insertar;
		
		try {
			insertar = conn.prepareStatement(sql);
			insertar.setString( 1, nueva.getRespuesta()       );
			insertar.setInt   ( 2, nueva.getNumeroRespuesta() );
			insertar.setInt   ( 3, nueva.getID_Plebiscito()   );
			insertar.setInt   ( 4, nueva.getID_Usuario()      );
			
			
			int i = insertar.executeUpdate();
			if( i > 0) {
				System.out.println("InsertarPlebiscito: Se inserto correctamente");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * guarda en la respuesta nueva, la fecha en que se inscribio
	 * @param nueva
	 */
	public void recuperar_fecha_respuesta_foro(RespuestaForo nueva) {				
		try{
			String sql =
					"select Fecha " +
					"from RespuestaForo " +
					"where Numero = ? and Plebiscito_Id_Plebiscito = ? "; 
			
			PreparedStatement buscar = conn.prepareStatement( sql );
			
			buscar.setInt(1, nueva.getNumeroRespuesta() );
			buscar.setInt(2, nueva.getID_Plebiscito() );
			
			ResultSet rs = buscar.executeQuery();
			
			if( rs.first() ) {
				nueva.setFecha( new java.util.Date( rs.getTimestamp("Fecha").getTime() ) );				
			}
				
		}
		catch(Exception e){
			
		}
	}
	
	/**
	 * recupera el nombre del usuario cuya id es id_usuario
	 * @param id_Usuario
	 */
	public String recuperar_nombre_usuario(int id_Usuario) {
		String nombre = null;
		try{
			
			String sql =
					"select Nombre, Apellido1, Apellido2 " +
					"from Usuario join Persona on Usuario.Persona_Cedula = Persona.Cedula " +
					"where Id_Usuario = ? "; 
			
			PreparedStatement buscar = conn.prepareStatement( sql );
			
			buscar.setInt(1, id_Usuario );
			
			ResultSet rs = buscar.executeQuery();
			
			if( rs.first() ) {
				nombre =  rs.getString("Nombre");
				nombre += " " + rs.getString("Apellido1");
				nombre += " " + rs.getString("Apellido2");
			}
				
		}
		catch(Exception e){
			
		}	
		return nombre;
	}

	/**
	 * Recupera todas las respuestas del foro de un plebiscito
	 * */
	public List<RespuestaForo> recuperar_lista_respuestas(int id_plebiscito) {
		
		List<RespuestaForo> respuestas = new ArrayList<RespuestaForo>();
		
		PreparedStatement rec;
		ResultSet result;
		
		try {
			String slq = 
					"select * " +
					"from RespuestaForo " +
					"where Plebiscito_Id_Plebiscito = ? order by Numero";
			
			 rec = conn.prepareStatement(slq);
			 rec.setInt(1, id_plebiscito);
			 
			 result = rec.executeQuery();
			 
			 int id_Usuario;
			 RespuestaForo nueva = null;
			 
			 while( result.next() ) {
				 nueva = new RespuestaForo();
				 
				 id_Usuario = result.getInt("Usuario_Id_Usuario");
				 
				 nueva.setID( result.getInt( "Id_RespuestaForo" ) );
				 nueva.setNombre_usuario( this.recuperar_nombre_usuario(id_Usuario) );
				 nueva.setRespuesta( result.getString("Respuesta") );
				 nueva.setNumeroRespuesta( result.getInt(  "Numero" ) );
				 nueva.setFecha(  new java.util.Date( result.getTimestamp("Fecha").getTime() ) );
				 nueva.setID_Usuario( id_Usuario );
				 nueva.setID_Plebiscito(id_plebiscito); // esto ya se conocia, es el parametro de la fun
				 
				 respuestas.add(nueva);
			 } 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return respuestas;
	}
}
