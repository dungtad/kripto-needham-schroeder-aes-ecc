/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

/**
 *
 * @author elzevir
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jonnatan
 */
public class Punto {

    private int x;
    private int y;

    public Punto() {
    }
    public Punto(int a, int b)
    {
        this.x =a;
        this.y = b;
    }

    /*multiplica un punto por un escalar k*/
    /*
     Funcion que: Multiplica un punto por un escalar k
     * ParÃ¡metros: El escalar k, el punto a multiplicar, el parÃ¡metro p,a,b de la curva respectivamente
     * Salida: El punto resultante de la multiplicacion
     */
    public Punto mult(int k,Punto q,int p, int a,int b)
    {
        Punto s;

        //primera suma
        s = suma(q,q,p,a,b);

     for(int i=3;i<=k;i++)
     {
        s = suma(s,q,p,a,b);
     }


        return s;

    }

    /*to String
     Funcion que: pasa el punto a una representacion en string
     * ParÃ¡metros: no requiere
     * Salida: El punto resultante en String
     */
    @Override
     public String toString()
     {
        String r = "("+this.x+","+this.y+")";
                return r;
     }

    /*Suma dos puntos*/
    /*
    Funcion que: Suma dos puntos, aplicando suma de iguales o distintos segun corresponda
     * ParÃ¡metros:  los pntos p1 y p2 a sumar, el parÃ¡metro p,a,b de la curva respectivamente
     * Salida: El punto resultante de la suma
     */
    public Punto suma(Punto p1, Punto p2, int p, int a, int b) {

        if((p1.x  == p2.x) &&(p2.y ==p1.y))
            return sumaIguales(p1,p,a,b);
        else
            return sumaDist(p1,p2,p,a,b);

    }


    /*suma distintos*/
    /*
    Funcion que: Suma dos puntos distintos
     * ParÃ¡metros:  los pntos p1 y p2 a sumar, el parÃ¡metro p,a,b de la curva respectivamente
     * Salida: El punto resultante de la suma
     */
    private Punto sumaDist(Punto p1, Punto p2, int p, int a, int b) {
        Punto res = new Punto();

        int lambda = mod((p2.y - p1.y) * inversoModP(p2.x - p1.x, p), p);
        int xs = mod((lambda * lambda - p1.x - p2.x), p);
        int ys = mod((lambda * (p1.x - xs) - p1.y), p);


        res.setX(xs);
        res.setY(ys);

        return res;
    }


    /*suma dos puntos iguales*/
    /*
    Funcion que: Suma dos puntos iguales
     * ParÃ¡metros:  el punto p1 a sumar consigo mismo, el parÃ¡metro p,a,b de la curva respectivamente
     * Salida: El punto resultante de la suma
     */
    private Punto sumaIguales(Punto q, int p, int a, int b) {
        Punto res = new Punto();

        int lambda = mod(((3 * q.x * q.x) + a) * inversoModP(2 * q.y, p), p);
        int xs = mod((lambda * lambda - 2 * q.x), p);
        int ys = mod((lambda * (q.x - xs) - q.y), p);

        res.setX(xs);
        res.setY(ys);

        return res;

    }

    /*inverso mod p*/
    /*
    Funcion que: enntregael inverso mod p de un numero x
     * ParÃ¡metros:  el numero x al cual se le busca el inverso y p que es el modulo
     * Salida: el entero resultante (inverso de x mod p)
     */
    private int inversoModP(int x, int p) {


        for (int i = 0; i < p; i++) {
            if (mod(x * i, p) == 1) {
                return i;
            }

        }

        return 0;

    }

    /*funcion modulo para negativos y positivos*/
    /*
    Funcion que: entrega el modulo de dos numeros, indistintamente si es negativo o positivo
     * ParÃ¡metros:  el entero x al cual se le quiere btener el modulo, y p (el modulo)
     * Salida: el entero resultante (x mod p)
     */
    private int mod(int x, int p) {
        if (x >= 0) {
            return (x % p);
        } else {
            return (x - (x * p)) % p;
        }

    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    public static void main(String args[])
    {
        Punto p1,p2;
        p1 = new Punto();
        p2 = new Punto();
        p1.setX(2);
        p1.setY(7);
        p2.x=5;
        p2.y=2;

        Punto p;
        //p = p1.suma(p1,p2,11,1,6);
        p = p1.mult(3,p1,11,1,6);
        System.out.println("el resultado es: ("+p.x+","+p.y+") " );

    }
}

