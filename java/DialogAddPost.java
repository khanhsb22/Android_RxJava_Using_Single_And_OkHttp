package com.example.demoapp;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DialogAddPost {

    DialogInterface dialogInterface;

    public void showDialog(Context context) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true); // true or false to close dialog when touch in screen
        dialog.setContentView(R.layout.custom_dialog2);

        Button button_add = dialog.findViewById(R.id.button_add);
        EditText edt_title_addPost = dialog.findViewById(R.id.edt_title_addPost);
        EditText edt_body_addPost = dialog.findViewById(R.id.edt_body_addPost);
        EditText edt_userId_addPost = dialog.findViewById(R.id.edt_userId_addPost);

        button_add.setOnClickListener(view -> {
            if (dialogInterface != null) {
                dialogInterface.sendDataToAddPost(edt_title_addPost.getText().toString().trim(),
                        edt_body_addPost.getText().toString().trim(), Integer.valueOf(edt_userId_addPost.getText().toString().trim()));
                dialog.dismiss();
            }
        });

        dialog.show();
        
        Window window = dialog.getWindow();
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    public void setDialogInterface(DialogInterface _dialogInterface) {
        dialogInterface = _dialogInterface;
    }

    public interface DialogInterface {
        void sendDataToAddPost(String title, String body, int userId);
    }
}
