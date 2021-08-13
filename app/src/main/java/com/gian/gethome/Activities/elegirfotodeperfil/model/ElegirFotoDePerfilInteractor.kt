package com.gian.gethome.Activities.elegirfotodeperfil.model

import android.graphics.drawable.Drawable
import android.net.Uri
import android.webkit.MimeTypeMap
import com.gian.gethome.Activities.elegirfotodeperfil.view.ElegirFotoDePerfilActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage

class ElegirFotoDePerfilInteractor {
    private lateinit var mAuth:FirebaseAuth
    private var imageUri: Uri? = null
    private var mUploadTask: StorageTask<*>? = null
    private lateinit var listener:onElegirFotoDePerfilListener


    interface onElegirFotoDePerfilListener{
        fun onStartHomeActivity()
        fun onStartSameActivity()
        fun onDatabaseError()
        fun onFotoPerfilNull()
        fun onSetImageProfile(imageUri: Uri?)
        fun onNoFileSelected()
        fun onShowProgressDialog()
        fun onHideProgressDialog()
        fun startActivityWithImageURL(imageURL: String)
        fun passImageUri(resultUri: Uri)
        fun passErrorRetrievingImageUri(error: Exception?)
    }

    fun checkIfUserIsSetInDb(listener: onElegirFotoDePerfilListener) {
        this.listener = listener
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            val ref = FirebaseDatabase.getInstance().reference.child("Users").
            child("Person").child(mAuth.currentUser!!.uid)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        if (data.exists()) {
                            listener.onStartHomeActivity()
                        } else {
                            //listener.onStartSameActivity()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onDatabaseError()
                }
            })
        }
    }



    fun checkAndUpload(profile: Drawable?, mStorageRef: StorageReference?,
                       context: ElegirFotoDePerfilActivity) {
        if (profile != null) {
            listener.onShowProgressDialog()
            uploadPicture(mStorageRef, context)
        } else {
            listener.onFotoPerfilNull()
        }
    }

    private fun getFileExtension(uri: Uri, context: ElegirFotoDePerfilActivity): String? {
        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadPicture(mStorageRef: StorageReference?, context: ElegirFotoDePerfilActivity) {
        if (imageUri != null) {
            val fileReference = mStorageRef!!.child(System.currentTimeMillis()
                    .toString() + "." + getFileExtension(imageUri!!, context))
            mUploadTask = fileReference.putFile(imageUri!!)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val model = Model(uri.toString())
                            storeValuesFirebase(model.imageURL)
                        }
                    }
                    .addOnFailureListener { e ->  }
        } else {
            listener.onNoFileSelected()
        }
    }


    private fun storeValuesFirebase(imageURL: String) {
        val crearUser: HashMap<String, Any> = HashMap()
        crearUser["imageURL"] = imageURL
        crearUser["userName"] = mAuth.currentUser?.displayName.toString()
        val currentUser = mAuth.currentUser
        val userId = currentUser!!.uid
        FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Person").child(userId).updateChildren(crearUser)
        listener.startActivityWithImageURL(imageURL)
        listener.onHideProgressDialog()

    }

    fun retrieveImageResult(result: CropImage.ActivityResult?, resultCode: Int) {
        if (resultCode == -1) {
            val resultUri: Uri = result!!.uri
            imageUri = resultUri
            listener.passImageUri(resultUri)
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            listener.passErrorRetrievingImageUri(result?.error)
        }
    }

}