package jp.vipsoft.timemanagement.view.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import jp.vipsoft.timemanagement.R
import jp.vipsoft.timemanagement.util.FormatUtil

class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todayViewModel =
            ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)

        val format = FormatUtil()
        val date: TextView = root.findViewById(R.id.txtTime)
        date.text = format.getTiem()
        val time: TextView = root.findViewById(R.id.txtDate)
        time.text = format.getDate()

        return root
    }
}