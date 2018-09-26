import ncurses.*

class Guppy : Fish("art/fishone.txt", 4)
class Octopus : Fish("art/fishtwo.txt", 1)
class Flounder : Fish("art/fishthree.txt", 3)

//fish-related behavior truncated
open class Fish(filePath: String, private val speed: Int = 3) {
    private val asciiData: List<String> = readFileData(filePath).split("\n")
    private val windowWidth = asciiData.max()!!.length + 5
    private val windowHeight = asciiData.size + 1
    private var posX = (Game.width - windowWidth).randRange(5)
    private var posY = (Game.height - windowHeight).randRange(5)
    private val window = newwin(windowHeight, windowWidth, posY, posX)!!
    private var directionX = 1
    private var directionY = 1
    private val computeX: Int
        get() {
            val rand = speed.randRange(-speed)
            val move = posX + rand * directionX
            if (move >= Game.width || move <= 0) {
                directionX *= -1
            }
            return rand * directionX
        }

    private val computeY: Int
        get() {
            val rand = speed.randRange(-3)
            val move = posY + rand * directionY
            if (move >= Game.height || move <= 0) {
                directionY *= -1
            }
            return rand * directionY
        }

    private val frame
        get() = asciiData.joinToString("\n")
        { if (directionX == -1) it else it.reversed() }

    fun update() {
        window.run {
            refresh()
            clear()
            move(posY, posX)
            print(frame)
        }
        posX += computeX
        posY += computeY
    }

}
