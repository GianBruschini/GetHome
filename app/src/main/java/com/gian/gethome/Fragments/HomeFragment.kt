package com.gian.gethome.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Activities.AnimalDetalleActivity
import com.gian.gethome.Activities.FilterActivity
import com.gian.gethome.Activities.HomeActivity
import com.gian.gethome.Adapters.HomeAdapter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment: Fragment(), HomeAdapter.OnItemClickListener, View.OnClickListener{

    private lateinit var pais: String
    private lateinit var provincia: String
    private lateinit var ordenarPorGenero: String
    private var ordenarPorFechaDePublicacion: Boolean? = false
    private var ordenarPorTransitoUrgente: Boolean? = false
    private var mlist: ArrayList<AnimalAdapterData> = arrayListOf()
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var imagenNotNull:String
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter:HomeAdapter
    private lateinit var myRecycler: RecyclerView
    private lateinit var cardView:CardView
    private lateinit var gatoImg:ImageView
    private lateinit var perroImg:ImageView
    private lateinit var loroImg:ImageView
    private lateinit var conejoImg:ImageView
    private lateinit var tortugaImg:ImageView
    private lateinit var todoImg:ImageView
    private lateinit var  chooseFilter:FloatingActionButton
    private lateinit var texto_resultado:TextView
    private lateinit var buscador:EditText
    private lateinit var homeActivity:HomeActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        cardView = view.findViewById(R.id.cardViewHome)
        getCountryAndProvinceFromHomeActivity()
        settingValues(view)
        loadRecycler()
        makeActionEditText()
        return view
    }

    private fun getCountryAndProvinceFromHomeActivity() {
        homeActivity = (activity as HomeActivity?)!!
        provincia = homeActivity.provincia.text.toString()
        pais = homeActivity.pais.text.toString()
    }


    private fun loadRecycler() {
        texto_resultado.visibility = View.GONE
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val animal: Animal = snap.getValue(Animal::class.java)!!
                        if (animal.provincia == provincia && animal.pais == pais) {
                            imagenNotNull = checkWhatImageIsNotNull(animal)
                            mlist.add(AnimalAdapterData(
                                    animal.nombre,
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
                        }

                    }
                }
                myRecycler.adapter?.notifyDataSetChanged()
                if (adapter.itemCount == 0) {
                    texto_resultado.visibility = View.VISIBLE
                }
            }

            private fun checkWhatImageIsNotNull(animal: Animal): String {
                if (animal.imagen1 != "null") {
                    return animal.imagen1
                }
                if (animal.imagen2 != "null") {
                    return animal.imagen2
                }
                if (animal.imagen3 != "null") {
                    return animal.imagen3
                }
                if (animal.imagen4 != "null") {
                    return animal.imagen4
                }
                return ""
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = HomeAdapter(mlist)
        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        myRecycler.layoutManager = gridLayoutManager
        myRecycler.setHasFixedSize(true)
        myRecycler.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    private fun settingValues(view: View) {
        myRecycler = view.findViewById(R.id.myRecycler)
        texto_resultado = view.findViewById(R.id.texto_resultados)
        buscador = view.findViewById(R.id.buscador)
        cardView = view.findViewById(R.id.cardViewHome)
        cardView.setBackgroundResource(R.drawable.card_rounded_design)
        gatoImg = view.findViewById(R.id.gato)
        perroImg = view.findViewById(R.id.perro)
        conejoImg = view.findViewById(R.id.conejo)
        loroImg = view.findViewById(R.id.loro)
        tortugaImg = view.findViewById(R.id.tortuga)
        chooseFilter = view.findViewById(R.id.chooseFilters)
        todoImg = view.findViewById(R.id.all)
        gatoImg.setOnClickListener(this)
        perroImg.setOnClickListener(this)
        loroImg.setOnClickListener(this)
        conejoImg.setOnClickListener(this)
        tortugaImg.setOnClickListener(this)
        todoImg.setOnClickListener(this)

        chooseFilter.setOnClickListener {
            val intent= Intent(context, FilterActivity::class.java)
            startActivityForResult(intent, 101)
        }
        setOnClickImages()
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Cargando publicaciones")
        progressDialog.setMessage("Por favor, espere")
        progressDialog.setCancelable(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            if(data!=null){
                ordenarPorTransitoUrgente = data.getBooleanExtra("filtrarPorTransito", false)!!
                val genero = data.getStringExtra("filtrarPorGenero");
                if(genero != "Género animal"){
                    ordenarPorGenero = genero.toString()
                }
            }
        }
    }

    private fun setOnClickImages() {
        gatoImg.setOnClickListener {
            clearListOfRecycler()
            setGatoImgselected()
            fillScreenWith("Gato")
            myRecycler.adapter!!.notifyDataSetChanged()
        }
        perroImg.setOnClickListener {
            clearListOfRecycler()
            setPerroImgselected()
            fillScreenWith("Perro")
            myRecycler.adapter!!.notifyDataSetChanged()
        }
        conejoImg.setOnClickListener {
            clearListOfRecycler()
            setConejoImgselected()
            fillScreenWith("Conejo")
            myRecycler.adapter!!.notifyDataSetChanged()
        }
        tortugaImg.setOnClickListener {
            clearListOfRecycler()
            setTortugaImgselected()
            fillScreenWith("Tortuga")
            myRecycler.adapter!!.notifyDataSetChanged()
        }
        loroImg.setOnClickListener {
            clearListOfRecycler()
            setLoroImgselected()
            fillScreenWith("Loro")
            myRecycler.adapter!!.notifyDataSetChanged()
        }
        todoImg.setOnClickListener {
            clearListOfRecycler()
            setTodoImgselected()
            loadRecycler()
            myRecycler.adapter!!.notifyDataSetChanged()
        }
    }



    private fun clearListOfRecycler() {
        val size: Int = mlist.size
        if (size > 0) {
            for (i in 0 until size) {
                mlist.removeAt(0)
            }
            myRecycler.adapter!!.notifyItemRangeRemoved(0, size)
        }
    }

    private fun fillScreenWith(theTypeOfAnimal: String) {
        texto_resultado.visibility = View.GONE
        mFirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference.child("Users").child("Animales")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    for (snap in dataSnapshot.children) {
                        val tipoAnimal: String = snap.child("tipoAnimal").value.toString();
                        if (tipoAnimal == theTypeOfAnimal) {
                            val animal: Animal = snap.getValue(Animal::class.java)!!
                            if (animal.provincia == provincia && animal.pais == pais) {
                                imagenNotNull = checkWhatImageIsNotNull(animal)
                                mlist.add(AnimalAdapterData(animal.nombre, animal.tipoAnimal,
                                        imagenNotNull,
                                        animal.edad, animal.fechaDePublicacion, animal.descripcion,
                                        animal.transitoUrgente,
                                        animal.userIDowner, animal.animalKey, animal.sexo, animal.pais, animal.provincia))
                            }

                        }

                    }
                }

                myRecycler.adapter?.notifyDataSetChanged()
                if (adapter.itemCount == 0) {
                    texto_resultado.visibility = View.VISIBLE
                }

            }

            private fun checkWhatImageIsNotNull(animal: Animal): String {
                if (animal.imagen1 != "null") {
                    return animal.imagen1
                }
                if (animal.imagen2 != "null") {
                    return animal.imagen2
                }
                if (animal.imagen3 != "null") {
                    return animal.imagen3
                }
                if (animal.imagen4 != "null") {
                    return animal.imagen4
                }
                return ""
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setGatoImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#306060"))
        gatoImg.setColorFilter(Color.parseColor("#ffffff"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setPerroImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#306060"))
        perroImg.setColorFilter(Color.parseColor("#ffffff"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setConejoImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#306060"))
        conejoImg.setColorFilter(Color.parseColor("#ffffff"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setLoroImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#306060"))
        loroImg.setColorFilter(Color.parseColor("#ffffff"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setTortugaImgselected(){
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#306060"))
        tortugaImg.setColorFilter(Color.parseColor("#ffffff"))
        cardAll.setCardBackgroundColor(Color.parseColor("#ffffff"))
        todoImg.setColorFilter(Color.parseColor("#000000"))
    }
    private fun setTodoImgselected() {
        cardGato.setCardBackgroundColor(Color.parseColor("#ffffff"))
        gatoImg.setColorFilter(Color.parseColor("#000000"))
        cardPerro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        perroImg.setColorFilter(Color.parseColor("#000000"))
        cardConejo.setCardBackgroundColor(Color.parseColor("#ffffff"))
        conejoImg.setColorFilter(Color.parseColor("#000000"))
        cardLoro.setCardBackgroundColor(Color.parseColor("#ffffff"))
        loroImg.setColorFilter(Color.parseColor("#000000"))
        cardTortuga.setCardBackgroundColor(Color.parseColor("#ffffff"))
        tortugaImg.setColorFilter(Color.parseColor("#000000"))
        cardAll.setCardBackgroundColor(Color.parseColor("#306060"))
        todoImg.setColorFilter(Color.parseColor("#ffffff"))
    }

    override fun onitemClick(position: Int) {
        val intent = Intent(activity, AnimalDetalleActivity::class.java)
        val animalClicked:AnimalAdapterData = mlist[position]
        intent.putExtra("nombre", animalClicked.nombre)
        intent.putExtra("tipoAnimal", animalClicked.tipoAnimal)
        intent.putExtra("descripcion", animalClicked.descripcion)
        intent.putExtra("edad", animalClicked.edad)
        intent.putExtra("transitoUrgente", animalClicked.transitoUrgente)
        intent.putExtra("userIDowner", animalClicked.idOwner)
        intent.putExtra("animalKey", animalClicked.animalKey)
        intent.putExtra("sexoAnimal", animalClicked.sexo)
        intent.putExtra("Provincia", animalClicked.provincia)
        intent.putExtra("Pais", animalClicked.pais)

        startActivity(intent)
    }

    override fun onClick(v: View?) {

    }

    private fun makeActionEditText() {
        buscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }


    private fun filter(text: String) {
        val filteredList: ArrayList<AnimalAdapterData> = ArrayList<AnimalAdapterData>()
        for (item in mlist) {
            if (item.nombre.toLowerCase().contains(text.toLowerCase()) ||
                    item.sexo.toLowerCase().contains(text.toLowerCase()) ||
                    item.provincia.toLowerCase().contains(text.toLowerCase()) ||
                    item.pais.toLowerCase().contains(text.toLowerCase()) ||
                    item.edad.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)


    }


}