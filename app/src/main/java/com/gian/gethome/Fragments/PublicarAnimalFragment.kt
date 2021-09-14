package com.gian.gethome.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Activities.imagenesanimal.view.ImagenesAnimalActivity
import com.gian.gethome.R
import com.gian.gethome.databinding.FragmentPublicarAnimalBinding
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_contact_info.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class PublicarAnimalFragment: Fragment(R.layout.fragment_publicar_animal) {
    private var binding: FragmentPublicarAnimalBinding?=null
    private lateinit var tipoAnimal:String
    private lateinit var nombreAnimal: String
    private lateinit var sexoAnimal: String
    private var transitoUrgente: Boolean =false
    private lateinit var edadAnimal:String
    private lateinit var descripcionAnimal: String
    private lateinit var awesomeValidation: AwesomeValidation
    private lateinit var pais: String
    private lateinit var provincia: String
    private lateinit var homeActivity: HomeActivity
    private lateinit var latitude: String
    private lateinit var longitude:String
    var countryCodePicker: CountryCodePicker? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPublicarAnimalBinding.inflate(inflater)
        initializeValues()
        getBundleFromHomeActivity()
        settingSpinner()
        settingOnClickListenerBotonContinuar()
        return binding!!.root
    }

    private fun getBundleFromHomeActivity() {
        //latitude = activity?.intent?.getStringExtra("latitude").toString()
        //longitude = activity?.intent?.getStringExtra("longitude").toString()

    }

    private fun initializeValues() {
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        homeActivity = (activity as HomeActivity?)!!
        pais= homeActivity.pais.text.toString()
        provincia = homeActivity.provincia.text.toString()
        latitude = homeActivity.latitudeTxt.text.toString()
        longitude = homeActivity.longitudeTxt.text.toString()
    }


    private fun settingOnClickListenerBotonContinuar() {
        binding?.botonContinuar?.setOnClickListener {
            getEditTextValues()
            checkInfoAndContinue()
        }
    }

    private fun checkInfoAndContinue() {

        awesomeValidation.addValidation(binding?.nombreAnimal,
                RegexTemplate.NOT_EMPTY, "Tiene que indicar un nombre")
        awesomeValidation.addValidation(binding?.descripcionAnimal,
                RegexTemplate.NOT_EMPTY, "Tiene que indicar una descripción")
        awesomeValidation.addValidation(binding?.whatsppNumber,
                RegexTemplate.TELEPHONE, "Tiene que indicar un número válido de Whatsapp")
        awesomeValidation.addValidation(binding?.phoneNumber,
                RegexTemplate.TELEPHONE, "Tiene que indicar un número válido de teléfono")
        awesomeValidation.addValidation(binding?.cantAnimales,
                RegexTemplate.NOT_EMPTY, "Tiene que indicar la cantidad de animales a publicar")


        validateSpinnerTipoAnimal()
        validateSpinnerSexoAnimal()
        validateSpinnerEdadAnimal()

        if(awesomeValidation.validate()){
            if(binding?.whatsppNumber?.text.toString().isEmpty() &&
                    binding?.phoneNumber?.text.toString().isEmpty() &&
                    binding?.mail?.text.toString().isEmpty()
                    && binding?.facebook?.text.toString().isEmpty()
                    && binding?.instagram?.text.toString().isEmpty()){
                Toast.makeText(context, "Debe indicar al menos un dato de contacto", Toast.LENGTH_LONG).show()

            }else{
                val intent = Intent(context, ImagenesAnimalActivity::class.java)
                intent.putExtra("nombreAnimal", nombreAnimal)
                intent.putExtra("cantAnimales", binding?.cantAnimales?.text.toString())
                intent.putExtra("tipoAnimal", tipoAnimal)
                intent.putExtra("sexoAnimal", sexoAnimal)
                intent.putExtra("transitoUrgente", transitoUrgente.toString())
                intent.putExtra("edadAnimal", edadAnimal)
                intent.putExtra("descripcionAnimal", descripcionAnimal)
                intent.putExtra("Pais", pais)
                intent.putExtra("Provincia", provincia)
                val nrowsp = binding!!.countryCodePeaker.selectedCountryCode+binding?.whatsppNumber?.text.toString()
                intent.putExtra("Whatsapp", nrowsp)
                intent.putExtra("Phone", binding?.phoneNumber?.text.toString())
                intent.putExtra("Mail", binding?.mail?.text.toString())
                intent.putExtra("Facebook", binding?.facebook?.text.toString())
                intent.putExtra("Instagram", binding?.instagram?.text.toString())
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                startActivity(intent)
            }
        }else{
            Toast.makeText(requireContext(), "Error al registrar los datos!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateSpinnerEdadAnimal() {
        awesomeValidation.addValidation(activity, R.id.spinnerAct, { validationHolder ->
            (validationHolder.view as Spinner).selectedItem.toString() != "Edad del animal"
        }, { validationHolder ->
            val textViewError = (validationHolder.view as Spinner).selectedView as TextView
            textViewError.error = validationHolder.errMsg
            textViewError.setTextColor(Color.RED)
        }, { validationHolder ->
            val textViewError = (validationHolder.view as Spinner).selectedView as TextView
            textViewError.error = null
            textViewError.setTextColor(Color.BLACK)
        }, R.string.ErrorSpinnerTipoAnimalMensaje)
    }

    private fun validateSpinnerSexoAnimal() {
        awesomeValidation.addValidation(activity, R.id.spinnerSexoAnimal, { validationHolder ->
            (validationHolder.view as Spinner).selectedItem.toString() != "Sexo animal"
        }, { validationHolder ->
            val textViewError = (validationHolder.view as Spinner).selectedView as TextView
            textViewError.error = validationHolder.errMsg
            textViewError.setTextColor(Color.RED)
        }, { validationHolder ->
            val textViewError = (validationHolder.view as Spinner).selectedView as TextView
            textViewError.error = null
            textViewError.setTextColor(Color.BLACK)
        }, R.string.ErrorSpinnerSexoAnimalMensaje)
    }

    private fun validateSpinnerTipoAnimal() {
        awesomeValidation.addValidation(activity, R.id.spinnerTipoAnimal, { validationHolder ->
            (validationHolder.view as Spinner).selectedItem.toString() != "Tipo de animal"
        }, { validationHolder ->
            val textViewError = (validationHolder.view as Spinner).selectedView as TextView
            textViewError.error = validationHolder.errMsg
            textViewError.setTextColor(Color.RED)
        }, { validationHolder ->
            val textViewError = (validationHolder.view as Spinner).selectedView as TextView
            textViewError.error = null
            textViewError.setTextColor(Color.BLACK)
        }, R.string.ErrorSpinnerTipoAnimalMensaje)
    }


    private fun settingSpinner() {
        val value = arrayOf("Edad del animal", "Menos de 2 semanas", "2 semanas a 1 mes", "1 a 3 meses", "3 a 6 meses", "6 meses a 1 año", "1 a 3 años", "3 a 5 años", "5 a 8 años", "Más de 8 años")
        val tipoAnimal = arrayOf("Tipo de animal", "Perro", "Gato", "Ave", "Conejo", "Tortuga", "Roedor", "Otro")
        val sexoAnimal = arrayOf("Sexo animal", "Macho", "Hembra")
        val arrayList = ArrayList(Arrays.asList(*value))
        val arrayListTipoAnimal = ArrayList(Arrays.asList(*tipoAnimal))
        val arrayListSexoAnimal = ArrayList(Arrays.asList(*sexoAnimal))
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, arrayList)
        val arrayAdapterTipoAnimal = ArrayAdapter(requireContext(), R.layout.spinner_layout, arrayListTipoAnimal)
        val arrayAdapterSexoAnimal = ArrayAdapter(requireContext(), R.layout.spinner_layout, arrayListSexoAnimal)
        binding?.spinnerAct?.adapter = arrayAdapter
        binding?.spinnerTipoAnimal?.adapter = arrayAdapterTipoAnimal
        binding?.spinnerSexoAnimal?.adapter = arrayAdapterSexoAnimal
    }


    private fun getEditTextValues() {
        tipoAnimal = binding?.spinnerTipoAnimal?.selectedItem.toString()
        nombreAnimal = binding?.nombreAnimal?.editableText.toString()
        sexoAnimal = binding?.spinnerSexoAnimal?.selectedItem.toString()
        if(binding?.transitoCheckBox?.isChecked == true){
            transitoUrgente = true
        }
        edadAnimal = binding?.spinnerAct?.selectedItem.toString()
        descripcionAnimal = binding?.descripcionAnimal?.editableText.toString()



    }

    //to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }



    fun putBundle(bundle: Bundle) {
            //latitude=bundle.getString("latitude").toString()
            //longitude=bundle.getString("longitude").toString()

    }


}