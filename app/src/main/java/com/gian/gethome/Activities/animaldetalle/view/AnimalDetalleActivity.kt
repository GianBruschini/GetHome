package com.gian.gethome.Activities.animaldetalle.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.gian.gethome.Activities.animaldetalle.interfaces.AnimalDetalleView
import com.gian.gethome.Activities.animaldetalle.model.AnimalDetalleInteractor
import com.gian.gethome.Activities.animaldetalle.presenter.AnimalDetallePresenter
import com.gian.gethome.Adapters.SliderPagerAdapterAnimal
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.SliderAnimalDetailScreen
import com.gian.gethome.Clases.UserInfo
import com.gian.gethome.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_animal_detalle.*
import kotlinx.android.synthetic.main.bottom_sheet_contact_info.*


class AnimalDetalleActivity : AppCompatActivity(),AnimalDetalleView {
    private lateinit var sliderPager: ViewPager
    private lateinit var indicator: TabLayout
    private val lstSlides:MutableList<SliderAnimalDetailScreen> = mutableListOf()
    private lateinit var tipoAnimal:String
    private lateinit var nombreAnimal: String
    private lateinit var sexoAnimal: String
    private lateinit var transitoUrgente: String
    private lateinit var edadAnimal:String
    private lateinit var descripcionAnimal: String
    private lateinit var userIDownerAnimal:String
    private lateinit var animalKey:String
    private lateinit var provincia:String
    private lateinit var pais:String
    private lateinit var toggleButtonFav:FloatingActionButton
    private lateinit var dialogBottomSheet: BottomSheetDialog
    private lateinit var viewBottomSheet:View
    private lateinit var image:CircleImageView
    private lateinit var dialogInfo: Dialog
    private var esFavorito:Boolean = true
    private val REQUEST_CALL = 1

