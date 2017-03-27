package ar.com.nicolasquartieri;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import ar.com.nicolasquartieri.list.ContactListFragment;
import ar.com.nicolasquartieri.ui.BaseActivity;

/**
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.main_container, ContactListFragment.newInstance());
            transaction.commit();
        }
    }

    @Override
    protected String getCurrentTitle() {
        return getString(R.string.title_contacts);
    }
}
