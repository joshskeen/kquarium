import kotlinx.cinterop.*
import platform.posix.*

fun readFileData(path: String) = memScoped {
    val pagesize = getpagesize()
    val charArray = allocArray<ByteVar>(pagesize)
    val filePointer = fopen(path, "r")
    val sb = StringBuilder()
    filePointer?.let {
        while (fgets(charArray, pagesize, filePointer) != null) {
            sb.append(charArray.toKString())
        }
        filePointer.closeFile()
    }
    sb.toString()
}


fun CPointer<FILE>.closeFile() = fclose(this)