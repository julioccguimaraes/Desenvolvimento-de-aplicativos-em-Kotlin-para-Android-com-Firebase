package com.julioguimaraes.projetofinaldm114.order

import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {
    @InverseMethod("stringToDate")
    @JvmStatic
    fun dateToString(newValue: Double): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
        return simpleDateFormat.format(newValue.toLong())
    }

    @JvmStatic
    fun stringToDate(newValue: String): Double {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val date: Date = simpleDateFormat.parse(newValue)
        val millis = date.time
        return millis.toDouble()
    }
}
