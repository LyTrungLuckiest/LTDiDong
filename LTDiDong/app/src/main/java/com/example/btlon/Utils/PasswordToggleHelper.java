package com.example.btlon.Utils;

import android.text.InputType;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.btlon.R;

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

                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                toggleButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_eye_open, 0, 0);
            } else {

                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                toggleButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_eye_close, 0, 0);
            }

            editText.setSelection(editText.getText().length());
        });
    }

}

