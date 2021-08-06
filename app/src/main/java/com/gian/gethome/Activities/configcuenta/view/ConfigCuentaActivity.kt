package com.gian.gethome.Activities.configcuenta.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gian.gethome.Activities.configcuenta.interfaces.ConfigCuentaView
import com.gian.gethome.Activities.configcuenta.model.ConfigCuentaInteractor
import com.gian.gethome.Activities.configcuenta.presenter.ConfigCuentaPresenter
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityConfigCuentaBinding

class ConfigCuentaActivity : AppCompatActivity(),ConfigCuentaView {
    private lateinit var binding:ActivityConfigCuentaBinding
    private val presenter = ConfigCuentaPresenter(this, ConfigCuentaInteractor())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLogicToButtons()

    }

    private fun setLogicToButtons() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}