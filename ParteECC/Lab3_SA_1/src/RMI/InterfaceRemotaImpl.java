/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RMI;

/**
 * Esta clase implementa (trata la logica de los metodos declarados en la
 * clase-interface InterfaceRemota que se encuentra en el paquete Interfaces)
 *
 * @author elzevir
 */
import Controlador.ControladorPrincipal;
import Interfaces.InterfaceRemota;
import Modelo.Autenticador;
import Modelo.Cifrador;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class InterfaceRemotaImpl extends UnicastRemoteObject implements InterfaceRemota {

    //ControladorPrincipal cPrincipal;
    String usuario;
    String pass;
    String salida;

    public InterfaceRemotaImpl() throws RemoteException {
        super();
    }

    public int Login(String usuario, String pass, String IP, int Puerto)
            throws RemoteException {
        Autenticador aut = new Autenticador();
        String salida;
        try {


                // System.out.println(usuario+"  => " + pass +" - "+ IP +" : "+ Puerto);
        ControladorPrincipal.agregaEvento(usuario + " Desde "+ IP +" : "+ Puerto+"\n");

        this.salida = aut.procesaMensaje(usuario);
        ControladorPrincipal.agregaEvento("<Paso 2> "+this.salida + " Hacia "+ IP +" : "+ Puerto+"\n");
        //ControladorMensajes.cambiarAmigo(IP,Puerto+1);
//		ControladorMensajes.agregarTexto(usuario+" ha iniciado sesión.\n\n");
		RMI.setNombre(usuario);
        return 0;

        }
        catch (Exception e){
            e.printStackTrace();
            return 3;
        }

    }

  /*  public int EnviarMensaje(String usuario, String mensaje)
            throws RemoteException {
	try{
	    if(!RMI.nombre.equals("")){
		Calendar calendario = new GregorianCalendar();

		Cifrador c = new Cifrador();
		//System.out.println("Clave: "+ControladorMensajes.clave);
		c.setClave(ControladorMensajes.clave);
		c.setVi("asdf");
		c.setTexoCifrado(mensaje);
		c.desEncripta();
		String textoClaro = c.getTextoClaro();

		ControladorMensajes.agregarDesencriptar(c.getReporte());
		ControladorMensajes.agregarTexto(usuario+" dice ("+calendario.get(Calendar.HOUR_OF_DAY)+":"+calendario.get(Calendar.MINUTE)+"):\n  "+textoClaro+"\n");
	    }
	    else{
		ControladorMensajes.agregarTexto("¡No hay nadie conectado!\n\n");
	    }
	    return 0;
	}
	catch(Exception e){
	    e.printStackTrace();
	    return 1;
	}
    }
*/
    public String getUsuario() throws RemoteException {
        return usuario;
    }

    public void setUsuario(String usuario) throws RemoteException {
        this.usuario = usuario;
    }

    public String getPass() throws RemoteException {
        return pass;
    }

    public void setPass(String pass) throws RemoteException {
        this.pass = pass;
    }

    public void cerrarSesion(String usuario) throws RemoteException {
	//ControladorMensajes.agregarTexto(usuario+" ha cerrado sesión.\n\n");
	RMI.setHostName("localhost");
	RMI.setNombre("");
    }

    public int EnviarMensaje(String usuario, String mensaje) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSalida() throws RemoteException {
        return this.salida;
    }

}
