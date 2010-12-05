/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Interfaces.InterfaceRemota;
import Modelo.Usuario;
import RMI.RMI;
import Vista.VistaPrincipal;
import java.awt.Color;
import java.rmi.registry.Registry;

/**
 * ControladorPrincipal: Esta clase se encarga de inicializar las vistas
 * para invocar a los modelos encargados de la transmision de los datos RMI
 * y las funciones que se necesitan para el server
 * @author estefania
 */
public class ControladorPrincipal {

    static ControladorPrincipal nuevo;
static private VistaPrincipal vista;
private Registry registro;

    public static  void agregaEvento(String string) {
       vista.agregaEvento(string);
    }

    public void agregarUsuario(String user, String pass1)
    {
        Usuario newUser   = new Usuario();
        newUser.agregarUsuario(user, pass1);
     }

    public void cargarUsuarios()
    {
        Usuario newUser   = new Usuario();
        vista.cargarUsers(newUser.cargarListaUsuario());

    }

public boolean levantarServicio()
{

     RMI.setRMIPort(1234);
    // RMI.setHostName("localhost");

     try
     {

        registro = RMI.iniciarRegistro();
        RMI.setLlamadaRemota(registro);

        InterfaceRemota llamada = null;
        llamada = RMI.getLlamadaRemota(llamada);

        return true;

     }
     catch(Exception ex)
     {
         System.out.println(ex.getMessage());
         return false;
     }
}
public void levantarVista()
{
    vista = new VistaPrincipal(this);

    if(this.levantarServicio())
        vista.setEstado("En ejecución ", Color.GREEN);
    else
        vista.setEstado("Detenido ", Color.RED);

    this.cargarUsuarios();
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       // JFrame.setDefaultLookAndFeelDecorated(true);
          nuevo = new ControladorPrincipal();
        nuevo.levantarVista();


    }


}
