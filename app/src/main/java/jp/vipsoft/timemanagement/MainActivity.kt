package jp.vipsoft.timemanagement

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import jp.vipsoft.timemanagement.dto.PunchIn
import jp.vipsoft.timemanagement.dto.Users

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: ここお試し部分
        // インテントからJSON文字列を取得する
        val json_users = intent.extras?.getString("users")
        // 型を指定して文字列からデシリアライズする
        val users = Gson().fromJson<Users>(json_users, Users::class.java)
        Log.d("デシリアライズ成功", "(｀・ω・´)デシリアライズできた！！！！！！！ ${users}")
//
//        // データクラスに更新したい値を格納する
//        val timeCard = PunchIn(930,1830, "02","テストですよ")
//        // 更新処理
//        updateWorkingInfo(users.user_id,"2020","01","02",timeCard)
        // TODO: お試し部分終了

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_today,
                R.id.navigation_calendar,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

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
