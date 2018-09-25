import kotlinx.cinterop.CPointer
import ncurses.*

fun CPointer<WINDOW>.getWidth() = getmaxx(this)
fun CPointer<WINDOW>.getHeight() = getmaxy(this)
fun CPointer<WINDOW>.move(positionY: Int, positionX: Int) = mvwin(this, positionY, positionX)
fun CPointer<WINDOW>.clear() = wclear(this)
fun CPointer<WINDOW>.refresh() = wrefresh(this)
fun CPointer<WINDOW>.print(data: String) = wprintw(this, data)