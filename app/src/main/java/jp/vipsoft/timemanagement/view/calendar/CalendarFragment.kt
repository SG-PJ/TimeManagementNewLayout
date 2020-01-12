package jp.vipsoft.timemanagement.view.calendar

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import jp.vipsoft.timemanagement.R
import jp.vipsoft.timemanagement.util.FormatUtil
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
            ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        val format = FormatUtil()
//        val textView: TextView = root.findViewById(R.id.text_calendar)
//        calendarViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

        val toDay = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).timeInMillis
        val calView = root.findViewById<CalendarView>(R.id.Viewcalendar)
        calView.setDate(toDay, true, true)
        calView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val inputView = layoutInflater.inflate(R.layout.activity_detail, null)
            val dialog = AlertDialog.Builder(context)
                .setTitle("勤怠入力")
                .setView(inputView)
                .setPositiveButton("編集", { dialog, which ->
                    // TODO:登録が押された時の挙動
                    val inputView = layoutInflater.inflate(R.layout.activity_edit, null)
                    val dialog = AlertDialog.Builder(context)
                        .setTitle("勤怠入力")
                        .setView(inputView)
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
                })
                .setNegativeButton("閉じる", { dialog, which ->
                    // TODO:閉じるが押された時の挙動
                })
                .create()
            dialog.show()
        }

        val userName: TextView = root.findViewById(R.id.nameViewText)
        userName.text = format.getName()

        return root
    }
}
