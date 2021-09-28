package com.gian.gethome.Fragments

import android.app.Dialog
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Activities.imagenesanimal.view.ImagenesAnimalActivity
import com.gian.gethome.R
import com.gian.gethome.databinding.FragmentPublicarAnimalBinding
import com.hbb20.CountryCodePicker
import com.skydoves.powerspinner.*
import kotlinx.android.synthetic.main.activity_contact_info.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class PublicarAnimalFragment: Fragment(R.layout.fragment_publicar_animal) {
    private var binding: FragmentPublicarAnimalBinding?=null
    private lateinit var tipoAnimal:String
    private lateinit var nombreAnimal: String
    private lateinit var sexoAnimal: String
    private var transitoUrgente: Boolean =false
    private  var aparecerEnMaps: Boolean = false
    private lateinit var edadAnimal:String
    private lateinit var descripcionAnimal: String
    private lateinit var awesomeValidation: AwesomeValidation
    private lateinit var pais: String
    private lateinit var provincia: String
    private lateinit var homeActivity: HomeActivity
    private lateinit var latitude: String
    private lateinit var longitude:String
    private lateinit var dialogInfo: Dialog
    var countryCodePicker: CountryCodePicker? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPublicarAnimalBinding.inflate(inflater)

        initializeValues()
        settingSpinner()
        settingOnClickListenerBotonContinuar()
        return binding!!.root


    }


    private fun initializeValues() {

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        homeActivity = (activity as HomeActivity?)!!
        pais= homeActivity.pais.text.toString()
        provincia = homeActivity.provincia.text.toString()
        latitude = homeActivity.latitudeTxt.text.toString()
        longitude = homeActivity.longitudeTxt.text.toString()
        /*val view = layoutInflater.inflate(R.layout.dialog_info_check_map, null)
        dialogInfo = Dialog(requireContext())
        dialogInfo.setContentView(view)
        dialogInfo.window?.setBackgroundDrawableResource(android.R.color.transparent);
        binding?.infoAboutMaps?.setOnClickListener {
            dialogInfo.show()
        }

         */

    }


    private fun settingOnClickListenerBotonContinuar() {
        binding?.botonContinuar?.setOnClickListener {
            getEditTextValues()
            checkInfoAndContinue()
        }
    }

    private fun checkInfoAndContinue() {

        awesomeValidation.addValidation(binding?.textName,
                RegexTemplate.NOT_EMPTY, "Tiene que indicar un nombre")
        awesomeValidation.addValidation(binding?.descripcionAnimal,
                RegexTemplate.NOT_EMPTY, "Tiene que indicar una descripción")
        awesomeValidation.addValidation(binding?.whatsppNumber,
                RegexTemplate.TELEPHONE, "Tiene que indicar un número válido de Whatsapp")
        awesomeValidation.addValidation(binding?.phoneNumber,
                RegexTemplate.TELEPHONE, "Tiene que indicar un número válido de teléfono")
        awesomeValidation.addValidation(binding?.textCant,
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
                intent.putExtra("cantAnimales", binding!!.cantAnimales.editText!!.text.toString())
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
            (validationHolder.view as PowerSpinnerView).text.toString() != "Seleccione la edad"
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
            (validationHolder.view as PowerSpinnerView).text.toString() != "Seleccione el sexo"
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
            (validationHolder.view as PowerSpinnerView).text.toString() != "Seleccione un tipo de animal"
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

    }


    private fun getEditTextValues() {
        tipoAnimal = binding!!.spinnerTipoAnimal.text.toString()
        nombreAnimal = binding?.nombreAnimal?.editText?.text.toString()
        sexoAnimal = binding!!.spinnerSexoAnimal.text.toString()
        if(binding?.transitoCheckBox?.isChecked == true){
            transitoUrgente = true
        }

        edadAnimal = binding!!.spinnerEdadAnimal.text.toString()
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