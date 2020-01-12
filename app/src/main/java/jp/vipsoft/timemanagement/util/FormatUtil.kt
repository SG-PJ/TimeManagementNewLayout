package jp.vipsoft.timemanagement.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
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

    fun getName() : String {

        // ログイン中ユーザ
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {

            // DisplayNameがGUIで設定できないのでここでUpdate
            // 全員分を設定したら不要な処理
            /**
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("大堂隼人")
                .build()
            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                }
             */

            // 表示名(画面上部表示用)
            val name = user.displayName

            // ユーザID(DBのクエリ用)
            val uid = user.uid

            return name.toString()
        }
        return "test"
    }

    fun getInputCurrentTime() : String {

        val cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        var calHour = cal.get(Calendar.HOUR_OF_DAY).toString()
        var calMin = cal.get(Calendar.MINUTE)

        var min = ""
        var result = calMin / 15
        if (result < 1) {
            min = "00"
        } else if (result < 2) {
            min = "15"
        } else if (result < 3) {
            min = "30"
        } else {
            min = "45"
        }

        return calHour + ":" + min
    }
}