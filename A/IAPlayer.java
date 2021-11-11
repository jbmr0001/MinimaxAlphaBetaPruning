/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta;

import java.util.LinkedList;

/**
 *
 * @author José María Serrano
 * @author Cristóbal J. Carmona
 * @version 1.4 Departamento de Informática. Universidad de Jáen
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Curso 2020-21: Se introducen obstáculos aleatorios Clase IAPlayer para
 * representar al jugador CPU que usa la poda Alfa Beta
 *
 * Esta clase es la que tenemos que implementar y completar
 *
 */
public class IAPlayer extends Player {
    //Establecemos las filas y columnas a 4 para poder mostrar todos los nodos
    private static final int COLUMNAS = 4;
    private static final int FILAS = 4;
    private static final int CONECTA = 4;
    private static final int PROFUNDIDAD = 7;

    public class Estado {

        /**
         * Vector de Estados hijo que descienden del actual
         */
        private final LinkedList<Estado> hijos;

        /**
         * Estado actual del tablero
         */
        private final int[][] tablero;

        /**
         * Indica si el estado actual es estado final. Es estado final si es
         * solución o si es nodo hoja.
         */
        private boolean estadoFinal;

        /**
         * Indica si el estado actual es solución.
         */
        private boolean solucion;

        /**
         * Jugador que le toca jugar este estado.
         */
        private final int jugador;

        /**
         * Constructor parametrizado.
         *
         * @param tablero Dato que alberga el Estado
         * @param jug Jugador que realiza el movimiento en este tablero.
         */
        public Estado(int[][] tablero, int jug) {
            this.hijos = new LinkedList<>();
            this.tablero = tablero;
            this.estadoFinal = false;
            this.solucion = false;
            this.jugador = jug; // Jugador inválido

        }

        /**
        * Función que muestra el tablero.
        */
        void mostrar() {
            //Recorremos todas las posiciones del tablero y las mostramos
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    System.out.print(tablero[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();

        }
        /**
        * Función para comprobar si se gana metiendo una ficha en la posición x,y.
        * @param x Coordenada en las filas.
        * @param y Coordenada en las columnas
         * @param conecta Numero de casillas seguidas necesarias para ganar.
        * @return int
        */
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

    }
    /**
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int jugada(Grid tablero, int conecta) {

        int columna = getRandomColumn(tablero);
        Estado e = new Estado(tablero.toArray(), 1);
        generaHijos(e);
       
        return tablero.checkWin(tablero.setButton(columna, Conecta.JUGADOR2), columna, conecta);

    }

    /**
     * Función que genera los hijos de un estado. 
     * @param e Estado padre.
     */
    public void generaHijos(Estado e) {
        
            hijoEnColumna(e);

            System.out.println("-----------PADRE-----------");
            e.mostrar();
            System.out.println("------------------------------");
            for (int i = 0; i < e.hijos.size(); i++) {
                System.out.println("Hijo " + i);
                e.hijos.get(i).mostrar();
            }
            for (int i = 0; i < e.hijos.size(); i++) {
                generaHijos(e.hijos.get(i));
            }
       
    }

    /**
     * Función que genera los hijos de un estado. 
     * @param e Estado padre.
     */
    public void generaHijos2(Estado e) {
        if(e.solucion){
        System.out.println("-----------NODO GANADOR-----------");
            e.mostrar();
            System.out.println("------------------------------");
        }else{
        if (e.estadoFinal == false) {
            hijoEnColumna(e);
            System.out.println("-----------PADRE-----------");
            e.mostrar();
            System.out.println("------------------------------");
            for (int i = 0; i < e.hijos.size(); i++) {
                System.out.println("Hijo " + i);
                e.hijos.get(i).mostrar();
            }

            for (int i = 0; i < e.hijos.size(); i++) {
                generaHijos2(e.hijos.get(i));
            }
        } else {
            System.out.println("-----------NODO HOJA-----------");
            e.mostrar();
            System.out.println("------------------------------");

        }
        }
    }

    /**
     * Función que genera un hijo por cada columna en la que hay un movimiento posible.
     * @param e Estado padre.
     */
    public void hijoEnColumna(Estado e) {
        
        for (int col = 0; col < 4; col++) {
            boolean filaLlena = false;//boolean para indicarnos si hay espacio en la columna.
            boolean rellenado = false;//boolean para indicarnos si se ha creado un hijo en esa columna.
            //Creamos una tabla nueva y pasamos dentro el tablero de e
            int[][] tab = new int[FILAS][COLUMNAS];
            //Copiamos el tablero entero en uno auxiliar
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

            //Creamos un nuevo estado con el tablero copiado y le asignamos su jugador
            Estado aux;
            if (e.jugador == -1) {
                aux = new Estado(tab, 1);
            } else {
                aux = new Estado(tab, -1);
            }

            //Recorremos todas las filas de la columna
            for (int j = FILAS - 1; j >= 0; j--) {
                if (tab[0][col] == 1 || tab[0][col] == -1) {//si la´última posición está ocupada
                    filaLlena = true;//la fila está llena
                }
                if (tab[j][col] == 0 && !rellenado) {//si no hemos insertado la ficha en la columna y hay espacio en la columna.
                    rellenado = true;
                    tab[j][col] = aux.jugador;
                }
                //Comprobación de si es un nodo ganador
                if (aux.checkWin(j, col, CONECTA) != 0) {
                    aux.solucion = true;
                }

            }
            if (!filaLlena) {
                //Si no tiene mas columnas libres es un estado final.
                if (aux.tablero[0][0] != 0 && aux.tablero[0][1] != 0 && aux.tablero[0][2] != 0 && aux.tablero[0][3] != 0) {
                    aux.estadoFinal = true;
                }
                
                e.hijos.add(aux);
            }
        }

    }

}
