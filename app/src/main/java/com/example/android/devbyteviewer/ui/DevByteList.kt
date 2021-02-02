package com.example.android.devbyteviewer.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.databinding.DevbyteItemBinding
import com.example.android.devbyteviewer.domain.DevByteVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DevByteAdapter(private val onClick: DevByteAction.OnClick) :
    ListAdapter<DevByteVideo, RecyclerView.ViewHolder>(DevByteDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DevByteViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DevByteViewHolder -> {
                val video = getItem(position) as DevByteVideo
                holder.bind(video)
            }
        }
    }

}

class DevByteViewHolder(view: View, private val onClick: DevByteAction.OnClick) :
    RecyclerView.ViewHolder(view) {

    fun bind(video: DevByteVideo) {
        val viewBinding = DevbyteItemBinding.bind(itemView)
        val resources = itemView.context.resources

        val videoThumbnail = viewBinding.videoThumbnail
        val clickableOverlay = viewBinding.clickableOverlay
        val description = viewBinding.description
        val playIcon = viewBinding.playIcon
        val title = viewBinding.title

        videoThumbnail.load(video.thumbnail)
        clickableOverlay.setOnClickListener { onClick.withVideo(video) }
        description.text = video.description
        playIcon.setImageResource(R.drawable.ic_play_circle_outline_black_48dp)
        title.text = video.title

        //itemView.setOnClickListener { onClick.withVideo(video) }
    }

    companion object {
        fun from(parent: ViewGroup, onClick: DevByteAction.OnClick): DevByteViewHolder {
            val layoutInflater =
                LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(
                    R.layout.devbyte_item,
                    parent, false
                )
            return DevByteViewHolder(view, onClick)
        }
    }
}

sealed class DevByteAction {
    class OnClick(private val action: (DevByteVideo) -> Unit) : DevByteAction() {
        fun withVideo(video: DevByteVideo) = action(video)
    }
}

class DevByteDiffCallback : DiffUtil.ItemCallback<DevByteVideo>() {
    override fun areItemsTheSame(oldItem: DevByteVideo, newItem: DevByteVideo): Boolean {
        return oldItem.url == newItem.url
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DevByteVideo, newItem: DevByteVideo): Boolean {
        return oldItem == newItem
    }
}
