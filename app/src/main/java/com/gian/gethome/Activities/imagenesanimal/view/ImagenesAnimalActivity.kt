package com.gian.gethome.Activities.imagenesanimal.view

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Activities.imagenesanimal.interfaces.ImagenesAnimalView
import com.gian.gethome.Activities.imagenesanimal.model.ImagenesAnimalInteractor
import com.gian.gethome.Activities.imagenesanimal.presenter.ImagenesAnimalPresenter
import com.gian.gethome.Clases.CommonUtils
import com.gian.gethome.databinding.ActivityImagenesAnimalBinding

import com.squareup.picasso.Picasso
import java.util.*


class ImagenesAnimalActivity : AppCompatActivity(),ImagenesAnimalView {
    private lateinit var binding: ActivityImagenesAnimalBinding
    private lateinit var tipoAnimal:String
    private lateinit var nombreAnimal: String
    private lateinit var sexoAnimal: String
    private lateinit var transitoUrgente: String
    private lateinit var edadAnimal:String
    private lateinit var descripcionAnimal: String
    private lateinit var whatsapp:String
    private lateinit var phone:String
    private lateinit var mail:String
    private lateinit var progressDialog: ProgressDialog
    private lateinit var pais:String
    private  lateinit var provincia:String
    private  lateinit var formatted:String
    private var loadingDialog: Dialog? = null

    private val presenter = ImagenesAnimalPresenter(this, ImagenesAnimalInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagenesAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.setLocalDate()
        presenter.setMutableList(binding.imagen1,binding.imagen2,binding.imagen3,binding.imagen4)
        getValues()
        setOnClickListenerAddButtons()
        setOnClickListenerDeleteButtons()

    }


    override fun hideProgressDialog() {
        //progressDialog.dismiss()

        loadingDialog?.let {
            if(it.isShowing)it.cancel()

        }
    }

    override fun showProgressDialog() {
        /*progressDialog = ProgressDialog(this@ImagenesAnimalActivity)
        progressDialog.setTitle("Publicando animal")
        progressDialog.setMessage("Por favor, espere")
        progressDialog.setCancelable(false)
        progressDialog.show()
         */
        hideProgressDialog()
        loadingDialog = CommonUtils.showLoadingDialog(this)
    }

    override fun setLocalDate(date:String) {
        formatted = date
    }

    override fun setImageSelected(imageUri: Uri?, imageView: ImageView) {
        Picasso.get().load(imageUri).into(imageView)
    }

    override fun addAtLeastOneImage() {
        Toast.makeText(this,
                "Debe agregar al menos una imágen",
                Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setOnClickListenerDeleteButtons() {
        binding.delete1.setOnClickListener {
            presenter.setImageViewNull(0)

        }
        binding.delete2.setOnClickListener {
            presenter.setImageViewNull(1)

        }
        binding.delete3.setOnClickListener {
            presenter.setImageViewNull(2)

        }
        binding.delete4.setOnClickListener {
            presenter.setImageViewNull(3)

        }
    }

    private fun setOnClickListenerAddButtons() {
        binding.add1.setOnClickListener {
            openImageOptions(1)
        }
        binding.add2.setOnClickListener {
            openImageOptions(2)
        }
        binding.add3.setOnClickListener {
            openImageOptions(3)
        }
        binding.add4.setOnClickListener {
            openImageOptions(4)
        }
    }

    private fun openImageOptions(requestCode: Int) {
        
        val gallery = Intent()
        gallery.type = "image/*"
        gallery.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(gallery, "Seleccione una imagen"), requestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            presenter.setImageViewWith(resultCode,data,requestCode,this)
        }

    }

    private fun getValues() {
        nombreAnimal = intent.getStringExtra("nombreAnimal").toString()
        tipoAnimal = intent.getStringExtra("tipoAnimal").toString()
        sexoAnimal = intent.getStringExtra("sexoAnimal").toString()
        transitoUrgente = intent.getStringExtra("transitoUrgente").toString()
        edadAnimal = intent.getStringExtra("edadAnimal").toString()
        descripcionAnimal = intent.getStringExtra("descripcionAnimal").toString()
        pais = intent.getStringExtra("Pais").toString()
        provincia = intent.getStringExtra("Provincia").toString()
        whatsapp = intent.getStringExtra("Whatsapp").toString()
        phone = intent.getStringExtra("Phone").toString()
        mail = intent.getStringExtra("Mail").toString()
    }

    fun publicarAnimal(view: View) {
        presenter.publicarAnimalDB(nombreAnimal,
                tipoAnimal,
                transitoUrgente,
                edadAnimal,descripcionAnimal,sexoAnimal,provincia,pais,whatsapp,phone,mail,this)

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    fun back(view: View) {
        finish()
    }

}