    private val presenter = AnimalDetallePresenter(this, AnimalDetalleInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detalle)
        //descripcionAnimalTxt.movementMethod = ScrollingMovementMethod()
        getIntentValues()
        setUIvalues()
        presenter.checkFavouritesInDB(animalKey)
        setLogicToToggleButtonFav()
        presenter.setFotoYnombrePerfil(userIDownerAnimal)
        presenter.loadImagesAnimal(animalKey)
        presenter.detectSexoAnimal(sexoAnimal)
        presenter.detectTransitoUrgente(transitoUrgente)
    }

    private fun setLogicToToggleButtonFav() {
       /* toggleButtonFav.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                presenter.saveLikes(animalKey, userIDownerAnimal)
            } else {
                presenter.deleteLike(animalKey)
            }
        })

        */
        toggleButtonFav.setOnClickListener {
            if(esFavorito){
                presenter.saveLikes(animalKey,userIDownerAnimal)
                toggleButtonFav.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.favourite_icon_red));
                esFavorito = false
            }else{
                presenter.deleteLike(animalKey)
                toggleButtonFav.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.favourite_icon));
                esFavorito = true
            }
        }

    }

    private fun settingViewPager(linkImage: String) {
        sliderPager = findViewById(R.id.imagenAnimalViewPager)
        indicator = findViewById(R.id.indicator)
        lstSlides.add(SliderAnimalDetailScreen(linkImage, null))

    }

   private fun getIntentValues() {
        nombreAnimal = intent.getStringExtra("nombre").toString()
        tipoAnimal = intent.getStringExtra("tipoAnimal").toString()
        descripcionAnimal = intent.getStringExtra("descripcion").toString()
        edadAnimal = intent.getStringExtra("edad").toString()
        transitoUrgente = intent.getStringExtra("transitoUrgente").toString()
        userIDownerAnimal = intent.getStringExtra("userIDowner").toString()
        animalKey = intent.getStringExtra("animalKey").toString()
        sexoAnimal = intent.getStringExtra("sexoAnimal").toString()
        provincia = intent.getStringExtra("Provincia").toString()
        pais = intent.getStringExtra("Pais").toString()
        toggleButtonFav = findViewById(R.id.addToFav)

    }

    @SuppressLint("SetTextI18n")
    private fun setUIvalues() {
        nombreAnimalTxt.text = nombreAnimal
        edadAnimalTxt.text = edadAnimal
        //descripcionAnimalTxt.text = descripcionAnimal
        locationTxt.text = "$provincia, $pais"
        cardViewDatos.setBackgroundResource(R.drawable.card_rounded_design_animal_detail)
        viewBottomSheet = layoutInflater.inflate(R.layout.bottom_sheet_contact_info, null)
        dialogBottomSheet = BottomSheetDialog(this, R.style.BottomShitDialogTheme)
        dialogBottomSheet.setCancelable(true)
        dialogBottomSheet.setContentView(viewBottomSheet)
        dialogBottomSheet.dismissWithAnimation = true
        image = dialogBottomSheet.findViewById(R.id.imageProfileBottomSheet)!!
        val view = layoutInflater.inflate(R.layout.dialog_detail_contact_info, null)
        dialogInfo = Dialog(this)
        dialogInfo.setContentView(view)
        dialogInfo.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun backArrowButton(view: View) {
        finish()
    }

    fun goToContactInfo(view: View) {
            presenter.retrieveContactInfoData(userIDownerAnimal, animalKey)

            /*val intent = Intent(this, ContactInfoActivity::class.java)
            intent.putExtra("idOwner", userIDownerAnimal)
            intent.putExtra("animalKey", animalKey)
            startActivity(intent)

             */
    }

    private fun setOnClickListenerContactInfo(animal: Animal) {


            dialogInfo.show()
            dialogInfo.findViewById<EditText>(R.id.whatsppNumber).setText(animal.whatsapp)
            dialogInfo.findViewById<EditText>(R.id.phoneNumber).setText(animal.phone)
            dialogInfo.findViewById<EditText>(R.id.instagram).setText(animal.instagram)
            dialogInfo.findViewById<EditText>(R.id.mail).setText(animal.mail)
            dialogInfo.findViewById<EditText>(R.id.whatsppNumber).setText(animal.whatsapp)


        dialogInfo.findViewById<EditText>(R.id.whatsppNumber)!!.setOnClickListener {
            makeWhatsappAction(animal)
        }
        dialogInfo.findViewById<EditText>(R.id.mail)!!.setOnClickListener {
            makeMailAction(animal)
        }
        dialogInfo.findViewById<EditText>(R.id.phoneNumber)!!.setOnClickListener {
            makePhoneAction(animal)
        }
        /*dialogBottomSheet.findViewById<ImageView>(R.id.facebook)!!.setOnClickListener {
            makeFacebookAction(animal)
        }

         */

        dialogInfo.findViewById<EditText>(R.id.instagram)!!.setOnClickListener {
            makeInstagramAction(animal)
        }

    }

    override fun databaseError() {
        Toast.makeText(this,
                "Error con el servicio, vuelva a intentarlo más tarde",
                Toast.LENGTH_SHORT).show()
    }

    override fun setFavButton() {
        toggleButtonFav.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.favourite_icon_red));
    }

    override fun setNombreYfoto(userInfo: UserInfo) {
        Picasso.get().load(userInfo.imageURL).placeholder(R.drawable.progress_animation).into(profileImage)
        nameOwner.text = userInfo.userName
        Picasso.get().load(userInfo.imageURL).placeholder(R.drawable.progress_animation).into(image)
    }

    override fun loadImages(animalImages: MutableList<String>) {
        for (pos in animalImages.indices) {
            if (animalImages[pos] != "null") {
                settingViewPager(animalImages[pos])
            }
        }
        val adapter = SliderPagerAdapterAnimal(applicationContext, lstSlides)
        sliderPager.adapter = adapter
        indicator.setupWithViewPager(sliderPager, true)
    }

    override fun setSexo(sexo: Int) {
        //Picasso.get().load(sexo).into(sexoAnimalImage)
    }

    override fun setTransitoUrgente(transito: String) {
        transitoUrgenteTxt.text = transito
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun getContactInfoData(animal: Animal) {
        setOnClickListenerContactInfo(animal)
        checkWhatInfoIsEmpty(animal)
        dialogInfo.show()
    }

    private fun checkWhatInfoIsEmpty(animal: Animal) {
        if(animal.whatsapp.isEmpty()){
            dialogBottomSheet.findViewById<ImageView>(R.id.whatsapp)!!.visibility = View.GONE

        }
        if(animal.phone.isEmpty()){
            dialogBottomSheet.findViewById<ImageView>(R.id.phone)!!.visibility = View.GONE
        }
        if(animal.mail.isEmpty()){
            dialogBottomSheet.findViewById<ImageView>(R.id.mail)!!.visibility = View.GONE
        }
        if(animal.instagram.isEmpty()){
            dialogBottomSheet.findViewById<ImageView>(R.id.instagram)!!.visibility = View.GONE
        }
        /*if(animal.facebook.isEmpty()){
            dialogBottomSheet.findViewById<ImageView>(R.id.facebook)!!.visibility = View.GONE
        }

         */


    }



    fun makeWhatsappAction(animal: Animal){
        if (animal.whatsapp.isNotEmpty()) {
            sendWhatsappMessage(animal)
        } else {
            Toast.makeText(baseContext,
                    "El usuario no ha proporcionado información sobre su Whatsapp!",
                    Toast.LENGTH_SHORT).show()
        }
    }

    fun makeMailAction(animal: Animal) {
        if (animal.mail.isNotEmpty()) {
            openEmail(animal)
        }else{

            Toast.makeText(baseContext,
                    "El usuario no ha proporcionado información sobre su email!",
                    Toast.LENGTH_SHORT).show()
        }
    }

    fun makePhoneAction(animal: Animal) {
        if (animal.phone.isNotEmpty()) {
            makePhoneCall(animal)
        } else {
            Toast.makeText(this,
                    "El usuario no ha proporcionado información sobre su teléfono!",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun makePhoneCall(animal: Animal) {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${animal.phone}")
            startActivity(intent)
        }

    }

    private fun openEmail(animal: Animal) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.type = "text/plain"
        val uriText:String = "mailto:" + Uri.encode(animal.mail)
        val uri: Uri = Uri.parse(uriText)
        emailIntent.data = uri
        startActivity(Intent.createChooser(emailIntent, animal.mail))
    }

    private fun sendWhatsappMessage(animal: Animal) {
        val installed: Boolean = isAppInstalled("com.whatsapp")
        if (installed) {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone="+animal.whatsapp+"&text="+"Hola, estoy interesado por" + " " + animal.nombre
            sendIntent.data = Uri.parse(uri)
            startActivity(sendIntent)
        } else {
            Toast.makeText(this, "No tienes Whatsapp en tu celular!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun isAppInstalled(s: String): Boolean {
        val packageManager = packageManager
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
        val uri: Uri = Uri.parse("https://instagram.com/" + animal.instagram)
        val insagram= Intent(Intent.ACTION_VIEW, uri)
        insagram.setPackage("com.instagram.android")
        try {
            startActivity(insagram)
        }catch (e: ActivityNotFoundException){
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://instagram.com/" + animal.instagram)))
        }
    }

    fun shareInfoButton(view: View) {

    }

    /*fun makeFacebookAction(animal: Animal) {

        try {
            val intent= Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + animal.facebook))
            startActivity(intent)
        }catch (e: ActivityNotFoundException){
            val intent= Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://facebook.com/" + animal.facebook))
            startActivity(intent)
        }
    }

     */
}