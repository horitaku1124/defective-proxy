package com.github.horitaku1124.kotlin

import com.github.horitaku1124.kotlin.util.ByteUtils.Companion.sliceToStr
import java.net.Socket

class Transfer(private var lower: Socket, var upper: Socket) : java.lang.Thread() {
  override fun run() {
    val fromLower = lower.getInputStream()
    val toLower = lower.getOutputStream()
    val fromUpper = upper.getInputStream()
    val toUpper = upper.getOutputStream()

    val buf = ByteArray(1024 * 1024)
    while (lower.isConnected && !lower.isClosed && upper.isConnected && !upper.isClosed) {
//      println("fromLower.available()=" + fromLower.available())
      if (fromLower.available() > 0) {
        val len = fromLower.read(buf)
        if (len < 0) {
          break
        }
        println(">>>" + len + " " + sliceToStr(buf, 20))
        toUpper.write(buf, 0, len)
      }

//      println("fromUpper.available()=" + fromUpper.available())
      if (fromUpper.available() > 0) {
        val len = fromUpper.read(buf)
        if (len < 0) {
          break
        }
        println("<<<" + len + " " + sliceToStr(buf, 20))
        toLower.write(buf, 0, len)
      }
      sleep(50)
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