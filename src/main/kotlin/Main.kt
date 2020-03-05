
import java.net.ServerSocket
import java.net.Socket

class Main {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val downPort = args[0].toInt()
      val upPort = args[1].toInt()
      val proxySock = ServerSocket(downPort)

      println("$downPort -> $upPort")
      while (true) {
        val accept = proxySock.accept()

        val clientSocket = Socket("localhost", upPort)
        val transfer = Transfer(accept!!, clientSocket)
        transfer.start()
      }
    }
  }
}