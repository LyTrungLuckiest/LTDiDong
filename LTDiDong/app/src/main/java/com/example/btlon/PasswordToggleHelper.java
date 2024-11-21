package com.example.btlon;

import android.text.InputType;
import android.widget.EditText;
import android.widget.ToggleButton;

public class PasswordToggleHelper {

    private boolean isPasswordVisible = false;

    private final EditText editText;
    private final ToggleButton toggleButton;


    public PasswordToggleHelper(EditText editText, ToggleButton toggleButton) {
        this.editText = editText;
        this.toggleButton = toggleButton;


        setupToggleFunctionality();
    }

    private void setupToggleFunctionality() {
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiển thị mật khẩu
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            } else {
                // Ẩn mật khẩu
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            }
            editText.setSelection(editText.getText().length());  // Giữ con trỏ ở cuối
        });
    }
}
