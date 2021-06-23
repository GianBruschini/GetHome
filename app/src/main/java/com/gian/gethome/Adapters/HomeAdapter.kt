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

class HomeAdapter(private val mData: List<AnimalAdapterData>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onitemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item,
        parent,false)
         return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem: AnimalAdapterData = mData[position]
        val imageUrl: String = currentItem.imageURL
        val title: String = currentItem.nombre
        holder.nombreAnimal.text = title
        holder.edadAnimal.text = currentItem.edad

        Picasso.get().load(imageUrl).placeholder(R.drawable.progress_animation).into(holder.fotoAnimal)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val fotoAnimal: ImageView = itemView.findViewById(R.id.fotoAnimalImage)
        val nombreAnimal: TextView = itemView.findViewById(R.id.nombreAnimalText)
        val edadAnimal: TextView = itemView.findViewById(R.id.edadText)

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