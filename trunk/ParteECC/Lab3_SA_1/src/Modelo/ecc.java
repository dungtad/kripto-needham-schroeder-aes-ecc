/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

/**
 *
 * @author elzevir
 */


import java.util.Vector;


public class ecc {
    private int p;
    private int a;
    private int b;
    private Punto G;
    private Punto Q;
    private int clavePrivada;
    private Punto c1,c2,c3;
    private Vector puntos;
    private String reporte;


    public ecc()
    {

    }

    public static void main(String args[])
    {

        ecc   modelo = new ecc();
        modelo.setA(Integer.valueOf("6"));
        modelo.setB(Integer.valueOf("5"));
        modelo.setP(Integer.valueOf("31"));
        modelo.setG(Integer.valueOf("22"), Integer.valueOf("11"));
        modelo.setClavePrivada(Integer.valueOf("10"));
        //modelo.setC2(new Punto(Integer.valueOf("12"),Integer.valueOf("21")));
        modelo.generaClavePublica();
        modelo.generarPuntos();
        modelo.inicializarReporte();

        System.out.println(modelo.validaClave(90));
        }


    /*Encriptar */
    /* Funcion que realiza el proceso de encriptado:
     * Parametros: el nuÃºmero aleatorio k, y el Pm (texto en claro) a encriptar.
     * Salida: Deja el texto encriptado, en las variables de clase c1y c2 (Puntos)
     */
    public void encriptar(int k,Punto Pm)
    {
         Punto temp;
         inicializarReporte();
         this.reporte +="Cm = {k*G, Pm + k*Q}\n";
         this.reporte +="Pm="+Pm.toString()+"\n";
         c1 = G.mult(k, G, p, a, b);
         registraOperacion(" * ",k,G,c1,"c1 = k*G =");
         this.reporte += "c2 = Pm + k*Q\n";
         temp = G.mult(k, Q, p, a, b);
         registraOperacion(" * ",k,Q,temp,"k*Q = ");
         c2 = Pm.suma(Pm, temp, p, a, b);
         registraOperacion(" + ",Pm,temp,c2,"c2 = ");
         this.reporte+= "El texto cifrado es: Cm = {"+c1.toString() +"; "+c2.toString()+"\n\n";
     }

    /*Desencriptar*/
    /* Funcion que realiza el proceso de desencriptado:
     * Parametros: el nuÃºmero aleatorio k, y el Punto cifrado .
     * Salida: Deja el texto en claro(punto), en las variables de clase c3.
     */
     public void desencriptar(int k,Punto cifrado)
     {
        Punto res;
        Punto temp;
        inicializarReporte();
        this.reporte +="Pm = {c2  - k*n*G }\n";
         this.reporte +="c2="+cifrado.toString()+"\n";
        temp = cifrado.mult(k, G, p, a, b);
        registraOperacion(" * ",k,G,temp,"k*G = ");
        res = temp.mult(this.clavePrivada, temp, p, a, b);
        registraOperacion(" * ",this.clavePrivada,temp,res,"n*k*G = ");

        this.c3 = resta(cifrado,res);
        registraOperacion(" - ",cifrado,res,this.c3,"Pm = ");
         this.reporte+= "El texto en claro  es: Pm = {"+c3.toString() +"\n\n";

     }
     /*registraOperacion*/
     /*
      Funcion que : Guarda en el string reporte, la operacion, operandos y resultado de una "operacion cualquiera"
      * parÃ¡metros: la operacion, los operandos, y el resultado
      * Salida: la salida se va ala variable reporte, para luego mostrarla.
      */
     private void registraOperacion(String op,Object op1,Object op2,Object res,String res1)
     {
        this.reporte += "\t "+res1+res.toString()+ " = " +op1.toString()+op+op2.toString()+"\n\n";
     }
     /* Iniciliza el reporte con los datos la curva*/
     /*
      Funcion que : Inicializa el reporte con los parÃ¡metros de la curva
      * parÃ¡metros:No necesita, dado que los datos que utiliza deben estar previamente seteado en el objeto
      * Salida: Lo guarda en reporte
      */
     private void inicializarReporte()
     {
        this.reporte  = "\n\t\tProceso de cifrado/descifrado usando Ecuaciones de Curva ElÃ­ptica\n\n";
        this.reporte += "ParÃ¡metros de la curva :\n\n";
        this.reporte += "\tOrden de la curva (p) = " + this.getP() +"\n\n";
        this.reporte += "\tParÃ¡metro a = " + this.getA() + "\n\n";
        this.reporte += "\tParÃ¡metro b = " + this.getB() + "\n\n";
        this.reporte += "\tClave privada(n)  = " + this.getClavePrivada() + "\n\n";
        this.reporte += "\tClave pÃºblica (Q)= (" + this.getQ().getX() + ","+this.getQ().getY() + ")\n\n";
        this.reporte += "\tPunto generador (G) = (" + this.getG().getX() + ","+this.getG().getY() + ")\n\n";
        this.reporte += "La ecuacion queda como: y^2 = (x^3 + "+this.getA()+"*x +"+this.getB()+ ") mod "+this.p+"\n\n";



     }

