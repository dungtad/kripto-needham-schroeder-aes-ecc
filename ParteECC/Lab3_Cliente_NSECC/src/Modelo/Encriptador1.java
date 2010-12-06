/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author estefania
 */
public class Encriptador1 {

    private String mensajeEnClaro[][];
    private String mensajeCifrado[][];
    private String clave[][];
    private final int dim = 4;
    private int ronda;
    private String reporte;
    

    /*Constructor*/
    /*bloque de 128 bits, y clave de 128 bits, por lo tanto
    de largo 16 letras, y con 10 rondas de procesamiento*/
    public Encriptador1() {
        mensajeEnClaro = new String[dim][dim];
        mensajeCifrado = new String[dim][dim];
        clave = new String[dim * 11][dim];//once rondas
        ronda = 0;
        reporte = "\t\tReporte de Rijndael\n";
    }

    /*Operaciones primaria de AES*/
    /*AddRoundKey*/
    // Aplica la operacion addroundkey, definida por rijndael, utilizando
    // la  i-esima clave, dependiendo de la ronda en que se
    //encuentre el algoritmo
    private void addRoundKey(int r) {
        String resultadoXor;
        String buffer[][] = new String[4][4];
        //buffer = mensajeCifrado;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                mensajeCifrado[i][j] = xor(mensajeCifrado[i][j], Integer.toString(Integer.parseInt((clave[j + (r * 4)][i]), 16)));
            //if(mensajeCifrado[i][j].length()<2) mensajeCifrado[i][j]="0"+mensajeCifrado[i][j];
            }
        }
    }
    /*ShitRow*/
   // operacion shiftrow, desplaza las filas del
   //texto cifrado segun corresponda
    private void shiftRow() {
        //la primera fila queda intacta
        //lasegunda 1 desplazamiento
        this.mensajeCifrado[1] = desplazaString(3, this.mensajeCifrado[1]);
        this.mensajeCifrado[2] = desplazaString(2, this.mensajeCifrado[2]);
        this.mensajeCifrado[3] = desplazaString(1, this.mensajeCifrado[3]);
     }

    /*MixColumns*/
    //operacion mix columns del algoritmos rijnadeal
    //trabaja directamente sobre el texto cifrado (variable de la clase)
    // por lo tanto no recibe parametros
    private void mixColumns() {
        String buff[] = new String[dim];
        String aux[] = new String[dim];
        int x1, x2, x3, x4;
        //inicializamos los parametros de la columna
        for (int j = 0; j < dim; j++) {
            for (int i = 0; i < dim; i++) {
                aux[i] = Integer.toHexString(Integer.valueOf(this.mensajeCifrado[i][j]));
                if (aux[i].length() < 2) {
                    aux[i] = "0" + aux[i];
                }
            // System.out.print(aux[i]+"  ");
            }
            //System.out.println();



            //multiplicamos por la matriz
            x1 = Integer.parseInt(multG("02", aux[0]), 16) ^ Integer.parseInt(multG("03", aux[1]), 16) ^ Integer.parseInt(multG("01", aux[2]), 16) ^ Integer.parseInt(multG("01", aux[3]), 16);
            x2 = Integer.parseInt(multG("01", aux[0]), 16) ^ Integer.parseInt(multG("02", aux[1]), 16) ^ Integer.parseInt(multG("03", aux[2]), 16) ^ Integer.parseInt(multG("01", aux[3]), 16);
            x3 = Integer.parseInt(multG("01", aux[0]), 16) ^ Integer.parseInt(multG("01", aux[1]), 16) ^ Integer.parseInt(multG("02", aux[2]), 16) ^ Integer.parseInt(multG("03", aux[3]), 16);
            x4 = Integer.parseInt(multG("03", aux[0]), 16) ^ Integer.parseInt(multG("01", aux[1]), 16) ^ Integer.parseInt(multG("01", aux[2]), 16) ^ Integer.parseInt(multG("02", aux[3]), 16);



            //System.out.println(x1+ " "+ x2+ " "+x3+ " "+x4+ " .");
            //System.out.println(Integer.toHexString(x1) + " "+ Integer.toHexString(x2)+ " "+Integer.toHexString(x3)+ " "+Integer.toHexString(x4)+ " .");
            //System.out.println("*****************************");
            this.mensajeCifrado[0][j] = Integer.toHexString(x1);
            this.mensajeCifrado[1][j] = Integer.toHexString(x2);
            this.mensajeCifrado[2][j] = Integer.toHexString(x3);
            this.mensajeCifrado[3][j] = Integer.toHexString(x4);

        }

    }
    //multiplicacion en galois
    /*
        Multiplicacion de dos numeros Hexadecimales en el campo Galois
     * Utiliza la L-tables y la E-table
     */
    private String multG(String a, String b) {
        String suma, resultado, q, z = "00";
        //buscamos en la L-table
        int aux = (Integer.parseInt(L(a), 16) + Integer.parseInt(L(b), 16)) % 255;
        //buscamos en la E- table
        suma = Integer.toHexString(aux);
        if (suma.length() < 2) {
            suma = "0" + suma;
        }
        resultado = E(suma);

        q = resultado;
        if (Integer.parseInt(a, 16) == 0) {
            resultado = z;
        } else {
            resultado = q;
        }
        if (Integer.parseInt(b, 16) == 0) {
            resultado = z;
        } else {
            q = z;
        }



        return resultado;


    }

    //Dado un valor en hexadecimal, entrega su equivalente valor de la L-tabla
    private String L(String x) {
        return LTable[Integer.parseInt((x).substring(0, 1), 16)][Integer.parseInt((x).substring(1, 2), 16)];
    }
    //Dado un valor en hexadecimal, entrega su equivalente valor de la E-tabla
    private String E(String x) {
        return ETable[Integer.parseInt((x).substring(0, 1), 16)][Integer.parseInt((x).substring(1, 2), 16)];
    }

    /*ByteSub*/
    //Funcion des sustitucion de rijndael, trabaja en base a la S-caja
    // lo hace directamente sobre la variable mensajeCIfrado
   //no recibe parametros
    //Recibe un decimal y entrega en Decimal
    private void byteSub() {
        String a;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                a = (Integer.toHexString(Integer.valueOf(this.mensajeCifrado[i][j])));

                if (a.length() < 2) {
                    a = "0" + a;
                }

                this.mensajeCifrado[i][j] = s_box[Integer.parseInt((a).substring(0, 1), 16)][Integer.parseInt((a).substring(1, 2), 16)];
                this.mensajeCifrado[i][j] = String.valueOf(Integer.parseInt(this.mensajeCifrado[i][j], 16));
            }
        }
    }

    /******************************************************************

    Inversas
     *******************************************************************/
    /*addRoundKey, es su misma inversa*/

    /*Inversa shiftRow*/
    //Revierte los desplzamientos realizados por shiftRow
    private void InvShiftRow() {
        //la primera fila queda intacta
        //lasegunda 3 desplazamiento
        this.mensajeCifrado[1] = desplazaString(1, this.mensajeCifrado[1]);
        this.mensajeCifrado[2] = desplazaString(2, this.mensajeCifrado[2]);
        this.mensajeCifrado[3] = desplazaString(3, this.mensajeCifrado[3]);
    }

    /*inversa Byte Sub*/
    //Revierte la sustitucion realizaba por byteSub
    //trabaja en base a la s-caja inversa
    private void InvbyteSub() {
        String a;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                a = (Integer.toHexString(Integer.valueOf(this.mensajeCifrado[i][j])));

                if (a.length() < 2) {
                    a = "0" + a;
                }

                this.mensajeCifrado[i][j] = invSBox[Integer.parseInt((a).substring(0, 1), 16)][Integer.parseInt((a).substring(1, 2), 16)];
                this.mensajeCifrado[i][j] = String.valueOf(Integer.parseInt(this.mensajeCifrado[i][j], 16));
            }
        }
    }

    /*MixColumns*/
    //revierte la operacion Mixcolumns, tambien es una multiplicacion de columnas
    //utiliza la funcion multG, para realizar la multiplicacion
    //utiliza el xor, para realizar las sumas
    //trabaja directamente sobre el texto cifrado
    private void InversaMixColumns() {
        String buff[] = new String[dim];
        String aux[] = new String[dim];
        int x1, x2, x3, x4;
        //inicializamos los parametros de la columna
        for (int j = 0; j < dim; j++) {
            for (int i = 0; i < dim; i++) {
                aux[i] = Integer.toHexString(Integer.valueOf(this.mensajeCifrado[i][j]));
                if (aux[i].length() < 2) {
                    aux[i] = "0" + aux[i];
                }
            //System.out.print(aux[i]+"  ");
            }
            //System.out.println();


            //multiplicamos por la matriz
            x1 = Integer.parseInt(multG("0e", aux[0]), 16) ^ Integer.parseInt(multG("0b", aux[1]), 16) ^ Integer.parseInt(multG("0d", aux[2]), 16) ^ Integer.parseInt(multG("09", aux[3]), 16);
            x2 = Integer.parseInt(multG("09", aux[0]), 16) ^ Integer.parseInt(multG("0e", aux[1]), 16) ^ Integer.parseInt(multG("0b", aux[2]), 16) ^ Integer.parseInt(multG("0d", aux[3]), 16);
            x3 = Integer.parseInt(multG("0d", aux[0]), 16) ^ Integer.parseInt(multG("09", aux[1]), 16) ^ Integer.parseInt(multG("0e", aux[2]), 16) ^ Integer.parseInt(multG("0b", aux[3]), 16);
            x4 = Integer.parseInt(multG("0b", aux[0]), 16) ^ Integer.parseInt(multG("0d", aux[1]), 16) ^ Integer.parseInt(multG("09", aux[2]), 16) ^ Integer.parseInt(multG("0e", aux[3]), 16);


            this.mensajeCifrado[0][j] = Integer.toHexString(x1);
            this.mensajeCifrado[1][j] = Integer.toHexString(x2);
            this.mensajeCifrado[2][j] = Integer.toHexString(x3);
            this.mensajeCifrado[3][j] = Integer.toHexString(x4);

        }

    }

    /******************************************************************

    GENERACION DE LA CLAVE
     *******************************************************************/
    /*Funciones para la generacion de la clave*/
    //Operacion rotByte, desplaza un posicion los elementos de una columna de la clave
    //recibe cmo parametro la columna a desplazar
    //retorn la columna desplazada un posicion
    private String[] rotByte(String w[]) {
        String buff[] = new String[dim];
        for (int i = 0; i < dim; i++) {
            buff[i] = w[i];
        }
        return desplazaString(3, buff);
    }

    /*subByteKey*/
    //Trabaja identicamente que subByte, pero esta vez
    //las sustituciones se le realizan a una columna de la clave
    private String[] subByteKey(String w[]) {
        String aux[] = new String[4];

        for (int i = 0; i < dim; i++) {
            aux[i] = s_box[Integer.parseInt(w[i].substring(0, 1), 16)][Integer.parseInt(w[i].substring(1, 2), 16)];

        }

        return aux;
    }

    /*generando claves*/
    //algoritmo definido en Rijndael
    //para la generacion de claves
    public void generacionClave() {
        String temp[] = new String[4];


        //pasamos la clave a Hexagecimal
        this.DecToHexKey();

        /**generando el reporte**/
        this.reporte +="\n\n\t\t Generacion de la clave\n\n";
        this.reporte +="\n\t\tClave ronda inicial\n\n";
        this.reporte +=this.getKey(0);


        for (int i = 4; i < 44; i++) {

            temp = this.clave[i - 1];
            //this.imprimeKey(0);

            if ((i % 4) == 0) {
                //this.imprimeKey(0);
                //System.out.print("W["+i+"] = ");
                temp = xorKey(subByteKey(rotByte(temp)), rcon[(i / 4) - 1]);

            }


            //System.out.print("W["+i+"] = ");
            this.clave[i] = xorKey(this.clave[i - 4], temp);

            //reporte clave i
            if((i % 4) == 3)
            {
                this.reporte +="\n\t\tClave ronda "+(i/4)+"\n\n";
                this.reporte +=this.getKey((i/4));
            }
            
           }
                 //this.reporte +="\t\tClave ronda "+11+"\n";
                //this.reporte +=this.getKey(10);

    }

    /*funcion que pasa la clave de Decimal a Hex*/
    public void DecToHexKey() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.clave[i][j] = Integer.toHexString(Integer.valueOf((this.clave[i][j])));
                if (this.clave[i][j].length() < 2) {
                    this.clave[i][j] = "0" + this.clave[i][j];
                }
            }

        }

    }

    /*Funcion auxliar de shift row*/
    //desplaza un String i-posiciones
    //recibe el desplazamiento
    //el string a desplazar
    // y devuelve el string dezplazado
    private String[] desplazaString(int desplazamiento, String dato[]) {
        String buff[];
        buff = new String[desplazamiento];

        for (int i = 0; i < desplazamiento; i++) {
            buff[i] = dato[dim - i - 1];
        }


        for (int i = dim - 1; i > desplazamiento - 1; i--) {
            dato[i] = dato[i - desplazamiento];
        }

        for (int i = 0; i < desplazamiento; i++) {
            dato[i] = buff[buff.length - i - 1];
        }
        return dato;
    }

    /*funcion XOR entre dos strings que contiene 8 bits de informacion*/
    private String xor(String a, String b) {
        String resultado;

        resultado = String.valueOf(Integer.parseInt(a) ^ Integer.parseInt(b));

        return resultado;
    }

    /*funcion XOR para la clave, para realizar el xor con RCON*/
    private String[] xorKey(String a[], String b) {
        String resultado[] = new String[dim];

        for (int i = 0; i < dim; i++) {
            if (i == 0) {
                resultado[i] = Integer.toHexString(Integer.parseInt(a[i], 16) ^ Integer.parseInt(b, 16));
            // System.out.println(resultado[i]+ " = " +a[i] +" XOR "+b );
            } else {
                resultado[i] = a[i];
            //System.out.println(resultado[i]+ " = " +a[i] +" XOR "+b );
            }
        }
        return resultado;
    }

    //funcion xor para la clave, pero esta es generica, es decir
    //funciona con cualquier par de columnas
    // y entrega en una nueva columna los resultados
    private String[] xorKey(String a[], String b[]) {
        String resultado[] = new String[dim];

        for (int i = 0; i < dim; i++) {

            resultado[i] = resultado[i] = Integer.toHexString(Integer.parseInt(a[i], 16) ^ Integer.parseInt(b[i], 16));
            // System.out.print(resultado[i]);
            // System.out.println(" = " + a[i] +" XOR "+b[i] );
            if (resultado[i].length() < 2) {
                resultado[i] = "0" + resultado[i]; //se arregla run pqño bug cuando da un n°< 10
            }
        }
        return resultado;
    }

    /*Setea un bloque de texto en claro*/

    public void setBloqueTextoEnClaro(String texto) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.mensajeEnClaro[j][i] = String.valueOf((int) texto.charAt(i * dim + j));
            }
        }
    }
    /*Setea un bloque de texto cifrado*/

    public void setBloqueTextoCifrado(String texto) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.mensajeCifrado[j][i] = String.valueOf((int) texto.charAt(i * dim + j));
            }
        }
    }
    //para utilizar con CBC
    //setea el bloque de texto cifrado a partir de otro bloque
    public void setBloqueTextoCifrado( String x[][])
    {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.mensajeCifrado[j][i] =x[j][i];
            }
        }
        //this.mensajeCifrado = x;
    }
    //entrega una copia del bloque de texto cifrado como bloque 4x4(string)
    public String[][] getBloqueTextoCifradoMatriz()
    {
        String res[][] = new String[dim][dim];
        //copiamos el mensaje cifrado
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                res[j][i] =this.mensajeCifrado[j][i];
            }
        }


        //this.HexToDecText();
        return res;
    }
    /*Entrega el bloque de texto en hexadecimal*/
    public String getBloqueTextoCifrado()
    {
        String resultado="\t";
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
               resultado += Integer.toHexString(Integer.valueOf(this.mensajeCifrado[i][j])) + "  " ;
            }
                resultado += "\n\t";
        }
        return resultado;

    }


