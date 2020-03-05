import java.net.Socket

class Transfer(private var lower: Socket, var upper: Socket) : java.lang.Thread() {
  override fun run() {
    val fromLower = lower.getInputStream()
    val toLower = lower.getOutputStream()
    val fromUpper = upper.getInputStream()
    val toUpper = upper.getOutputStream()

    val buf = ByteArray(1024 * 1024)
    while (lower.isConnected && !lower.isClosed) {
      if (fromLower.available() > 0) {
        var len = fromLower.read(buf)
        if (len < 0) {
          break
        }
        println("> " + len)
        toUpper.write(buf, 0, len)
      }

      if (fromUpper.available() > 0) {
        var len = fromUpper.read(buf)
        if (len < 0) {
          break
        }
        println("< " + len)
        toLower.write(buf, 0, len)
      }
      Thread.sleep(10)
    }
    println("finish")
  }
}