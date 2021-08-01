package com.gian.gethome.Fragments.mispublicaciones.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Adapters.HomeAdapter
import com.gian.gethome.Adapters.MisPubsAdapter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.Fragments.mispublicaciones.interfaces.MisPublicacionesView
import com.gian.gethome.Fragments.mispublicaciones.model.MisPublicacionesInteractor
import com.gian.gethome.Fragments.mispublicaciones.presenter.MisPublicacionesPresenter
import com.gian.gethome.R

import java.util.ArrayList

class MisPublicacionesFragment: Fragment(), MisPublicacionesView,MisPubsAdapter.OnItemClickListener {
    private lateinit var recyclerLikes: RecyclerView
    private var mlist: ArrayList<AnimalAdapterData> = arrayListOf()
    private lateinit var imagenNotNull:String
    private lateinit var textoEmpty: TextView
    private lateinit var adapter: MisPubsAdapter
    private var dialog: Dialog? = null
    private val presenter= MisPublicacionesPresenter(this, MisPublicacionesInteractor())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mis_likes, container, false)
        getValues(view)
        presenter.getMyPubsData()
        return view

    }

    private fun getValues(view: View) {
        recyclerLikes = view.findViewById(R.id.recyclerview_likes)
        textoEmpty = view.findViewById(R.id.texto_aviso)
    }

    override fun fillRecyclerViewWith(animal: Animal, imagenNotNull: String) {
        mlist.add(AnimalAdapterData(animal.nombre,
                animal.tipoAnimal,
                imagenNotNull,
                animal.edad,
                animal.fechaDePublicacion,
                animal.descripcion,
                animal.transitoUrgente,
                animal.userIDowner,
                animal.animalKey,
                animal.sexo,
                animal.pais,
                animal.provincia))
        adapter = MisPubsAdapter(mlist)
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        recyclerLikes.layoutManager = gridLayoutManager
        recyclerLikes.setHasFixedSize(true)
        recyclerLikes.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    override fun notifyDataChanged() {
        recyclerLikes.adapter?.notifyDataSetChanged()
    }

    override fun setTextViewVisible() {
        textoEmpty.visibility = View.VISIBLE
    }

    override fun showDataBaseError() {
        Toast.makeText(requireContext(),
                "Error con el servicio, vuelva a intentarlo pronto",
                Toast.LENGTH_LONG).show()
    }

    override fun notifyPubDeleted(position: Int) {
        adapter.notifyItemRemoved(position)
    }

    override fun onEditClick(position: Int) {
        dialog = Dialog(requireContext())
        dialog?.setContentView(R.layout.dialog_deleteacc_layout)
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
        val yesDelate: ImageView = dialog!!.findViewById(R.id.yesDelate)
        val noDelate: ImageView = dialog!!.findViewById(R.id.noDelate)
        yesDelate.setOnClickListener {
            presenter.deleteAnimalFromDB(position)
        }
        noDelate.setOnClickListener {
            dialog?.dismiss()
        }

    }


}