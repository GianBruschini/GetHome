package com.gian.gethome.Activities.imagenesanimal.interfaces

import android.net.Uri
import android.widget.ImageView

interface ImagenesAnimalView {
    fun hideProgressDialog()
    fun showProgressDialog()
    fun setLocalDate(date:String)
    fun setImageSelected(imageUri: Uri?, imageView: ImageView)
    fun addAtLeastOneImage()
    fun navigateTo()



}