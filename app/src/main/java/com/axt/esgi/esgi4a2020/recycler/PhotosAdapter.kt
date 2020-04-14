package com.axt.esgi.esgi4a2020.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.axt.esgi.esgi4a2020.MyDiffUtilCallback
import com.axt.esgi.esgi4a2020.R
import com.axt.esgi.esgi4a2020.data.model.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    var data: List<Photo> = listOf()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(MyDiffUtilCallback(data, value))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    var listener: ((Photo) -> Unit)? = null

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }


    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = data[position]

        with(holder) {
            titleTv.text = photo.title
            Glide.with(itemView)
                .load(photo.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(thumbnailImv)

            itemView.setOnClickListener { listener?.invoke(photo) }
        }

    }

    fun removeAt(position: Int) {
        var newPhotos = data.toMutableList()

        newPhotos.removeAt(position)

        val diffCallback = MyDiffUtilCallback(data, newPhotos)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        data = newPhotos

        diffResult.dispatchUpdatesTo(this)
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnailImv: ImageView = itemView.findViewById(R.id.item_photo_imv)
        var titleTv: TextView = itemView.findViewById(R.id.item_photo_tv)
    }
}