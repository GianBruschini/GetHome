package com.gian.gethome.Adapters

import android.graphics.Bitmap
import android.media.ExifInterface
import android.media.ExifInterface.ORIENTATION_NORMAL
import android.media.ExifInterface.TAG_ORIENTATION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.R
import com.squareup.picasso.Picasso
import java.io.IOException
import java.util.*


class HomeAdapter(private var mData: ArrayList<AnimalAdapterData>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onitemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item,
                parent, false)
         return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem: AnimalAdapterData = mData[position]
        holder.nombreAnimal.text = currentItem.nombre
        holder.edadAnimal.text = currentItem.edad
        val distanceInKm: Double =  1.60934 * currentItem.distance.toDouble()

        val distance = "A ${distanceInKm.toInt()} km de distancia"
        holder.distance.text = distance
        when(currentItem.sexo){
            "Macho" -> Picasso.get().load(R.drawable.male).into(holder.genreIcon)
            "Hembra" -> Picasso.get().load(R.drawable.female).into(holder.genreIcon)
        }
        when(currentItem.transitoUrgente){
            "true" -> holder.esTransitoUrgente.visibility = View.VISIBLE
            "false" -> holder.esTransitoUrgente.visibility = View.INVISIBLE
        }

        handleImageOrientation(currentItem.imageURL, holder)
        /*Picasso.get().
        load(currentItem.imageURL).
        placeholder(R.drawable.progress_animation).
        into(holder.fotoAnimal)

         */


    }

    private fun handleImageOrientation(currentPhotoPath: String, holder: HomeViewHolder) {
        var rotation = 0f
        try {
            val exifInterface: ExifInterface = ExifInterface(currentPhotoPath)
            val orientation = exifInterface.getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    rotation = -90f
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    rotation = -180f
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    rotation = 90f
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        Picasso.get().load(currentPhotoPath).placeholder(R.drawable.progress_animation)
                .into(holder.fotoAnimal)
    }


    override fun getItemCount(): Int {
        return mData.size
    }

    fun filterList(filteredList: ArrayList<AnimalAdapterData>) {
        mData = filteredList
        notifyDataSetChanged()
    }

    fun clearList(){
        if (mData.size > 0) {
            for (i in mData.indices) {
                mData.removeAt(0)
            }
            notifyItemRangeRemoved(0, mData.size)
        }
    }


    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    inner class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val fotoAnimal: ImageView = itemView.findViewById(R.id.fotoAnimalImage)
        val nombreAnimal: TextView = itemView.findViewById(R.id.nombreAnimalText)
        val edadAnimal: TextView = itemView.findViewById(R.id.edadText)
        val genreIcon: ImageView = itemView.findViewById(R.id.iconGenre)
        val distance: TextView = itemView.findViewById(R.id.distanceText)
        val esTransitoUrgente: ImageView = itemView.findViewById(R.id.esTransitoUrgente)

        init {
            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onitemClick(position)
                    }
                }
            }
        }

    }
}