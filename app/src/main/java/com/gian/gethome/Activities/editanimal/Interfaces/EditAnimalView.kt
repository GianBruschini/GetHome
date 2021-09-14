package com.gian.gethome.Activities.editanimal.Interfaces

import com.gian.gethome.Clases.Animal

interface EditAnimalView {
    fun showDatabaseError()
    fun showAnimalData(animal: Animal)
    fun showAnimationAnimalUpdated()
    fun navigateToHome()




}