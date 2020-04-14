package com.axt.esgi.esgi4a2020

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.axt.esgi.esgi4a2020.data.model.Photo

class MyDiffUtilCallback(private val oldPhotos: List<Photo>, private val newPhotos: List<Photo>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPhotos.get(oldItemPosition).id == newPhotos.get(newItemPosition).id
    }

    override fun getOldListSize(): Int {
        return oldPhotos.size
    }

    override fun getNewListSize(): Int {
        return newPhotos.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPhotos.get(oldItemPosition).equals(newPhotos.get(newItemPosition))
    }

}