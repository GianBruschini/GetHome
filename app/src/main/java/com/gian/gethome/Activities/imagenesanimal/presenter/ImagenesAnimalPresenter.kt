package com.gian.gethome.Activities.imagenesanimal.presenter

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.gian.gethome.Activities.imagenesanimal.interfaces.ImagenesAnimalView
import com.gian.gethome.Activities.imagenesanimal.model.ImagenesAnimalInteractor
import com.gian.gethome.Activities.imagenesanimal.view.ImagenesAnimalActivity

class ImagenesAnimalPresenter(var imagenesAnimalView: ImagenesAnimalView?,
                              var imagenesAnimalInteractor: ImagenesAnimalInteractor):ImagenesAnimalInteractor.onImagenesAnimalListener {


    override fun setImageSelected(imageUri: Uri?, imageView: ImageView) {
        imagenesAnimalView?.setImageSelected(imageUri,imageView)
    }

    override fun onAddAtLeastOneImage() {
        imagenesAnimalView?.addAtLeastOneImage()
    }

    override fun onShowProgressDialog() {
        imagenesAnimalView?.showProgressDialog()
    }

    override fun onHideProgressDialog() {
        imagenesAnimalView?.hideProgressDialog()
    }

    override fun navigateToHome() {
        imagenesAnimalView?.navigateToHome()
    }

    fun setLocalDate() {
        imagenesAnimalInteractor.setLocalDate(this)
    }

    fun setMutableList(imagen1: ImageView, imagen2: ImageView, imagen3: ImageView, imagen4: ImageView) {
        imagenesAnimalInteractor.setMutableList(imagen1,imagen2,imagen3,imagen4,this)
    }

    fun setImageViewWith(resultCode: Int, data: Intent?, requestCode: Int,context:ImagenesAnimalActivity) {
        imagenesAnimalInteractor.setImageViewResult(resultCode, data!!,requestCode,this,context)
    }

    fun setImageViewNull(posImage: Int) {
        imagenesAnimalInteractor.setImageInNull(posImage)
    }

    fun publicarAnimalDB(nombreAnimal: String,
                         tipoAnimal: String,
                         transitoUrgente: String,
                         edadAnimal: String,
                         descripcionAnimal: String,
                         sexoAnimal: String,
                         provincia: String,
                         pais: String,
                         whatsapp: String,
                         phone: String,
                         mail: String,
                         imagenesAnimalActivity: ImagenesAnimalActivity,
                         facebook: String,
                         instagram: String,
                         cantAnimales: String,
                         latitude: String,
                         longitude: String) {
        imagenesAnimalInteractor.publicarAnimalDB(nombreAnimal
                ,tipoAnimal,transitoUrgente,edadAnimal,descripcionAnimal,sexoAnimal
                ,provincia,pais,whatsapp,phone,
                mail,this,imagenesAnimalActivity,
                facebook,instagram,cantAnimales,latitude,longitude)
    }

    fun onDestroy() {
        imagenesAnimalView = null
    }
}