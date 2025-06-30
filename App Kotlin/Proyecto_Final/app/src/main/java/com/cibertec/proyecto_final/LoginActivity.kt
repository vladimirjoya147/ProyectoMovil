package com.cibertec.proyecto_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var btnCrearCuenta: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnIniciarSesion.setOnClickListener {
            val alias = etUsuario.text.toString().trim()
            val contrasena = etPassword.text.toString().trim()

            if (alias.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Paso 1: Buscamos el correo asociado al usuario(alias)
            db.collection("usuarios")
                .whereEqualTo("usuario", alias)
                .get()
                .addOnSuccessListener { documentos ->
                    if (!documentos.isEmpty) {
                        val doc = documentos.documents[0]
                        val correo = doc.getString("correo") ?: ""

                        // Paso 2: Conectamos con el correo encontrado y contraseña
                        auth.signInWithEmailAndPassword(correo, contrasena)
                            .addOnSuccessListener {
                                FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.
                                    addOnSuccessListener {
                                        val token = it.token
                                        RetrofitClient.SessionManager.firebaseToken = token
                                        Log.d("FirebaseToken", "Token guardado: $token")
                                    }?.addOnFailureListener{
                                    Log.e("FirebaseToken", "Error al obtener token", it)
                                }
                                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error de autenticación: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al buscar usuario: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        btnCrearCuenta.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}
