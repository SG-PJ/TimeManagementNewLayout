package jp.vipsoft.timemanagement.view.today

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.vipsoft.timemanagement.util.FormatUtil
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import jp.vipsoft.timemanagement.R
import jp.vipsoft.timemanagement.dto.PunchIn
import jp.vipsoft.timemanagement.dto.Users
import kotlinx.android.synthetic.main.activity_edit.view.*
import jp.vipsoft.timemanagement.MainActivity
import java.time.Year
import java.util.*

class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel
    private val db = FirebaseFirestore.getInstance()
    val format = FormatUtil()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todayViewModel =
            ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)

        // ユーザ情報取得
        // TODO 毎回取るか持ち回るか
        val auth = FirebaseAuth.getInstance()
        val loadUsersDoc = db.collection("users").document(auth.uid.toString())
        val mAct = MainActivity()
        loadUsersDoc.get().addOnSuccessListener { documentSnapshot ->
            val users = documentSnapshot.toObject(Users::class.java)
            if (users == null) {
                // ユーザー情報オブジェクト未作成エラー
                Log.d("エラー", "ユーザー情報オブジェクトが取得できません。AuthID: ${auth.uid.toString()}")
            }
        }

        //FAB処理
        val editWorkTimebtn = root.findViewById<FloatingActionButton>(R.id.editWorkTime)
        editWorkTimebtn.setOnClickListener {
            val inputView = layoutInflater.inflate(R.layout.activity_edit, null, false)
            val dialog = AlertDialog.Builder(context)
                .setTitle("勤怠入力")
                .setView(inputView)
                .setPositiveButton("登録", { dialog, which ->
                    // TODO:登録が押された時の挙動
                    // データクラスに更新したい値を設定する
                    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
                    val year: String = cal.get(Calendar.YEAR).toString()
                    val monthInt: Int = cal.get(Calendar.MONTH) + 1
                    val month: String = monthInt.toString()
                    val day: String = cal.get(Calendar.DATE).toString()
                    val workingFrom: EditText = inputView.findViewById(R.id.startWorkTimeEditText)
                    val workingTo: EditText = inputView.findViewById(R.id.endWorkTimeEditText)
                    val workingFromStr: String = workingFrom.text.toString()
                    val workingToStr: String = workingTo.text.toString()
                    // 置換
                    val workingFromLong: Long = workingFromStr.replace(":", "").toLong()
                    val workingToLong: Long = workingToStr.replace(":", "").toLong()
                    val checkBoxYuukyu: CheckBox = inputView.findViewById(R.id.checkYuukyu)
                    val checkBoxDaikyu: CheckBox = inputView.findViewById(R.id.checkDaikyu)
                    var specifiedTime = "00"
                    if (checkBoxYuukyu.isChecked) {
                        specifiedTime = "01"
                    }
                    val remarks = "テスト①"
                    // データクラスに更新したい値を格納する
                    val timeCard = PunchIn(workingFromLong, workingToLong, specifiedTime, remarks)
                    // 更新処理
                    updateWorkingInfo(loadUsersDoc.id, year, month, day, timeCard)
                })
                .setNegativeButton("閉じる", { dialog, which ->
                    // TODO:閉じるが押された時の挙動
                })
                .setNeutralButton("現在時刻", { dialog, which ->
                    // TODO:現在時刻が押された時の挙動
                })
                .create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                val startTime: EditText = inputView.findViewById(R.id.startWorkTimeEditText)
                val endTime: EditText = inputView.findViewById(R.id.endWorkTimeEditText)
                val currentTime: String = format.getInputCurrentTime()

                if (startTime.hasFocus()) {
                    startTime.setText(currentTime)
                }
                if (endTime.hasFocus()) {
                    endTime.setText(currentTime)
                }
            }
        }

        // 出社時刻の制御
        val yukyu = false

        if (yukyu) {
            // 勤務時間を非表示
            val startTime: LinearLayout = root.findViewById(R.id.startTimeTodayLinear)
            startTime.visibility = INVISIBLE
            val endTime: LinearLayout = root.findViewById(R.id.endTimeTodayLinear)
            endTime.visibility = INVISIBLE

        } else if (!yukyu) {
            // 有給を非表示
            val checkDaikyu: ImageView = root.findViewById(R.id.checkResiduePaidHolidays)
            checkDaikyu.visibility = INVISIBLE
            val textDaikyu: TextView = root.findViewById(R.id.checkResiduePaidHolidaysTextView)
            textDaikyu.visibility = INVISIBLE
            val checkYukyu: ImageView = root.findViewById(R.id.checkCompensatoryDayOff)
            checkYukyu.visibility = INVISIBLE
            val textYukyu: TextView = root.findViewById(R.id.checkCompensatoryDayOffTextView)
            textYukyu.visibility = INVISIBLE
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

//        val format = FormatUtil()
        val date: TextView = root.findViewById(R.id.txtTime)
        date.text = format.getTiem()
        val time: TextView = root.findViewById(R.id.txtDate)
        time.text = format.getDate()
        // TODO MainActivityからUsersを取得しuserNameを設定
        val userName: TextView = root.findViewById(R.id.nameViewText)
        userName.text = format.getName()

        return root
    }

    // TODO:一旦、MainActivityをコピベ。後で消すこと
    /**
     * 勤怠情報更新
     * @param db FirebaseFirestoreインスタンス
     * @param userId ユーザーID
     * @param year 就業年月（年）
     * @param month 就業年月（月）
     * @param day 就業年月（日）
     * @param timeCard 勤怠情報
     *
     */
    private fun updateWorkingInfo(
        userId: String,
        year: String,
        month: String,
        day: String,
        timeCard: PunchIn
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("attendance_info")
            .document(userId)
            .collection("employment_date_year")
            .document(year)
            .collection("employment_date_month")
            .document(month)
            .collection("employment_date_day")
            .document(day)
            .set(timeCard)
            .addOnSuccessListener { documentReference ->
                Log.d("更新成功", "(｀・ω・´)ｼｬｷｰﾝ")
            }
            .addOnFailureListener { e -> Log.d("更新成功", "(´・ω・`)" + e) }
    }
}