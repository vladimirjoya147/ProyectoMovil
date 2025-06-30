package com.cibertec.proyecto_final

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton

class NuevoEquipoActivity : AppCompatActivity() {

    private lateinit var etNombreEquipo: TextInputEditText
    private lateinit var etSkuEquipo: TextInputEditText
    private lateinit var spTipoEquipo: Spinner
    private lateinit var spSede: Spinner
    private lateinit var etDescripcion: TextInputEditText
    private lateinit var btnGuardar: MaterialButton
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_equipo)

        // Referencias
        etNombreEquipo = findViewById(R.id.etNombreEquipo)
        etSkuEquipo = findViewById(R.id.etSkuEquipo)
        spTipoEquipo = findViewById(R.id.spTipoEquipo)
        spSede = findViewById(R.id.spSedeEquipo)
        etDescripcion = findViewById(R.id.etDescripcionEquipo)
        btnGuardar = findViewById(R.id.btnGuardarEquipo)
        toolbar = findViewById(R.id.toolbarNuevoEquipo)

        // Toolbar navegación
        toolbar.setNavigationOnClickListener { finish() }

        // Configurar opciones de Spinner Tipo
        val tipos = arrayOf("Selecciona tipo", "Portátil", "Monitor", "PC Escritorio", "Red", "Impresora")
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipoEquipo.adapter = tipoAdapter

        // Configurar opciones de Spinner Sede
        val sedes = arrayOf("Selecciona sede", "Sede Trujillo", "Sede Arequipa", "Sede Lima", "Sede Cusco", "Sede Piura", "Sede Chiclayo")
        val sedeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sedes)
        sedeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSede.adapter = sedeAdapter

        // Guardar equipo
        btnGuardar.setOnClickListener {
            val nombre = etNombreEquipo.text.toString()
            val sku = etSkuEquipo.text.toString()
            val tipo = spTipoEquipo.selectedItem.toString()
            val sede = spSede.selectedItem.toString()
            val descripcion = etDescripcion.text.toString()

            if (nombre.isEmpty() || sku.isEmpty() || tipo == "Selecciona tipo" || sede == "Selecciona sede") {
                Toast.makeText(this, "Completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = intent.apply {
                putExtra("nombre", nombre)
                putExtra("sku", sku)
                putExtra("tipo", tipo)
                putExtra("descripcion", descripcion)
                putExtra("sede", sede)
            }
            setResult(RESULT_OK, intent)

            // Aquí podrías guardar en BD o enviarlo a lista
            Toast.makeText(this, "Equipo '$nombre' registrado correctamente.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
