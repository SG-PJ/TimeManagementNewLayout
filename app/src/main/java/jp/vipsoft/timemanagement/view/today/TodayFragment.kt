package jp.vipsoft.timemanagement.view.today

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.google.firebase.firestore.FirebaseFirestore
import jp.vipsoft.timemanagement.R

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

        //FAB処理
        val editWorkTimebtn = root.findViewById<FloatingActionButton>(R.id.editWorkTime)
        editWorkTimebtn.setOnClickListener {
            val inputView = layoutInflater.inflate(R.layout.activity_edit, null, false)
            val dialog = AlertDialog.Builder(context)
                .setTitle("勤怠入力")
                .setView(inputView)
                .setPositiveButton("登録", { dialog, which ->
                    // TODO:Yesが押された時の挙動
                })
                .setNegativeButton("閉じる", { dialog, which ->
                    // TODO:Noが押された時の挙動
                })
                .create()
            dialog.show()
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

        val format = FormatUtil()
        val date: TextView = root.findViewById(R.id.txtTime)
        date.text = format.getTiem()
        val time: TextView = root.findViewById(R.id.txtDate)
        time.text = format.getDate()
        val userName: TextView = root.findViewById(R.id.nameViewText)
        userName.text = format.getName()

        return root
    }
}