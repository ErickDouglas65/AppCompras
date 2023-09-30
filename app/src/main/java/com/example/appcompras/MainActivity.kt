package com.example.appcompras

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcompras.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {
    lateinit var dataset: MutableList<Compra>
    lateinit var customAdapter: CustomAdapter
    val REQUEST_CODE  = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* pegar os dados do sharedPreferences*/
        val sharedPreferences = getSharedPreferences("MeuSharedPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("dados", null)

        if (json != null)
        {
            // converter os dados json para lista
            val type = object : TypeToken<MutableList<Compra>>() {}.type
            dataset = gson.fromJson(json, type)
        }
        else
        {
            dataset = mutableListOf()
        }


        customAdapter = CustomAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter



        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder: AlertDialog.Builder? = this?.let {
                    AlertDialog.Builder(this@MainActivity)
                }
                builder?.setMessage("Tem certeza que deseja excluir item?")
                    ?.setTitle("Confirmação")

               val alertDialog: AlertDialog? = this?.let {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.apply {
                        setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog, id ->
                                removeItem(viewHolder,customAdapter)
                            })
                        setNegativeButton("Cancel",
                            DialogInterface.OnClickListener { dialog, id ->
                               customAdapter.notifyDataSetChanged()
                            })
                    }
                    // Set other dialog properties
                    builder?.setMessage("Tem certeza que deseja excluir item?")
                        ?.setTitle("Confirmação")

                    // Create the AlertDialog
                    builder.create()
                }

                alertDialog?.show()


            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    fun removeItem(viewHolder: RecyclerView.ViewHolder, customAdapter:CustomAdapter){
        val position = viewHolder.adapterPosition
        dataset.removeAt(position)
        customAdapter.notifyItemRemoved(position)
    }
    class CustomAdapter(private val dataSet: MutableList<Compra>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
       class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
           val textViewTitulo: TextView
           val textViewQuantidade: TextView
           val textViewMercado: TextView
           init {
               textViewTitulo = view.findViewById(R.id.textViewTitulo)
               textViewQuantidade = view.findViewById(R.id.textViewQuantidade)
               textViewMercado = view.findViewById(R.id.textViewMercado)
           }
       }
        fun updateDataset(newDataset: MutableList<Compra>) {
            dataSet.clear() // Limpa o conjunto de dados atual
            dataSet.addAll(newDataset) // Adiciona os novos dados
            notifyDataSetChanged() // Notifica o RecyclerView de que os dados foram alterados
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CustomAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.text_row_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
            val itemCompra = dataSet[position]
            holder.textViewTitulo.text = itemCompra.titulo
            holder.textViewQuantidade.text = itemCompra.quantidade.toString()
            holder.textViewMercado.text = itemCompra.mercado
            // ...
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, CriarItemActivity::class.java)
                intent.putExtra("titulo", itemCompra.titulo)
                intent.putExtra("quantidade", itemCompra.quantidade)
                intent.putExtra("mercado", itemCompra.mercado)
                it.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("MeuSharedPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("dados", null)

        if (json != null) {
            val type = object : TypeToken<MutableList<Compra>>() {}.type
            dataset = gson.fromJson(json, type)
        } else {
            dataset = mutableListOf()
        }

        // atualizar adapter com novos dados
        customAdapter.updateDataset(dataset)
        customAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Verifique se o código de solicitação e o resultado são os esperados
            if (data != null) {
                // Extraia os dados da Intent
                val novoTitulo = data.getStringExtra("novotitulo")
                val novaQuantidade = data.getStringExtra("novaquantidade")
                val novoMercado = data.getStringExtra("novomercado")

                // Crie um objeto Compra com os dados
                val novaCompra = Compra(novoTitulo, novaQuantidade, novoMercado)

                // Adicione a nova compra ao seu dataset ou preferência compartilhada
                dataset.add(novaCompra)

                // Notifique o adaptador que os dados mudaram (caso esteja usando um RecyclerView)
                customAdapter.notifyDataSetChanged()
            }
        }
    }


    fun addCompra(view: View) {
        val intent = Intent(this, CriarItemActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }
}