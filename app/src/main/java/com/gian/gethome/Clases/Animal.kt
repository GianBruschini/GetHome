package com.gian.gethome.Clases

data class Animal(val animalKey:String,val descripcion:String,val edad:String,val imagen1:String, val imagen2:String,val imagen3:String,val imagen4:String,
val nombre:String, val tipoAnimal:String, val transitoUrgente:String,val userIDowner:String) {


     constructor() : this("", "",
        "", "", "1",
        "", "","","","",""
        )
}