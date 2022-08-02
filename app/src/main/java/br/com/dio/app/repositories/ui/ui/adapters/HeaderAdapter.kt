package br.com.dio.app.repositories.ui.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.app.repositories.databinding.ItemHomeHeaderBinding

class HeaderAdapter(private val onClick: () -> Unit) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeHeaderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            sortBy.setOnClickListener {
                onClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(val binding: ItemHomeHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)
}