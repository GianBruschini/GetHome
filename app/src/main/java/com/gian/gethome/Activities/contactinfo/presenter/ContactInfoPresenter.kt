package com.gian.gethome.Activities.contactinfo.presenter

import com.gian.gethome.Activities.contactinfo.interfaces.ContactInfoView
import com.gian.gethome.Activities.contactinfo.model.ContactInfoInteractor
import com.gian.gethome.Activities.contactinfo.view.ContactInfoActivity
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo

class ContactInfoPresenter(var contactInfoView:ContactInfoView,
                           var contactInfoInteractor: ContactInfoInteractor):ContactInfoInteractor.onContactInfoListener {

    fun detectUserInfo(idOwner:String){
            contactInfoInteractor.detectuserInfo(idOwner,this)
    }

    fun makeWhatsappAction(animal: Animal, contactInfoActivity: ContactInfoActivity) {
        contactInfoInteractor.makeWhatsappAction(animal,contactInfoActivity,this)
    }

    fun makeEmailAction(animal: Animal, contactInfoActivity: ContactInfoActivity) {
        contactInfoInteractor.makeMailAction(animal,contactInfoActivity,this)
    }

    fun makePhoneAction(animal: Animal, contactInfoActivity: ContactInfoActivity) {
        contactInfoInteractor.makePhoneAction(animal,contactInfoActivity,this)
    }

    fun detectContactInfo(idOwner: String, animalKey: String) {
            contactInfoInteractor.detectContactInfo(idOwner,animalKey,this)
    }


    override fun onUserInfo(userInfo: UserInfo) {
        contactInfoView.setUserInfo(userInfo)
    }


    override fun onDatabaseError() {
        contactInfoView.dataBaseError()
    }

    override fun onPassAnimalData(animal: Animal) {
        contactInfoView.makeViewActions(animal)
    }

    override fun onNotMailData() {

        contactInfoView.notMailData()
    }

    override fun onNotWhatsappData() {
        contactInfoView.notWhatsappData()
    }

    override fun onNotPhonelData() {
        contactInfoView.notPhoneData()
    }

    override fun onNotWhatsappInstalled() {
        contactInfoView.notWhatsappInstalled()
    }


}