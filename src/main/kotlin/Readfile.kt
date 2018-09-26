import kotlinx.cinterop.*
import platform.posix.*

fun readFileData(path: String) = memScoped {
    val pagesize = getpagesize()
    val stringBuilder = StringBuilder()
    val charArray = allocArray<ByteVar>(pagesize)
    val filePointer: CPointer<FILE>? = fopen(path, "r")!!
    filePointer?.let {
        while (filePointer.readLine(charArray, pagesize) != null) {
            stringBuilder.append(charArray.toKString())
        }
        filePointer.closeFile()
    }
    stringBuilder.toString()
}

fun CPointer<FILE>.readLine(charArray: CArrayPointer<ByteVar>, size: Int) = fgets(charArray, size, this)
fun CPointer<FILE>.closeFile() = fclose(this)