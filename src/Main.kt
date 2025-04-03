fun main() {
    println("===================BUSCAMINAS===================")
    println()
    println("Bienvenido al mítico juego del buscaminas")
    println()
    println("Vamos a crear el tablero de juego")
    println()
    println("Introduce el número de filas que quieras que tenga: ")
    val filas = readln().toInt()
    println("Introduce el número de columnas que quieras que tenga: ")
    val columnas = readln().toInt()
    println()
    println("Ahora introduce el número de minas que quieras que tenga, debe ser menor al número de celdas del tablero: ")
    val minas = readln().toInt()
    println()
    println("Creando un tablero de $filas x $columnas con $minas minas")
    println()

    // Crear el tablero
    var buscaminas = Buscaminas(filas, columnas, minas)
    mostrarTablero(buscaminas)


    // Empieza el juego
    var estadoJuego = true
    while (estadoJuego) {
        // Turno
        println("Primero decide que quieres hacer [B]andera [D]estapar")
        println("Después introduce las coordenadas de una casilla separadas por un espacio, primero la fila y depués la columna: ")

        var coor = readln().split(" ")

        var accion = coor[0].uppercase()
        // Corregimos las coordenadas
        var fila = coor[1].toInt() - 1
        var columna = coor[2].toInt() - 1

        // Destapamos celda y miramos si perdió
        estadoJuego = buscaminas.destapar(accion, fila, columna)

        // Mostramos el tablero depués de cada turno
        mostrarTablero(buscaminas)

        // Comprobamos si termino el juego
        if (!finJuego(buscaminas)) {
            break
        }

    }

    // Mensajes de fin de juego
    if (estadoJuego) {
        println("FELICIDADES HAS GANADO")
        println("¡¡¡MUY BIEN!!!")
    } else {
        println("PERDISTE")
        println("Ahí había una mina :(")
        println("Más suerte la próxima vez")
    }


}

fun mostrarTablero(buscaminas: Buscaminas) {
    // Mostramos los número para que sea más facil localizar las casillas
    for (i in 0..buscaminas.filas) {
        print(" - $i -")
    }
    println()

    // Enseñamos el tablero
    for (i in buscaminas.tablero.indices) {
        // Ponemos las coordenadas, pero le sumamos 1 para que sea más facil
        print(" - ${i + 1} -")
        // Mostramos las casillas
        for (j in buscaminas.tablero[i].indices) {
            print(" | ${buscaminas.tablero[i][j].celda} |")
        }
        println()
    }
    println()
}