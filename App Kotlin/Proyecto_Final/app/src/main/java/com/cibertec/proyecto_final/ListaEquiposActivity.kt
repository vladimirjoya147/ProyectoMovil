package com.cibertec.proyecto_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaEquiposActivity : AppCompatActivity() {

    private lateinit var rvEquipos: RecyclerView
    private lateinit var adapter: EquipoAdapter
    private val listaEquipos = mutableListOf<Equipo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_equipos)

        val toolbar = findViewById<Toolbar>(R.id.toolbarListaEquipos)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        rvEquipos = findViewById(R.id.rvEquipos)
        rvEquipos.layoutManager = LinearLayoutManager(this)

        val dbHelper = EquipoDBHelper(this)
        adapter = EquipoAdapter(listaEquipos)
        rvEquipos.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val equipo = listaEquipos[posicion]

                if (direction == ItemTouchHelper.RIGHT) {
                    mostrarDialogoEditar(equipo, posicion, dbHelper)
                } else if (direction == ItemTouchHelper.LEFT) {
                    AlertDialog.Builder(this@ListaEquiposActivity)
                        .setTitle("Eliminar equipo")
                        .setMessage("¿Deseas eliminar este equipo?")
                        .setPositiveButton("Sí") { _, _ ->
                            Log.i("SYNC", "Eid del equipo"+equipo.id)
                            if (equipo.id > 0 && isInternetAvailable()) {
                                FirebaseAuth.getInstance().currentUser?.getIdToken(true)
                                    ?.addOnSuccessListener { result ->
                                        RetrofitClient.SessionManager.firebaseToken = result.token

                                        RetrofitClient.instance.eliminarEquipo(equipo.id).enqueue(object : Callback<ResponseBody> {
                                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                                Log.i("SYNC", "Eliminado de la API")
                                                Toast.makeText(this@ListaEquiposActivity, "Equipo eliminado correctamente", Toast.LENGTH_SHORT).show()

                                                dbHelper.eliminarEquipo(equipo.id)

                                                val index = listaEquipos.indexOfFirst { it.id == equipo.id }
                                                if (index != -1) {
                                                    listaEquipos.removeAt(index)
                                                    adapter.notifyItemRemoved(index)
                                                } else {
                                                    Log.w("SYNC", "El equipo con id ${equipo.id} no estaba en la lista visible")
                                                    adapter.notifyDataSetChanged()
                                                }
                                            }


                                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                                Log.e("SYNC", "Fallo al eliminar en API", t)
                                                Toast.makeText(this@ListaEquiposActivity, "Error al eliminar equipo", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }
                                    ?.addOnFailureListener {
                                        Log.e("AUTH", "No se pudo obtener el token", it)
                                        Toast.makeText(this@ListaEquiposActivity, "Error de autenticación", Toast.LENGTH_SHORT).show()
                                    }
                            }


                        }
                        .setNegativeButton("Cancelar") { _, _ ->
                            adapter.notifyItemChanged(posicion)
                        }
                        .show()
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(rvEquipos)

        cargarDatosDesdeApiOlocal(dbHelper)
        sincronizarPendientes(dbHelper)

        findViewById<FloatingActionButton>(R.id.fabAgregarEquipo).setOnClickListener {
            val intent = Intent(this, NuevoEquipoActivity::class.java)
            agregarEquipoLauncher.launch(intent)
        }

        findViewById<SearchView>(R.id.searchEquipos).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun cargarDatosDesdeApiOlocal(dbHelper: EquipoDBHelper) {
        RetrofitClient.instance.getEquipos().enqueue(object : Callback<List<Equipo>> {
            override fun onResponse(call: Call<List<Equipo>>, response: Response<List<Equipo>>) {
                if (response.isSuccessful && response.body() != null) {
                    val equipos = response.body()!!
                    //dbHelper.eliminarTodos()
                    //equipos.forEach { dbHelper.insertarEquipo(it) }
                    listaEquipos.clear()
                    listaEquipos.addAll(equipos)
                    adapter.filter.filter("")
                } else {
                    val equiposLocales = dbHelper.obtenerEquipos()
                    listaEquipos.clear()
                    listaEquipos.addAll(equiposLocales)
                    adapter.filter.filter("")
                }
            }

            override fun onFailure(call: Call<List<Equipo>>, t: Throwable) {
                Log.e("API_ERROR", "Fallo de red. Mostrando datos locales", t)
                val equiposLocales = dbHelper.obtenerEquipos()
                listaEquipos.clear()
                listaEquipos.addAll(equiposLocales)
                adapter.filter.filter("")
            }
        })
    }

    private fun sincronizarPendientes(dbHelper: EquipoDBHelper) {
        if (isInternetAvailable()) {
            val pendientes = dbHelper.obtenerEquiposNoSincronizados()
            pendientes.forEach { equipo ->
                RetrofitClient.instance.insertarEquipo(equipo).enqueue(object : Callback<Equipo> {
                    override fun onResponse(call: Call<Equipo>, response: Response<Equipo>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                dbHelper.eliminarEquipo(equipo.id)
                                dbHelper.insertarEquipo(it)
                                val index = listaEquipos.indexOfFirst { e -> e.id == equipo.id }
                                if (index != -1) {
                                    listaEquipos[index] = it
                                    adapter.notifyItemChanged(index)
                                } else {
                                    listaEquipos.add(it)
                                    adapter.notifyItemInserted(listaEquipos.size - 1)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<Equipo>, t: Throwable) {
                        Log.e("SYNC", "No se pudo sincronizar equipo pendiente", t)
                    }
                })
            }
        }
    }

    private fun mostrarDialogoEditar(equipo: Equipo, posicion: Int, dbHelper: EquipoDBHelper) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_editar_equipo, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etSku = dialogView.findViewById<EditText>(R.id.etSku)
        val etTipo = dialogView.findViewById<EditText>(R.id.etTipo)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)
        val etSede = dialogView.findViewById<EditText>(R.id.etSede)

        etNombre.setText(equipo.nombre)
        etSku.setText(equipo.sku)
        etTipo.setText(equipo.tipo)
        etDescripcion.setText(equipo.descripcion)
        etSede.setText(equipo.sede)

        AlertDialog.Builder(this)
            .setTitle("Editar equipo")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val equipoEditado = equipo.copy(
                    nombre = etNombre.text.toString(),
                    sku = etSku.text.toString(),
                    tipo = etTipo.text.toString(),
                    descripcion = etDescripcion.text.toString(),
                    sede = etSede.text.toString()
                )
                dbHelper.actualizarEquipo(equipoEditado)
                listaEquipos[posicion] = equipoEditado
                adapter.notifyItemChanged(posicion)

                if (isInternetAvailable()) {
                    RetrofitClient.instance.actualizarEquipo(equipoEditado.id, equipoEditado).enqueue(object : Callback<Equipo> {
                        override fun onResponse(call: Call<Equipo>, response: Response<Equipo>) {
                            response.body()?.let {
                                dbHelper.eliminarEquipo(equipoEditado.id)
                                dbHelper.insertarEquipo(it)
                                listaEquipos[posicion] = it
                                adapter.notifyItemChanged(posicion)
                                Toast.makeText(this@ListaEquiposActivity, "Equipo actualizado correctamente", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Equipo>, t: Throwable) {
                            Log.e("SYNC", "Fallo al actualizar en API", t)
                            Toast.makeText(this@ListaEquiposActivity, "Error al actualizar equipo", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

            }
            .setNegativeButton("Cancelar") { _, _ ->
                adapter.notifyItemChanged(posicion)
            }
            .show()
    }

    private val agregarEquipoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val dbHelper = EquipoDBHelper(this)
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            data?.let {
                val tieneInternet = isInternetAvailable()
                val provisionalId = if (tieneInternet) 0 else dbHelper.obtenerProximoIdNegativo()
                val nuevoEquipo = Equipo(
                    id = provisionalId,
                    nombre = it.getStringExtra("nombre") ?: "",
                    sku = it.getStringExtra("sku") ?: "",
                    tipo = it.getStringExtra("tipo") ?: "",
                    descripcion = it.getStringExtra("descripcion") ?: "",
                    sede = it.getStringExtra("sede") ?: ""
                )

                dbHelper.insertarEquipo(nuevoEquipo)
                listaEquipos.add(nuevoEquipo)
                adapter.notifyItemInserted(listaEquipos.size - 1)

                if (tieneInternet) {
                    RetrofitClient.instance.insertarEquipo(nuevoEquipo).enqueue(object : Callback<Equipo> {
                        override fun onResponse(call: Call<Equipo>, response: Response<Equipo>) {
                            response.body()?.let {
                                dbHelper.eliminarEquipo(nuevoEquipo.id)
                                dbHelper.insertarEquipo(it)
                                val index = listaEquipos.indexOfFirst { eq -> eq.id == nuevoEquipo.id }
                                if (index != -1) {
                                    listaEquipos[index] = it
                                    adapter.notifyItemChanged(index)
                                    Toast.makeText(this@ListaEquiposActivity, "Equipo agregado correctamente", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<Equipo>, t: Throwable) {
                            Log.e("SYNC", "Fallo al sincronizar. Se queda localmente.", t)
                            Toast.makeText(this@ListaEquiposActivity, "No se pudo sincronizar el nuevo equipo", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    fun isInternetAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}