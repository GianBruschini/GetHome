package com.gian.gethome.Activities.contactinfo.presenter

import com.gian.gethome.Activities.contactinfo.interfaces.ContactInfoView
import com.gian.gethome.Activities.contactinfo.model.ContactInfoInteractor
import com.gian.gethome.Activities.contactinfo.view.ContactInfoActivity
import com.gian.gethome.Clases.Animal
import com.gian.gethome.Clases.UserInfo

class ContactInfoPresenter(var contactInfoView:ContactInfoView,
                           var contactInfoInteractor: ContactInfoInteractor):ContactInfoInteractor.onContactInfoListener {

    fun detectUserInfo(idOwner: String, context: ContactInfoActivity){
            contactInfoInteractor.detectuserInfo(idOwner,this,context)
    }

    fun makeWhatsappAction(animal: Animal, contactInfoActivity: ContactInfoActivity) {
        contactInfoInteractor.makeWhatsappAction(animal)
    }

    fun makeEmailAction(animal: Animal, contactInfoActivity: ContactInfoActivity) {
        contactInfoInteractor.makeMailAction(animal)
    }

    fun makePhoneAction(animal: Animal, contactInfoActivity: ContactInfoActivity) {
        contactInfoInteractor.makePhoneAction(animal)
    }

    fun detectContactInfo(idOwner: String, animalKey: String) {
            contactInfoInteractor.detectContactInfo(idOwner,animalKey)
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

    override fun onDeleteWhatsapp() {
        contactInfoView.deleteWhatsapp()
    }

    override fun onDeletePhone() {
        contactInfoView.deletePhone()
    }

    override fun onDeleteMail() {
        contactInfoView.deleteMail()
    }

    override fun onDeleteInstagram() {
        contactInfoView.deleteInstagram()
    }

    override fun onDeleteFacebook() {
        contactInfoView.deleteFacebook()
    }

    fun makeInstagramAction(animal: Animal) {
        contactInfoInteractor.makeInstagramAction(animal)
    }

    fun makeFacebookAction(animal: Animal) {
        contactInfoInteractor.makeFacebookAction(animal)
    }


}