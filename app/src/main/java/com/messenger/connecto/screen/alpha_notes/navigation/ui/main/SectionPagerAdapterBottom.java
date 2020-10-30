package com.messenger.connecto.screen.alpha_notes.navigation.ui.main;



import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.messenger.connecto.R;
import com.messenger.connecto.screen.alpha_notes.tabs.AllNotesFragment;
import com.messenger.connecto.screen.alpha_notes.tabs.VaultNotesFragment;
import java.util.Locale;

public class SectionPagerAdapterBottom extends FragmentPagerAdapter {

    protected Context mContext;

    public SectionPagerAdapterBottom(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page..
        switch(position) {
            case 0:
                return new AllNotesFragment();
            case 1:
                return new VaultNotesFragment();

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
                return mContext.getString(R.string.all_notes).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.vault_notes).toUpperCase(l);

        }
        return null;
    }
}