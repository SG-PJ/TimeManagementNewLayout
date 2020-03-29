package jp.vipsoft.timemanagement.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import jp.vipsoft.timemanagement.MainActivity
import jp.vipsoft.timemanagement.R
import jp.vipsoft.timemanagement.dto.Users
import kotlinx.android.synthetic.main.activity_login.*

class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {

            var auth = FirebaseAuth.getInstance()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val pass = findViewById<EditText>(R.id.editPassword).text.toString()

//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

            if (email.isNotBlank() && pass.isNotBlank()) {

                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            Log.d("成功", "signInWithEmail:success")
                            Toast.makeText(baseContext, "ログイン成功", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)

                            /** ログイン成功時ユーザーIDでDBから取得 */
                            val db = FirebaseFirestore.getInstance()
                            val loadUsersDoc = db.collection("users").document(auth.uid.toString())
                            loadUsersDoc.get().addOnSuccessListener { documentSnapshot ->
                                val data = documentSnapshot.toObject(Users::class.java)
                                if (data != null) {
                                    Log.d("成功", "(｀・ω・´)ユーザーデータ取れた！！！！！！！ ${data}")

                                    /** 一度取得したユーザ情報をGSONを使ってJSON文字列にする */
                                    val resultData: String = Gson().toJson(data)

                                    // インテントに格納
                                    intent.putExtra("users", resultData)
                                    startActivity(intent)
                                } else {
                                    Log.d("成功", "(´・ω・`)ユーザーデータ取れなかった")
                                }
                            }



                        } else {
                            Toast.makeText(baseContext, "ログイン失敗", Toast.LENGTH_SHORT).show()
                            Log.d("失敗", "signInWithEmail:success")

                        }
                    }
            } else if (email.isBlank()) {
                Toast.makeText(baseContext, "メールアドレスを入力して下さい", Toast.LENGTH_SHORT).show()

            } else if (pass.isBlank()) {
                Toast.makeText(baseContext, "パスワードを入力して下さい", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
