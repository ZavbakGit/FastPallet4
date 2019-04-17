package com.anit.fastpallet4.domain.utils

fun getDecimalStr(str:String?):String{
   return "[^\\d,.]".toRegex().replace(str?:"", "").replace(",",".")
}