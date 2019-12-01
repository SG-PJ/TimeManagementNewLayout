package jp.vipsoft.timemanagement.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import jp.vipsoft.timemanagement.MainActivity
import jp.vipsoft.timemanagement.R
import kotlinx.android.synthetic.main.activity_login.*

class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {

            var auth = FirebaseAuth.getInstance()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val pass = findViewById<EditText>(R.id.editPassword).text.toString()

            if (email.isNotBlank() && pass.isNotBlank()) {

                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            Log.d("成功", "signInWithEmail:success")
                            Toast.makeText(baseContext, "ログイン成功", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(baseContext, "ログイン失敗", Toast.LENGTH_SHORT).show()
                            Log.d("失敗", "signInWithEmail:success")

                        }
                    }
            }
        }
    }
}
