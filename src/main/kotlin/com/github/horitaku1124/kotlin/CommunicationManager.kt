package com.github.horitaku1124.kotlin

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class CommunicationManager(lower: Socket, upper: Socket) {
  var fromLower: InputStream
  var toLower: OutputStream
  var fromUpper: InputStream
  var toUpper: OutputStream

  init {
    fromLower = lower.getInputStream()
    toLower = lower.getOutputStream()
    fromUpper = upper.getInputStream()
    toUpper = upper.getOutputStream()
  }
}