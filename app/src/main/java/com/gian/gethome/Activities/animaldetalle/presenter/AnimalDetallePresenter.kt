package com.gian.gethome.Activities.animaldetalle.presenter

import com.gian.gethome.Activities.animaldetalle.interfaces.AnimalDetalleView
import com.gian.gethome.Activities.animaldetalle.model.AnimalDetalleInteractor
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo

class AnimalDetallePresenter (var animalDetalleView: AnimalDetalleView?,
                              var animalDetalleInteractor: AnimalDetalleInteractor):AnimalDetalleInteractor.onAnimalDetalleListener {


    fun checkFavouritesInDB(animalKey: String) {
        animalDetalleInteractor.checkFavouritesInDB(animalKey,this)
    }
    fun saveLikes(animalKey: String, userIDownerAnimal: String) {
        animalDetalleInteractor.saveLikes(animalKey,userIDownerAnimal)
    }
    fun deleteLike(animalKey: String) {
        animalDetalleInteractor.deleteLike(animalKey)
    }

    fun setFotoYnombrePerfil(userIDownerAnimal: String) {
        animalDetalleInteractor.setFotoPerfilYNombredePerfil(this,userIDownerAnimal)
    }

    fun loadImagesAnimal(animalKey: String) {
        animalDetalleInteractor.loadImagesOfTheAnimal(animalKey,this)
    }

    fun detectSexoAnimal(sexoAnimal: String) {
        animalDetalleInteractor.detectSexoAnimal(sexoAnimal,this)
    }

    fun detectTransitoUrgente(transitoUrgente: String) {
        animalDetalleInteractor.detectTransitoUrgente(transitoUrgente,this)
    }
    fun onDestroy(){
        animalDetalleView = null
    }
//-------------------------------------------------------------------------------------------
    override fun onDatabaseError() {
        animalDetalleView?.databaseError()
    }

    override fun onSetLikeButton() {
        animalDetalleView?.setFavButton()
    }

    override fun onSetFotoYnombre(userInfo: UserInfo) {
        animalDetalleView?.setNombreYfoto(userInfo)
    }

    override fun onLoadImages(animalImages: MutableList<String>) {
        animalDetalleView?.loadImages(animalImages)
    }

    override fun onTransitoUrgente(transitoUrgente: String) {
        animalDetalleView?.setTransitoUrgente(transitoUrgente)
    }

    override fun onDetectSexo(sexo: Int) {
        animalDetalleView?.setSexo(sexo)
    }

    override fun onPassAnimalData(animal: Animal) {
        animalDetalleView?.getContactInfoData(animal)
    }

    fun retrieveContactInfoData(userIDownerAnimal: String, animalKey: String) {
        animalDetalleInteractor.retrieveContactInfOf(userIDownerAnimal,animalKey)
    }

}