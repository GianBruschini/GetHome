package com.gian.gethome.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.Fragments.mispublicaciones.view.MisPublicacionesFragment
import com.gian.gethome.R
import com.squareup.picasso.Picasso
import java.util.*

class MisPubsAdapter(var mData: ArrayList<AnimalAdapterData>,
                     var context: MisPublicacionesFragment) :
        RecyclerView.Adapter<MisPubsAdapter.HomeViewHolder>() {
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
        var left:Int = dpToPx(24)
        var top:Int = dpToPx(12)
        var right:Int = dpToPx(24)
        var bottom:Int = dpToPx(12)

        var spanCount = 2

        var isFirst2Items:Boolean = position < spanCount
        var isLast2Items:Boolean = position > itemCount - spanCount

        if(isFirst2Items){
            top = dpToPx(24)
        }

        if(isLast2Items){
            bottom = dpToPx(24)
        }

        var isLeftSide: Boolean = (position + 1 ) % spanCount != 0
        var isRightSide: Boolean = !isLeftSide

        if(isLeftSide){
            right = dpToPx(12)
        }

        if(isRightSide){
            left = dpToPx(12)
        }

        val layoutParamsTo = holder.cardView.layoutParams as FrameLayout.LayoutParams
        layoutParamsTo.setMargins(left, top, right, bottom)
        holder.cardView.setLayoutParams(layoutParamsTo)
    }

    private fun dpToPx(dp: Int):Int{
        var px:Float = dp * context.resources.displayMetrics.density
        return px.toInt()
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombreAnimal: TextView = itemView.findViewById(R.id.animalName)
        val editImage : ImageView = itemView.findViewById(R.id.editAnimal)
        val imageAnimal: ImageView = itemView.findViewById(R.id.fotoAnimalImage)
        val cardView: CardView = itemView.findViewById(R.id.cardView1)

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
