package com.example.btlon.Utils;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardHelper {

    // Hàm lắng nghe sự kiện Enter và ẩn bàn phím
    public static void hideKeyboardOnEnter(EditText editText, Context context) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard(v, context);
                return true;  // Trả về true để ngừng hành động mặc định
            }
            return false;
        });
    }

    // Hàm ẩn bàn phím
    private static void hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
