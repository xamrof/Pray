package com.example.pray;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pray.R;

import io.socket.client.Socket;

public class Lock extends Activity {
    private Socket socket;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.password_native_activity);
        final EditText passwordEditText = findViewById(R.id.EditText_Lock_Password);
        final Button unlockButton = findViewById(R.id.Button_Lock_Unlock);
        final ImageView imageLock = findViewById(R.id.imageView_Lock_AccessDenied);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()){
            Log.d("Lock", "Software Keyboard was shown");
        }else{
            Log.d("Lock", "Software keyboard was not shown");
        }

        final TextView text = findViewById(R.id.TextView_Lock_AccessDenied);
        final View contentView = findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private int mPreviousHeight;
            @Override
            public void onGlobalLayout() {
                int newHeight = contentView.getHeight();
                Log.d("show", "previousHeight: "+mPreviousHeight);
                if(mPreviousHeight != 0){
                    if(mPreviousHeight > newHeight){
                        Log.d("Lock", "Software Keyboard was shown");
                        imageLock.setVisibility(View.GONE);
                    }
                    else if (mPreviousHeight < newHeight){
                        Log.d("Lock", "Software keyboard was not shown");
                        imageLock.setVisibility(View.VISIBLE);
                    }
                }
                mPreviousHeight = newHeight;
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text.setText(R.string.lock_access_denied);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String unlock = "unlock";
                    String key = passwordEditText.getText().toString().trim();

                    if(unlock != null && unlock.equals(key)){
                        text.setText("Logged In");
                    }else{
                        text.setText("Wrong Password");
                    }
                }catch (Exception e){
                    System.out.println("Error:"+e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            Log.d("SocketIO", "Disconnecting");
            socket.disconnect();
        }
    }
}
