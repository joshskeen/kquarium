import kotlinx.cinterop.*
import ncurses.*
import platform.posix.*

val sealife: MutableList<Fish> = ((0..4).map { Guppy() } +
        (0..0).map { Octopus() } +
        (0..2).map { Flounder() }).toMutableList()

fun main(args: Array<String>) {
    Game.run()
}

object Game {
    private var mainWindow: CPointer<WINDOW> = initscr()!!
    val width: Int = mainWindow.pointed._maxx.toInt()
    val height: Int = mainWindow.pointed._maxy.toInt()

    fun run() {
        curs_set(0)
        while (true) {
            mainWindow.clear()
            for (fish in sealife) {
                mainWindow.refresh()
                fish.update()
            }
            mainWindow.refresh()
            usleep(1000 * 350)
        }
    }
}

fun Int.randRange(min: Int) = rand() % (this - min)