package project.part2.editimage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViewPager(0);
    }

    public void setViewPager(int i){
        Fragment fragment;
        switch (i){
            case 0:
                fragment = new MenuFragment();
                break;
            case 1:
                fragment = new FilterFragment();
                break;
            case 2:
                fragment = new ContrastFragment();
                break;
            default:
                fragment = new MenuFragment();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activityMainFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}