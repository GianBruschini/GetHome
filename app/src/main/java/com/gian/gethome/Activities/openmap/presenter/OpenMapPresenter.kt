package com.gian.gethome.Activities.openmap.presenter

import com.gian.gethome.Activities.openmap.Model.Coordinates
import com.gian.gethome.Activities.openmap.`interface`.OpenMapView
import com.gian.gethome.Activities.openmap.interactor.OpenMapInteractor
import com.gian.gethome.Clases.AnimalAdapterData

class OpenMapPresenter(var onOpenMapView: OpenMapView,var openMapInteractor: OpenMapInteractor):OpenMapInteractor.onOpenMapInteractorListener {
    fun retrieveDataOfLocations() {
        openMapInteractor.retrieveDataOfLocations(this)
    }

    override fun onDataBaseError() {

    }

    override fun onPassListOfCoordinates(listOfCoordinates: MutableList<Coordinates>) {
        onOpenMapView.passListOfCoordinates(listOfCoordinates)
    }

    override fun onPassListOfAnimals(mlistOfAnimals: ArrayList<AnimalAdapterData>) {
        onOpenMapView.passListOfAnimals(mlistOfAnimals)
    }

    fun retrieveDataFromDBOf(idUserOwner: String?, myCurrentLongitude: String, myCurrentLatitude: String) {
        openMapInteractor.retrieveDataFromDB(idUserOwner,myCurrentLatitude,myCurrentLongitude)
    }

}