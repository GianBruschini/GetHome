package com.gian.gethome.Activities.contactinfo.interfaces

import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo


interface ContactInfoView {
    fun setUserInfo(userInfo: UserInfo)
    fun dataBaseError()
    fun makeViewActions(animal: Animal)
    fun notWhatsappData()
    fun notMailData()
    fun notPhoneData()
    fun notWhatsappInstalled()
    fun deleteWhatsapp()
    fun deletePhone()
    fun deleteMail()
    fun deleteInstagram()
    fun deleteFacebook()



}