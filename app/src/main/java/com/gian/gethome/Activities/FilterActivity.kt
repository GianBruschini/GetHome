package com.gian.gethome.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.gian.gethome.R
import kotlinx.android.synthetic.main.activity_filter.*
import java.util.*


class FilterActivity : AppCompatActivity() {
    private  var transitoChecked:Boolean = false
    private  var fechaPubChecked:Boolean = false
    private lateinit var awesomeValidation: AwesomeValidation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        setLogicToSwitches()
        setLogicToSpinner()
    }

    private fun setLogicToSpinner() {
        val sexoAnimal = arrayOf("Género animal", "Macho", "Hembra")
        val arrayListSexoAnimal = ArrayList(Arrays.asList(*sexoAnimal))
        val arrayAdapterSexoAnimal = ArrayAdapter(this, R.layout.spinner_layout, arrayListSexoAnimal)
        spinnerGenero.adapter = arrayAdapterSexoAnimal

    }

    private fun setLogicToSwitches() {
        switchTransito.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            transitoChecked = isChecked
        })

        switchFecha.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            fechaPubChecked = isChecked
        })

    }


    fun confirmarFiltros(view: View) {
        val returnIntent = Intent()
        returnIntent.putExtra("filtrarPorTransito", transitoChecked)
        returnIntent.putExtra("filtrarPorGenero", spinnerGenero.selectedItem.toString())
        setResult(101, returnIntent)
        finish()
    }

    fun returnToHome(view: View) {
        finish()
    }
}