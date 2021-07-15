package com.gian.gethome.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.AnimalAdapterData
import com.gian.gethome.Clases.UserInfo
import com.gian.gethome.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_contact_info.*

class ContactInfoActivity : AppCompatActivity() {
    private lateinit var idOwner:String
    private lateinit var animalKey:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)
        getValues()
    }

    private fun getValues() {
        idOwner = intent.getStringExtra("idOwner").toString()
        animalKey = intent.getStringExtra("animalKey").toString()
        val databaseUserAccess = FirebaseDatabase.getInstance().
        reference.
        child("Users").
        child("Person").
        child(idOwner)
        databaseUserAccess.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val userInfo:UserInfo = snapshot.getValue(UserInfo::class.java)!!
                    Picasso.get().load(userInfo.imageURL).placeholder(R.drawable.loader).fit().into(animalImg)
                    userOwnerName.text = userInfo.userName
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val databaseAnimalAccess = FirebaseDatabase.getInstance().
        reference.
        child("Users").
        child("Animales").
        child(idOwner).child(animalKey)
        databaseAnimalAccess.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val animal:Animal = snapshot.getValue(Animal::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }
}