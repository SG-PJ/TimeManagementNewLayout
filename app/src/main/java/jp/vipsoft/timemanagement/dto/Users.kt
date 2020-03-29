package jp.vipsoft.timemanagement.dto

/**
 * CloudFirestore:usersコレクションを格納するための
 * データクラス
 */
data class Users(
    /**名 */
    val first_name: String = "",
    /**氏 */
    val last_name: String = "",
    /**E-mail */
    val mail: String = "",
    /**有給 */
    val paid_vacation: Long = 0L, //Number方はLongで取得しないとクラッシュする？
    /**PassWord */
    val pass: String = "",
    /**ユーザーID */
    val user_id: String = ""

)