
package com.sevenrocks.firebasedb.Adapters;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sevenrocks.firebasedb.R;


/**
 * Baseclass of all Activities of the Demo Application.
 * 
 * @author Philipp Jahoda
 */
public abstract class DemoBase extends AppCompatActivity {



    protected String[] lotdescription = new String[] {
            "Garment is proper Dimension.", "Garment is slightly short.", "Garment is very short can not send to customer."
    };

    protected String[] mlot = new String[] {
            "Green", "Yellow", "Red"
    };

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTfRegular = Typeface.createFromAsset(getAssets(), "regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "light.ttf");
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
