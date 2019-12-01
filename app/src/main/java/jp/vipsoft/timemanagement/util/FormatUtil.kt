package jp.vipsoft.timemanagement.util

import java.text.SimpleDateFormat
import java.util.*

class FormatUtil {

    fun getDate() : String {

        val df = SimpleDateFormat("yyyy/MM/dd")
        val date = Date()

        return df.format(date)
    }

    fun getTiem() : String {

        val df = SimpleDateFormat("HH:mm")
        val date = Date()

        return df.format(date)
    }
}