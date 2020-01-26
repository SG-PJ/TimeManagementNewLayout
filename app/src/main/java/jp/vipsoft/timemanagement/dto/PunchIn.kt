package jp.vipsoft.timemanagement.dto

/**
 * CloudFirestore:attendance_infoコレクションを格納するための
 * データクラス
 */
data class PunchIn(
    /**出社時刻 */
    val scheduled_working_from: Long = 0L,
    /**退社時刻 */
    val scheduled_working_to: Long = 0L,
    /**有給取得 */
    val specified_time: String = "",
    /**備考 */
    val remarks: String = ""

)