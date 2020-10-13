package com.example.whatsappnow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared

            try {
                String formattedNumber = PhoneNumberUtils.formatNumber(sharedText, "IL");
                EditText phoneNumber = (EditText)findViewById(R.id.editText2);
                phoneNumber.setText(formattedNumber);
            } catch (Exception e) {
                Toast.makeText(this, "Unsupported text", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void formatUrl(View view) {

        EditText phoneNumber = (EditText)findViewById(R.id.editText2);
        EditText message = (EditText) findViewById(R.id.editText);

        String phone = phoneNumber.getText().toString();
        String mess = message.getText().toString();

        try {
            String formattedNumber = PhoneNumberUtils.formatNumberToE164(phone, "IL");
            formattedNumber = formattedNumber.substring(1);
            String url = "https://wa.me/" + formattedNumber + "?text=" + mess;
            sendMessage(url);

        } catch (Exception e) {
            Toast.makeText(this, "Unsupported text", Toast.LENGTH_SHORT).show();
        }

    }

    public void sendMessage(String url) {
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
