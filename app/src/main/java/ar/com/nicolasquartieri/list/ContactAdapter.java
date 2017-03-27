package ar.com.nicolasquartieri.list;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ar.com.nicolasquartieri.R;
import ar.com.nicolasquartieri.detail.ContactDetailActivity;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.widget.LoadingImageView;

/**
 * Handle the flow and the creation of each {@link Contact} in the list.
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
	/** The Fragment */
	private final Fragment fragment;
	/** List of {@link Contact} */
	private List<Contact> contactList;

	/**
	 * Constructor.
	 * @param fragment The fragment.
     */
	ContactAdapter(final Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		View contactElementView = inflater.inflate(R.layout.contact_recycle_element, parent,
				false);
		return new ContactHolder(contactElementView);
	}

	@Override
	public void onBindViewHolder(ContactHolder holder, int position) {
		final Contact contact = contactList.get(position);

		holder.contactName.setText(contact.getFirstName() + contact.getLastName());
		holder.contactImageView.setImageUrl(contact.getThumb());
		holder.contactImageView.setImageUrl(contact.getPhoto());
	}

	@Override
	public int getItemCount() {
		return contactList != null && contactList.size() > 0 ? contactList.size() : 0;
	}

	public void setContacts(List<Contact> contacts) {
		if (this.contactList == null) {
			this.contactList = new ArrayList<>();
		}

		if (contacts != null) {
			this.contactList = contacts;
		} else {
			this.contactList.clear();
		}
		notifyDataSetChanged();
	}

	class ContactHolder extends RecyclerView.ViewHolder
			implements View.OnClickListener {
		LoadingImageView contactImageView;
		TextView contactName;

		ContactHolder(View view) {
			super(view);

			this.contactName = (TextView) view.findViewById(R.id.txt_contact);
			this.contactImageView = (LoadingImageView) view.findViewById(R.id.contact_img);

			view.setOnClickListener(this);
		}

		@Override
        public void onClick(View v) {
			Contact contact = contactList.get(getAdapterPosition());
            if (contact != null) {
				Activity activity = fragment.getActivity();
				activity.startActivity(ContactDetailActivity.getIntent(activity, contact));
            }
        }
	}
}
