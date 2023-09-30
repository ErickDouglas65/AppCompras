package com.example.appcompras

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.appcompras.R

class CriarItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_item)

        val textViewTitulo:EditText = findViewById(R.id.editTextTitulo)
        val textViewQuantidade:EditText = findViewById(R.id.editTextQuantidade)
        val textViewMercado:EditText = findViewById(R.id.editTextMercado)

        val titulo = intent.getStringExtra("titulo")
        val quantidade = intent.getIntExtra("quantidade", 0)
        val mercado = intent.getStringExtra("mercado")

        textViewTitulo.setText(titulo)
        if (quantidade > 0) {
            textViewQuantidade.setText(quantidade.toString())
        }
        textViewMercado.setText(mercado)
    }

    fun salvarCompra(view: View) {
        // Obtenha uma instância do SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MeuSharedPreferences", Context.MODE_PRIVATE)

        // Obtenha uma instância do SharedPreferences.Editor para fazer alterações
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // Recupere as informações da compra dos campos de entrada
        val textViewTitulo: EditText = findViewById(R.id.editTextTitulo)
        val textViewQuantidade: EditText = findViewById(R.id.editTextQuantidade)
        val textViewMercado: EditText = findViewById(R.id.editTextMercado)

        val titulo = textViewTitulo.text.toString()
        val quantidade = textViewQuantidade.text.toString()
        val mercado = textViewMercado.text.toString()



        // Salve as informações da compra no SharedPreferences
        editor.putString("titulo", titulo)
        editor.putString("quantidade", quantidade)
        editor.putString("mercado", mercado)

        // salvar as alterações assíncronas
        editor.apply()

        // Crie um Intent para enviar os dados de volta
        val intent = Intent()
        intent.putExtra("novotitulo", titulo)
        intent.putExtra("novaquantidade", quantidade)
        intent.putExtra("novomercado", mercado)
        setResult(Activity.RESULT_OK, intent)

        // Encerra a atividade
        finish()

        // mensagem de confirmação
        Toast.makeText(this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show()
    }

    fun voltar(view: View) {
        finish()
    }
}

