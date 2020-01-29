package jp.vipsoft.timemanagement.view.calendar

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import jp.vipsoft.timemanagement.R
import jp.vipsoft.timemanagement.util.FormatUtil
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        calendarViewModel =
            ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        val format = FormatUtil()

        // サンプルコピペ　あとで消す
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

        // 勤務体系コレクション取得
        val serviceSystem = db.collection("user")
        serviceSystem.whereEqualTo("break_time_four", 1130)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "where ${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        // カレンダー
        val calView = root.findViewById<CalendarView>(R.id.Viewcalendar)
        // 今日日付を設定
        val toDay = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).timeInMillis
        calView.setDate(toDay, true, true)

        // 日付選択時の動作
        calView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            // 詳細表示
            val detailView = layoutInflater.inflate(R.layout.activity_detail, null)
            val dialog = AlertDialog.Builder(context)
                .setTitle("$year/${month}1/$dayOfMonth")
                .setView(detailView)
                .setPositiveButton("編集") { dialog, which ->
                    val editView = layoutInflater.inflate(R.layout.activity_edit, null)
                    val dialog = AlertDialog.Builder(context)
                        .setTitle("$year/${month}1/$dayOfMonth")
                        .setView(editView)
                        .setPositiveButton("登録", { dialog, which ->
                            // TODO:登録が押された時の挙動
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
                        val startTime: EditText = editView.findViewById(R.id.startWorkTimeEditText)
                        val endTime: EditText = editView.findViewById(R.id.endWorkTimeEditText)
                        val currentTime: String = format.getInputCurrentTime()

                        if (startTime.hasFocus()) {
                            startTime.setText(currentTime)
                        }
                        if (endTime.hasFocus()) {
                            endTime.setText(currentTime)
                        }
                    }
                }
                .setNegativeButton("閉じる", { dialog, which ->
                    // TODO:閉じるが押された時の挙動
                })
                .create()
            dialog.show()
        }

        // TODO MainActivityからUsersを取得しuserNameを設定
        val userName: TextView = root.findViewById(R.id.nameViewText)
        userName.text = format.getName()
        return root
    }
}
