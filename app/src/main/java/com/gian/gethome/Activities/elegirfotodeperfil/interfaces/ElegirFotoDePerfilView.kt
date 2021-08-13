package com.gian.gethome.Activities.elegirfotodeperfil.interfaces

import android.net.Uri

interface ElegirFotoDePerfilView {

    fun navigateToHomeActivity()
    fun navigateToSameActivity()
    fun onDatabaseError()
    fun avisoFotoNull()
    fun onSetImageProfile(imageUri: Uri)
    fun noFileSelected()
    fun showProgressDialog()
    fun hideProgressDialog()
    fun startActivityWithImageURL(imageURL: String)
    fun showImageCrop(resultUri: Uri)
    fun showErrorRetrievingImageUri(error: Exception?)



}