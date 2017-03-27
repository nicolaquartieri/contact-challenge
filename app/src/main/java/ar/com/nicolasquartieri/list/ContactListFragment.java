package ar.com.nicolasquartieri.list;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ar.com.nicolasquartieri.R;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.remote.ApiErrorResponse;
import ar.com.nicolasquartieri.ui.BaseFragment;
import ar.com.nicolasquartieri.ui.utils.AnimationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Display the list of {@link Contact}.
 *
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ContactListFragment extends BaseFragment
        implements Callback<List<Contact>> {
    /** Recycler View */
    private RecyclerView mRecyclerView;
    /** Recycler View Adapter */
    private ContactAdapter mAdapter;
    /** Nothing Layout */
    private LinearLayout mNothingLayout;
    /** Loading Fetch Bar  */
    private View mFetchBar;

    private ContactsService service;

    /**
     * New {@link ContactListFragment} instance.
     *
     * @return The fragment.
     */
    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create Contacts Service.
        service = retrofit.create(ContactsService.class);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNothingLayout = (LinearLayout) view.findViewById(R.id.nothing_layout);
        mFetchBar = view.findViewById(R.id.fetch_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.photo_list);
        // Linear Layout Manager.
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView
                .addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.m0_125));
        // Avoid open multiple wallpaper screen at the same time. just take the first one.
        mRecyclerView.setMotionEventSplittingEnabled(false);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // Sync cause data from remote service.
        service.getContacts().enqueue(this);
    }

    @Override
    public void onLoadingFinished(Intent intent) {
        super.onLoadingFinished(intent);
        AnimationUtils.fadeOutView(mFetchBar);
    }

    @Override
    protected void onCreateAdapter(Context context) {
        super.onCreateAdapter(context);
        if (mAdapter == null) {
            mAdapter = new ContactAdapter(this);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
        List<Contact> contacts;
        Bundle args = new Bundle();
        if (response.isSuccessful()) {
            contacts = response.body();
            if (contacts != null && !contacts.isEmpty()) {
                mAdapter.setContacts(contacts);
                mNothingLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            mNothingLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mAdapter.setContacts(null);

            args.putParcelable(ApiErrorResponse.EXTRA_RESPONSE_ERROR,
                    new ApiErrorResponse(ApiErrorResponse.ERROR_SERVICE,
                    response.code()));
        }

        onLoadingResponse(new Intent().putExtras(args));
        finishLoading();
    }

    @Override
    public void onFailure(Call<List<Contact>> call, Throwable t) {
        mAdapter.setContacts(new ArrayList<Contact>());

        Bundle args = new Bundle();
        args.putParcelable(ApiErrorResponse.EXTRA_RESPONSE_ERROR, new ApiErrorResponse(
                ApiErrorResponse.ERROR_CONNECTION));
        onLoadingResponse(new Intent().putExtras(args));
        finishLoading();
        Log.d("Contact-App", t.getMessage());
    }

    /**
     * Handle the space between {@link RecyclerView} columns and rows.
     *
     * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
     */
    class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        /**
         * Constructor.
         *
         * @param itemOffset The space distance between columns and rows. Could be 0 (zero).
         */
        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        /**
         * Constructor.
         *
         * @param context The {@link Context}, can't be null.
         * @param itemOffsetId The offset id.
         */
        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}
