package ms.cs.countries_quiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CountryQuizAdapter extends FragmentStateAdapter {
    int position = 7;
    public CountryQuizAdapter(
            FragmentManager fragmentManager, Lifecycle lifecycle ) {
        super( fragmentManager, lifecycle );
    }
    @NonNull
    @Override
    public Fragment createFragment(int position){

        if (position < 6) {
            return CountryQuizFragment
                    .newInstance( position );
        }else{
            return ResultFragment.newInstance();
        }


    }
    @Override
    public int getItemCount() {
    //    return CountryQuizFragment.getNumberOfQuestions();
        return position;
    }
}
