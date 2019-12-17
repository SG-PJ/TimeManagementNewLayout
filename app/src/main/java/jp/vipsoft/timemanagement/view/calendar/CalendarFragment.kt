package jp.vipsoft.timemanagement.view.calendar

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import jp.vipsoft.timemanagement.R
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
//        val textView: TextView = root.findViewById(R.id.text_calendar)
//        calendarViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

        val toDay = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).timeInMillis
        val calView = root.findViewById<CalendarView>(R.id.Viewcalendar)
        calView.setDate(toDay, true, true)
        calView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val inputView = layoutInflater.inflate(R.layout.activity_edit, null)
            val dialog = AlertDialog.Builder(context)
                .setTitle("勤怠入力")
                .setView(inputView)
                .setPositiveButton("OK", { dialog, which ->
                    // TODO:Yesが押された時の挙動
                })
                .setNegativeButton("No", { dialog, which ->
                    // TODO:Noが押された時の挙動
                })
                .create()
            dialog.show()
        }
        return root
    }
}
