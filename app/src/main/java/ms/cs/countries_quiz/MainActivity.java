package ms.cs.countries_quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 pager = findViewById( R.id.viewpager );
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
                if (position == 5) {
                    Toast.makeText(MainActivity.this, "Last page", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            Log.d( DEBUG_TAG, "CountriesInfoActivity.onCreate(): in landscape mode; finishing" );
            finish();
            return;
        }
    }
}