import konan.worker.Future
import konan.worker.startWorker
import ncurses.*
import platform.AppKit.NSSound

class Guppy : Fish("art/fishone.txt", 4) {
    override val samplePrefix = "lt"
    override val numSamples = 1
}

class SeaDemon : Fish("art/seademon.txt", 1) {
    override val samplePrefix = "demon"
    override val numSamples = 2
    override fun play() {
        if(20.randRange(0) >= 19) {
            super.play()
        }
    }
}

class Octopus : Fish("art/fishfour.txt", 4) {
    override val samplePrefix = "arp"
    override val numSamples = 3
}

class Shark : Fish("art/fishtwo.txt") {
    override val samplePrefix = "cp"
    override val numSamples = 5
}

//fast and low to the bottom of the ocean, the flounder emanates phat bass as he cruises
class Flounder : Fish("art/fishthree.txt", 6)

abstract class Fish(filePath: String, private val speed: Int = 3) {
    private val asciiData: List<String> = readFileData(filePath).split("\n")
    private val windowWidth = asciiData.max()!!.length + 5
    private val windowHeight = asciiData.size + 1
    open val samplePrefix = "bd"
    open val numSamples = 7

    var playing = false
    val hitSize = 2
    var posX = (Game.width - windowWidth).randRange(5)
    var posY = (Game.height - windowHeight).randRange(5)
    private val maxX: Int
        get() = (posX + hitSize)
    private val maxY: Int
        get() = (posY + hitSize)

    val window = newwin(windowHeight, windowWidth, posY, posX)!!

    fun sampleNumber(): Int {
        val stepSize = Game.height / numSamples
        var result = 1
        0.until(numSamples).map { it * stepSize..(it * stepSize + stepSize - 1) }
                .forEachIndexed { index: Int, list: IntRange ->
                    if (list.contains(this.posY)) {
                        result = index + 1
                    }
                }
        return result
    }
    
    open fun play() {
        val file = "sounds/$samplePrefix/$samplePrefix-${sampleNumber()}.wav"
        val nsSound = NSSound(file, false)
        nsSound.setVolume(posX.toFloat() / Game.width.toFloat())
        nsSound.stop()
        nsSound.play()
    }

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

    fun hitTest(fish: Fish) = when {
        fish.maxX < posX || fish.posX > maxX || fish.maxY > posY || fish.posY > maxY -> {
            false
        }
        else -> {
            true
        }
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
//            clear()
            move(posY, posX)
            print(frame)
        }
        posX += computeX
        posY += computeY
    }

    fun toData(): FishData {
        return FishData(posX, posY)
    }

}
