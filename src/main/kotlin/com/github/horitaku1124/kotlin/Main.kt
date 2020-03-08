package com.github.horitaku1124.kotlin
import java.net.ServerSocket
import java.net.Socket

class Main {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val downPort = args[0].toInt()
      var (host, port) = args[1].let { str ->
        if (str.contains(':')) {
          str.split(":").let {list ->
            Pair(list[0], list[1].toInt())
          }
        } else {
          Pair("localhost", str.toInt())
        }
      }
      val pattern = if (args.size > 2) args[2] else "Transfer"
      val proxySock = ServerSocket(downPort)

      println("$downPort -> $port")
      while (true) {
        println("*start accept")
        val accept = proxySock.accept()

        val clientSocket = Socket(host, port)

        if (pattern == "AutoClose") {
          val autoClose = AutoClose(accept!!, clientSocket)
          autoClose.timeout = args[3].toLong()
          autoClose.start()
          autoClose.join()
          break
        } else if (pattern == "NoPipe") {
          val noPipe = NoPipe(accept!!, clientSocket)
          noPipe.timeout = args[3].toLong()
          noPipe.start()
          noPipe.join()
          break
        } else {
          val transfer = Transfer(accept!!, clientSocket)
          transfer.start()
        }
      }
    }
  }
}