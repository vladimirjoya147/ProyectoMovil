package com.cibertec.proyecto_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

class ListaOrdenesActivity : AppCompatActivity() {

    private lateinit var rvOrdenes: RecyclerView
    private lateinit var adapter: OrdenServicioAdapter
    private val listaOrdenes = mutableListOf<MantenimientoDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ordenes)

        val toolbar = findViewById<Toolbar>(R.id.toolbarListaOrdenes)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        rvOrdenes = findViewById(R.id.rvOrdenes)
        rvOrdenes.layoutManager = LinearLayoutManager(this)
        adapter = OrdenServicioAdapter(listaOrdenes)
        rvOrdenes.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val orden = listaOrdenes[posicion]

                AlertDialog.Builder(this@ListaOrdenesActivity)
                    .setTitle("Eliminar orden")
                    .setMessage("¿Deseas eliminar esta orden de servicio?")
                    .setPositiveButton("Sí") { _, _ ->
                        if (isInternetAvailable()) {
                            FirebaseAuth.getInstance().currentUser?.getIdToken(true)
                                ?.addOnSuccessListener { result ->
                                    RetrofitClient.SessionManager.firebaseToken = result.token

                                    RetrofitClient.instance.eliminarMantenimiento(orden.id)
                                        .enqueue(object : Callback<ResponseBody> {
                                            override fun onResponse(
                                                call: Call<ResponseBody>,
                                                response: Response<ResponseBody>
                                            ) {
                                                Toast.makeText(
                                                    this@ListaOrdenesActivity,
                                                    "Orden eliminada correctamente",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                listaOrdenes.removeAt(posicion)
                                                adapter.notifyItemRemoved(posicion)
                                            }

                                            override fun onFailure(
                                                call: Call<ResponseBody>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    this@ListaOrdenesActivity,
                                                    "Error al eliminar orden",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                adapter.notifyItemChanged(posicion)
                                            }
                                        })
                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(
                                        this@ListaOrdenesActivity,
                                        "Error de autenticación",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    adapter.notifyItemChanged(posicion)
                                }
                        } else {
                            Toast.makeText(
                                this@ListaOrdenesActivity,
                                "Sin conexión",
                                Toast.LENGTH_SHORT
                            ).show()
                            adapter.notifyItemChanged(posicion)
                        }
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        adapter.notifyItemChanged(posicion)
                    }
                    .setCancelable(false)
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(rvOrdenes)

        cargarOrdenesDesdeApi()
    }


    private fun cargarOrdenesDesdeApi() {
        RetrofitClient.instance.listarMantenimientos().enqueue(object :
            Callback<List<MantenimientoDTO>> {
            override fun onResponse(
                call: Call<List<MantenimientoDTO>>,
                response: Response<List<MantenimientoDTO>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    listaOrdenes.clear()
                    listaOrdenes.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@ListaOrdenesActivity,
                        "Error al obtener órdenes",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<MantenimientoDTO>>, t: Throwable) {
                Toast.makeText(this@ListaOrdenesActivity, "Fallo de red", Toast.LENGTH_SHORT).show()
                Log.e("API", "Error: ${t.localizedMessage}", t)
            }
        })
    }

    private fun isInternetAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
