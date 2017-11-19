package android.hpulsa.com.hpulsanew.adapter;

import android.hpulsa.com.hpulsanew.fragment.FrLupaPass_1;
import android.hpulsa.com.hpulsanew.fragment.FrLupaPass_2;
import android.hpulsa.com.hpulsanew.fragment.FrLupaPass_3;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by regopantes_apps on 19/11/17.
 */

public class LupaPassPagerAdapter  extends FragmentPagerAdapter {

    public LupaPassPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FrLupaPass_1();
            case 1:
                return new FrLupaPass_2();
            case 2:
                return new FrLupaPass_3();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
