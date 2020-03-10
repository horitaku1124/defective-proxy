package com.github.horitaku1124.kotlin

import com.github.horitaku1124.kotlin.util.ByteUtils
import com.github.horitaku1124.kotlin.util.ByteUtils.Companion.sliceToStr
import java.net.Socket

class NoPipe(private var lower: Socket, var upper: Socket) : java.lang.Thread() {
  private var cm: CommunicationManager = CommunicationManager(lower, upper)
  var timeout: Long = 0
  override fun run() {
    val buf = ByteArray(1024 * 1024)
    val started = System.currentTimeMillis()
    while (lower.isConnected && !lower.isClosed && upper.isConnected && !upper.isClosed) {
//      println("fromLower.available()=" + fromLower.available())
      val elapsed = System.currentTimeMillis() - started
      if (elapsed < timeout) {
        if (cm.fromLower.available() > 0) {
          val len = cm.fromLower.read(buf)
          if (len < 0) {
            break
          }
          println(">>>" + len + " " + sliceToStr(buf, 20))
          cm.toUpper.write(buf, 0, len)
        }

//      println("fromUpper.available()=" + fromUpper.available())
        if (cm.fromUpper.available() > 0) {
          val len = cm.fromUpper.read(buf)
          if (len < 0) {
            break
          }
          println("<<<" + len + " " + sliceToStr(buf, 20))
          cm.toLower.write(buf, 0, len)
        }
      } else {
        if (cm.fromLower.available() > 0) {
          val len = cm.fromLower.read(buf)
          if (len < 0) {
            break
          }
          println(">>>" + len)
        }

//      println("fromUpper.available()=" + fromUpper.available())
        if (cm.fromUpper.available() > 0) {
          val len = cm.fromUpper.read(buf)
          if (len < 0) {
            break
          }
          println("<<<" + len)
        }
      }
      sleep(10)
    }
    println("finish")
    cm.fromLower.close()
    cm.toLower.close()
    cm.fromUpper.close()
    cm.toUpper.close()
    println("close")

  }
}