package com.github.horitaku1124.kotlin

import java.net.Socket

class AutoClose(private var lower: Socket, var upper: Socket) : java.lang.Thread() {
  var timeout: Long = 0
  override fun run() {
    val fromLower = lower.getInputStream()
    val toLower = lower.getOutputStream()
    val fromUpper = upper.getInputStream()
    val toUpper = upper.getOutputStream()

    val buf = ByteArray(1024 * 1024)
    var started = System.currentTimeMillis()
    while (lower.isConnected && !lower.isClosed && upper.isConnected && !upper.isClosed) {
//      println("fromLower.available()=" + fromLower.available())
      if (fromLower.available() > 0) {
        var len = fromLower.read(buf)
        if (len < 0) {
          break
        }
        var limit = if (len > 20) 20 else len
        println(">>>" + len + " " + String(buf, 0, limit))
        toUpper.write(buf, 0, len)
      }

//      println("fromUpper.available()=" + fromUpper.available())
      if (fromUpper.available() > 0) {
        var len = fromUpper.read(buf)
        if (len < 0) {
          break
        }
        var limit = if (len > 20) 20 else len
        println("<<<" + len + " " + String(buf, 0, limit))
        toLower.write(buf, 0, len)
      }
      Thread.sleep(50)

      var elapsed = System.currentTimeMillis() - started
      if (elapsed > timeout) {
        println("timeout")
        break
      }
    }
    println("finish")
    fromLower.close()
    toLower.close()
    fromUpper.close()
    toUpper.close()
    println("close")

  }
}