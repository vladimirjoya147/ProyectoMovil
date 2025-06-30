package com.cibertec.proyecto_final

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val etNombre = findViewById<EditText>(R.id.etNombreCompleto)
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val etDni = findViewById<EditText>(R.id.etDni)
        val rgGenero = findViewById<RadioGroup>(R.id.rgGenero)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // Firebase autenticacion y firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val edad = etEdad.text.toString().trim()
            val dni = etDni.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val usuario = etUsuario.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val generoId = rgGenero.checkedRadioButtonId
            val genero = if (generoId != -1) findViewById<RadioButton>(generoId).text.toString() else ""

            // Validaciones
            if (nombre.isEmpty() || edad.isEmpty() || dni.isEmpty() || genero.isEmpty() ||
                correo.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Paso 1: Verificamos si el Usuario(alias) existe
            db.collection("usuarios")
                .whereEqualTo("usuario", usuario)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        Toast.makeText(this, "Ese alias de usuario ya está en uso", Toast.LENGTH_SHORT).show()
                    } else {
                        // Paso 2: Creamos usuario en Firebase Auth con correo y contraseña
                        auth.createUserWithEmailAndPassword(correo, contrasena)
                            .addOnSuccessListener {
                                val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

                                // Paso 3: Guardamos los datos en Firestore
                                val datosUsuario = hashMapOf(
                                    "nombre" to nombre,
                                    "edad" to edad.toInt(),
                                    "dni" to dni,
                                    "genero" to genero,
                                    "correo" to correo,
                                    "usuario" to usuario,
                                    "contrasena" to contrasena // por ahora sin encriptar
                                )

                                db.collection("usuarios").document(uid).set(datosUsuario)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()

                                        // Paso 4: Regresamos al Login
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al registrar: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al verificar alias: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
