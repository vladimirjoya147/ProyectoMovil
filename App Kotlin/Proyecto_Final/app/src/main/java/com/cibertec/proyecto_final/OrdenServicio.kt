package com.cibertec.proyecto_final


data class OrdenServicio(
    val cliente: String,
    val fecha: String,
    val equipo: String,
    val problema: String,
    var estado: String  // ← cambió de val a var
)