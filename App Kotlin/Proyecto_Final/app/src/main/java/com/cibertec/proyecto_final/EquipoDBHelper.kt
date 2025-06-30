package com.cibertec.proyecto_final

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EquipoDBHelper(context: Context) : SQLiteOpenHelper(context, "equipos.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE equipo (
                id INTEGER PRIMARY KEY,
                nombre TEXT,
                sku TEXT,
                tipo TEXT,
                descripcion TEXT,
                sede TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS equipo")
        onCreate(db)
    }

    fun insertarEquipo(equipo: Equipo): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", equipo.id)
            put("nombre", equipo.nombre)
            put("sku", equipo.sku)
            put("tipo", equipo.tipo)
            put("descripcion", equipo.descripcion)
            put("sede", equipo.sede)
        }
        return db.insert("equipo", null, values)
    }

    fun obtenerEquipos(): List<Equipo> {
        val lista = mutableListOf<Equipo>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM equipo", null)
        while (cursor.moveToNext()) {
            val equipo = Equipo(
                id = cursor.getInt(0),
                nombre = cursor.getString(1),
                sku = cursor.getString(2),
                tipo = cursor.getString(3),
                descripcion = cursor.getString(4),
                sede = cursor.getString(5)
            )
            lista.add(equipo)
        }
        cursor.close()
        return lista
    }

    fun obtenerEquiposNoSincronizados(): List<Equipo> {
        val lista = mutableListOf<Equipo>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM equipo WHERE id < 0", null)
        while (cursor.moveToNext()) {
            val equipo = Equipo(
                id = cursor.getInt(0),
                nombre = cursor.getString(1),
                sku = cursor.getString(2),
                tipo = cursor.getString(3),
                descripcion = cursor.getString(4),
                sede = cursor.getString(5)
            )
            lista.add(equipo)
        }
        cursor.close()
        return lista
    }

    fun actualizarEquipo(equipo: Equipo): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", equipo.nombre)
            put("sku", equipo.sku)
            put("tipo", equipo.tipo)
            put("descripcion", equipo.descripcion)
            put("sede", equipo.sede)
        }
        return db.update("equipo", values, "id=?", arrayOf(equipo.id.toString()))
    }

    fun eliminarEquipo(id: Int): Int {
        val db = writableDatabase
        return db.delete("equipo", "id=?", arrayOf(id.toString()))
    }

    fun eliminarTodos(): Int {
        val db = writableDatabase
        return db.delete("equipo", null, null)
    }

    fun obtenerProximoIdNegativo(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT MIN(id) FROM equipo WHERE id < 0", null)
        val proximoIdNegativo = if (cursor.moveToFirst()) {
            val minId = cursor.getInt(0)
            if (minId >= 0) -1 else minId - 1
        } else {
            -1
        }
        cursor.close()
        return proximoIdNegativo
    }
}
