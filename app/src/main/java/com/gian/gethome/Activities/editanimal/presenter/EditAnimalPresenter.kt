package com.gian.gethome.Activities.editanimal.presenter

import com.gian.gethome.Activities.editanimal.Interfaces.EditAnimalView
import com.gian.gethome.Activities.editanimal.model.EditAnimalInteractor
import com.gian.gethome.Clases.Animal

class EditAnimalPresenter(val editAnimalView: EditAnimalView,
                          val editAnimalInteractor: EditAnimalInteractor):EditAnimalInteractor.onEditAnimalInteractor {
    fun retrieveDataFromDB(animalKey: String) {
        editAnimalInteractor.retrieveDataFromDB(this,animalKey)
    }

    override fun onDataBaseError() {
        editAnimalView.showDatabaseError()
    }

    override fun passAnimalData(animal: Animal) {
        editAnimalView.showAnimalData(animal)
    }

    override fun onAnimalUpdated() {
        editAnimalView.showAnimationAnimalUpdated()

    }

    fun editAnimalFromDB(tipoAnimal: String, nombre: String,
                         cantAnimales: String, sexo: String,
                         transito: String, edad: String,
                         descripcion: String, whatsapp: String,
                         phone: String, mail: String, instagram: String, animalKey: String) {
        editAnimalInteractor.editDataAnimal(tipoAnimal,nombre,cantAnimales,transito,
                edad,descripcion,whatsapp,phone,mail,instagram,animalKey,sexo)

    }
}