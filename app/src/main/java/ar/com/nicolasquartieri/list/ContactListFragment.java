package ar.com.nicolasquartieri.list;

import java.util.List;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ar.com.nicolasquartieri.R;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.remote.ResponseType;
import ar.com.nicolasquartieri.ui.BaseFragment;
import ar.com.nicolasquartieri.ui.utils.AnimationUtils;

/**
 * Display the list of {@link Contact}.
 *
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ContactListFragment extends BaseFragment {
    /** Recycler View */
    private RecyclerView mRecyclerView;
    /** Swipe Refresh Layout */
    private SwipeRefreshLayout mSwipeToRefresh;
    /** Recycler View Adapter */
    private ContactAdapter mAdapter;
    /** Nothing Layout */
    private LinearLayout mNothingLayout;
    /** Loading Fetch Bar  */
    private View mFetchBar;
    /** View Model */
    private ContactListViewModel contactListViewModel;

    /**
     * New {@link ContactListFragment} instance.
     *
     * @return The fragment.
     */
    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
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

        mSwipeToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contactListViewModel.onPullRefresh();
            }
        });
    }

    /**
     * Initiate all related view models of this screen.
     */
    private void initViewModel() {
        // 1. Create ViewModel.
        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
        // 2. Creates the observer.
        Observer<ResponseType<List<Contact>>> contactObserver = new Observer<ResponseType<List<Contact>>>() {
            @Override
            public void onChanged(@Nullable ResponseType<List<Contact>> response) {
                List<Contact> contacts = response.getPlayload();
                if (contacts != null) {
                    mNothingLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mSwipeToRefresh.isRefreshing()) {
                        mSwipeToRefresh.setRefreshing(false);
                    }
                } else {
                    mNothingLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                mAdapter.setContacts(contacts);
                onLoadingResponse(new Intent().putExtras(response.getArgs()));
                finishLoading();
            }
        };
        // 2. Subscribe the observer.
        contactListViewModel.getCurrentContacts().observe(this, contactObserver);
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
