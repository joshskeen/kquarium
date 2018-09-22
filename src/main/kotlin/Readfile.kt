import kotlinx.cinterop.*
import platform.posix.*

fun readFileData(path: String) = memScoped {
    val charArray = allocArray<ByteVar>(1000)
    val filePointer = fopen(path, "r")
    val sb = StringBuilder()
    filePointer?.let {
        while (fgets(charArray, 1000, filePointer) != null) {
            sb.append(charArray.toKString())
        }
        filePointer.closeFile()
    }
    sb.toString()
}


fun CPointer<FILE>.closeFile() = fclose(this)