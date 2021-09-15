package com.gian.gethome.Clases

data class UserInfo(val aparecerEnMapa:String,val imageURL:String,val userName:String) {


    constructor() : this("","",""
    )
}