package com.example.demoapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyCustomDialog extends DialogFragment {
    private View v;
    private EditText edt_title, edt_body, edt_userId;
    private Button button_update, button_delete;
    private static final String TAG = "MyCustomDialog";
    private int id;
    private int userId;
    private String title;
    private String body;

    public MyCustomDialog(int id, int userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public interface OnInputListener {
        void sendInput(int id, String title, String body, int userId);
        void sendId(int id);
    }
    public OnInputListener onInputListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.custom_dialog, container, false);
        button_update = v.findViewById(R.id.button_update);
        button_delete = v.findViewById(R.id.button_delete);
        edt_title = v.findViewById(R.id.edt_title);
        edt_body = v.findViewById(R.id.edt_body);
        edt_userId = v.findViewById(R.id.edt_userId);

        edt_title.setText(title);
        edt_body.setText(body);
        edt_userId.setText(String.valueOf(userId));

        button_update.setOnClickListener(view -> {
            String title = edt_title.getText().toString().trim();
            String body = edt_body.getText().toString().trim();
            String userId = edt_userId.getText().toString().trim();
            if (!title.equals("") && !body.equals("") && !userId.equals("")) {
                onInputListener.sendInput(id, title, body, Integer.valueOf(userId));
                getDialog().dismiss();
            }
        });

        button_delete.setOnClickListener(view -> {
            onInputListener.sendId(this.id);
            getDialog().dismiss();
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: error " + e.getMessage());
        }
    }
}
