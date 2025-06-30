package com.cibertec.proyecto_final

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbarMain)
        setSupportActionBar(toolbar)

        tvBienvenida = findViewById(R.id.tvBienvenida)

        // Obtener el UID(identificador) del usuario actual
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            FirebaseFirestore.getInstance().collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: "Usuario"
                        tvBienvenida.text = "Bienvenido, $nombre"
                    } else {
                        tvBienvenida.text = "Bienvenido, Usuario"
                    }
                }
                .addOnFailureListener {
                    tvBienvenida.text = "Bienvenido, Usuario"
                }
        } else {
            tvBienvenida.text = "Bienvenido, Usuario"
        }

        val btnVerOrdenes = findViewById<Button>(R.id.btnVerOrdenes)
        val btnVerEquipos = findViewById<Button>(R.id.btnVerEquipos)

        btnVerOrdenes.setOnClickListener {
            startActivity(Intent(this, ListaOrdenesActivity::class.java))
        }

        btnVerEquipos.setOnClickListener {
            startActivity(Intent(this, ListaEquiposActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {

                // Cierre de sesión Firebase
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
