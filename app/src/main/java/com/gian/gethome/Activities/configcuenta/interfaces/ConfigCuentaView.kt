package com.gian.gethome.Activities.configcuenta.interfaces

import android.net.Uri

interface ConfigCuentaView {
    fun showDatabaseError()
    fun showProfileImage(imageURL:String)
    fun showUserName(userName: String)
    fun showAccountDeleted()
    fun showImageCrop(resultUri: Uri)
    fun showErrorRetrievingImageUri(error: Exception?)
    fun showChangesSaved()
    fun showChangesNotMade()
    fun showProgressDialog()
    fun hideProgresDialog()
    fun setAparecerEnMapaValue(value: String)



}