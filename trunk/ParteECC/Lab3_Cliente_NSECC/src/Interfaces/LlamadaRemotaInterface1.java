package Interfaces;

//import Controlador.ControladorPrincipal;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LlamadaRemotaInterface1 extends Remote {
    public int Login(String usuario, String pass, String IP, int PuertoPropio)
            throws RemoteException;
   
    public String getSalida() throws RemoteException;
    public String getIb() throws RemoteException;
     public String getIbCifrado() throws RemoteException;
     public boolean finAutenticación(String ib) throws RemoteException;


}
