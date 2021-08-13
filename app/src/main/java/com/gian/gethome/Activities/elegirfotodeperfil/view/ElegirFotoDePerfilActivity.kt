package com.gian.gethome.Activities.elegirfotodeperfil.view

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable.ConstantState
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gian.gethome.Activities.elegirfotodeperfil.interfaces.ElegirFotoDePerfilView
import com.gian.gethome.Activities.elegirfotodeperfil.model.ElegirFotoDePerfilInteractor
import com.gian.gethome.Activities.elegirfotodeperfil.presenter.ElegirFotoDePerfilPresenter
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Clases.CommonUtils
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityElegirFotoDePerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_elegir_foto_de_perfil.*


class ElegirFotoDePerfilActivity : AppCompatActivity(),ElegirFotoDePerfilView {
    private lateinit var binding: ActivityElegirFotoDePerfilBinding
    private lateinit var mFirebaseAuth: FirebaseAuth
    private val PICK_IMAGE: Int = 1
    private var imageUri: Uri? = null
    private var mStorageRef: StorageReference? = null
    private val presenter = ElegirFotoDePerfilPresenter(this, ElegirFotoDePerfilInteractor())
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElegirFotoDePerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.checkIfUserIsSetInDB()
        initializeValues()
        openOptionsImage()
    }

    private fun initializeValues() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().
        getReference("UsersProfilePictures" + "/" + mFirebaseAuth.currentUser?.uid)

    }
    fun Continuar(view: View?) {
        val constantStateDrawableA: ConstantState = profile.drawable.constantState!!
        val constantStateDrawableB: ConstantState? = resources.getDrawable(R.drawable.background_pet1).constantState
        if (constantStateDrawableA != constantStateDrawableB) {
            presenter.uploadImageProfile(profile.drawable, mStorageRef, this)
        } else {
            Toast.makeText(this, "Seleccione una foto de perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openOptionsImage() {
        selectImage.setOnClickListener {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            presenter.retrieveUri(result, resultCode)
        }
    }
    override fun navigateToHomeActivity() {
        navigateTo(Intent(this@ElegirFotoDePerfilActivity, HomeActivity::class.java))

    }

    private fun navigateTo(intent: Intent) {
        startActivity(intent)
        finish()
    }

    override fun navigateToSameActivity() {
        navigateTo(Intent(this@ElegirFotoDePerfilActivity, ElegirFotoDePerfilActivity::class.java))
    }

    override fun onDatabaseError() {
        Toast.makeText(this, "Error, intentelo luego", Toast.LENGTH_SHORT).show()
    }

    override fun avisoFotoNull() {
        Toast.makeText(this, "Seleccione una foto de perfil", Toast.LENGTH_SHORT).show()
    }

    override fun onSetImageProfile(imageUri: Uri) {
        Picasso.get().load(imageUri).into(binding.profile)
    }

    override fun noFileSelected() {
        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressDialog() {
        hideProgressDialog()
        loadingDialog = CommonUtils.showLoadingDialog(this)
    }

    override fun hideProgressDialog() {
        loadingDialog?.let {
            if(it.isShowing)it.cancel()
        }
    }

    override fun startActivityWithImageURL(imageURL: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("drawableProfile", imageURL)
        startActivity(intent)
        finish()
    }

    override fun showImageCrop(resultUri: Uri) {
        profile.setImageURI(resultUri)
    }

    override fun showErrorRetrievingImageUri(error: Exception?) {

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }


}