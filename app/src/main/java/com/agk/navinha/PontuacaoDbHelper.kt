package com.agk.navinha

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

// Definindo a estrutura da tabela
private const val DATABASE_NAME = "jogo.db"
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "pontuacao"
private const val COLUMN_ID = "id"
private const val COLUMN_PONTUACAO = "pontuacao"

// Classe para criar e gerenciar o banco de dados
class PontuacaoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_PONTUACAO INTEGER)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Função para salvar uma nova pontuação
    fun addPontuacao(pontuacao: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PONTUACAO, pontuacao)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Função para pegar a maior pontuação
    fun getHighestPontuacao(): Int {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT MAX($COLUMN_PONTUACAO) FROM $TABLE_NAME", null)
        var pontuacao = 0
        if (cursor.moveToFirst()) {
            pontuacao = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return pontuacao
    }
}
