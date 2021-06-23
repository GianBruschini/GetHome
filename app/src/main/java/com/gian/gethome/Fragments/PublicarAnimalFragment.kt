package com.gian.gethome.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback
import com.gian.gethome.Activities.ImagenesAnimalActivity
import com.gian.gethome.R
import com.gian.gethome.databinding.FragmentPublicarAnimalBinding
import kotlinx.android.synthetic.main.fragment_publicar_animal.*
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPublicarAnimalBinding.inflate(inflater)
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        settingSpinner()
        settingOnClickListenerBotonContinuar()
        return binding!!.root
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

        validateSpinnerTipoAnimal()
        validateSpinnerSexoAnimal()
        validateSpinnerEdadAnimal()

        if(awesomeValidation.validate()){
            val intent = Intent(context, ImagenesAnimalActivity::class.java)
            intent.putExtra("nombreAnimal",nombreAnimal)
            intent.putExtra("tipoAnimal",tipoAnimal)
            intent.putExtra("sexoAnimal",sexoAnimal)
            intent.putExtra("transitoUrgente",transitoUrgente.toString())
            intent.putExtra("edadAnimal",edadAnimal)
            intent.putExtra("descripcionAnimal",descripcionAnimal)
            Log.i("InfoAnimal",nombreAnimal)
            Log.i("InfoAnimal",tipoAnimal)
            Log.i("InfoAnimal",sexoAnimal)
            Log.i("InfoAnimal",transitoUrgente.toString())
            Log.i("InfoAnimal",edadAnimal)
            Log.i("InfoAnimal",descripcionAnimal)
            startActivity(intent)
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
        val value = arrayOf("Edad del animal", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
        val tipoAnimal = arrayOf("Tipo de animal", "Perro", "Gato", "Loro", "Conejo", "Tortuga", "Otro")
        val sexoAnimal = arrayOf("Sexo animal","Macho", "Hembra")
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
}