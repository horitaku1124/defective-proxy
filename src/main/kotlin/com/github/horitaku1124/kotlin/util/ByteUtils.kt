package com.github.horitaku1124.kotlin.util

class ByteUtils {
  companion object {
    fun sliceToStr(bytes: ByteArray, len: Int): String {
      val len2 = Math.min(len, bytes.size)
      val sb = StringBuffer(len2)
      for (i in 0 until len2) {
        val c = bytes[i].toChar()
        if (c == '\r' || c == '\n' || c == '\t') {
          sb.append(' ')
        } else {
          sb.append(c)
        }
      }
      return sb.toString()
    }
  }
}