package ar.com.nicolasquartieri.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ar.com.nicolasquartieri.R;
import ar.com.nicolasquartieri.model.Address;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.model.Phones;
import ar.com.nicolasquartieri.remote.ResponseType;
import ar.com.nicolasquartieri.ui.BaseFragment;
import ar.com.nicolasquartieri.ui.dialog.ImageDialog;
import ar.com.nicolasquartieri.widget.LoadingImageView;

/**
 * Display the information of the respective {@link Contact}.
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ContactDetailFragment extends BaseFragment {
    private static final int IMAGE_DIALOG_REQUEST_CODE = 1500;
    private static final String SUPPORT_DIALOG_TAG_NAME = "CONTACT_DETAIL_FRAGMENT_REQUEST";
    public static final String ARG_CONTACT = "ARG_CONTACT";

    /** Contact Entity */
    private Contact mContact;
    /** Addresses of the Contact */
    private TextView mAddress;
    /** View Model */
    private ContactDetailViewModel contactDetailViewModel;

    /**
     * New {@link ContactDetailFragment} instance.
     *
     * @return The fragment.
     */
    public static Fragment newInstance(Contact contact) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONTACT, contact);
        ContactDetailFragment fragment = new ContactDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initViewModel();
    }

    /**
     * Initiate all related view components of this screen.
     * @param view The actual {@link View} of this screen.
     */
    private void initView(View view) {
        mContact = getArguments().getParcelable(ARG_CONTACT);

        TextView mRealNameTextView = (TextView) view.findViewById(R.id.realname_txt);
        TextView mBday = (TextView) view.findViewById(R.id.bday_txt);
        mAddress = (TextView) view.findViewById(R.id.address_txt);
        TextView mPhones = (TextView) view.findViewById(R.id.phones_txt);
        LoadingImageView mContactImageView = (LoadingImageView) view.findViewById(R.id.contact_img);
        LoadingImageView mAvatarImageView = (LoadingImageView) view.findViewById(R.id.avatar_img);

        if (mContact != null) {
            mRealNameTextView.setText(mContact.getFirstName() + mContact.getLastName());
            mBday.setText(getString(R.string.bday_string) + mContact.getBirthDate());
            mAvatarImageView.setImageUrl(mContact.getThumb());
            for (Phones phone : mContact.getPhones()) {
                if (phone.getNumber() != null) {
                    mPhones.setText(phone.getType() + " : " + phone.getNumber() + "\n\r");
                }
            }

            mContactImageView.setImageUrl(mContact.getPhoto());
            mContactImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageDialog imageDialog = ImageDialog.getImageDialog(mContact.getPhoto());
                    imageDialog.setTargetFragment(ContactDetailFragment.this, IMAGE_DIALOG_REQUEST_CODE);
                    imageDialog.show(getFragmentManager(), SUPPORT_DIALOG_TAG_NAME);
                }
            });
        }
    }

    /**
     * Initiate all related view models of this screen.
     */
    private void initViewModel() {
        // 1. Create ViewModel.
        contactDetailViewModel = ViewModelProviders.of(this).get(ContactDetailViewModel.class);
        // 2. Creates the observer.
        Observer<ResponseType<Contact>> observer = new Observer<ResponseType<Contact>>() {
            @Override
            public void onChanged(@Nullable ResponseType<Contact> response) {
                Contact contact = response.getPlayload();
                if (contact != null) {
                    for (Address address : contact.getAddresses()) {
                        String home = address.getHome();
                        if (home != null) {
                            mAddress.setText(getString(R.string.home) + home + "\n\r");
                        }
                        String work = address.getWork();
                        if (work != null) {
                            mAddress.setText(getString(R.string.work) + work + "\n\r");
                        }
                    }
                } else {
                    Log.d("Contact-App", "Contact Detail screen null");
                }
                finishLoading();
            }
        };
        // 2. Subscribe the observer.
        contactDetailViewModel.getCurrentContact().observe(this, observer);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // Sync contact data from remote service.
        contactDetailViewModel.onCallService(mContact.getUserId());
    }
}
