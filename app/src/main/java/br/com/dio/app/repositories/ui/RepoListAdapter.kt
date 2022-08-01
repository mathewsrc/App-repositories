package br.com.dio.app.repositories.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.app.repositories.R
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.databinding.ItemRepoBinding
import com.bumptech.glide.Glide
import java.util.*

class RepoListAdapter : ListAdapter<Repo, RepoListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemRepoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Repo) {
            val context = binding.root.context
            binding.tvRepoName.text = item.name
            binding.tvRepoDescription.text = item.description
            binding.chipStar.text = item.stargazersCount.toString()
            binding.tvRepoLanguage.apply {
                item.language?.let {  language ->
                    val string = SpannableString(language).apply {
                        val rnd = Random()
                        val backgroundColor = Color.argb(155, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                        setSpan(BackgroundColorSpan(backgroundColor), 0, language.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    text = string
                }
            }
            item.language

            binding.btnOpenLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.htmlURL))
                context.startActivity(intent)
            }
            binding.ivShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, item.name)
                    putExtra(Intent.EXTRA_TEXT, item.htmlURL)
                }
                context.startActivity(
                    Intent.createChooser(
                        intent,
                        context.getString(R.string.open_link_with)
                    )
                )

            }

            Glide.with(binding.root.context)
                .load(item.owner.avatarURL).into(binding.ivOwner)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem.id == newItem.id
}