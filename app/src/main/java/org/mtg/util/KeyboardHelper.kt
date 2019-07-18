package org.mtg.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {
    fun hideSoftInput(view: View) =
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            0
        )
}
