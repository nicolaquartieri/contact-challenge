package ar.com.nicolasquartieri.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import ar.com.nicolasquartieri.R;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.ui.BaseActivity;

/**
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ContactDetailActivity extends BaseActivity {

    public static Intent getIntent(@NonNull final Context context, Contact contact) {
        final Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(ContactDetailFragment.ARG_CONTACT, contact);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            final Bundle bundle = (getIntent().getExtras() != null)
                    ? getIntent().getExtras() : new Bundle();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, ContactDetailFragment.newInstance(
                    (Contact) bundle.getParcelable(ContactDetailFragment.ARG_CONTACT)));
            transaction.commit();
        }
    }

    @Override
    protected String getCurrentTitle() {
        return getString(R.string.title_detail);
    }
}
