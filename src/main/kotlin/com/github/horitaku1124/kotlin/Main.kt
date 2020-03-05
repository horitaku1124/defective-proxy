package com.github.horitaku1124.kotlin
import java.net.ServerSocket
import java.net.Socket

class Main {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val downPort = args[0].toInt()
      val upPort = args[1].toInt()
      val pattern = if (args.size > 2) args[2] else "com.github.horitaku1124.kotlin.Transfer"
      val proxySock = ServerSocket(downPort)

      println("$downPort -> $upPort")
      while (true) {
        println("*start accept")
        val accept = proxySock.accept()

        val clientSocket = Socket("localhost", upPort)

        if (pattern == "com.github.horitaku1124.kotlin.AutoClose") {
          val autoClose = AutoClose(accept!!, clientSocket)
          autoClose.timeout = args[3].toLong()
          autoClose.start()
          autoClose.join()
          break
        } else {
          val transfer = Transfer(accept!!, clientSocket)
          transfer.start()
        }
      }
    }
  }
}