//  Pasa el bloque de texto cifrado de base hexadecimal a base decimal
    public void HexToDecText() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.mensajeCifrado[j][i] = String.valueOf(Integer.parseInt(this.mensajeCifrado[j][i], 16));
            }
        }
    }
/*
    public void setBloqueTextoCifrado() {
        int k = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {

                this.mensajeCifrado[j][i] = String.valueOf(k);
                k++;
            }
        }
    }
*/
    /*Setea la clave en el formato de la matriz que el algoritmo AES necesita*/
    public void setClave(String key) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.clave[j][i] = String.valueOf((int) key.charAt(j * dim + i));
            }
        }
    }
/*
    public void setClave() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.clave[j][i] = this.mensajeCifrado[i][j];
            }
        }
    }
*/
    //imprime el valor del texto cifrado en base decimal
    public void imprime() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(this.mensajeCifrado[i][j] + " ");
            }

            System.out.println();
        }

    }
//imprime el valor del texto cifrado en base hexadecimal
    public void imprimeEx() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(Integer.valueOf((this.mensajeCifrado[i][j]))) + " ");
            }

            System.out.println();
        }

    }
//imprime el valor de la clave de la ronda r-esima en hexadecimal
    public void imprimeExKey(int r) {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(Integer.valueOf((this.clave[j + (r * 4)][i]))) + " ");
            }

            System.out.println();
        }
    }
    
    
    /*Entrega la key en hexadecimal, contenida en un string de la ronda r-esima del algortimo*/
    public String getKey(int r) {

        String res="\t";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //res+= Integer.toHexString(Integer.valueOf((this.clave[j + (r * 4)][i]))) + " ";
                res+=this.clave[j + (r * 4)][i] + "  ";
            }

            res+="\n\t";
        }

        return res;
    }

