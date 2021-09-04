package com.gian.gethome.Activities.openmap.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gian.gethome.Activities.animaldetalle.view.AnimalDetalleActivity
import com.gian.gethome.Activities.openmap.Model.Coordinates
import com.gian.gethome.Activities.openmap.`interface`.OpenMapView
import com.gian.gethome.Activities.openmap.interactor.OpenMapInteractor
import com.gian.gethome.Activities.openmap.presenter.OpenMapPresenter
import com.gian.gethome.Adapters.LocationInfoAdapter
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*


class OpenMapActivity : AppCompatActivity(), OnMapReadyCallback,OpenMapView, GoogleMap.OnMarkerClickListener, LocationInfoAdapter.OnItemClickListener {
    private lateinit var map:GoogleMap
    private lateinit var myCurrentLatitude:String
    private lateinit var myCurrentLongitude:String
    private val presenter = OpenMapPresenter(this, OpenMapInteractor())
    private lateinit var customMarker:Marker
    private lateinit var adapter: LocationInfoAdapter
    private lateinit var myRecycler: RecyclerView
    private var mlistOfAnimals: ArrayList<AnimalAdapterData> = arrayListOf()
    private lateinit var dialog: BottomSheetDialog
    private lateinit var marker:Marker



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_map)
        getValues()
        createFragment()
        presenter.retrieveDataOfLocations()
    }

    private fun getValues() {
        myCurrentLatitude = intent.getStringExtra("currentLatitude").toString() //despues borrar
        myCurrentLongitude = intent.getStringExtra("currentLongitude").toString() //despues borrar
        dialog = BottomSheetDialog(this, R.style.BottomShitDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_maps, null)
        myRecycler = view.findViewById(R.id.recyclerInfoPets)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.dismissWithAnimation = true
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        map.setOnMarkerClickListener(this)
    }

    private fun createMarker(listOfCoordinates: MutableList<Coordinates>) {
        for(item in listOfCoordinates.indices){
            marker = makeMarker(
                    listOfCoordinates[item].latitude,
                    listOfCoordinates[item].longitude,
                    listOfCoordinates[item].idUserOwner,
                    R.drawable.ic_foot)
            marker.snippet = listOfCoordinates[item].idUserOwner
        }
    }

    private fun makeMarker(latitude: String, longitude: String, idUserOwner: String, icFoot: Int): Marker {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom
        (LatLng(latitude.toDouble(),
                longitude.toDouble()),
                14f),
                1,
                null)

        customMarker = map.addMarker(MarkerOptions()
                .position(LatLng(latitude.toDouble(), longitude.toDouble()))
                .title("Holaaa")
                .snippet(idUserOwner)
                .anchor(0.5f, 0.5f)
                .icon(bitmapFromVector(this, icFoot)))
        return customMarker
    }


    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun passListOfCoordinates(listOfCoordinates: MutableList<Coordinates>) {
        createMarker(listOfCoordinates)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        dialog.show()
        presenter.retrieveDataFromDBOf(marker.snippet, myCurrentLongitude, myCurrentLatitude)
        return true
    }

    override fun passListOfAnimals(mlistOfAnimals: ArrayList<AnimalAdapterData>) {
        this.mlistOfAnimals = mlistOfAnimals
        adapter = LocationInfoAdapter(mlistOfAnimals, this)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myRecycler.layoutManager = linearLayoutManager
        myRecycler.setHasFixedSize(true)
        myRecycler.adapter = adapter
        adapter.setOnItemClickListener(this)

    }

    override fun onitemClick(position: Int) {
        val intent = Intent(this, AnimalDetalleActivity::class.java)
        val animalClicked:AnimalAdapterData = mlistOfAnimals[position]
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


}