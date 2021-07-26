package com.gian.gethome.Activities.elegirfotodeperfil.presenter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import com.gian.gethome.Activities.elegirfotodeperfil.interfaces.ElegirFotoDePerfilView
import com.gian.gethome.Activities.elegirfotodeperfil.model.ElegirFotoDePerfilInteractor
import com.gian.gethome.Activities.elegirfotodeperfil.view.ElegirFotoDePerfilActivity
import com.google.firebase.storage.StorageReference

class ElegirFotoDePerfilPresenter(var elegirFotoDePerfilView: ElegirFotoDePerfilView?,
                                  var elegirFotoDePerfilInteractor: ElegirFotoDePerfilInteractor):ElegirFotoDePerfilInteractor.onElegirFotoDePerfilListener {


    fun checkIfUserIsSetInDB(){
        elegirFotoDePerfilInteractor.checkIfUserIsSetInDb(this)
    }

    override fun onStartHomeActivity() {
        elegirFotoDePerfilView?.navigateToHomeActivity()
    }

    override fun onStartSameActivity() {
        elegirFotoDePerfilView?.navigateToSameActivity()
    }

    override fun onDatabaseError() {
        elegirFotoDePerfilView?.onDatabaseError()
    }

    override fun onFotoPerfilNull() {
        elegirFotoDePerfilView?.avisoFotoNull()
    }

    override fun onSetImageProfile(imageUri: Uri?) {
        elegirFotoDePerfilView?.onSetImageProfile(imageUri!!)
    }

    override fun onNoFileSelected() {
        elegirFotoDePerfilView?.noFileSelected()
    }

    override fun onShowProgressDialog() {
        elegirFotoDePerfilView?.showProgressDialog()
    }

    override fun onHideProgressDialog() {
        elegirFotoDePerfilView?.hideProgressDialog()
    }

    override fun startActivityWithImageURL(imageURL: String) {
        elegirFotoDePerfilView?.startActivityWithImageURL(imageURL)
    }

    fun setActivityResultData(imageUri: Uri?, requestCode: Int, resultCode: Int, data: Intent?, context: ElegirFotoDePerfilActivity) {
        elegirFotoDePerfilInteractor.setActivityResultData(imageUri, requestCode, resultCode, data,this,context)
    }

    fun uploadImageProfile(drawable: Drawable?, mStorageRef: StorageReference?, elegirFotoDePerfilActivity: ElegirFotoDePerfilActivity) {
        elegirFotoDePerfilInteractor.checkAndUpload(drawable,mStorageRef,this,elegirFotoDePerfilActivity)
    }

    fun onDestroy() {
        elegirFotoDePerfilView = null //muy importante
    }
}