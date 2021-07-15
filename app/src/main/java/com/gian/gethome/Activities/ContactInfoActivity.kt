package com.gian.gethome.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityContactInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_contact_info.*


class ContactInfoActivity : AppCompatActivity() {
    private lateinit var idOwner:String
    private lateinit var animalKey:String
    private lateinit var binding: ActivityContactInfoBinding
    private val REQUEST_CALL = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                val userInfo: UserInfo = snapshot.getValue(UserInfo::class.java)!!
                setUserInfo(userInfo)
            }

            private fun setUserInfo(userInfo: UserInfo) {
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
                val animal: Animal = snapshot.getValue(Animal::class.java)!!
                setContactInfo(animal)
            }

            private fun setContactInfo(animal: Animal) {

                binding.whatsapp.setOnClickListener {
                    if (animal.whatsapp.isNotEmpty()) {
                        sendWhatsappMessage(animal)
                    } else {
                        Toast.makeText(this@ContactInfoActivity,
                                "El usuario no ha proporcionado información sobre su Whatsapp!",
                                Toast.LENGTH_SHORT).show()
                    }

                }


                binding.phone.setOnClickListener {
                    if (animal.phone.isNotEmpty()) {
                        makePhoneCall(animal)
                    } else {
                        Toast.makeText(this@ContactInfoActivity,
                                "El usuario no ha proporcionado información sobre su teléfono!",
                                Toast.LENGTH_SHORT).show()
                    }

                }

                binding.mail.setOnClickListener {
                    if (animal.mail.isNotEmpty()) {
                        openEmail()
                    }else{
                        Toast.makeText(this@ContactInfoActivity,
                                "El usuario no ha proporcionado información sobre su email!",
                                Toast.LENGTH_SHORT).show()
                    }
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }

    private fun makePhoneCall(animal: Animal) {
        if (ContextCompat.checkSelfPermission(this@ContactInfoActivity,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@ContactInfoActivity, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${animal.phone}")
            startActivity(intent)
        }

    }


    private fun openEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        startActivity(emailIntent)
    }

    private fun sendWhatsappMessage(animal: Animal) {
        val installed: Boolean = isAppInstalled("com.whatsapp")
        if (installed) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=" +
                    animal.whatsapp +
                    "&text=" +
                    "Hola, estoy interesado por" + " " + animal.nombre)
            startActivity(intent)
        } else {
            Toast.makeText(this@ContactInfoActivity, "No tienes instalado Whatsapp!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun isAppInstalled(s: String): Boolean {
        val packageManager = packageManager
        var is_installed: Boolean
        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES)
            is_installed = true
        } catch (e: PackageManager.NameNotFoundException) {
            is_installed = false
            e.printStackTrace()
        }
        return is_installed
    }

    fun back(view: View) {
        finish()
    }


}