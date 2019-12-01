package jp.vipsoft.timemanagement.view.today

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.vipsoft.timemanagement.R
import jp.vipsoft.timemanagement.util.FormatUtil

class TodayViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is today Fragment"
    }
    val text: LiveData<String> = _text

}