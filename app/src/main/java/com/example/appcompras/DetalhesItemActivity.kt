package com.example.appcompras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.appcompras.R

class DetalhesItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_item)

        val titulo = intent.getStringExtra("titulo")
        val mercado = intent.getStringExtra("mercado")
        val quantidade = intent.getStringExtra("quantidade")

        val textViewTitulo: TextView = findViewById(R.id.textViewTitulo)
        val textViewMercado: TextView = findViewById(R.id.textViewMercado)
        val textviewQuantidade: TextView = findViewById(R.id.textViewQuantidade)

        textViewTitulo.text = titulo
        textViewMercado.text = mercado
        textviewQuantidade.text = quantidade
    }
}