package com.gian.gethome.Fragments.perfil.presenter

import com.gian.gethome.Fragments.perfil.interfaces.PerfilView
import com.gian.gethome.Fragments.perfil.model.PerfilInteractor

class PerfilPresenter(var perfilView: PerfilView?, var perfilInteractor: PerfilInteractor):PerfilInteractor.onPerfilInteractorListener {
    fun getImageFromDB() {
        perfilInteractor.getImage(this)
    }

    override fun onSetProfileImage(imageURL: String) {
        perfilView?.setImageProfile(imageURL)
    }

    override fun onSetUserName(name: String) {
        perfilView?.setUserNameText(name)
    }

    override fun onAccountDeleted() {
        perfilView?.accounWasDeleted()
    }

    fun getUserNameFromDB() {
        perfilInteractor.getUserNameFromDB(this)
    }

    fun deleteAccount() {
        perfilInteractor.deleteAccount(this)
    }

    fun onDestroy() {
        perfilView = null
    }


}