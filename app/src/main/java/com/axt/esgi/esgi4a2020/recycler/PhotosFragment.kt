package com.axt.esgi.esgi4a2020.recycler

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axt.esgi.esgi4a2020.R
import com.axt.esgi.esgi4a2020.common.showError
import com.axt.esgi.esgi4a2020.data.api.FlickrProvider
import com.axt.esgi.esgi4a2020.data.api.Listener
import com.axt.esgi.esgi4a2020.data.model.Photo

/**
 * A simple [Fragment] subclass.
 */
class PhotosFragment : Fragment() {

    private lateinit var photosRecyclerView: RecyclerView
    private val photosAdapter: PhotosAdapter by lazy { PhotosAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photosRecyclerView = view.findViewById(R.id.photos_rcv)
        initRecyclerView()

        getPhotos()
    }

    private fun getPhotos() {
        FlickrProvider.getPhotos(object : Listener<List<Photo>> {
            override fun onSuccess(data: List<Photo>) {
                photosAdapter.data = data
            }

            override fun onError(t: Throwable) {
                showError(t)
            }
        })
    }

    private fun initRecyclerView() {
        photosRecyclerView.layoutManager = LinearLayoutManager(context)
        photosRecyclerView.adapter = photosAdapter
        photosRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT
            ) {

                private val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.delete)!!
                private val intrinsicWidth = deleteIcon.intrinsicWidth
                private val intrinsicHeight = deleteIcon.intrinsicHeight
                private val background = ColorDrawable(Color.RED)

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    recyclerView.adapter?.notifyItemMoved(
                        viewHolder.adapterPosition,
                        target.adapterPosition
                    )
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    photosAdapter.removeAt(position)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val itemHeight = itemView.bottom - itemView.top

                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)

                    val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val iconMargin = (itemHeight - intrinsicHeight) / 2
                    val iconLeft = itemView.right - iconMargin - intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    val iconBottom = iconTop + intrinsicHeight

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    deleteIcon.draw(c)
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

            }
        )
        itemTouchHelper.attachToRecyclerView(photosRecyclerView)

        photosAdapter.listener = this::navigateToDetail
    }

    private fun navigateToDetail(photo: Photo) {
        findNavController().navigate(
            PhotosFragmentDirections.actionPhotosFragmentToPhotoDetailFragment(
                photo.id
            )
        )
    }
}
