/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

/**
 *
 * @author elzevir
 */
public class Usuario {

    private String user;
    private String pass;

    public Usuario() {

    }

    public Usuario(String u,String p)
    {
        this.user = u;
        this.pass = p;
    }

   public Vector cargarListaUsuario()
    {
        Vector usuarios = new Vector();

        try
        {
            String buffer;
            String datos[];
            FileReader fr = new FileReader("usuarios.txt");
            BufferedReader br = new BufferedReader(fr);

            while((buffer = br.readLine())!= null)
            {
                datos = buffer.split(",");
                usuarios.add(new Usuario(datos[0],datos[1]));
            }

             br.close();
            fr.close();


        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            usuarios = null;
        }finally
        {
            return usuarios;

        }
    }

    public void agregarUsuario(String nombre, String pass)
    {
        try
        {
            String buffer;
            FileWriter fw = new FileWriter("usuarios.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nombre + "," +pass);
            bw.newLine();

            bw.close();
            fw.close();


        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());

        }


    }



    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }




}
