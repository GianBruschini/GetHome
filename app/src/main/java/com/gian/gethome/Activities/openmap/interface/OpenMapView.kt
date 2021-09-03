package com.gian.gethome.Activities.openmap.`interface`

import com.gian.gethome.Activities.openmap.Model.Coordinates
import com.gian.gethome.Clases.AnimalAdapterData
import java.util.ArrayList

interface OpenMapView {
    fun passListOfCoordinates(listOfCoordinates: MutableList<Coordinates>)
    fun passListOfAnimals(mlistOfAnimals: ArrayList<AnimalAdapterData>)

}