package com.example.ladm_u4_tarea1_calllog

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    val siLecturaLlamadas=10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Llamadas recibidas")

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siLecturaLlamadas)
        }
        button2.setOnClickListener {
            cargarListaLlamadas()
        }
    }

    @SuppressLint("Range")
    private fun cargarListaLlamadas() {
        var res ="Llamadas: \n\n"
        var cursorLlamadas = contentResolver.query(
            CallLog.Calls.CONTENT_URI,null,null,null,null
        )
        if(cursorLlamadas != null){
            if(cursorLlamadas.moveToFirst()){
                do{
                    var idLlamada = cursorLlamadas.getInt(cursorLlamadas.getColumnIndex(CallLog.Calls._ID))
                    var nombre = cursorLlamadas.getString(cursorLlamadas.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    var numLlamada = cursorLlamadas.getString(cursorLlamadas.getColumnIndex(CallLog.Calls.CACHED_FORMATTED_NUMBER))
                    var fechaLlamada = cursorLlamadas.getLong(cursorLlamadas.getColumnIndex(CallLog.Calls.DATE))
                    var dateCall = obtenerFecha(fechaLlamada)
                    res +="ID: "+idLlamada+"\nNombre: "+nombre+"\nNumero de la llamada: "+numLlamada+"\nFecha de la llamada: "+dateCall+"\n\n-------------------\n\n"
                }while (cursorLlamadas.moveToNext())
                textView.setText(res)
            }
            else{
                res = "Llamadas:\nNo hay llamadas guardadas"
            }
        }
    }

    private fun obtenerFecha(s: Long): String? {
        try {
            val formato = SimpleDateFormat("dd/MM/yyyy")
            val fechaNeta = Date(s)
            return formato.format(fechaNeta)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == siLecturaLlamadas){
            setTitle("Permiso Otorgado")
        }
    }
}