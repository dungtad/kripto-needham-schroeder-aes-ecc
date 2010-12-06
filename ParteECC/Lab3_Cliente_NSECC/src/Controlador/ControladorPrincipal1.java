/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import Interfaces.LlamadaRemotaInterface;
import Modelo.AutenticadorTerminal;
import Vista.VistaTerminal;
import java.net.InetAddress;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import javax.swing.JFrame;
import RMI.*;
/**
 *
 * @author estefania
 */
public class ControladorPrincipal1 {



private VistaTerminal vista;
    private Registry registro;
    static ControladorPrincipal1 nuevo;
    String nonce;
    private LlamadaRemotaInterface remota2;


     public static void agregaEvento(String string) {
       nuevo.vista.addEvento(string );
    }

    public void bajarServicio()
    {
        try{
            this.registro.unbind("LlamadaRemotaTerminal");
         }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    public static String[] datos()
    {
        return nuevo.vista.datos();
    }
    public void enviarMensaje(String msg,String ip,String port)
    {
        String retorno;
        String partes[]=null;
        AutenticadorTerminal auten = new AutenticadorTerminal();

        try{
            InetAddress address = InetAddress.getLocalHost();
            int temp = address.toString().indexOf("/");
            String IPPropia = address.toString().substring(temp+1);

            //seteo la direccion IP del servidor y el puerto
            RMI.setHostName(ip);
            RMI.setRMIPort(Integer.valueOf(port));

            LlamadaRemotaInterface remota = null;
            remota = RMI.getLlamadaRemota(remota);
            remota.Login(msg,msg,IPPropia,Integer.valueOf(port));
            vista.addEvento(msg +"Hacia "+ ip );
            retorno = remota.getSalida();
            vista.addEvento(retorno +"Desde "+ ip );
            //procesar el mensaje recibido
            retorno = auten.descifrar(vista.datos()[0], vista.datos()[1],retorno);
            //mensaje descifrado
            vista.addEvento(retorno);
            
            partes = auten.descomponeMensaje(retorno);
            System.out.println(this.nonce);
            System.out.println(partes[0].substring(5));
           
            if(partes[0].substring(5).trim().equals(this.nonce))
                vista.addEvento("Nonce (Ia) Válido");
            else
            {
               vista.addEvento("Nonce (Ia) Inválido");
               ControladorPrincipal1.agregaEvento("Autenticación corrupta" );
               return;
            }

            if(partes[1].substring(5).equals(vista.datos()[2]))
              vista.addEvento("Destino (B) Válido");
            else
            {
               vista.addEvento("Destino (B) Inválido");
               ControladorPrincipal1.agregaEvento("Autenticación corrupta" );
               return;
            }

            vista.addEvento("Ck = " + partes[2]);
            vista.addEvento("<Paso 3> Enviado a B: {Ck,A}Kb");
            
            
            //enviando mensaje a B
            /*Paso 3 enviar: {Ck,A}Kb*/
           retorno =  this.enviarMensajeB("<Paso 3> "+partes[3]/*{Ck,A}Kb*/, ip, "1235");
           //desencriptamos el Nonce de B (Ib)
           
           String paso4  = auten.descifrarNonce(partes[2]/*ck*/,retorno/*{Ib}Ck*/);
           String Ib =paso4.substring(0, paso4.indexOf('#'));
           vista.addEvento("<Paso 4> Ib = " + Ib+"\n");

            /*Paso 5  enviar : {Ib-1}Ck*/
            
           vista.addEvento("<Paso 5> Ib-1 = " + String.valueOf(Long.parseLong(Ib)-1));
           if(this.remota2.finAutenticación(String.valueOf(Long.parseLong(Ib)-1))){

            ControladorPrincipal1.agregaEvento("<Paso 5>: Ib Válido" );
            ControladorPrincipal1.agregaEvento("Autenticación completada" );
           }else
           {
            
            ControladorPrincipal1.agregaEvento("<Paso 5>: Ib Inválido" );
            ControladorPrincipal1.agregaEvento("Autenticación corrupta" );
           }
            
        }catch(Exception ex)
        {
            ex.printStackTrace();
            vista.addEvento("Error de autenticación: ...\n");
            ControladorPrincipal1.agregaEvento("Autenticación corrupta" );

        }
        
    }

      public String enviarMensajeB(String msg,String ip,String port)
    {
        String retorno;
         AutenticadorTerminal auten = new AutenticadorTerminal();

        try{
            InetAddress address = InetAddress.getLocalHost();
            int temp = address.toString().indexOf("/");
            String IPPropia = address.toString().substring(temp+1);

            //seteo la direccion IP del servidor y el puerto
            RMI.setHostName(ip);
            RMI.setRMIPort(Integer.valueOf(port));

            this.remota2 = null;
            this.remota2 = RMI.getLlamadaRemotaTerminal(this.remota2);
            this.remota2.Login(msg,msg,IPPropia,Integer.valueOf(port));
            vista.addEvento(msg +"Hacia "+ ip );
            retorno = this.remota2.getIbCifrado();
            
            vista.addEvento("<Paso 4> {Ib}Ck = "+retorno +"Desde "+ ip );
            //desifrar Ib con Ck

            
            return retorno;

            //retorno = remota.getSalida();
            //vista.addEvento(retorno +"Desde "+ ip );

        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            vista.addEvento("Error en el Paso anterior: ...\n");
            return null;
        }

    }
      public boolean levantarServicio()
{

     RMI.setRMIPort(1235);
    // RMI.setHostName("localhost");

     try
     {

        registro = RMI.iniciarRegistro();
        RMI.setLlamadaRemotaTerminal(registro);

        LlamadaRemotaInterface llamada = null;
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
    vista = new VistaTerminal(this);
   


}

//Esto debiera ir una capa más abajo pero 
//se dejará así
public String generarNonce()
{
        Calendar calendario = new GregorianCalendar();
        Random nonce = new Random();
        nonce.setSeed(calendario.get(Calendar.MILLISECOND));
        this.nonce = String.valueOf(nonce.nextLong());
        return this.nonce;
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame.setDefaultLookAndFeelDecorated(true);
         nuevo = new ControladorPrincipal1();
        nuevo.levantarVista();


    }

}
