/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

/**
 *
 * @author elzevir
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRemota extends Remote {
    public int Login(String usuario, String pass, String IP, int PuertoPropio)
            throws RemoteException;
    public void cerrarSesion(String usuario)
	    throws RemoteException;
    public int EnviarMensaje(String usuario, String mensaje)
            throws RemoteException;
    public String getUsuario()
            throws RemoteException;
    public void setUsuario(String usuario)
            throws RemoteException;
    public String getPass()
            throws RemoteException;
    public void setPass(String pass)
            throws RemoteException;
     public String getSalida() throws RemoteException;


}
