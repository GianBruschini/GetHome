package com.gian.gethome.Clases

data class Animal(val animalKey:String,val descripcion:String,val edad:String,val fechaDePublicacion:String,val imagen1:String, val imagen2:String,val imagen3:String,val imagen4:String,
val nombre:String, val tipoAnimal:String, val transitoUrgente:String,val userIDowner:String,val sexo:String,val pais:String,val provincia:String,
                  val facebook:String,
                  val instagram:String,
                  val twitter:String,
                  val webPage:String,
                  val whatsapp:String,
                  val mail:String,
                  val phone:String) {


     constructor() : this("", "",
        "", "", "1",
        "", "","","","","","","","","","","","","","","",""
        )
}