/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import Controlador.ControladorPrincipal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 *
 * @author estefania
 */
public class AutenticadorTerminal1 {

    Cifrador coder ;

    public AutenticadorTerminal1()
    {
        coder = new Cifrador();
    }

    public String cifrarNonce(String clave,String nonce) {

        coder = new Cifrador();
        coder.setVi("1234567890");
        coder.setClave(clave);
        coder.setTextoClaro(nonce);
       // System.out.println("Nonce a cifrar: "+coder.getTextoClaro());
        coder.encriptar();

        return coder.getTexoCifrado();
    }


    public String descifrar(String user,String pass,String msg)
    {
        try{
        coder.setVi("1234567890");
        coder.setClave(pass);
        coder.setTexoCifrado(msg);
        //    System.out.println(msg);
        coder.desEncripta();

        return coder.getTextoClaro();
        }catch(Exception ex)
        {
                ex.printStackTrace();
                return null;
        }
    }

    public String descifrarNonce(String pass, String nonce) {
        coder = new Cifrador();
        coder.setVi("1234567890");
        coder.setClave(pass);
        coder.setTexoCifrado(nonce);
        //System.out.println(coder.getTexoCifrado());
        coder.desEncripta();
        //System.out.println("texto en claro paso 4: "+coder.getTextoClaro());
        
        return coder.getTextoClaro();

    }
    public String generarNonce()
{
        Calendar calendario = new GregorianCalendar();
        Random nonce = new Random();
        nonce.setSeed(calendario.get(Calendar.MILLISECOND));

        return String.valueOf(nonce.nextLong());
}

    public String[] descomponeMensaje(String msg)
    {
        String buff;
        String partes[];
        buff = msg.substring(9);
        partes = buff.split(",");
        return partes; //    [0] Nonce ; [1]; B; [2] Ck ; [3]{Ck,B}Kb
    }

    public String procesarMensaje(String msg)
    {
        String buff = msg.substring(9);
        String cifrado = (String) buff.subSequence(0, buff.indexOf('#'));
        coder.setVi("1234567890");
        coder.setClave(ControladorPrincipal.datos()[1]);
        coder.setTexoCifrado(cifrado);
        coder.desEncripta();

        return coder.getTextoClaro();
    }

}
