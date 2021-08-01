package com.gian.gethome.Activities.contactinfo.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gian.gethome.Activities.contactinfo.interfaces.ContactInfoView
import com.gian.gethome.Activities.contactinfo.model.ContactInfoInteractor
import com.gian.gethome.Activities.contactinfo.presenter.ContactInfoPresenter
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityContactInfoBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_contact_info.*


class ContactInfoActivity : AppCompatActivity(),ContactInfoView {
    private lateinit var idOwner:String
    private lateinit var animalKey:String
    private lateinit var binding: ActivityContactInfoBinding
    private val presenter = ContactInfoPresenter(this, ContactInfoInteractor())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getValues()
        presenter.detectUserInfo(idOwner,this)
        presenter.detectContactInfo(idOwner, animalKey)

    }

    private fun getValues() {
        idOwner = intent.getStringExtra("idOwner").toString()
        animalKey = intent.getStringExtra("animalKey").toString()
    }

    fun back(view: View) {
        finish()
    }

    override fun setUserInfo(userInfo: UserInfo) {
        Picasso.get().load(userInfo.imageURL).placeholder(R.drawable.progress_animation).fit().into(animalImg)
        userOwnerName.text = userInfo.userName
    }


    override fun dataBaseError() {
        Toast.makeText(this,
                "Error, vuelva a intentarlo luego",
                Toast.LENGTH_SHORT).show()
    }

    override fun makeViewActions(animal: Animal) {


        binding.whatsapp.setOnClickListener {
            presenter.makeWhatsappAction(animal, this)
        }
        binding.mail.setOnClickListener {
            presenter.makeEmailAction(animal, this)
        }
        binding.phone.setOnClickListener {
            presenter.makePhoneAction(animal, this)
        }
        binding.facebook.setOnClickListener {
            presenter.makeFacebookAction(animal)
        }

        binding.instagram.setOnClickListener {
            presenter.makeInstagramAction(animal)
        }


    }


    override fun notWhatsappData() {
        Toast.makeText(baseContext,
                "El usuario no ha proporcionado información sobre su Whatsapp!",
                Toast.LENGTH_SHORT).show()
    }

    override fun notMailData() {
        Toast.makeText(baseContext,
                "El usuario no ha proporcionado información sobre su email!",
                Toast.LENGTH_SHORT).show()
    }

    override fun notPhoneData() {
        Toast.makeText(baseContext,
                "El usuario no ha proporcionado información sobre su teléfono!",
                Toast.LENGTH_SHORT).show()
    }

    override fun notWhatsappInstalled() {
        Toast.makeText(baseContext,
                "No tienes whatsapp instalado!",
                Toast.LENGTH_SHORT).show()
    }

    override fun deleteWhatsapp() {
        binding.whatsapp.visibility = View.GONE
    }

    override fun deletePhone() {
        binding.phone.visibility = View.GONE
    }

    override fun deleteMail() {
        binding.mail.visibility = View.GONE
    }

    override fun deleteInstagram() {
        binding.instagram.visibility = View.GONE
    }

    override fun deleteFacebook() {
        binding.facebook.visibility = View.GONE
    }


}