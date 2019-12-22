package jp.vipsoft.timemanagement.view.today

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.vipsoft.timemanagement.util.FormatUtil
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todayViewModel =
            ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(jp.vipsoft.timemanagement.R.layout.fragment_today, container, false)
        val auth = FirebaseAuth.getInstance()

        // ログイン中ユーザ
        var user = auth.currentUser

        if (user != null) {

            // DisplayNameがGUIで設定できないのでここでUpdate
            // 全員分を設定したら不要な処理
            /**
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("大堂 隼人")
                .build()
            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                }
            */

            // 表示名(画面上部表示用)
            val name = user.displayName

            // ユーザID(DBのクエリ用)
            val uid = user.uid
        }

        val database = FirebaseDatabase.getInstance()
        //val myRef = database.getReference("message")

        // TODO DBから、ログイン中ユーザ、本日日付のデータを取得


        //FAB処理
        val editWorkTimebtn = root.findViewById<FloatingActionButton>(jp.vipsoft.timemanagement.R.id.editWorkTime)
        editWorkTimebtn.setOnClickListener {
            val inputView = layoutInflater.inflate(jp.vipsoft.timemanagement.R.layout.activity_edit, null, false)
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

        val format = FormatUtil()
        val date: TextView = root.findViewById(jp.vipsoft.timemanagement.R.id.txtTime)
        date.text = format.getTiem()
        val time: TextView = root.findViewById(jp.vipsoft.timemanagement.R.id.txtDate)
        time.text = format.getDate()

        return root
    }
}