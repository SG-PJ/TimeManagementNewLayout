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
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("竹内靖晃")
                .build()
            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                }

            // 表示名(画面上部表示用)
            val name = user.displayName

            // ユーザID(DBのクエリ用)
            val uid = user.uid

            return name.toString()
        }
        return "test"
    }
}