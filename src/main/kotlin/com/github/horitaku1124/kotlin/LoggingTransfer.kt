package com.github.horitaku1124.kotlin

import com.github.horitaku1124.kotlin.util.ByteUtils.Companion.sliceToStr
import java.io.BufferedWriter
import java.net.Socket
import java.time.LocalDateTime
import java.util.*

class LoggingTransfer(private var lower: Socket, var upper: Socket, var logger: BufferedWriter) : java.lang.Thread() {
  private var cm: CommunicationManager = CommunicationManager(lower, upper)
  override fun run() {
    val buf = ByteArray(1024 * 1024)
    while (lower.isConnected && !lower.isClosed && upper.isConnected && !upper.isClosed) {
      if (cm.fromLower.available() > 0) {
        val len = cm.fromLower.read(buf)
        if (len < 0) {
          break
        }
        println(">>>" + len + " " + sliceToStr(buf, 20))
        cm.toUpper.write(buf, 0, len)
        logger.write(
          String.format("% 4d %s >> % 6d %s\n",
            currentThread().id,
            LocalDateTime.now().toString(),
            len,
            sliceToStr(buf, 150)
        ))
        logger.flush()
      }

      if (cm.fromUpper.available() > 0) {
        val len = cm.fromUpper.read(buf)
        if (len < 0) {
          break
        }
        println("<<<" + len + " " + sliceToStr(buf, 20))
        cm.toLower.write(buf, 0, len)

        logger.write(
          String.format("% 4d %s << % 6d %s\n",
            currentThread().id,
            LocalDateTime.now().toString(),
            len,
            sliceToStr(buf, 150)
          ))
        logger.flush()
      }
      sleep(3)
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