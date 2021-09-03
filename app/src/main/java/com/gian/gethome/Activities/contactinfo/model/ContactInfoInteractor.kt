package com.gian.gethome.Activities.contactinfo.model

import android.Manifest
import android.content.ActivityNotFoundException
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
    private lateinit var context: ContactInfoActivity
    private lateinit var listener: onContactInfoListener
    interface onContactInfoListener{
        fun onUserInfo(userInfo: UserInfo)
        fun onDatabaseError()
        fun onPassAnimalData(animal:Animal)
        fun onNotMailData()
        fun onNotWhatsappData()
        fun onNotPhonelData()
        fun onNotWhatsappInstalled()
        fun onDeleteWhatsapp()
        fun onDeletePhone()
        fun onDeleteMail()
        fun onDeleteInstagram()
        fun onDeleteFacebook()
    }

    fun detectuserInfo(idOwner: String, listener: ContactInfoPresenter, context: ContactInfoActivity){
        this.context = context
        this.listener = listener
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

    fun detectContactInfo(idOwner: String,animalKey:String){
        val databaseAnimalAccess = FirebaseDatabase.getInstance().
        reference.
        child("Users").
        child("Animales").
        child(idOwner).child(animalKey)
        databaseAnimalAccess.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val animal: Animal = snapshot.getValue(Animal::class.java)!!
                checkWhatInfoIsEmpty(animal)
                listener.onPassAnimalData(animal)
            }

            override fun onCancelled(error: DatabaseError) {
               listener.onDatabaseError()
            }
        })
    }

    private fun checkWhatInfoIsEmpty(animal: Animal) {
        if(animal.whatsapp.isEmpty()){
            listener.onDeleteWhatsapp()
        }
        if(animal.phone.isEmpty()){
            listener.onDeletePhone()
        }
        if(animal.mail.isEmpty()){
            listener.onDeleteMail()
        }
        if(animal.instagram.isEmpty()){
            listener.onDeleteInstagram()
        }
        if(animal.facebook.isEmpty()){
            listener.onDeleteFacebook()
        }
    }

    fun makeWhatsappAction(animal: Animal){
        if (animal.whatsapp.isNotEmpty()) {
            sendWhatsappMessage(animal)
        } else {
            listener.onNotWhatsappData()
        }
    }

    fun makeMailAction(animal: Animal) {
        if (animal.mail.isNotEmpty()) {
            openEmail(animal)
        }else{

            listener.onNotMailData()
        }
    }

    fun makePhoneAction(animal: Animal) {
        if (animal.phone.isNotEmpty()) {
            makePhoneCall(animal)
        } else {
            listener.onNotPhonelData()
        }
    }

    private fun makePhoneCall(animal: Animal) {
        if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${animal.phone}")
            context.startActivity(intent)
        }

    }

    private fun openEmail(animal: Animal) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.type = "text/plain"
        val uriText:String = "mailto:" + Uri.encode(animal.mail)
        val uri:Uri = Uri.parse(uriText)
        emailIntent.data = uri
        context.startActivity(Intent.createChooser(emailIntent,animal.mail))
    }

    private fun sendWhatsappMessage(animal: Animal) {
        val installed: Boolean = isAppInstalled("com.whatsapp")
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


    private fun isAppInstalled(s: String): Boolean {
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

    fun makeInstagramAction(animal: Animal) {
        val uri:Uri= Uri.parse("https://instagram.com/"+animal.instagram)
        val insagram= Intent (Intent.ACTION_VIEW,uri)
        insagram.setPackage("com.instagram.android")
        try {
            context.startActivity(insagram)
        }catch (e: ActivityNotFoundException){
            context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://instagram.com/"+animal.instagram)))
        }
    }

    fun makeFacebookAction(animal: Animal) {

        try {
            val intent= Intent (Intent.ACTION_VIEW,Uri.parse("fb://page/"+animal.facebook))
            context.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            val intent= Intent (Intent.ACTION_VIEW,
                    Uri.parse("https://facebook.com/"+animal.facebook))
            context.startActivity(intent)
        }
    }


}