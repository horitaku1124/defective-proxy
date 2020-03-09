package com.github.horitaku1124.kotlin

import java.net.Socket

class NoPipe(private var lower: Socket, var upper: Socket) : java.lang.Thread() {
  private var cm: CommunicationManager = CommunicationManager(lower, upper)
  var timeout: Long = 0
  override fun run() {
    val buf = ByteArray(1024 * 1024)
    var started = System.currentTimeMillis()
    while (lower.isConnected && !lower.isClosed && upper.isConnected && !upper.isClosed) {
//      println("fromLower.available()=" + fromLower.available())
      var elapsed = System.currentTimeMillis() - started
      if (elapsed < timeout) {

        if (cm.fromLower.available() > 0) {
          var len = cm.fromLower.read(buf)
          if (len < 0) {
            break
          }
          var limit = if (len > 20) 20 else len
          println(">>>" + len + " " + String(buf, 0, limit))
          cm.toUpper.write(buf, 0, len)
        }

//      println("fromUpper.available()=" + fromUpper.available())
        if (cm.fromUpper.available() > 0) {
          var len = cm.fromUpper.read(buf)
          if (len < 0) {
            break
          }
          var limit = if (len > 20) 20 else len
          println("<<<" + len + " " + String(buf, 0, limit))
          cm.toLower.write(buf, 0, len)
        }
      }
      sleep(50)
    }
    println("finish")
    cm.fromLower.close()
    cm.toLower.close()
    cm.fromUpper.close()
    cm.toUpper.close()
    println("close")

  }
}