//Imprime el valor de la clave correspondiente a la ronda r-esima en base decimal
    public void imprimeKey(int r) {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(this.clave[j + (r * 4)][i] + " ");
            }

            System.out.println();
        }

    }

    //entrage texto cifrado (un puntero) no una copia
    public String[][] getTextoCifradoHex()
    {
        return this.mensajeCifrado;
    }

    //entrega el texto cifrado en un string continuo, no bloque
    public String getTextoCifradoLineal()
    {
        String resultado="";
        char ch;
        
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {

                ch =(char)((int)(Integer.valueOf(this.mensajeCifrado[i][j])));
               resultado += String.valueOf(ch) ;

            }
                
        }
        return resultado;
    }

    //Algoritmo rindael con sus 11 rondas correspondientes
    //dado que se utilizan 128 bits
    //esta es la funcion que encripta
    //trabaja directamente sobre el texto cifrado
    public void rijndael() {
        this.generacionClave();
        
        /********* Reporte *******/
        this.reporte +="\n\n\t\t Aplicacion del Algoritmo Rijndael Encriptando\n";
        this.reporte +="\n\t\tDatos iniciales en Hexadecimal\n";
        this.reporte +=this.getBloqueTextoCifrado();
        //ronda inicial
        this.addRoundKey(0);
        this.reporte +="\n\t\t Ronda 1 - Resultado AddroundKey(0) \n";
        this.reporte +=this.getBloqueTextoCifrado();

        //r-1 rondas
        for (int i = 1; i < 10; i++) {
            this.reporte +="\n\t\t Ronda "+ i +" \n\n";
            this.byteSub();
            this.reporte +="\n\t\t Resultado byteSub\n\n";
            this.reporte +=this.getBloqueTextoCifrado();
            this.shiftRow();
            this.reporte +="\n\t\t Resultado Shiftrow\n\n";
            this.reporte +=this.getBloqueTextoCifrado();
            this.mixColumns();
            this.HexToDecText();//sólo para facilitar el calculo
            this.reporte +="\n\t\t Resultado MixColumns\n\n";
            this.reporte +=this.getBloqueTextoCifrado();
            this.addRoundKey(i);
            this.reporte +="\n\t\t Resultado Addroundkey\n\n";
            this.reporte +=this.getBloqueTextoCifrado();
        }
        //ronda final
        this.reporte +="\n\t\t Ronda Final \n\n";
        this.byteSub();
        this.reporte +="\n\t\t Resultado byteSub\n\n";
       this.reporte +=this.getBloqueTextoCifrado();
        this.shiftRow();
        this.reporte +="\n\t\t Resultado ShiftRow\n\n";
       this.reporte +=this.getBloqueTextoCifrado();
        this.addRoundKey(10);
        this.reporte +="\n\t\t Resultado AddroundKey\n\n";
       this.reporte +=this.getBloqueTextoCifrado();
    }

    //Algoritmo rindael con sus 11 rondas correspondientes
    //dado que se utilizan 128 bits
    //esta es la funcion que desencripta
    //trabaja directamente sobre el texto cifrado
    public void decifrarRijndael() {

         /********* Reporte *******/
        this.reporte +="\n\n\t\t Aplicacion del Algoritmo Rijndael Desencriptando\n";
        this.reporte +="\n\t\tDatos iniciales en Hexadecimal\n";
        this.reporte +=this.getBloqueTextoCifrado();

        //ronda inicial
        this.reporte +="\n\t\t Ronda Inicial \n\n";

        this.addRoundKey(10);
        this.reporte +="\n\t\t Resultado inversoAddroundKey\n\n";
       this.reporte +=this.getBloqueTextoCifrado();

        this.InvShiftRow();
        this.reporte +="\n\t\t Resultado inversoShiftRow\n\n";
       this.reporte +=this.getBloqueTextoCifrado();
        this.InvbyteSub();
        this.reporte +="\n\t\t Resultado inversoByteSub\n\n";
       this.reporte +=this.getBloqueTextoCifrado();
        
        for (int i = 9; i > 0; i--) {
        this.reporte +="\n\n\t\t Ronda "+(10-i) +"\n\n";
            this.addRoundKey(i);
        this.reporte +="\n\t\t Resultado inversoAddroundKey\n\n";
        this.reporte +=this.getBloqueTextoCifrado();
            this.InversaMixColumns();
            this.HexToDecText(); //solamente por comodidad a la hora de trabajar con los datos
            this.reporte +="\n\n\t\t Resultado inversoMixColumns\n\n";
            this.reporte +=this.getBloqueTextoCifrado();
            this.InvShiftRow();
            this.reporte +="\n\t\t Resultado inversoShiftRow\n\n";
            this.reporte +=this.getBloqueTextoCifrado();
            this.InvbyteSub();
            this.reporte +="\n\t\t Resultado inversoByteSub\n\n";
            this.reporte +=this.getBloqueTextoCifrado();
        
        }
       this.reporte +="\n\n\t\t Ronda Final \n\n";
       //ronda final
        this.addRoundKey(0);
        this.reporte +="\n\t\t Resultado inversoAddroundKey\n\n";
        this.reporte +=this.getBloqueTextoCifrado();
    }

    public String getReporte()
    {
        return this.reporte;
    }
    public void setReporte(String t)
    {
         this.reporte=t;
    }

    public static void main(String args[]) {
        Encriptador1 n = new Encriptador1();

        //n.setBloqueTextoEnClaro("La casa es chica");
        n.setBloqueTextoCifrado("aa2aaaa aaaaaaaa");
        n.setClave("Eres Informatico");


        //n.imprimeExKey(0);
        n.rijndael();
        


        System.out.println("Texto cifrado");
        System.out.println(n.getBloqueTextoCifrado());
        n.decifrarRijndael();
        System.out.println("Texto claro");
        System.out.println(n.getBloqueTextoCifrado());
        System.out.println(n.getTextoCifradoLineal());

        System.out.println(n.reporte);

    
    }

    /*Tablas y vectores utilizados en el procedimiento*/
    private static String rcon[] = {"1", "2", "4", "8", "10", "20", "40", "80", "1b", "36"};
    private static String s_box[][] = {
        {"63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76"},
        {"ca", "82", "c9", "7d", "fa", "59", "47", "f0", "ad", "d4", "a2", "af", "9c", "a4", "72", "c0"},
        {"b7", "fd", "93", "26", "36", "3f", "f7", "cc", "34", "a5", "e5", "f1", "71", "d8", "31", "15"},
        {"04", "c7", "23", "c3", "18", "96", "05", "9a", "07", "12", "80", "e2", "eb", "27", "b2", "75"},
        {"09", "83", "2c", "1a", "1b", "6e", "5a", "a0", "52", "3b", "d6", "b3", "29", "e3", "2f", "84"},
        {"53", "d1", "00", "ed", "20", "fc", "b1", "5b", "6a", "cb", "be", "39", "4a", "4c", "58", "cf"},
        {"d0", "ef", "aa", "fb", "43", "4d", "33", "85", "45", "f9", "02", "7f", "50", "3c", "9f", "a8"},
        {"51", "a3", "40", "8f", "92", "9d", "38", "f5", "bc", "b6", "da", "21", "10", "ff", "f3", "d2"},
        {"cd", "0c", "13", "ec", "5f", "97", "44", "17", "c4", "a7", "7e", "3d", "64", "5d", "19", "73"},
        {"60", "81", "4f", "dc", "22", "2a", "90", "88", "46", "ee", "b8", "14", "de", "5e", "0b", "db"},
        {"e0", "32", "3a", "0a", "49", "06", "24", "5c", "c2", "d3", "ac", "62", "91", "95", "e4", "79"},
        {"e7", "c8", "37", "6d", "8d", "d5", "4e", "a9", "6c", "56", "f4", "ea", "65", "7a", "ae", "08"},
        {"ba", "78", "25", "2e", "1c", "a6", "b4", "c6", "e8", "dd", "74", "1f", "4b", "bd", "8b", "8a"},
        {"70", "3e", "b5", "66", "48", "03", "f6", "0e", "61", "35", "57", "b9", "86", "c1", "1d", "9e"},
        {"e1", "f8", "98", "11", "69", "d9", "8e", "94", "9b", "1e", "87", "e9", "ce", "55", "28", "df"},
        {"8c", "a1", "89", "0d", "bf", "e6", "42", "68", "41", "99", "2d", "0f", "b0", "54", "bb", "16"}
    };
    private static String invSBox[][] = {
        {"52", "09", "6a", "d5", "30", "36", "a5", "38", "bf", "40", "a3", "9e", "81", "f3", "d7", "fb"},
        {"7c", "e3", "39", "82", "9b", "2f", "ff", "87", "34", "8e", "43", "44", "c4", "de", "e9", "cb"},
        {"54", "7b", "94", "32", "a6", "c2", "23", "3d", "ee", "4c", "95", "0b", "42", "fa", "c3", "4e"},
        {"08", "2e", "a1", "66", "28", "d9", "24", "b2", "76", "5b", "a2", "49", "6d", "8b", "d1", "25"},
        {"72", "f8", "f6", "64", "86", "68", "98", "16", "d4", "a4", "5c", "cc", "5d", "65", "b6", "92"},
        {"6c", "70", "48", "50", "fd", "ed", "b9", "da", "5e", "15", "46", "57", "a7", "8d", "9d", "84"},
        {"90", "d8", "ab", "00", "8c", "bc", "d3", "0a", "f7", "e4", "58", "05", "b8", "b3", "45", "06"},
        {"d0", "2c", "1e", "8f", "ca", "3f", "0f", "02", "c1", "af", "bd", "03", "01", "13", "8a", "6b"},
        {"3a", "91", "11", "41", "4f", "67", "dc", "ea", "97", "f2", "cf", "ce", "f0", "b4", "e6", "73"},
        {"96", "ac", "74", "22", "e7", "ad", "35", "85", "e2", "f9", "37", "e8", "1c", "75", "df", "6e"},
        {"47", "f1", "1a", "71", "1d", "29", "c5", "89", "6f", "b7", "62", "0e", "aa", "18", "be", "1b"},
        {"fc", "56", "3e", "4b", "c6", "d2", "79", "20", "9a", "db", "c0", "fe", "78", "cd", "5a", "f4"},
        {"1f", "dd", "a8", "33", "88", "07", "c7", "31", "b1", "12", "10", "59", "27", "80", "ec", "5f"},
        {"60", "51", "7f", "a9", "19", "b5", "4a", "0d", "2d", "e5", "7a", "9f", "93", "c9", "9c", "ef"},
        {"a0", "e0", "3b", "4d", "ae", "2a", "f5", "b0", "c8", "eb", "bb", "3c", "83", "53", "99", "61"},
        {"17", "2b", "04", "7e", "ba", "77", "d6", "26", "e1", "69", "14", "63", "55", "21", "0c", "7d"}
    };
    private static String LTable[][] = {
        {"00", "00", "19", "01", "32", "02", "1a", "c6", "4b", "c7", "1b", "68", "33", "ee", "df", "03"},
        {"64", "04", "e0", "0e", "34", "8d", "81", "ef", "4c", "71", "08", "c8", "f8", "69", "1c", "c1"},
        {"7d", "c2", "1d", "b5", "f9", "b9", "27", "6a", "4d", "e4", "a6", "72", "9a", "c9", "09", "78"},
        {"65", "2f", "8a", "05", "21", "0f", "e1", "24", "12", "f0", "82", "45", "35", "93", "da", "8e"},
        {"96", "8f", "db", "bd", "36", "d0", "ce", "94", "13", "5c", "d2", "f1", "40", "46", "83", "38"},
        {"66", "dd", "fd", "30", "bf", "06", "8b", "62", "b3", "25", "e2", "98", "22", "88", "91", "10"},
        {"7e", "6e", "48", "c3", "a3", "b6", "1e", "42", "3a", "6b", "28", "54", "fa", "85", "3d", "ba"},
        {"2b", "79", "0a", "15", "9b", "9f", "5e", "ca", "4e", "d4", "ac", "e5", "f3", "73", "a7", "57"},
        {"af", "58", "a8", "50", "f4", "ea", "d6", "74", "4f", "ae", "e9", "d5", "e7", "e6", "ad", "e8"},
        {"2c", "d7", "75", "7a", "eb", "16", "0b", "f5", "59", "cb", "5f", "b0", "9c", "a9", "51", "a0"},
        {"7f", "0c", "f6", "6f", "17", "c4", "49", "ec", "d8", "43", "1f", "2d", "a4", "76", "7b", "b7"},
        {"cc", "bb", "3e", "5a", "fb", "60", "b1", "86", "3b", "52", "a1", "6c", "aa", "55", "29", "9d"},
        {"97", "b2", "87", "90", "61", "be", "dc", "fc", "bc", "95", "cf", "cd", "37", "3f", "5b", "d1"},
        {"53", "39", "84", "3c", "41", "a2", "6d", "47", "14", "2a", "9e", "5d", "56", "f2", "d3", "ab"},
        {"44", "11", "92", "d9", "23", "20", "2e", "89", "b4", "7c", "b8", "26", "77", "99", "e3", "a5"},
        {"67", "4a", "ed", "de", "c5", "31", "fe", "18", "0d", "63", "8c", "80", "c0", "f7", "70", "07"}
    };
    private static String ETable[][] = {
        {"01", "03", "05", "0f", "11", "33", "55", "ff", "1a", "2e", "72", "96", "a1", "f8", "13", "35"},
        {"5f", "e1", "38", "48", "d8", "73", "95", "a4", "f7", "02", "06", "0a", "1e", "22", "66", "aa"},
        {"e5", "34", "5c", "e4", "37", "59", "eb", "26", "6a", "be", "d9", "70", "90", "ab", "e6", "31"},
        {"53", "f5", "04", "0c", "14", "3c", "44", "cc", "4f", "d1", "68", "b8", "d3", "6e", "b2", "cd"},
        {"4c", "d4", "67", "a9", "e0", "3b", "4d", "d7", "62", "a6", "f1", "08", "18", "28", "78", "88"},
        {"83", "9e", "b9", "d0", "6b", "bd", "dc", "7f", "81", "98", "b3", "ce", "49", "db", "76", "9a"},
        {"b5", "c4", "57", "f9", "10", "30", "50", "f0", "0b", "1d", "27", "69", "bb", "d6", "61", "a3"},
        {"fe", "19", "2b", "7d", "87", "92", "ad", "ec", "2f", "71", "93", "ae", "e9", "20", "60", "a0"},
        {"fb", "16", "3a", "4e", "d2", "6d", "b7", "c2", "5d", "e7", "32", "56", "fa", "15", "3f", "41"},
        {"c3", "5e", "e2", "3d", "47", "c9", "40", "c0", "5b", "ed", "2c", "74", "9c", "bf", "da", "75"},
        {"9f", "ba", "d5", "64", "ac", "ef", "2a", "7e", "82", "9d", "bc", "df", "7a", "8e", "89", "80"},
        {"9b", "b6", "c1", "58", "e8", "23", "65", "af", "ea", "25", "6f", "b1", "c8", "43", "c5", "54"},
        {"fc", "1f", "21", "63", "a5", "f4", "07", "09", "1b", "2d", "77", "99", "b0", "cb", "46", "ca"},
        {"45", "cf", "4a", "de", "79", "8b", "86", "91", "a8", "e3", "3e", "42", "c6", "51", "f3", "0e"},
        {"12", "36", "5a", "ee", "29", "7b", "8d", "8c", "8f", "8a", "85", "94", "a7", "f2", "0d", "17"},
        {"39", "4b", "dd", "7c", "84", "97", "a2", "fd", "1c", "24", "6c", "b4", "c7", "52", "f6", "01"}
    };
}



