package ms.cs.countries_quiz;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CountryQuizAdapter extends FragmentStateAdapter {

    private static final String DEBUG_TAG = "CountryQuizAdapter";
    int position = 6;
    public CountryQuizAdapter(
            FragmentManager fragmentManager, Lifecycle lifecycle ) {
        super( fragmentManager, lifecycle );
    }
    @NonNull
    @Override
    public Fragment createFragment(int position){
        Log.d(DEBUG_TAG, "Calling newInstance of CountryQuizFragment, position is : " + position);
        return CountryQuizFragment
                .newInstance( position );
    }
    @Override
    public int getItemCount() {
    //    return CountryQuizFragment.getNumberOfQuestions();
        return position;
    }
}
