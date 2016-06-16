package com.example.hello.mymap.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.example.hello.mymap.fragment.ImageGridFragment;

public class ImageGridActivity extends Activity {

	private static final String TAG = "ImageGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (BuildConfig.DEBUG) {
//            Utils.enableStrictMode();
//        }
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            //pyj
            ft.add(android.R.id.content, new ImageGridFragment(), TAG);
            ft.commit();
        }
    }
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		
		
		
	}
	
	
}
