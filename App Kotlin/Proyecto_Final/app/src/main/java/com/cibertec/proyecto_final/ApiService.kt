package com.cibertec.proyecto_final

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("equipo/listar")
    fun getEquipos(): Call<List<Equipo>>

    @POST("equipo/registrar")
    fun insertarEquipo(@Body equipo: Equipo): Call<Equipo>

    @PUT("equipo/actualizar/{id}")
    fun actualizarEquipo(
        @Path("id") id: Int,
        @Body equipo: Equipo
    ): Call<Equipo>

    @DELETE("equipo/{id}")
    fun eliminarEquipo(@Path("id") id: Int): Call<ResponseBody>

    @DELETE("ordenes/{id}")
    fun eliminarMantenimiento(@Path("id") id: Int): Call<ResponseBody>

    @GET("ordenes/todas")
    fun listarMantenimientos(): Call<List<MantenimientoDTO>>

}