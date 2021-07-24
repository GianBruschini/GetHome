package com.gian.gethome.Activities.contactinfo.model

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gian.gethome.Activities.contactinfo.presenter.ContactInfoPresenter
import com.gian.gethome.Activities.contactinfo.view.ContactInfoActivity
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactInfoInteractor {
    private val REQUEST_CALL = 1
    interface onContactInfoListener{
        fun onUserInfo(userInfo: UserInfo)
        fun onDatabaseError()
        fun onPassAnimalData(animal:Animal)
        fun onNotMailData()
        fun onNotWhatsappData()
        fun onNotPhonelData()
        fun onNotWhatsappInstalled()
    }

    fun detectuserInfo(idOwner:String,listener: ContactInfoPresenter){
        val databaseUserAccess = FirebaseDatabase.getInstance().
        reference.
        child("Users").
        child("Person").
        child(idOwner)
        databaseUserAccess.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userInfo: UserInfo = snapshot.getValue(UserInfo::class.java)!!
                listener.onUserInfo(userInfo)
            }
            override fun onCancelled(error: DatabaseError) {
                listener.onDatabaseError()
            }
        })
    }

    fun detectContactInfo(idOwner: String,animalKey:String,listener: onContactInfoListener){
        val databaseAnimalAccess = FirebaseDatabase.getInstance().
        reference.
        child("Users").
        child("Animales").
        child(idOwner).child(animalKey)
        databaseAnimalAccess.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val animal: Animal = snapshot.getValue(Animal::class.java)!!
                listener.onPassAnimalData(animal)
            }

            override fun onCancelled(error: DatabaseError) {
               listener.onDatabaseError()
            }
        })
    }

    fun makeWhatsappAction(animal: Animal,context:ContactInfoActivity,listener: onContactInfoListener){
        if (animal.whatsapp.isNotEmpty()) {
            sendWhatsappMessage(animal,context,listener)
        } else {
            listener.onNotWhatsappData()
        }
    }

    fun makeMailAction(animal: Animal, context: ContactInfoActivity,listener: onContactInfoListener) {
        if (animal.mail.isNotEmpty()) {
            openEmail(context)
        }else{

            listener.onNotMailData()
        }
    }

    fun makePhoneAction(animal: Animal, context: ContactInfoActivity,listener: onContactInfoListener) {
        if (animal.phone.isNotEmpty()) {
            makePhoneCall(animal,context)
        } else {
            listener.onNotPhonelData()
        }
    }

    private fun makePhoneCall(animal: Animal, context: ContactInfoActivity) {
        if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${animal.phone}")
            context.startActivity(intent)
        }

    }


    private fun openEmail(context: ContactInfoActivity) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        context.startActivity(emailIntent)
    }

    private fun sendWhatsappMessage(animal: Animal, context: ContactInfoActivity, listener: onContactInfoListener) {
        val installed: Boolean = isAppInstalled("com.whatsapp",context)
        if (installed) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=" +
                    animal.whatsapp +
                    "&text=" +
                    "Hola, estoy interesado por" + " " + animal.nombre)
            context.startActivity(intent)
        } else {
            listener.onNotWhatsappInstalled()
        }
    }


    private fun isAppInstalled(s: String, context: ContactInfoActivity): Boolean {
        val packageManager = context.packageManager
        var isInstalled: Boolean
        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES)
            isInstalled = true
        } catch (e: PackageManager.NameNotFoundException) {
            isInstalled = false
            e.printStackTrace()
        }
        return isInstalled
    }


}