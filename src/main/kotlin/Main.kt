
import java.net.ServerSocket
import java.net.Socket

class Main {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      println("ok")
      var portFrom = 6378
      var portTo = 6379
      val proxySock = ServerSocket(portFrom)
      val accept = proxySock.accept()

      val clientSocket = Socket("localhost", portTo)
      var transfer = Transfer(accept!!, clientSocket)
      transfer.start()
    }
  }
}