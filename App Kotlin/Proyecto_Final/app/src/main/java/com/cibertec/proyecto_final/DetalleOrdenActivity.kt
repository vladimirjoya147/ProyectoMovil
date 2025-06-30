package com.cibertec.proyecto_final

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DetalleOrdenActivity : AppCompatActivity() {

    private lateinit var spinnerEstado: Spinner
    private lateinit var etNotas: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_orden)

        val toolbar = findViewById<Toolbar>(R.id.toolbarDetalleOrden)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Recibir los datos por intent
        val cliente = intent.getStringExtra("cliente")
        val fecha = intent.getStringExtra("fecha")
        val equipo = intent.getStringExtra("equipo")
        val problema = intent.getStringExtra("problema")
        val estadoRecibido = intent.getStringExtra("estado")
        val tecnico = intent.getStringExtra("tecnico") ?: "No asignado"
        val sede = intent.getStringExtra("sede") ?: "Sin sede"

        // Referencias a los TextView
        findViewById<TextView>(R.id.tvDetalleCliente).text = cliente
        findViewById<TextView>(R.id.tvDetalleFecha).text = fecha
        findViewById<TextView>(R.id.tvDetalleEquipo).text = equipo
        findViewById<TextView>(R.id.tvDetalleProblema).text = problema
        findViewById<TextView>(R.id.tvDetalleTecnico).text = tecnico
        findViewById<TextView>(R.id.tvDetalleSede).text = sede

        // Referencia al EditText de Notas
        etNotas = findViewById(R.id.etNotas)

        // Configuración del Spinner de Estado
        spinnerEstado = findViewById(R.id.spDetalleEstado)
        val estados = arrayOf("Pendiente", "En proceso", "Finalizado")

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = spinnerAdapter

        // Seleccionar el estado recibido
        val posicionEstado = estados.indexOf(estadoRecibido)
        if (posicionEstado >= 0) spinnerEstado.setSelection(posicionEstado)

        // Cambiar color de fondo del Spinner según estado seleccionado
        spinnerEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val estado = estados[position]
                val color = when (estado) {
                    "Pendiente" -> Color.parseColor("#FFA726")   // naranja
                    "En proceso" -> Color.parseColor("#29B6F6") // celeste
                    "Finalizado" -> Color.parseColor("#66BB6A") // verde
                    else -> Color.GRAY
                }
                val fondo = spinnerEstado.background as? GradientDrawable
                fondo?.setColor(color)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