     /*Pertenece: */
     /*
      Funcion que : dado un punto, verifica si este pertenece o no a la curva
      * parÃ¡metros:el punto
      * Salida:true si pertenece, false en otro caso
      */
     public boolean pertenece(Punto a)
     {
        this.generarPuntos();

        for(int i=0;i<this.puntos.size();i++)
        {
            if((a.getX()==((Punto)this.puntos.get(i)).getX()) && (a.getY()==((Punto)this.puntos.get(i)).getY()))
                return true;
        }

        return false;
     }

     /*verifica la validez de los parÃ¡metros*/
     /*
      Funcion que :Verifica si los parametrso a,b,p, de la curva son validos
      * parÃ¡metros: los datos utilizados deben estar previamente seteados en el objeto
      * Salida:true si es valida, false en otro caso
      */
     public boolean curvaValida()
     {
        int temp= 4*this.a*this.a*this.a + 27*this.b*this.b;
        temp = temp % this.p;

        if(temp != 0)
            return true;
        else
            return false;
     }
     /*Resta entre dos puntos dentro de la curva*/
     /*
      Funcion que: resta dos puntos dentro de la curva
      * ParÃ¡metros: a y b, los dos puntos a restar, resta a - b
      * Salida: retorna un punto, que es la resta de a-b
      */
     private Punto resta(Punto a,Punto b)
     {
        Punto res = b;
        b.setY(b.getY()*-1);

        return res.suma(a, res, p, this.a,this.b);

     }
    /*Calcula clave publica*/
     /*
      Funcion que: genera la clave publica de la curva
      * ParÃ¡metros: los datos utilzados deben estar previemante  seteado en la claso
      * Salida: guarda la clave publica en la variable Q de la clase
      */
    public void generaClavePublica()
    {
       // System.out.println(this.clavePrivada + " - "+this.G.getX()+ "- "+this.G.getY());
        //System.out.println(this.p + "- "+ this.a+ "- "+this.b);

        this.Q = this.G.mult(this.clavePrivada, this.G, this.p, this.a, this.b);
    }
    /*valida clave*/
    /*
      Funcion que: Verifica si la clave privada ingresa es valida
      * ParÃ¡metros: la clave como entero
      * Salida: true si es valida, false en otro caso
      */
    public boolean validaClave(int clave)
    {
        if((clave>0) && (clave<this.puntos.size()))
            return true;
        else return false;
    }
   /*Genera puntos*/
    /*
      Funcion que: genera todos los puntos que pertenece a la curva
      * ParÃ¡metros: no requiere
      * Salida: almacena los puntos, en un vector de la clase llamado "puntos"
      */
    private void generarPuntos()
    {
        Vector x = new Vector();
        Vector y = new Vector();
        this.puntos = new Vector();
        Object temp;

        for(int i=0;i<this.p;i++)
        {
           x.add((i*i*i + this.a*i + this.b)%this.p);
           y.add((i*i)%this.p);


        }

        for(int i=0;i<this.p;i++)
        {
            temp = x.get(i);

            for(int j=0;j<this.p;j++)
            {
                if(temp.equals(y.get(j)))
                    this.puntos.add(new Punto(i,j));


            }

        }


    }





    /**
     * @return the p
     */
    public int getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(int p) {
        this.p = p;
    }

    /**
     * @return the a
     */
    public int getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(int a) {
        this.a = a;
    }

    /**
     * @return the b
     */
    public int getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(int b) {
        this.b = b;
    }

    /**
     * @return the G
     */
    public Punto getG() {
        return G;
    }

    /**
     * @param G the G to set
     */
    public void setG(int x,int y) {
        this.G = new Punto();
        this.G.setX(x);
        this.G.setY(y);
    }

    /**
     * @return the Q
     */
    public Punto getQ() {
        return Q;
    }

    /**
     * @param Q the Q to set
     */
    public void setQ(Punto Q) {
        this.Q = Q;
    }

    /**
     * @return the clavePrivada
     */
    public int getClavePrivada() {
        return clavePrivada;
    }

    /**
     * @param clavePrivada the clavePrivada to set
     */
    public void setClavePrivada(int clavePrivada) {
        this.clavePrivada = clavePrivada;
    }






     public Punto getC3() {
        return c3;
    }

    /**
     * @return the c1
     */
    public Punto getC1() {
        return c1;
    }

    /**
     * @param c1 the c1 to set
     */
    public void setC1(Punto c1) {
        this.c1 = c1;
    }

    /**
     * @return the c2
     */
    public Punto getC2() {
        return c2;
    }

    /**
     * @param c2 the c2 to set
     */
    public void setC2(Punto c2) {
        this.c2 = c2;
    }

    /**
     * @return the reporte
     */
    public String getReporte() {
        return reporte;
    }
}
