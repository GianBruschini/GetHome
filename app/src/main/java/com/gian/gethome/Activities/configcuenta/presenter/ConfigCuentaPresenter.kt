package com.gian.gethome.Activities.configcuenta.presenter

import android.net.Uri
import com.gian.gethome.Activities.configcuenta.interfaces.ConfigCuentaView
import com.gian.gethome.Activities.configcuenta.model.ConfigCuentaInteractor
import com.gian.gethome.Activities.configcuenta.view.ConfigCuentaActivity
import com.theartofdev.edmodo.cropper.CropImage

class ConfigCuentaPresenter(var configCuentaView: ConfigCuentaView?,
                            val configCuentaInteractor: ConfigCuentaInteractor):ConfigCuentaInteractor.onConfigCuentaListener {
    fun getProifleImage() {
        configCuentaInteractor.getProfileImageFromDB(this)
    }

    override fun onPassProfileImage(imageViewURL: String) {
        configCuentaView?.showProfileImage(imageViewURL)
    }

    override fun onPassUserName(userName: String) {
        configCuentaView?.showUserName(userName)
    }

    override fun onDataBaseError() {
        configCuentaView?.showDatabaseError()
    }

    override fun onAccountDeleted() {
        configCuentaView?.showAccountDeleted()
    }

    override fun passImageUri(resultUri: Uri) {
        configCuentaView?.showImageCrop(resultUri)
    }

    override fun passErrorRetrievingImageUri(error: Exception?) {
       configCuentaView?.showErrorRetrievingImageUri(error)
    }

    override fun onChangesSaved() {
        configCuentaView?.showChangesSaved()
    }

    override fun onShowChangesNotMade() {
        configCuentaView?.showChangesNotMade()
    }

    override fun onHideProgressDialog() {
        configCuentaView?.showProgressDialog()
    }

    override fun onShowProgressDialog() {
        configCuentaView?.hideProgresDialog()
    }

    override fun onPassAparecerEnMapaValue(value: String) {
        configCuentaView?.setAparecerEnMapaValue(value)
    }

    fun deleteAccount() {
        configCuentaInteractor.deleteAccount()
    }

    fun onDestroy() {
        configCuentaView = null
    }

    fun retrieveUri(result: CropImage.ActivityResult?, resultCode: Int) {
        configCuentaInteractor.retrieveImageResult(result,resultCode)
    }

    fun saveNewChanges(nombrePerfil: String, context: ConfigCuentaActivity) {
        configCuentaInteractor.saveChangesDB(nombrePerfil,context)
    }

    fun guardarAparecerEnMapa() {
        configCuentaInteractor.guardarAparecerEnMapaDB()
    }

    fun eliminarAparecerEnMapa() {
        configCuentaInteractor.eliminarAparecerEnMapaDB()
    }

    fun checkIfAparecerEnMapaIsChecked() {
        configCuentaInteractor.checkIfAparecerEnMapaIsChecked()
    }
}