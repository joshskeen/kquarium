import konan.worker.*
import kotlinx.cinterop.*
import platform.posix.*
import ncurses.*

val sealife: MutableList<Fish> = (
        (0..3).map { Flounder() } +
                (0..1).map { Shark() } +
                Octopus() +
                Guppy() +
                SeaDemon()
        ).toMutableList()

fun main(args: Array<String>) {
    Game.run()
}

object Game {
    val window: CPointer<WINDOW> = initscr()!!
    val width: Int = window.pointed._maxx.toInt()
    val height: Int = window.pointed._maxy.toInt()

    init {
        start_color()
    }

    fun run() {
        curs_set(0)
        while (true) {
            window.clear()
            sealife.forEach {
                window.refresh()
                it.update()
            }
            sealife.map { outer ->
                val contains = sealife.map { inner -> inner.hitTest(outer) }.contains(true)
                if (contains) outer.play()
            }.all { false }
            usleep(110000)
        }
    }
}

data class FishData(val x: Int, val y: Int)

fun Int.randRange(min: Int) = rand() % (this - min)