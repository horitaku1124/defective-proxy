package com.github.horitaku1124.kotlin

import java.net.Socket
import com.github.horitaku1124.kotlin.util.ByteUtils.Companion.sliceToStr

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
        println(">>>" + len + " " + sliceToStr(buf, 20))
        toUpper.write(buf, 0, len)
      }

//      println("fromUpper.available()=" + fromUpper.available())
      if (fromUpper.available() > 0) {
        var len = fromUpper.read(buf)
        if (len < 0) {
          break
        }
        println(">>>" + len + " " + sliceToStr(buf, 20))
        toLower.write(buf, 0, len)
      }
      sleep(50)

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
    lower.close()
    upper.close()
    println("close")

  }
}