package com.anit.fastpallet4.common

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: Date?): String {
    var dateFormat = SimpleDateFormat("dd.MM.yy HH:mm:ss")
    return dateFormat.format(date)
}

fun formatDateForServer(date: Date?): String {
    var dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    return dateFormat.format(date)
}

fun formatDateForFile(date: Date?): String {
    var dateFormat = SimpleDateFormat("yy_MM_dd_ss_mm_HH")
    return dateFormat.format(date)
}