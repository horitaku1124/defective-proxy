package com.github.horitaku1124.kotlin

import java.net.Socket
import com.github.horitaku1124.kotlin.util.ByteUtils.Companion.sliceToStr

class AutoClose(private var lower: Socket, var upper: Socket) : java.lang.Thread() {
  private var cm: CommunicationManager = CommunicationManager(lower, upper)
  var timeout: Long = 0
  override fun run() {
    val buf = ByteArray(1024 * 1024)
    var started = System.currentTimeMillis()
    while (lower.isConnected && !lower.isClosed && upper.isConnected && !upper.isClosed) {
//      println("fromLower.available()=" + fromLower.available())
      if (cm.fromLower.available() > 0) {
        var len = cm.fromLower.read(buf)
        if (len < 0) {
          break
        }
        println(">>>" + len + " " + sliceToStr(buf, 20))
        cm.toUpper.write(buf, 0, len)
      }

//      println("fromUpper.available()=" + fromUpper.available())
      if (cm.fromUpper.available() > 0) {
        var len = cm.fromUpper.read(buf)
        if (len < 0) {
          break
        }
        println("<<<" + len + " " + sliceToStr(buf, 20))
        cm.toLower.write(buf, 0, len)
      }
      sleep(10)

      var elapsed = System.currentTimeMillis() - started
      if (elapsed > timeout) {
        println("timeout")
        break
      }
    }
    println("finish")
    cm.fromLower.close()
    cm.toLower.close()
    cm.fromUpper.close()
    cm.toUpper.close()
    lower.close()
    upper.close()
    println("close")

  }
}