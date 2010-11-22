package com.midlandroid.apps.android.gameengine.v1;


import com.midlandroid.apps.android.gameengine.R;
import com.midlandroid.apps.android.gameengine.R.id;
import com.midlandroid.apps.android.gameengine.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Main Activity displayed to the user at start of the application.
 * 
 * @author Jason Del Ponte
 *
 */
public class Main extends Activity {
	private static final String LOG_TAG = Main.class.getName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Create the callback that will start the game engine
        findViewById(R.id.game_start_btn).setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		Log.d(LOG_TAG, "Creating game engine view.");
        		
				Intent i = new Intent(Main.this, GameEngineV1.class);
				startActivity(i);
			}
        });
    }
}