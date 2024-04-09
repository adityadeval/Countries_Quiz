package ms.cs.countries_quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "MainActivity";
    private static ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d( DEBUG_TAG, "Inside onCreate()" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById( R.id.viewpager );

        CountryQuizAdapter avpAdapter = new
                CountryQuizAdapter(
                getSupportFragmentManager(), getLifecycle() );
        pager.setOrientation(
                ViewPager2.ORIENTATION_HORIZONTAL );
        pager.setAdapter( avpAdapter );

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 6) {
                     Toast.makeText(MainActivity.this, "Quiz done", Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    ResultFragment resultFragment = (ResultFragment) fragmentManager.findFragmentById(R.id.fragment_result);
                    Log.d(DEBUG_TAG, "resultFragment " + resultFragment);
                    if (resultFragment == null) {
                        resultFragment = ResultFragment.newInstance(position);
                        Log.d(DEBUG_TAG, "resultFragment was null " + resultFragment);
                    }

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_quiz, resultFragment);
                    transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE );
                    transaction.addToBackStack(null);
                    transaction.commit();


                }
            }
        });


        // Below if checks if any previous app state has been saved (in case onCreate() is being called after an interrupt
        // occured. In case there is a saved state, the key = currentFragmentNo is passed to fetch the fragment number
        // the user was on, before the app was stopped.
        // This fragment number is then used to open the exact fragment the user was on before the interrupt occured.
        if (savedInstanceState != null) {
            int currentPosition = savedInstanceState.getInt("currentFragmentNo", 0);
            Log.d( DEBUG_TAG, "Inside onCreate() AGAIN. Fragment number being restored = " + currentPosition );
            pager.setCurrentItem(currentPosition, false); // Set to the saved position
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d( DEBUG_TAG, "Inside onSaveInstanceState()" );
        // Assuming 'viewPager' is your ViewPager2 instance
        int currentPosition = pager.getCurrentItem();
        outState.putInt("currentFragmentNo", currentPosition);
        Log.d( DEBUG_TAG, "Saving fragment number in Bundle :" + currentPosition );
    }
}