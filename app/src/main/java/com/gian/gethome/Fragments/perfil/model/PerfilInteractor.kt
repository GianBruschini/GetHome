package com.gian.gethome.Fragments.perfil.model


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class PerfilInteractor {
    private lateinit var mFirebaseAuth:FirebaseAuth


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
        val profileUserName = FirebaseDatabase.getInstance().
        reference.child("Users").
        child("Person").
        child(mFirebaseAuth.currentUser!!.uid)
                .child("userName")
        profileUserName.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userName = dataSnapshot.value as String?
                listener.onSetUserName(userName.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }




}