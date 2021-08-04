package com.gian.gethome.Activities.configcuenta.presenter

import com.gian.gethome.Activities.configcuenta.interfaces.ConfigCuentaView
import com.gian.gethome.Activities.configcuenta.model.ConfigCuentaInteractor

class ConfigCuentaPresenter(var configCuentaView: ConfigCuentaView,
                            configCuentaInteractor: ConfigCuentaInteractor):ConfigCuentaInteractor.onConfigCuentaListener {
}