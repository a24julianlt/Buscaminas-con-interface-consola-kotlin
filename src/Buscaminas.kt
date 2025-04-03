class Buscaminas(var filas: Int, private var columnas: Int, var minas: Int) {
    // Creamos el tablero
    var tablero = llenarConMinas(MutableList(filas) { MutableList(columnas) { Celda() } }, minas)


    // destapamos la casilla
    fun destapar(accion: String, fila: Int, columna: Int): Boolean {
        var estadoJuego = true

        val celda = tablero[fila][columna]
        var mostrar: Any = 'X'

        // Comprobamos si esta tapada o no
        if (celda.tapada) {
            // Si está tapada
            if (accion == "D") {
                // Miramos si tiene bandera o mina
                if (celda.bandera) {
                    //BANDERA
                    mostrar = 'B'

                    // si tiene bandera le volvemos a pedir otra coor
                    estadoJuego = true
                } else {
                    if (celda.mina) {
                        //MINA
                        mostrar = 'M'

                        // si tiene mina PIERDE
                        estadoJuego = false

                    } else {
                        // si no tiene ninguna de las 2 contamos alrededor
                        destaparEncadenado(fila, columna, tablero)
                        mostrar = celda.count
                    }
                }
            }

            // Colocamos bandera cuando lo pide o la quitamos
            if (accion == "B") {
                // Si ya había bandera la quitamos
                if (celda.bandera) {
                    // Quitamos la bandera
                    celda.bandera = false

                    mostrar = 'X'
                } else {
                    // Si no había bandera la ponemos
                    mostrar = 'B'
                    celda.bandera = true
                }
            }


            // Cambiamos el valor de tapada
            if (accion == "D" && !celda.bandera) celda.tapada = false


        } else return true  // Si está destapada


        // cambiamos la apariencia
        celda.celda = mostrar


        // Después de mirar todo, volvemos
        return estadoJuego
    }

}

class Celda {
    var celda: Any = 'X'
    var mina: Boolean = false
    var bandera: Boolean = false
    var tapada: Boolean = true
    var count: Int = 0   // cuenta las minas alrededor
}

// llena con minas el tablero
fun llenarConMinas(tablero: MutableList<MutableList<Celda>>, minas: Int): MutableList<MutableList<Celda>> {
    val filas = tablero.size
    val columnas = tablero[0].size
    val posiciones = mutableListOf<Pair<Int, Int>>()

    // Generamos todas las posiciones posibles
    for (fila in 0 until filas) {
        for (columna in 0 until columnas) {
            posiciones.add(Pair(fila, columna))
        }
    }

    // Barajamos y seleccionamos `minas` posiciones
    posiciones.shuffle()
    repeat(minas) { i ->
        val (fila, columna) = posiciones[i]
        tablero[fila][columna].mina = true
    }

    return tablero
}


// cuenta las minas alrededor
fun destaparEncadenado(i: Int, j: Int, mapa: MutableList<MutableList<Celda>>): MutableList<MutableList<Celda>> {
    // Comprobamos que la posición esté dentro de los límites
    if (i !in mapa.indices || j !in mapa[i].indices) return mapa

    val celda = mapa[i][j]

    // Si la celda ya está destapada, no hacemos nada
    if (!celda.tapada) return mapa

    // Contamos las minas alrededor
    val bombas = contarMinas(i, j, mapa)

    // Destapamos la celda y asignamos el número de minas alrededor
    celda.tapada = false
    celda.count = bombas
    celda.celda = if (bombas > 0) bombas else '0'

    // Recorremos las celdas
    if (bombas == 0) {
        mapa[i][j].celda = '0'

        if (mapa[i][j].celda == '0') {
            destaparEncadenado(i - 1, j, mapa) // norte
            destaparEncadenado(i + 1, j, mapa) // sur
            destaparEncadenado(i, j - 1, mapa) // oeste
            destaparEncadenado(i, j + 1, mapa) // este
            destaparEncadenado(i - 1, j - 1, mapa) // noroeste
            destaparEncadenado(i - 1, j + 1, mapa) // noreste
            destaparEncadenado(i + 1, j - 1, mapa) // suroeste
            destaparEncadenado(i + 1, j + 1, mapa) // sureste
        }
    }

    return mapa
}

fun contarMinas(i: Int, j: Int, mapa: MutableList<MutableList<Celda>>): Int {

    var bombas = 0

    for (x in i - 1..i + 1) {
        for (y in j - 1..j + 1) {
            if (x in mapa.indices && y in mapa[x].indices && mapa[x][y].mina) {
                bombas++
            }
        }
    }

    return bombas
}

// Comprueba si terminó el juego
fun finJuego(buscaminas: Buscaminas): Boolean {
    // Comprobamos si todas las minas tienen bandera
    var mina_bandera = 0
    for (i in buscaminas.tablero.indices) {
        for (j in buscaminas.tablero[i].indices) {
            if (buscaminas.tablero[i][j].mina == true && buscaminas.tablero[i][j].bandera == true) {
                mina_bandera++
            }
        }
    }


    // Contamos las celdas tapadas
    var celdasTapadas = 0
    for (i in buscaminas.tablero.indices) {
        for (j in buscaminas.tablero[i].indices) {
            if (buscaminas.tablero[i][j].tapada) {
                celdasTapadas++
            }
        }
    }

    // Si el número de celdas tapadas es igual al número de minas, el jugador ha ganado
    if (celdasTapadas == buscaminas.minas || mina_bandera == buscaminas.minas) {
        return false
    }

    return true
}

// comprueba las filas y columnas introducidas
fun comprobar(f_c: Int) {
    if (f_c < 1) {
        throw Exception("Número de filas/columnas inválido")
    }
}