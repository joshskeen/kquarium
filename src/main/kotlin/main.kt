import konan.worker.*
import kotlinx.cinterop.*
import platform.posix.*
import ncurses.*

val sealife: MutableList<Fish> = ((0..4).map { Guppy() } +
        (0..0).map { Octopus() } +
        (0..2).map { Flounder() }).toMutableList()

fun main(args: Array<String>) {
    Game.run()
    println("thread's id: ${pthread_self()}")
}

object Game {
    val window: CPointer<WINDOW> = initscr()!!
    val width: Int = window.pointed._maxx.toInt()
    val height: Int = window.pointed._maxy.toInt()

    fun run() {
        curs_set(0)
        while (true) {
            window.clear()
            sealife.forEach {
                window.refresh()
                it.update()
            }
            window.refresh()
            sealife.forEach { fish ->
                sealife.forEach {
                    if (it != fish && fish.hitTest(it)) {
                        libcsynth.play(it.posX * 15, 50)
                    }
                }
            }
        }
    }
}

data class FishData(val x: Int, val y: Int)

fun Int.randRange(min: Int) = rand() % (this - min)