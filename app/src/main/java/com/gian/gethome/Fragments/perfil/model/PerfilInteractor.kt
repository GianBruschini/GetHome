package com.gian.gethome.Fragments.perfil.model


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class PerfilInteractor {
    private lateinit var mFirebaseAuth:FirebaseAuth
    private lateinit var mStorageReference: StorageReference
    private lateinit var user: FirebaseUser


    interface onPerfilInteractorListener{
        fun onSetProfileImage(imageURL: String)
        fun onSetUserName(name: String)
        fun onAccountDeleted()
    }

    fun getImage(listener: onPerfilInteractorListener) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val profilePicture = FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Person").
        child(mFirebaseAuth.currentUser!!.uid)
                .child("imageURL")
        profilePicture.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageUrl = dataSnapshot.value as String?
                listener.onSetProfileImage(imageUrl!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getUserNameFromDB(listener: onPerfilInteractorListener) {
        listener.onSetUserName(mFirebaseAuth.currentUser?.displayName.toString())
    }

    fun deleteAccount(listener: onPerfilInteractorListener) {
        val user = mFirebaseAuth.currentUser
        user?.delete()
        deleteAllOcurrences(listener)
    }

    private fun deleteAllOcurrences(listener: onPerfilInteractorListener) {
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

        user = mFirebaseAuth.currentUser!!
        user.delete().addOnSuccessListener {
            mFirebaseAuth.signOut()
        }

        listener.onAccountDeleted()

    }
}