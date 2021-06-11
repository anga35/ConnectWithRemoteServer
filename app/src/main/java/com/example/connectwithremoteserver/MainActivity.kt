package com.example.connectwithremoteserver

import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.net.*

class MainActivity : AppCompatActivity() {
    var messageList=ArrayList<Message>()
    lateinit var writer: DataOutputStream
    var handler:Handler?=null
    var send=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        btn_connect.setOnClickListener {
StartSocket().execute()

        }
        var senderThread=Thread(SocketOutput())
        senderThread.start()

        fab_sendMessage.setOnClickListener {
            if(!et_typeMessage.text.isNullOrEmpty()){

                send=true

                messageList.add(Message(et_typeMessage.text.toString(),sent = true))

                refreshMessages()

            }

        }

    }

    inner class SocketOutput():Runnable{
        override fun run() {
            while (true) {
                if (send == true) {

                    writer.writeUTF(et_typeMessage.text.toString())
                    et_typeMessage.text?.clear()
                    send = false
                }
            }
            }


        }




    inner class StartSocket(): AsyncTask<Any, String, String>() {
        var iNetAddress=InetAddress.getByName("192.168.8.177")
        val socketAddress=InetSocketAddress("192.168.8.177",9999)
        var initConnectMsg="Connecting to remote server..."

        lateinit var socket:Socket

        override fun doInBackground(vararg params: Any?): String{
            while(true) {

publishProgress(initConnectMsg)
                try {
                     socket = Socket()



                    socket.connect(socketAddress,10000)
                    if(socket.isConnected){
                        publishProgress("Connected.")
                        break
                    }
                }
                catch (e:ConnectException){
                    initConnectMsg=initConnectMsg+"\n Connection not found,Reconnecting..."
                    publishProgress(initConnectMsg)
                }
                catch(e:SocketTimeoutException){
                    initConnectMsg=initConnectMsg+"\n Connection not found,Reconnecting..."
                    publishProgress(initConnectMsg)
                }



            }

            handler=Handler(Looper.getMainLooper())
            handler?.post {
                btn_connect.visibility = View.GONE
                rv_messages.visibility=View.VISIBLE
                et_typeMessage.visibility=View.VISIBLE
                fab_sendMessage.visibility=View.VISIBLE

            }

            var reader=DataInputStream(socket.getInputStream())
             writer=DataOutputStream(socket.getOutputStream())



            while(true){
                var currentMessage=reader.readUTF()
                messageList.add(Message(currentMessage,false))
                handler= Handler(Looper.getMainLooper())
            handler!!.post {
            refreshMessages()
            }

            }




            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)

            tv_terminal.text= values[0]

        }


    }

    fun refreshMessages(){
       var messageAdapter=MessageAdapter(this,messageList)
       rv_messages.layoutManager=LinearLayoutManager(this)
        rv_messages.adapter=messageAdapter

    }


}