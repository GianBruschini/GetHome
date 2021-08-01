package com.gian.gethome.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.R
import com.squareup.picasso.Picasso
import java.util.*

class MisPubsAdapter(private var mData: ArrayList<AnimalAdapterData>) : RecyclerView.Adapter<MisPubsAdapter.HomeViewHolder>() {
    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onEditClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_mispubs,
                parent, false)
        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.nombreAnimal.text = mData[position].nombre
        Picasso.get().load(mData[position].imageURL).
        placeholder(R.drawable.progress_animation).
        into(holder.imageAnimal)
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreAnimal: TextView = itemView.findViewById(R.id.animalName)
        val editImage : ImageView = itemView.findViewById(R.id.editAnimal)
        val imageAnimal: ImageView = itemView.findViewById(R.id.fotoAnimalImage)

        init {

            editImage.setOnClickListener(View.OnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onEditClick(position)
                    }
                }
            })
        }

    }



}
