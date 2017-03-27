package ar.com.nicolasquartieri.detail;

import android.os.Bundle;
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
import ar.com.nicolasquartieri.ui.BaseFragment;
import ar.com.nicolasquartieri.ui.dialog.ImageDialog;
import ar.com.nicolasquartieri.widget.LoadingImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Display the information of the respective {@link Contact}.
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ContactDetailFragment extends BaseFragment
        implements Callback<Contact> {
    private static final int IMAGE_DIALOG_REQUEST_CODE = 1500;
    private static final String SUPPORT_DIALOG_TAG_NAME = "CONTACT_DETAIL_FRAGMENT_REQUEST";
    public static final String ARG_CONTACT = "ARG_CONTACT";

    /** Contact Entity */
    private Contact mContact;
    /** Addresses of the Contact */
    private TextView mAddress;
    /** Contact Detail Service */
    private ContactsDetailService service;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create Contacts DetailService.
        service = retrofit.create(ContactsDetailService.class);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // Sync cause data from remote service.
        service.getContactDetail(mContact.getUserId()).enqueue(this);
    }

    @Override
    public void onResponse(Call<Contact> call, Response<Contact> response) {
        Contact contact;
        if (response.isSuccessful()) {
            contact = response.body();
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
            }
        }
        finishLoading();
    }

    @Override
    public void onFailure(Call<Contact> call, Throwable t) {
        Log.d("Contact-App", t.getMessage());
        finishLoading();
    }
}
