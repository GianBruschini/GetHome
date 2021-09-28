package com.gian.gethome.Activities.configcuenta.model

import android.net.Uri
import android.webkit.MimeTypeMap
import com.gian.gethome.Activities.configcuenta.view.ConfigCuentaActivity
import com.gian.gethome.Activities.elegirfotodeperfil.model.Model
import com.gian.gethome.Clases.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage


class ConfigCuentaInteractor {
    private lateinit var mFirebaseAuth:FirebaseAuth
    private lateinit var listener:onConfigCuentaListener
    private lateinit var mStorageReference: StorageReference
    private lateinit var user:FirebaseUser
    private  var resultUri: Uri? = null
    private lateinit var mStorageRef: StorageReference
    private var mUploadTask: StorageTask<*>? = null
    private lateinit var currentImage:String
    private lateinit var currentUserName:String


    interface onConfigCuentaListener{
            fun onPassProfileImage(imageViewURL: String)
            fun onPassUserName(userName: String)
            fun onDataBaseError()
            fun onAccountDeleted()
            fun passImageUri(resultUri: Uri)
            fun passErrorRetrievingImageUri(error: Exception?)
             fun onChangesSaved()
            fun onShowChangesNotMade()
            fun onHideProgressDialog()
            fun onShowProgressDialog()
            fun onPassAparecerEnMapaValue(value: String)

    }


    fun getProfileImageFromDB(listener: onConfigCuentaListener) {
        this.listener = listener
        mFirebaseAuth = FirebaseAuth.getInstance()
        val profilePicture = FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Person").
        child(mFirebaseAuth.currentUser!!.uid)
        profilePicture.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: UserInfo = dataSnapshot.getValue(UserInfo::class.java)!!
                currentImage = user.imageURL
                currentUserName = user.userName
                listener.onPassProfileImage(user.imageURL)
                listener.onPassUserName(user.userName)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onDataBaseError()
            }
        })
    }

    fun deleteAccount() {
        val user = mFirebaseAuth.currentUser
        user?.delete()
        deleteAllOcurrences()
    }

    private fun deleteAllOcurrences() {
        val deleteOcurrencesFromAnimales = FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Animales").
        child(mFirebaseAuth.currentUser!!.uid)
        deleteOcurrencesFromAnimales.removeValue()

        val deleteOcurrencesFromPerson = FirebaseDatabase
                .getInstance().
                reference.
                child("Users").child("Person")
                .child(mFirebaseAuth.currentUser!!.uid)
        deleteOcurrencesFromPerson.removeValue()

        mStorageReference = FirebaseStorage.getInstance().getReference("UsersProfilePictures" + "/" + mFirebaseAuth.currentUser!!.uid)
        val storageReferenceUserProfile = FirebaseStorage.getInstance().reference.
        child("UsersProfilePictures").child(mFirebaseAuth.currentUser!!.uid)
        storageReferenceUserProfile.delete()


        mStorageReference = FirebaseStorage.getInstance().getReference("UploadsAnimals" + "/" + mFirebaseAuth.currentUser!!.uid)
        val storageReferenceUploadAnimals = FirebaseStorage.getInstance().reference.
        child("UploadsAnimals").child(mFirebaseAuth.currentUser!!.uid)
        storageReferenceUploadAnimals.delete()

        val user:FirebaseUser = mFirebaseAuth.currentUser!!
        user.delete().addOnSuccessListener {
            mFirebaseAuth.signOut()
        }

        listener.onAccountDeleted()
    }

    fun retrieveImageResult(result: CropImage.ActivityResult?, resultCode: Int) {
        if (resultCode == -1) {
            if (result != null) {
                resultUri = result.uri
            }
            listener.passImageUri(resultUri!!)

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            listener.passErrorRetrievingImageUri(result?.error)
        }
    }

    fun saveChangesDB(nombrePerfil: String,
                      context: ConfigCuentaActivity,
                      estadoMapa: Int) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        uploadChangesOnDB(context, nombrePerfil,estadoMapa)
    }

    private fun getFileExtension(uri: Uri, context: ConfigCuentaActivity): String? {
        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadChangesOnDB(
            context: ConfigCuentaActivity, nombrePerfil: String, estadoMapa: Int,
    ) {
        mStorageRef = FirebaseStorage.getInstance().
        getReference("UsersProfilePictures" + "/" + mFirebaseAuth.currentUser?.uid)
        if (resultUri != null) {
            val fileReference = mStorageRef.child(System.currentTimeMillis()
                    .toString() + "." + getFileExtension(resultUri!!, context))
            mUploadTask = fileReference.putFile(resultUri!!)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val model = Model(uri.toString())
                             storeValuesFirebase(model.imageURL, nombrePerfil,estadoMapa)
                        }
                    }
                    .addOnFailureListener { e ->  }
        }else{
            if(nombrePerfil != currentUserName || estadoMapa!=0){
                if(estadoMapa != 0){
                    when(estadoMapa){
                        1->guardarAparecerEnMapaDB()
                        2->eliminarAparecerEnMapaDB()
                    }
                }
                if(nombrePerfil != currentUserName){
                    storeValuesFirebase(null.toString(), nombrePerfil,estadoMapa)
                }
            }else{
                listener.onShowChangesNotMade()
                listener.onHideProgressDialog()
            }
        }
    }

    private fun storeValuesFirebase(
            imageURL: String, nombrePerfil: String,estadoMapa: Int
    ) {

        if(estadoMapa != 0){
            when(estadoMapa){
                1->guardarAparecerEnMapaDB()
                2->eliminarAparecerEnMapaDB()
            }
        }
        if(imageURL!="null"){
            val crearUser: HashMap<String, Any> = HashMap()
            crearUser["imageURL"] = imageURL
            crearUser["userName"] = nombrePerfil
            val currentUser = mFirebaseAuth.currentUser
            val userId = currentUser!!.uid
            FirebaseDatabase.getInstance().
            reference.child("Users").
            child("Person").child(userId).updateChildren(crearUser)
            listener.onHideProgressDialog()
            listener.onChangesSaved()
        }else{
            val crearUser: HashMap<String, Any> = HashMap()
            crearUser["userName"] = nombrePerfil
            val currentUser = mFirebaseAuth.currentUser
            val userId = currentUser!!.uid
            FirebaseDatabase.getInstance().
            reference.child("Users").
            child("Person").child(userId).updateChildren(crearUser)
            listener.onHideProgressDialog()
            listener.onChangesSaved()
        }


    }

    fun guardarAparecerEnMapaDB() {
        val ref = FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Person").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("mapa").child("figurar")
        val boolean = true
        ref.setValue(boolean.toString())

    }

    fun eliminarAparecerEnMapaDB() {
        FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Person").
        child(FirebaseAuth.getInstance().currentUser!!.uid).
        child("mapa").child("figurar").setValue("false")

    }

    fun checkIfAparecerEnMapaIsChecked() {
        val database = FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Person").
        child(FirebaseAuth.getInstance().currentUser!!.uid).child("mapa")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    if(dataSnapshot.exists()){
                        val quieroAparecerEnMapa =
                                dataSnapshot.getValue(String::class.java)!!
                        listener.onPassAparecerEnMapaValue(quieroAparecerEnMapa)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                listener.onDataBaseError()
            }
        })
    }

}