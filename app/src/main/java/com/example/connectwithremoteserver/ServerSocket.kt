package com.example.connectwithremoteserver

import java.io.*
import java.lang.Exception
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

lateinit var serverSocket: ServerSocket

class ServerOutput(val outputStream: DataOutputStream) : Runnable {
    var writer:DataOutputStream?=null

    init {
        writer=outputStream
    }

    override  fun run() {
        while(true) {

            var inputScanner = Scanner(System.`in`)
            var inputStr = inputScanner.nextLine()
            println("Output:")
            writer?.writeUTF(inputStr)

        }
    }

}

class ClientThread(val socket:Socket,userName:String):Runnable{
        var message:String=""
    override fun  run() {

        var writer=DataOutputStream(socket.getOutputStream())

        var writeThread=Thread(ServerOutput(writer))
        writeThread.start()
        while(true){
            var reader=DataInputStream(socket.getInputStream())
            message=reader.readUTF()


        }


    }

}


    fun main() {


            try {
                serverSocket = ServerSocket(9999)
                serverSocket.soTimeout = 100000
                print("Waiting for connections\n")

                var communeSocket= serverSocket.accept()


                if(communeSocket.isConnected){
                    println("Connected to ${communeSocket.remoteSocketAddress}")

                    var reader=DataInputStream(communeSocket.getInputStream())
                    var writer=DataOutputStream(communeSocket.getOutputStream())
                    var writeThread=Thread(ServerOutput(writer))
                    writeThread.start()
                    while(true){
                        print(reader.readUTF())
                        print("Input:")


                    }



                }






            } catch (e: IOException) {

            }





}
