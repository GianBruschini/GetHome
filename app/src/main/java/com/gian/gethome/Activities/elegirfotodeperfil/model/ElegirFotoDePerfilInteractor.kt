package com.gian.gethome.Activities.elegirfotodeperfil.model

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.gian.gethome.Activities.elegirfotodeperfil.view.ElegirFotoDePerfilActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import java.io.IOException

class ElegirFotoDePerfilInteractor {
    private lateinit var mAuth:FirebaseAuth
    private val PICK_IMAGE: Int = 1
    private var imageUri: Uri? = null
    private var mUploadTask: StorageTask<*>? = null


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




    }

    fun checkIfUserIsSetInDb(listener: onElegirFotoDePerfilListener) {
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
                            listener.onStartSameActivity()
                        }
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    listener.onDatabaseError()
                }
            })
        }
    }


    fun setActivityResultData(imageUriRef: Uri?, requestCode: Int, resultCode: Int, data: Intent?,listener: onElegirFotoDePerfilListener) {
        if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null) {

            imageUri  = imageUriRef
            imageUri = data.data
            try {
                listener.onSetImageProfile(imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun checkAndUpload(profile: Drawable?, mStorageRef: StorageReference?,
                       listener: onElegirFotoDePerfilListener,
                       context: ElegirFotoDePerfilActivity) {
        if (profile != null) {
            listener.onShowProgressDialog()
            uploadPicture(mStorageRef,context,listener)
        } else {
            listener.onFotoPerfilNull()
        }
    }

    private fun getFileExtension(uri: Uri, context: ElegirFotoDePerfilActivity): String? {
        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadPicture(mStorageRef: StorageReference?, context: ElegirFotoDePerfilActivity, listener: onElegirFotoDePerfilListener) {
        if (imageUri != null) {
            val fileReference = mStorageRef!!.child(System.currentTimeMillis()
                    .toString() + "." + getFileExtension(imageUri!!,context))
            mUploadTask = fileReference.putFile(imageUri!!)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val model = Model(uri.toString())
                            storeValuesFirebase(model.imageURL,listener)
                        }
                    }
                    .addOnFailureListener { e ->  }
        } else {
            listener.onNoFileSelected()
        }
    }


    private fun storeValuesFirebase(imageURL: String, listener: onElegirFotoDePerfilListener) {
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


}