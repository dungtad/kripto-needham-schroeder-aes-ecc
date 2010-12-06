package RMI;

//import Controlador.ControladorMensajes;
import Controlador.ControladorPrincipal;
import Interfaces.LlamadaRemotaInterface;
import Modelo.AutenticadorTerminal;
import Modelo.Cifrador;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LlamadaRemotaTerminal1 extends UnicastRemoteObject implements LlamadaRemotaInterface {

    String usuario;
    String pass;
    String Ib;
    String IbCifrado;

    public String getIb()
    {
        return this.Ib;
    }
    public String getIbCifrado()
    {
        return this.IbCifrado;
    }

    public LlamadaRemotaTerminal1() throws RemoteException {
        super();
    }

    public int Login(String usuario, String pass, String IP, int Puerto)
            throws RemoteException {
        try {
        String retorno;
        String partes[];
        AutenticadorTerminal auten = new AutenticadorTerminal();


        //autenticar
        ControladorPrincipal.agregaEvento(usuario + " Desde "+ IP +" : "+ Puerto+"\n");
        retorno = auten.procesarMensaje(usuario);
        ControladorPrincipal.agregaEvento("<Paso 3> "+ retorno + " Desde "+ IP +" : "+ Puerto+"\n");
        partes = auten.descomponeMensaje("123456789"+retorno);
        ControladorPrincipal.agregaEvento("Ck = "+partes[0]+"\n");
        ControladorPrincipal.agregaEvento("Origen = "+partes[1]+"\n");
        //nonce Ib
        this.Ib = auten.generarNonce();
        this.IbCifrado = auten.cifrarNonce(partes[0],this.Ib);

        
        ControladorPrincipal.agregaEvento("<Paso 4> Ib ="+this.Ib+ " Hacia "+ IP +" : "+ Puerto+"\n");
         ControladorPrincipal.agregaEvento("<Paso 4> Ib ="+this.IbCifrado+ " Hacia "+ IP +" : "+ Puerto+"\n");;
        //falta cifrar con Clave de sesion
        //ciframos con la clave de sesion

        


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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean finAutenticación(String ib) throws RemoteException {
       if(this.Ib.equals(String.valueOf(Long.parseLong(ib)+1)))
       {
            ControladorPrincipal.agregaEvento("<Paso 5>: Ib - 1 = " + ib);
            ControladorPrincipal.agregaEvento("<Paso 5>: Ib Válido" );
            ControladorPrincipal.agregaEvento("Autenticación completada" );
           return true;
       }
       else
       {
           ControladorPrincipal.agregaEvento("<Paso 5>: Ib - 1 = " + ib);
            ControladorPrincipal.agregaEvento("<Paso 5>: Ib Inválido" );
            ControladorPrincipal.agregaEvento("Autenticación corrupta" );
           return false;
       }
    }

}
