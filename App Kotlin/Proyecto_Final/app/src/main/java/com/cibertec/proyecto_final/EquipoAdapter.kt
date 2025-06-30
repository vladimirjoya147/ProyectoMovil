package com.cibertec.proyecto_final

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale

class EquipoAdapter(private val listaOriginal: List<Equipo>) :
    RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>(), Filterable {

    private var listaFiltrada: MutableList<Equipo> = listaOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = listaFiltrada[position]
        holder.tvNombre.text = equipo.nombre
        holder.tvSku.text = equipo.sku
        holder.tvTipo.text = equipo.tipo
        holder.tvSede.text = equipo.sede
    }

    override fun getItemCount(): Int = listaFiltrada.size

    inner class EquipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEquipo)
        val tvSku: TextView = itemView.findViewById(R.id.tvSkuEquipo)
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipoEquipo)
        val tvSede: TextView = itemView.findViewById(R.id.tvSedeEquipo)
    }

    // --- Filtro para el SearchView ---
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase(Locale.getDefault()) ?: ""
                val resultadosFiltrados = if (query.isEmpty()) {
                    listaOriginal
                } else {
                    listaOriginal.filter {
                        it.nombre.lowercase(Locale.getDefault()).contains(query) ||
                                it.tipo.lowercase(Locale.getDefault()).contains(query) ||
                                it.sede.lowercase(Locale.getDefault()).contains(query)
                    }
                }
                val results = FilterResults()
                results.values = resultadosFiltrados
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = (results?.values as? List<Equipo>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}
