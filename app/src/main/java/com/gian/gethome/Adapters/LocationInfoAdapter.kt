package com.gian.gethome.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Activities.openmap.view.OpenMapActivity
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.R
import com.squareup.picasso.Picasso
import java.util.ArrayList

class LocationInfoAdapter(var mData: ArrayList<AnimalAdapterData>,var context:OpenMapActivity): RecyclerView.Adapter<LocationInfoAdapter.LocationViewHolder>() {




    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onitemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item,
                parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.nombreAnimal.text = mData[position].nombre
        Picasso.get().
        load(mData[position].imageURL)
                .placeholder(R.drawable.progress_animation)
                .into(holder.imageAnimal)

        holder.edadAnimal.text = mData[position].edad
        val distanceInKm: Double =  1.60934 * mData[position].distance.toDouble()

        val distance = "A ${distanceInKm.toInt()} km de distancia"
        holder.distance.text = distance
        when(mData[position].sexo){
            "Macho" -> Picasso.get().load(R.drawable.male).into(holder.genreIcon)
            "Hembra" -> Picasso.get().load(R.drawable.female).into(holder.genreIcon)
        }
        when(mData[position].transitoUrgente){
            "true" -> holder.esTransitoUrgente.visibility = View.VISIBLE
            "false" -> holder.esTransitoUrgente.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun clearList(){
        if (mData.size > 0) {
            for (i in mData.indices) {
                mData.removeAt(0)
            }
            notifyItemRangeRemoved(0, mData.size)
        }
    }


    inner class LocationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreAnimal: TextView = itemView.findViewById(R.id.nombreAnimalText)
        val imageAnimal:ImageView = itemView.findViewById(R.id.fotoAnimalImage)
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