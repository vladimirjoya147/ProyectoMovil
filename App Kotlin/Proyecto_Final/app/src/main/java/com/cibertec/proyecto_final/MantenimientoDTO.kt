package com.cibertec.proyecto_final

data class MantenimientoDTO(
    val id: Int,
    val nombreEquipo: String,
    val nombreTecnico: String,
    val tipoMantenimiento: String,
    val estado: String
)
