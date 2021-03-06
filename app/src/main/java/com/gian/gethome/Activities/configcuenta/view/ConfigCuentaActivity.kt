package com.gian.gethome.Activities.configcuenta.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gian.gethome.Activities.configcuenta.interfaces.ConfigCuentaView
import com.gian.gethome.Activities.configcuenta.model.ConfigCuentaInteractor
import com.gian.gethome.Activities.configcuenta.presenter.ConfigCuentaPresenter
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Clases.CommonUtils
import com.gian.gethome.MainActivity
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityConfigCuentaBinding
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_config_cuenta.*

class ConfigCuentaActivity : AppCompatActivity(),ConfigCuentaView {
    private lateinit var binding:ActivityConfigCuentaBinding
    private val presenter = ConfigCuentaPresenter(this, ConfigCuentaInteractor())
    private var dialog: Dialog? = null
    private var loadingDialog: Dialog? = null
    private lateinit var dialogInfo: Dialog
    private var estadoMapa:Int = 0 // 0 no fue check, 1 fue check, 2 unchecked
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLogicToButtons()
        presenter.getProifleImage()
        presenter.checkIfAparecerEnMapaIsChecked()
    }


    private fun setLogicToButtons() {
        val view = layoutInflater.inflate(R.layout.dialog_info_check_map, null)
        dialogInfo = Dialog(this)
        dialogInfo.setContentView(view)
        dialogInfo.window?.setBackgroundDrawableResource(android.R.color.transparent);
        binding.infoAboutMaps.setOnClickListener {
            dialogInfo.show()
        }
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.habilitarMapa.setOnClickListener {
            if(binding.habilitarMapa.isChecked){
                estadoMapa = 1
            }else{
                estadoMapa = 2
            }
        }


        binding.botonEliminarCuenta.setOnClickListener {
            dialog = Dialog(this)
            dialog?.setContentView(R.layout.dialog_deleteacc_layout)
            dialog?.setCancelable(false)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
            val yesDelate: ImageView = dialog!!.findViewById(R.id.yesDelate)
            val noDelate: ImageView = dialog!!.findViewById(R.id.noDelate)
            yesDelate.setOnClickListener {
                presenter.deleteAccount()
            }
            noDelate.setOnClickListener {
                dialog?.dismiss()
            }
        }

        binding.editButton.setOnClickListener {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        binding.botonGuardar.setOnClickListener {
            if(!binding.nombrePerfil.text.toString().isNullOrEmpty()){
                showProgressDialog()
                presenter.saveNewChanges(
                        binding.nombrePerfil.text.toString(), this,estadoMapa)
            }else{
                Toast.makeText(this, "Debes indicar un nombre de perfil", Toast.LENGTH_SHORT).show()
            }

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            presenter.retrieveUri(result, resultCode)
        }
    }

    override fun showDatabaseError() {
        Toast.makeText(this,
                "Service error, try again later",
                Toast.LENGTH_SHORT).show()
    }

    override fun showProfileImage(imageURL: String) {
        Picasso.get().
        load(imageURL).
        placeholder(R.drawable.progress_animation).
        into(binding.profile)
    }

    override fun showUserName(userName: String) {
        binding.nombrePerfil.setText(userName)
    }

    override fun showAccountDeleted() {
        val editor: SharedPreferences.Editor = this.getSharedPreferences("prefCheckUser", Context.MODE_PRIVATE).edit()
        editor.putInt("code", 0)
        editor.apply()
        navigateTo(Intent(this, MainActivity::class.java))
    }


    override fun hideProgresDialog() {

    }

    override fun setAparecerEnMapaValue(value: String) {
        if(value.toString() == "true"){
            binding.habilitarMapa.isChecked = true
        }else{
            binding.habilitarMapa.isChecked = false
        }

    }

    override fun showProgressDialog() {
        hideProgressDialog()
        loadingDialog = CommonUtils.showLoadingDialog(this)
    }

    private fun hideProgressDialog() {
        loadingDialog?.let {
            if(it.isShowing)it.cancel()

        }
    }

    override fun showImageCrop(resultUri: Uri) {
        profile.setImageURI(resultUri)
    }

    override fun showErrorRetrievingImageUri(error: Exception?) {

    }

    override fun showChangesSaved()
    {
        Toast.makeText(this, "Cambios guardados!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showChangesNotMade() {
        Toast.makeText(this, "No has hecho cambios!", Toast.LENGTH_SHORT).show()
    }



    private fun navigateTo(intent: Intent) {
        startActivity(intent)

    }
    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}