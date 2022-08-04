package br.com.dio.app.repositories.ui.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.app.repositories.R
import br.com.dio.app.repositories.databinding.ItemHomeHeaderBinding
import br.com.dio.app.repositories.presentation.HomeViewModel

class HeaderAdapter(private val onClick: (HomeViewModel.SortByStar) -> Unit) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeHeaderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                when (checkedIds[0]) {
                    R.id.chip_sort_ascent -> {
                        onClick(HomeViewModel.SortByStar.ASCENDING)
                    }
                    R.id.chip_sort_descent -> {
                        onClick(HomeViewModel.SortByStar.DESCENDING)
                    }
                    else -> {
                        onClick(HomeViewModel.SortByStar.NONE)
                    }
                }
            }else{
                onClick(HomeViewModel.SortByStar.NONE)
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(val binding: ItemHomeHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)
}