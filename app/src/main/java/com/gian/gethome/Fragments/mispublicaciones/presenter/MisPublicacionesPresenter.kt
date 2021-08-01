package com.gian.gethome.Fragments.mispublicaciones.presenter

import com.gian.gethome.Clases.Animal
import com.gian.gethome.Fragments.mispublicaciones.interfaces.MisPublicacionesView
import com.gian.gethome.Fragments.mispublicaciones.model.MisPublicacionesInteractor

class MisPublicacionesPresenter(var misPublicacionesView: MisPublicacionesView,
                                var misPublicacionesInteractor: MisPublicacionesInteractor): MisPublicacionesInteractor.onMisPublicacionesListener {
    fun getMyPubsData() {
        misPublicacionesInteractor.getPubData(this)
    }

    override fun onPassAnimalData(animal: Animal, imagenNotNull: String) {
        misPublicacionesView.fillRecyclerViewWith(animal,imagenNotNull)
    }

    override fun onNotifyDataChanged() {
        misPublicacionesView.notifyDataChanged()
    }

    override fun onSetTextoEmptyVisible() {
        misPublicacionesView.setTextViewVisible()
    }

    override fun onDataBaseError() {
        misPublicacionesView.showDataBaseError()
    }

    override fun onNotifyPubDeleted(position: Int) {
        misPublicacionesView.notifyPubDeleted(position)
    }

    fun deleteAnimalFromDB(position: Int) {
        misPublicacionesInteractor.deleteAnimalFromDB(position)
    }
}