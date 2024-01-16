

package com.example.calc1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the CalculatorActivity when MainActivity is launched
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);

        // Finish MainActivity to prevent returning back to it when pressing back button
        finish();
    }
}
