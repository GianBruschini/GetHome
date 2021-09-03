package com.gian.gethome.Activities.editanimal.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gian.gethome.Activities.editanimal.Interfaces.EditAnimalView
import com.gian.gethome.Activities.editanimal.model.EditAnimalInteractor
import com.gian.gethome.Activities.editanimal.presenter.EditAnimalPresenter
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.CommonUtils
import com.gian.gethome.Fragments.HomeFragment
import com.gian.gethome.Fragments.mispublicaciones.view.MisPublicacionesFragment
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityEditAnimalBinding
import java.util.*


class EditAnimalActivity : AppCompatActivity(),EditAnimalView {
    private lateinit var binding:ActivityEditAnimalBinding
    private lateinit var animalKey:String
    private lateinit var animalUrlImage:String
    private var loadingDialog: Dialog? = null
    private var dialog: Dialog? = null
    private val presenter = EditAnimalPresenter(this, EditAnimalInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLogicButtons()
        animalKey = intent.getStringExtra("animalKey").toString()
        animalUrlImage = intent.getStringExtra("animalUrlImage").toString()
        presenter.retrieveDataFromDB(animalKey)
    }

    private fun setLogicButtons() {

        binding.botonEditar.setOnClickListener {
            presenter.editAnimalFromDB(binding.spinnerTipoAnimal.selectedItem.toString(),
                    binding.nombreAnimal.text.toString(), binding.cantAnimales.text.toString(),
                    binding.spinnerSexoAnimal.selectedItem.toString(),
                    binding.transitoCheckBox.isChecked.toString(), binding.spinnerAct.selectedItem.toString(),
                    binding.descripcionAnimal.text.toString(), binding.whatsppNumber.text.toString(),
                    binding.phoneNumber.text.toString(), binding.mail.text.toString(),
                    binding.instagram.text.toString(), animalKey)
        }

        binding.botonEliminar.setOnClickListener {
            dialog = Dialog(this)
            dialog?.setContentView(R.layout.dialog_deletepub_layout)
            dialog?.setCancelable(false)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
            val yesDelate: ImageView = dialog!!.findViewById(R.id.yesDelate)
            val noDelate: ImageView = dialog!!.findViewById(R.id.noDelate)
            yesDelate.setOnClickListener {
                presenter.deleteAnimalFromDB(animalKey, animalUrlImage)
                finish()
            }
            noDelate.setOnClickListener {
                dialog?.dismiss()
            }
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setSpinners(animal: Animal) {
        val value = arrayOf("Edad del animal", "Menos de 2 semanas", "2 semanas a 1 mes", "1 a 3 meses", "3 a 6 meses", "6 meses a 1 año", "1 a 3 años", "3 a 5 años", "5 a 8 años", "Más de 8 años")
        val tipoAnimal = arrayOf("Tipo de animal", "Perro", "Gato", "Ave", "Conejo", "Tortuga", "Roedor", "Otro")
        val sexoAnimal = arrayOf("Sexo animal", "Macho", "Hembra")
        val arrayList = ArrayList(Arrays.asList(*value))
        val arrayListTipoAnimal = ArrayList(Arrays.asList(*tipoAnimal))
        val arrayListSexoAnimal = ArrayList(Arrays.asList(*sexoAnimal))
        val arrayAdapter = ArrayAdapter(this, R.layout.spinner_layout, arrayList)
        val arrayAdapterTipoAnimal = ArrayAdapter(this, R.layout.spinner_layout, arrayListTipoAnimal)
        val arrayAdapterSexoAnimal = ArrayAdapter(this, R.layout.spinner_layout, arrayListSexoAnimal)
        binding.spinnerAct.adapter = arrayAdapter
        binding.spinnerTipoAnimal.adapter = arrayAdapterTipoAnimal
        binding.spinnerSexoAnimal.adapter = arrayAdapterSexoAnimal
        binding.spinnerTipoAnimal.setSelection(
                arrayAdapterTipoAnimal.getPosition(animal.tipoAnimal))
        binding.spinnerAct.setSelection(arrayAdapter.getPosition(animal.edad))
        binding.spinnerSexoAnimal.
        setSelection(arrayAdapterSexoAnimal.getPosition(animal.sexo))
    }

    override fun showDatabaseError() {
        Toast.makeText(this,
                "Error con el servicio, vuelva a intentarlo más tarde",
                Toast.LENGTH_LONG).show()
    }

    override fun showAnimalData(animal: Animal){
        setSpinners(animal)
        binding.nombreAnimal.setText(animal.nombre)
        binding.cantAnimales.setText(animal.cantAnimales)
        binding.descripcionAnimal.setText(animal.descripcion)
        binding.whatsppNumber.setText(animal.whatsapp)
        binding.phoneNumber.setText(animal.phone)
        binding.mail.setText(animal.mail)
        binding.instagram.setText(animal.instagram)
    }

    override fun showAnimationAnimalUpdated() {
        hideProgressDialog()
        loadingDialog = CommonUtils.showCheckDialog(this)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                hideProgressDialog()
                finish()
            }
        }, 1700)

    }

    private fun navigateTo(intent: Intent) {
        startActivity(intent)
        finish()
    }

    fun hideProgressDialog() {
        loadingDialog?.let {
            if(it.isShowing)it.cancel()

        }
    }

    fun setFragment(fragment: Fragment?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment!!)
        fragmentTransaction.commit()
        finish()
    }
}