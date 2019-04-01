package com.anit.fastpallet4.domain.utils

import io.reactivex.Flowable
import java.math.BigDecimal

fun getWeightByBarcode(barcode: String, start: Int, finish: Int, coff: Float): Float {

    if (start == 0) {
        return 0F
    }

    if (finish == 0) {
        return 0F
    }

    if (barcode.length == 0) return 0F
    if (start >= finish) return 0F
    if (barcode.length < finish) return 0F

    var weightInt: Int

    try {
        weightInt = barcode.subSequence(start - 1, finish).toString().toIntOrNull() ?: 0
    } catch (e: Exception) {
        weightInt = 0
    }

    return BigDecimal(weightInt)
        .multiply(BigDecimal(coff.toString())).toString().toFloatOrNull() ?: 0f

}

fun isPallet(barcode: String): Boolean {
    return barcode.startsWith("<pal>", true) && barcode.endsWith("</pal>", true)
}

fun getNumberDocByBarCode(barcode: String): String {
    //<pal>021400000007</pal>
    if (!isPallet(barcode)) {
        throw Throwable("Не паллета!")
    }

    var strCode = barcode.replace("<pal>", "").replace("</pal>", "")


    var finishPref = 2 + strCode.substring(0, 2).toInt()
    var strKir = strCode.substring(2, finishPref)

    var prefKir = Flowable.fromIterable(strKir.toCharArray().toList())
        .buffer(2)
        .map {
            getKirilLetterByNumber(it.joinToString(""))
        }
        .toList().blockingGet().joinToString("")


    return prefKir + strCode.takeLast(strCode.length - finishPref)
}

fun getKirilLetterByNumber(s: String): String {

    return when (s) {
        "01" -> "А"

        "02" -> "Б"

        "03" -> "В"

        "04" -> "Г"

        "05" -> "Д"

        "06" -> "Е"

        "07" -> "Ё"

        "08" -> "Ж"

        "09" -> "З"

        "10" -> "И"

        "11" -> "Й"

        "12" -> "К"

        "13" -> "Л"

        "14" -> "М"

        "15" -> "Н"

        "16" -> "О"

        "17" -> "П"

        "18" -> "Р"

        "19" -> "С"

        "20" -> "Т"

        "21" -> "У"

        "22" -> "Ф"

        "23" -> "Х"

        "24" -> "Ц"

        "25" -> "Ч"

        "26" -> "Ш"

        "27" -> "Щ"

        "28" -> "Ъ"

        "29" -> "Ы"

        "30" -> "Ь"

        "31" -> "Э"

        "32" -> "Ю"

        "33" -> "Я"

        else -> ""
    }
}