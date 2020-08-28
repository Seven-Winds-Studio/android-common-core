package mobi.sevenwinds.common.core.utils.extensions

import android.widget.EditText

var EditText.textStr: String
    get() = this.text.toString()
    set(value) = this.setText(value)
