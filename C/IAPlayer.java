/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import javafx.util.Pair;

public class IAPlayer extends Player {
    
    private static final int FILAS = 6;
    private static final int COLUMNAS = 7;
    private static final int CONECTA = 4;
    private static final int PROFUNDIDAD = 5;


    public class Estado {

        /**
         * Vector de Estados que descienden del actual
         */
        //private Estado[] hijos;
        private LinkedList<Estado> hijos;
        //private LinkedList<Estado> hijos;

        /**
         * Estado actual del tablero
         */
        private int[][] tablero;
        private int insertax;
        private int insertay;

        int seguidas1 = 0;
        int seguidas2 = 0;


        double valorVertical1;
        double valorHorizontal1;
        double valorOblicuo1;

        /**
         * Indica si el estado actual es estado final. Es estado final si es
         * solución o si es nodo hoja.
         */
        private boolean estadoFinal;

        private double valor;

        /**
         * Indica si el estado actual es solución.
         */
        private boolean solucion;

        /**
         * Jugador que le toca jugar este estado.
         */
        private int jugador;
        //Generación del estado
        private int generacion;

        /**
         * Constructor parametrizado.
         *
         * @param tablero Dato que alberga el Estado
         */
        public Estado(int[][] tablero, int jug) {
            this.hijos = new LinkedList<Estado>();
            this.tablero = tablero;
            this.estadoFinal = false;
            this.solucion = false;
            this.jugador = jug; 
            this.generacion = 0;
        }
        //Muestra los valores del estado actual y su tablero
        void mostrarValor(){
            System.out.println("------------------------------");
            System.out.println("VALOR VERTICAL: " + valorVertical1);
            System.out.println("VALOR HORIZONTAL: " + valorHorizontal1);
            System.out.println("VALOR OBLICUO: " + valorOblicuo1);
            System.out.println("VALOR TOTAL: "+ valor);
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    System.out.print(tablero[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("------------------------------");
        }

        //Función que muestra el tablero actual, su jugador y donde se inserta
        void mostrar() {
            System.out.println("Se inserta en columna: " + insertay);
           // System.out.println(" Fila: " + insertax);
            System.out.println(" Jugador: " + jugador);
            //System.out.println(" SEGUIDAS1: "+seguidas1);
            //System.out.println(" SEGUIDAS2: "+seguidas2);
            //System.out.println(" V vertical1: " + valorVertical1);
            //System.out.println(" V vertical2: " + valorVertical2);

            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    System.out.print(tablero[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();

        }
        //Funcion que calcula la heuristica
        public void heuristica() {
            int val = 0;
            int jugadorNuestro = -1;
            int jugadorContrario = 1;
            val += calculohorizontal(jugadorNuestro); //se calcula el valor de la heuristica en horizontal de nuestro jugador
            // System.out.println("CALCULO SUMATORIA HORIZONTAL DE LA IA: " + val);
            int valcontrario = -calculohorizontal(jugadorContrario);//se calcula el valor de la heuristica en horizontal del jugador contario
            //System.out.println("CALCULO SUMATORIA HORIZONTAL DEL ADVERSARIO: " + valcontrario);
            val += valcontrario;
            valorHorizontal1=val;
            valorVertical(); //Se llama a las funciones para calcular el valor vertical y oblicuo
            valorOblicuo();
            this.valor += val; //se suma al valor la variable val
            
            
            //PARA MOSTRAR EL VALOR Y EL TABLERO ACTUAL
          //  System.out.println();   
           // System.out.println("EL VALOR ES: " + this.valor);
           // mostrar();
        }
        //Usamos una versión modificada del checkwin de Grid
        public int checkWin(int x, int y, int conecta) {
            /*
		 *	x fila
		 *	y columna
             */

            //Comprobar vertical
            int ganar1 = 0;
            int ganar2 = 0;
            int ganador = 0;
            boolean salir = false;
            for (int i = 0; (i < FILAS) && !salir; i++) {
                if (tablero[i][y] != 0) {
                    if (tablero[i][y] == 1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = 1;
                        salir = true;
                    }
                    if (!salir) {
                        if (tablero[i][y] == -1) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = -1;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
            // Comprobar horizontal
            ganar1 = 0;
            ganar2 = 0;
            for (int j = 0; (j < COLUMNAS) && !salir; j++) {
                if (tablero[x][j] != 0) {
                    if (tablero[x][j] == 1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = 1;
                        salir = true;
                    }
                    if (ganador != 1) {
                        if (tablero[x][j] == -1) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = -1;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
            }
            // Comprobar oblicuo. De izquierda a derecha
            ganar1 = 0;
            ganar2 = 0;
            int a = x;
            int b = y;
            while (b > 0 && a > 0) {
                a--;
                b--;
            }
            while (b < COLUMNAS && a < FILAS && !salir) {
                if (tablero[a][b] != 0) {
                    if (tablero[a][b] == 1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = 1;
                        salir = true;
                    }
                    if (ganador != 1) {
                        if (tablero[a][b] == -1) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = -1;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
                a++;
                b++;
            }
            // Comprobar oblicuo de derecha a izquierda 
            ganar1 = 0;
            ganar2 = 0;
            a = x;
            b = y;
            //buscar posición de la esquina
            while (b < COLUMNAS - 1 && a > 0) {
                a--;
                b++;
            }
            while (b > -1 && a < FILAS && !salir) {
                if (tablero[a][b] != 0) {
                    if (tablero[a][b] == 1) {
                        ganar1++;
                    } else {
                        ganar1 = 0;
                    }
                    // Gana el jugador 1
                    if (ganar1 == conecta) {
                        ganador = 1;
                        salir = true;
                    }
                    if (ganador != 1) {
                        if (tablero[a][b] == -1) {
                            ganar2++;
                        } else {
                            ganar2 = 0;
                        }
                        // Gana el jugador 2
                        if (ganar2 == conecta) {
                            ganador = -1;
                            salir = true;
                        }
                    }
                } else {
                    ganar1 = 0;
                    ganar2 = 0;
                }
                a++;
                b--;
            }

            return ganador;
        } // checkWin
        //Funcion que nos calculará la heuristica en horizontal del jugador jug
        public int calculohorizontal(int jug) {
            int val = 0;
            int contador1 = 0;
            int contadorbolas1 = 0;
            boolean fina = false; //creamos contadores, val y un booleado para el final
            for (int j = FILAS - 1; j >= 0 && fina == false; j--) { //pasamos por todas las filas de abajo a arriba 
                for (int z = 0; z < COLUMNAS; z++) {//Comprobamos si la fila está vacía, en tal caso, se terminará el conteo
                    if (this.tablero[j][z] != 2 && this.tablero[j][z] != 0) {
                        contadorbolas1++; 
                    }
                }
                if (contadorbolas1 == 0) {
                    fina = true;
                }
                //reiniciamos el contador de bolas 
                contadorbolas1 = 0;
                for (int i = 0; i < 4; i++) { //recorreremos 4 veces la fila en intervalos de 4, contando los huecos, las y nuestras bolas
                    if (this.tablero[j][i] != jug && this.tablero[j][i] != 0) { //en caso de encontrar una bola contraria o piedra se terminará ese conteo y pasaremos al siguiente
                        contadorbolas1 = 0;
                        i = 4;
                    } else {
                        contador1++;
                        if (this.tablero[j][i] == jug) {
                            contadorbolas1++;
                        }
                    }

                } //cada vez que terminemos un conteo sumaremos al valor que devolveremos las bolas que hay en esos 4 huecos
                if (contadorbolas1 > 0) {
                    val += Math.pow(10, contadorbolas1);
                    contadorbolas1 = 0;
                }
                for (int i = 3; i < 7; i++) {
                    if (this.tablero[j][i] != jug && this.tablero[j][i] != 0) {
                        contadorbolas1 = 0;
                        i = 7;
                    } else {
                        contador1++;
                        if (this.tablero[j][i] == jug) {
                            contadorbolas1++;
                        }
                    }
                }
                if (contadorbolas1 > 0) {
                    val += Math.pow(10, contadorbolas1);
                    contadorbolas1 = 0;
                }
                for (int i = 1; i < 5; i++) {
                    if (this.tablero[j][i] != jug && this.tablero[j][i] != 0) {
                        contadorbolas1 = 0;
                        i = 5;
                    } else {
                        contador1++;
                        if (this.tablero[j][i] == jug) {
                            contadorbolas1++;
                        }
                    }

                }
                if (contadorbolas1 > 0) {
                    val += Math.pow(10, contadorbolas1);
                    contadorbolas1 = 0;
                }
                for (int i = 2; i < 6; i++) {
                    if (this.tablero[j][i] != jug && this.tablero[j][i] != 0) {
                        contadorbolas1 = 0;
                        i = 6;
                    } else {
                        contador1++;
                        if (this.tablero[j][i] == jug) {
                            contadorbolas1++;
                        }
                    }

                }
                if (contadorbolas1 > 0) {
                    val += Math.pow(10, contadorbolas1);
                    contadorbolas1 = 0;
                }
            }
            return val;
        }

        void valorVertical() {
            for (int y = 0; y < COLUMNAS; y++) {
                int ultima = 0;
                int ultimaI = 0;
                seguidas1 = 0;
                seguidas2 = 0;
                if (tablero[FILAS - 1][y] != 0) {
                    for (int i = FILAS - 1; i >= 0; i--) {
                        ultimaI = i;
                        if (tablero[i][y] == 1) {
                            seguidas1++;
                            seguidas2 = 0;
                            ultima = 1;
                        }
                        if (tablero[i][y] == -1) {
                            seguidas2++;
                            seguidas1 = 0;
                            ultima = -1;
                        }
                    }
                    //System.out.println();
                    int seg = 0;
                    int jug = 0;
                    if (ultima == 1) {
                        seg = seguidas1;
                        jug = -1;
                    } else if (ultima == -1) {
                        seg = seguidas2;
                        jug = 1;
                    }

                    if (FILAS - (ultimaI + 1) + seg >= 4) {
                        valorVertical1 = +valorVertical1 + jug * Math.pow(10, seg);
                        //     System.out.println("Valor vertical: " + y + " " + jug * Math.pow(10, seg));
                    }
                }
            }
            // System.out.println("Valor vertical total: " + valorVertical1);
            this.valor += valorVertical1;
        }

        void valorOblicuo() {
            for (int i = 0; i < FILAS; i++) {
                int seguidas1 = 0;
                int seguidas2 = 0;
                int vacias1 = 0;
                int vacias2 = 0;
                int ultima = 0;
                int ultimaI = 0;
               // System.out.println();
                int a = i;
                int b = 0;

                while (b > 0 && a > 0) {
                    a--;
                    b--;
                }

                while (b < COLUMNAS && a < FILAS) {
                  //  System.out.print(tablero[a][b] + " ");

                    if (tablero[a][b] == 1) {
                        seguidas1++;
                        seguidas2 = 0;
                        vacias2 = 0;
                        ultima = 1;

                    }
                    if (tablero[a][b] == -1) {
                        seguidas2++;
                        seguidas1 = 0;
                        vacias1 = 0;
                        ultima = -1;
                    }
                    if (tablero[a][b] == 0) {
                        vacias1++;
                        vacias2++;
                        ultima = 0;
                    }

                    int jug = 0;
                    int seg = 0;
                    if (vacias1 + seguidas1 >= 4 && ultima != 0) {
                        seg = seguidas1;
                        jug = -1;
                       // System.out.print("Oblicuo: " + i + " " + jug * Math.pow(10, seg));
                        valorOblicuo1 = valorOblicuo1 + jug * Math.pow(10, seg);

                        vacias1 = 0;
                        seguidas1 = 0;
                    }
                    if (vacias2 + seguidas2 >= 4 && ultima != 0) {
                        seg = seguidas2;
                        jug = 1;
                        //System.out.print("Oblicuo: " + i + " " + jug * Math.pow(10, seg));
                        valorOblicuo1 = valorOblicuo1 + jug * Math.pow(10, seg);
                        vacias2 = 0;
                        seguidas2 = 0;
                    }

                    a++;
                    b++;
                }
            }
            //System.out.println("Valor oblicuo `+ "+valorOblicuo1);
           // System.out.println();
            for (int i = 0; i < FILAS; i++) {
                // Comprobar oblicuo de derecha a izquierda 
                int seguidas1 = 0;
                int seguidas2 = 0;
                int vacias1 = 0;
                int vacias2 = 0;
                int ultima = 0;
               // System.out.println();
                int a = i;
                int b = COLUMNAS - 1;

                //buscar posición de la esquina
                while (b < COLUMNAS - 1 && a > 0) {
                    a--;
                    b++;
                }
                while (b > -1 && a < FILAS) {
                   // System.out.print(tablero[a][b] + " ");

                    if (tablero[a][b] == 1) {
                        seguidas1++;
                        seguidas2 = 0;
                        vacias2 = 0;
                        ultima = 1;

                    }
                    if (tablero[a][b] == -1) {
                        seguidas2++;
                        seguidas1 = 0;
                        vacias1 = 0;
                        ultima = -1;
                    }
                    if (tablero[a][b] == 0) {
                        vacias1++;
                        vacias2++;
                        ultima = 0;
                    }

                    int jug = 0;
                    int seg = 0;
                    if (vacias1 + seguidas1 >= 4 && ultima != 0) {
                        seg = seguidas1;
                        jug = -1;
                     //   System.out.print("Oblicuo: " + i + " " + jug * Math.pow(10, seg));
                        valorOblicuo1 = valorOblicuo1 + jug * Math.pow(10, seg);

                        vacias1 = 0;
                        seguidas1 = 0;
                    }
                    if (vacias2 + seguidas2 >= 4 && ultima != 0) {
                        seg = seguidas2;
                        jug = 1;
                     //   System.out.print("Oblicuo: " + i + " " + jug * Math.pow(10, seg));
                        valorOblicuo1 = valorOblicuo1 + jug * Math.pow(10, seg);
                        vacias2 = 0;
                        seguidas2 = 0;
                    }

                    a++;
                    b--;
                }
            }

            //System.out.println("Valor Oblicuo Total: " + valorOblicuo1);
            this.valor += valorOblicuo1;
        }
    }

    /**
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int jugada(Grid tablero, int conecta) {
        //Creamos la columna
        int columna; //Creamos el estado con el tablero y un jugador 1
        Estado e = new Estado(tablero.toArray(), -1); //generamos los hijos del estado e
        generaHijos2(e); //metemos en columna la salida del minimax que es la columna a la que añadiremos el valor
        columna = MiniMax(e);
        e.heuristica(); //Calculamos la heuristica de el estado actual
        e.mostrarValor(); //Mostramos el valor de esta heuristica
        return tablero.checkWin(tablero.setButton(columna, Conecta.JUGADOR2), columna, conecta); //devolvemos el checkwin con la ficha añadida

    }

    //Función que genera los hijos hijos a través de un Estado dado y los muestra
    public void generaHijos2(Estado e) {
        //En caso de ser un nodo ganador
        if (e.solucion) { //EN CASO DE QUERER MOSTRAR LOS ESTADOS GANADORES CONFORME SE GENERAN
            e.heuristica();
            /*          System.out.println("-----------NODO GANADOR-----------");
            e.mostrar();
            System.out.println("------------------------------");
             */        } else {
            if (e.estadoFinal == false && e.generacion < PROFUNDIDAD) {
                hijoEnColumna(e);
                //EN CASO DE QUERER MOSTRAR LOS PADRES E HIJOS CONFORME SE GENERAN
                /*              System.out.println("-----------PADRE-----------");
                e.mostrar();
                System.out.println("------------------------------");

                for (int i = 0; i < e.hijos.size(); i++) {
                    System.out.println("Hijo " + i);
                    e.hijos.get(i).mostrar();
                }
                 */
                for (int i = 0; i < e.hijos.size(); i++) {
                    generaHijos2(e.hijos.get(i));
                }
            } else { //EN CASO DE SER UN NODO HOJA
                e.estadoFinal=true;
                //Generamos la heurística del nodo hoja
                e.heuristica();
                //SI QUEREMOS MOSTRAR LOS NODOS HOJA
                /*   System.out.println("-----------NODO HOJA-----------");
                System.out.println("VALOR: " + e.valor);
                e.mostrar();
                System.out.println("------------------------------");
                 */
            }
        }
    }
    //Función que crea un hijo del Estado e con cada una de las columnas i
    public void hijoEnColumna(Estado e) {
        //Pasamos por las columnas 
        for (int col = 0; col < 7; col++) {
            boolean colLlena = false;

            //Creamos una tabla nueva y pasamos dentro el tablero de e
            int[][] tab = new int[FILAS][COLUMNAS];
            for (int in = 0; in < FILAS; in++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    switch (e.tablero[in][j]) {
                        case 1:
                            tab[in][j] = 1;
                            break;
                        case -1:
                            tab[in][j] = -1;
                            break;
                        case 2:
                            tab[in][j] = 2;
                            break;
                        default:
                            tab[in][j] = 0;
                            break;
                    }
                }

            }

            //Creamos un nuevo estado con el jugador contrario al actual y el nuevo tablero
            Estado aux;
            if (e.jugador == -1) {
                aux = new Estado(tab, 1);
            } else {
                aux = new Estado(tab, -1);
            }
            //creamos un booleano para cuando hayamos rellenado el tablero con 1 ficha
            boolean rellenado = false;
            //Pasamos por cada fila de las columnas de abajo a arriba
            for (int j = FILAS - 1; j >= 0; j--) {
                if (tab[0][col] == 1 || tab[0][col] == -1) { //En caso de tener la fila columna llena 
                    colLlena = true;
                }
                if (tab[j][col] == 0 && !rellenado) {//En caso de no haber rellenado y tener un hueco en ese espacio, metemos ahí la ficha
                    rellenado = true;

                    tab[j][col] = aux.jugador;
                    aux.insertax = j; //guardamos el lugar donde se ha añadido la ficha en el estado
                    aux.insertay = col;
                    if (aux.checkWin(j, col, CONECTA) != 0) { //comprobamos si con esa ficha nuestro estado ha ganado y en ese caso lo marcamos cómo solución
                        aux.solucion = true;
                    }
                }

            }
            if (!colLlena) { //Si la fila no estaba llena, llegaremos aquí y añadiremos el hijo a la lista de estados del padre
                //Si el tablero está lleno, marcamos el estado cómo final
                if (aux.tablero[0][0] != 0 && aux.tablero[0][1] != 0 && aux.tablero[0][2] != 0 && aux.tablero[0][3] != 0 && aux.tablero[0][4] != 0 && aux.tablero[0][5] != 0 && aux.tablero[0][6] != 0) {
                    aux.estadoFinal = true;
                }
                //Sumamos 1 a la generación del estado, una vez se llegue a 5 parará de crearse
                aux.generacion = e.generacion + 1;
                //Añadimos el estado a los hijos de el estado padre
                e.hijos.add(aux);
            }
        }

    }
//Genera una columna en la que insertar la siguiente ficha
    public int MiniMax(Estado e) { 
        int valor = Max(e,-10000000,100000000).getValue(); //Llamamos a la función Max con el estado actual y sacamos de este el valor del pair que es el indice del hijo que nos sirve
        return e.hijos.get(valor).insertay; //Devolvemos el lugar donde el hijo de e añadio la ficha 
    }
    //Devuelve un pair con el valor de la heuristica y la posición del estado hijo en la lista enlazada del padre
    public Pair<Double, Integer> Max(Estado e,double alfa,double beta) {
        LinkedList<Double> valores; //creamos una lista enlazada con los valores de la heuristica
        valores = new LinkedList<>(); 
        for (int i = 0; i < e.hijos.size(); i++) { //recorremos todos los hijos del estado e
            if (e.hijos.get(i).estadoFinal == true||e.hijos.get(i).generacion==PROFUNDIDAD) { //en caso de ser un estado final o un estado con 5 generaciones
                valores.add(e.hijos.get(i).valor); //añadimos el valor a la lista de valores 
            } else { //en caso de ser un estado intermedio

                valores.add(Mini(e.hijos.get(i),alfa,beta).getKey()); //llamamos a la función mini con el hijo de i, haciendo que nos devuelva valor de la heuristica de este
            }
        }
        int lugar = 0; //Ahora recorremos la lista enlaza buscando el valor mayor y guardando la posicion de este en la lista
        Double max = -100000000000000000000.0;
        for (int i = 0; i < valores.size(); i++) {
            if (valores.get(i) > max) {
                max = valores.get(i);
                lugar = i;
            }
            alfa=max;
            if(beta<=alfa){
                break;
            }
        } //creamos un pair con el valor heuristico mayor y el lugar de este en la lista de hijos de e
        Pair<Double, Integer> par = new Pair<>(max, lugar);
        return par; //devolvemos el pair
    }
//Devuelve un pair con el valor de la heuristica y la posición del estado hijo en la lista enlazada del padre
    public Pair<Double, Integer> Mini(Estado e,double alfa,double beta) {
        LinkedList<Double> valores;//creamos una lista enlazada con los valores de la heuristica
        valores = new LinkedList<>();
        for (int i = 0; i < e.hijos.size(); i++) {//recorremos todos los hijos del estado e
            if (e.hijos.get(i).estadoFinal == true||e.hijos.get(i).generacion==PROFUNDIDAD) {//en caso de ser un estado final o un estado con 5 generaciones
                valores.add(e.hijos.get(i).valor);//añadimos el valor a la lista de valores 
            } else {//en caso de ser un estado intermedio
                valores.add(Max(e.hijos.get(i),alfa,beta).getKey());//llamamos a la función max con el hijo de i, haciendo que nos devuelva valor de la heuristica de este
            }
        }
        int lugar = 0;//Ahora recorremos la lista enlaza buscando el valor menor y guardando la posicion de este en la lista
        Double min = 100000000000000000000.0;
        for (int i = 0; i < valores.size(); i++) {
            if (valores.get(i) < min) {
                min = valores.get(i);
                lugar = i;
            }
            beta=min;
            if(beta<=alfa){
                break;
            }
        }//creamos un pair con el valor heuristico menor y el lugar de este en la lista de hijos de e
        Pair<Double, Integer> par = new Pair<>(min, lugar);
        return par;//devolvemos el pair
    }
}
