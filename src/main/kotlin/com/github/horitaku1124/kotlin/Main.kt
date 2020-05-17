package com.github.horitaku1124.kotlin
import java.io.BufferedWriter
import java.net.ServerSocket
import java.net.Socket
import java.nio.file.Files
import java.nio.file.Path

class Main {
  companion object {
    var logger: BufferedWriter? = null
    fun myLogger(): BufferedWriter {
      if (logger == null) {
        logger = Files.newBufferedWriter(Path.of("./bin.log"))
      }
      return logger!!
    }

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

      println("$downPort -> ${host}:${port}")
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
        } else if (pattern == "BrokenData") {
          val broken = BrokenData(accept!!, clientSocket)
          broken.start()
          broken.join()
          break
        } else if (pattern == "NoPipe") {
          val noPipe = NoPipe(accept!!, clientSocket)
          noPipe.timeout = args[3].toLong()
          noPipe.start()
          noPipe.join()
          break
        } else if (pattern == "Logging") {
          val transfer = LoggingTransfer(accept!!, clientSocket, myLogger())
          transfer.start()
          break
        } else {
          val transfer = Transfer(accept!!, clientSocket)
          transfer.start()
        }
      }
//      if (logger != null) {
//        logger!!.close()
//      }
    }
  }
}