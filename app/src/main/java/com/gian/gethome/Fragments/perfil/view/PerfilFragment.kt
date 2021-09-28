package com.gian.gethome.Fragments.perfil.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gian.gethome.Activities.configcuenta.view.ConfigCuentaActivity
import com.gian.gethome.Activities.homeactivity.view.HomeActivity
import com.gian.gethome.Fragments.perfil.interfaces.PerfilView
import com.gian.gethome.Fragments.perfil.model.PerfilInteractor
import com.gian.gethome.Fragments.perfil.presenter.PerfilPresenter
import com.gian.gethome.MainActivity
import com.gian.gethome.R
import com.gian.gethome.databinding.FragmentMiPerfilBinding
import com.gian.gethome.databinding.FragmentPublicarAnimalBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.dialog_deleteacc_layout.*

class PerfilFragment: Fragment(R.layout.fragment_mi_perfil),PerfilView,View.OnClickListener {
    private var binding: FragmentMiPerfilBinding?=null
    private var dialog: Dialog? = null
    private val presenter = PerfilPresenter(this, PerfilInteractor())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMiPerfilBinding.inflate(inflater)
        initializeValuesOnClick()
        getImageFromDB()
        getUserNameFromDB()
        return binding!!.root
    }

    private fun initializeValuesOnClick() {
        binding!!.buttonConfiguracion.setOnClickListener(this)
        dialog?.setContentView(R.layout.dialog_deleteacc_layout)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        getImageFromDB()
        getUserNameFromDB()
        super.onResume()
    }

    private fun getUserNameFromDB() {
        presenter.getUserNameFromDB()
    }

    private fun getImageFromDB() {
        presenter.getImageFromDB()
    }

    override fun setImageProfile(imageURL: String) {
        Picasso.get().load(imageURL).placeholder(R.drawable.progress_animation).into(binding!!.profileImage)
    }

    override fun setUserNameText(name: String) {
        binding!!.userName.text = name
    }

    override fun accounWasDeleted() {
        val editor: SharedPreferences.Editor = requireContext().getSharedPreferences("prefCheckUser", Context.MODE_PRIVATE).edit()
        editor.putInt("code", 0)
        editor.apply()
        navigateTo(Intent(requireContext(), MainActivity::class.java))
    }

    private fun navigateTo(intent: Intent) {
        startActivity(intent)
    }

    @SuppressLint("CutPasteId")
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonConfiguracion -> {
                navigateTo(Intent(requireContext(),ConfigCuentaActivity::class.java))
            }
        }
    }



    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }


}