package com.cibertec.proyecto_final

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class OrdenServicioAdapter(
    private val lista: List<MantenimientoDTO>
) : RecyclerView.Adapter<OrdenServicioAdapter.OrdenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orden_servicio, parent, false)
        return OrdenViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
        val orden = lista[position]
        holder.bind(orden)
    }

    override fun getItemCount(): Int = lista.size

    inner class OrdenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreEquipo: TextView = itemView.findViewById(R.id.tvNombreEquipo)
        private val tvSkuEquipo: TextView = itemView.findViewById(R.id.tvSkuEquipo)
        private val tvTipoEquipo: TextView = itemView.findViewById(R.id.tvTipoEquipo)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)

        fun bind(orden: MantenimientoDTO) {
            tvNombreEquipo.text = "Equipo: ${orden.nombreEquipo}"
            tvSkuEquipo.text = "Técnico: ${orden.nombreTecnico}"
            tvTipoEquipo.text = "Tipo: ${orden.tipoMantenimiento}"
            tvEstado.text = orden.estado

            val colorEstado = when (orden.estado) {
                "En proceso" -> Color.parseColor("#FFA726")     // Naranja
                "Recibido" -> Color.parseColor("#29B6F6")     // Azul
                "Finalizado" -> Color.parseColor("#66BB6A")     // Verde
                else -> Color.GRAY
            }

            // Establecer color de fondo del estado
            val background = tvEstado.background.mutate()
            if (background is GradientDrawable) {
                background.setColor(colorEstado)
            }
        }
    }

}

