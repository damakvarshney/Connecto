package com.messenger.connecto.screen.beta_messenger.navigation.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messenger.connecto.R;
import com.messenger.connecto.screen.beta_messenger.tabs.CallFragment;
import com.messenger.connecto.screen.beta_messenger.tabs.ChatFragment;

import java.util.Locale;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page..
        switch(position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new CallFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.chats).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.calls).toUpperCase(l);

        }
        return null;
    }
}