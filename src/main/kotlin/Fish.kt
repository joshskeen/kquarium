import ncurses.*

class Guppy : Fish("art/fishone.txt", 4)
class Octopus : Fish("art/fishtwo.txt", 1)
class Flounder : Fish("art/fishthree.txt", 3)

open class Fish(filePath: String, private val stepSize: Int = 3) {
    val asciiData: List<String> = readFileData(filePath).split("\n")
    val windowWidth = asciiData.max()!!.length + 5
    val windowHeight = asciiData.size + 1
    var posX = (Game.width - windowWidth).randRange(5)
    var posY = (Game.height - windowHeight).randRange(5)
    val window = newwin(windowHeight, windowWidth, posY, posX)!!
    var directionX = 1
    var directionY = 1
    val computeX: Int
        get() {
            val rand = stepSize.randRange(-stepSize)
            val move = posX + rand * directionX
            if (move >= Game.width || move <= 0) {
                directionX *= -1
            }
            return rand * directionX
        }

    val computeY: Int
        get() {
            val rand = stepSize.randRange(-3)
            val move = posY + rand * directionY
            if (move >= Game.height || move <= 0) {
                directionY *= -1
            }
            return rand * directionY
        }

    val frame
        get() = asciiData.joinToString("\n")
        { if (directionX == -1) it else it.reversed() }


    fun debug() = window.print("w: $windowWidth, h: $windowHeight")//window.print("x: $posX y: $posY, $windowWidth $windowHeight, gw: ${Game.width}, gh ${Game.height}")

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
