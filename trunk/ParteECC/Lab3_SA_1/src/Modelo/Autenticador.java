/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Estefania
 */
public class Autenticador {

    Usuario a;
    Usuario b;
    public Autenticador()
    {

    }
    /*
        Verificamos que el origen existe y su clave sea
     * la entregada
     */
    boolean verificarUserYPass(String dat)
    {
        Usuario nuevo = new Usuario();
        Vector lista = nuevo.cargarListaUsuario();
        String buff = dat.substring(4);
        String user = buff.split(":")[0];
        String pass = buff.split(":")[1];

        for(int i = 0;i < lista.size();i++ )
        {
            if(((Usuario)lista.get(i)).getUser().equals(user))
            {
                if((((Usuario)lista.get(i)).getPass().equals(pass)))
                {
                    this.a = (Usuario)lista.get(i);
                    return true;
                }
            }
        }


        return false;

    }

    boolean verificarDestino(String b)
    {
        Usuario nuevo = new Usuario();
        Vector lista = nuevo.cargarListaUsuario();
        String buff = b.substring(4);
        buff = buff.trim();


        for(int i = 0;i < lista.size();i++ )
        {
            if(((Usuario)lista.get(i)).getUser().equals(buff))
            {
                    this.b = (Usuario)lista.get(i);
                    return true;
            }
        }

        return false;
    }



   public String procesaMensaje(String msg)
    {
        /*Inicializamos el cifrador*/
        Cifrador coder = new Cifrador();
        coder.setVi("1234567890"); /*Vector de Inicializacion para el CBC*/

        /*Fin de incializacion del cifrador AES con CBC*/

        String paso2 ="";
        String buff,datos[];
        buff = msg.substring(9);
        //separamos el origen, destino y Nonce (Ia)
        datos = buff.split(",");
        
        if(!verificarUserYPass(datos[0]))
        {
            paso2 = "<Error>: Usuario/Password Origen incorrectas";
        }else if(!verificarDestino(datos[1]))
        {
            paso2 = "<Error>: Usuario Destino incorrecto";
        }else
        {//se arma la respuesta del servidor
            paso2 = "<Paso 2> "+ datos[2]/*Nonce Ia*/ +","+datos[1]/*B*/+",";
            String ck = generaClaveSesión();
            paso2 += ck; //agragamos la clave de sesion
            coder.setClave(this.b.getPass());/*Clave del destino B*/
            coder.setTextoClaro(ck+","+this.a.getUser());//encriptamos Ck,A- clave de sesion y origen
            coder.encriptar(); //encriptamos
            paso2 +=","+coder.getTexoCifrado();
            //System.out.println(coder.getTexoCifrado());

            //encriptamos todo el mensaje con la clave del origen
            coder.setClave(this.a.getPass());
            coder.setTextoClaro(paso2);
            coder.encriptar();

            //listo para enviar.
            paso2 = coder.getTexoCifrado();
        }
        return paso2;
    }

   
    public static void main(String args[])
    {

        

    }

    /*Generacion de clave aleatoria*/
    private String generaClaveSesión() {
        Calendar calendario = new GregorianCalendar();
        Random r = new Random();
        r.setSeed(calendario.get(Calendar.MILLISECOND));

        return String.valueOf(r.nextInt());
    }

}
