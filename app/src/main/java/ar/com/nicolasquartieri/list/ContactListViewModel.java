package ar.com.nicolasquartieri.list;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ar.com.nicolasquartieri.manager.ManagerService;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.remote.ApiErrorResponse;
import ar.com.nicolasquartieri.remote.ResponseType;
import ar.com.nicolasquartieri.ui.utils.RetrofitProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This {@link ViewModel} retrieves the necessary data from the DataModel, applies the UI logic and
 * then exposes relevant data for the View to consume. Similar to the DataModel, the ViewModel exposes
 * the data via Observables.
 *
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ContactListViewModel extends ViewModel implements Callback<List<Contact>> {
    private MutableLiveData<ResponseType<List<Contact>>> contacts;
    private ManagerService managerService = ManagerService.getManagerService();

    public ContactListViewModel() {
        // This call can be places in a separate method and called in the onStartLoading() method of the view.
        managerService.callContactService(this);
    }

    public void onPullRefresh() {
        managerService.callContactService(this);
    }

    public MutableLiveData<ResponseType<List<Contact>>> getCurrentContacts() {
        if (contacts == null) {
            contacts = new MutableLiveData<>();
        }

        return contacts;
    }

    private void setCurrentContacts(List<Contact> contacts) {
        setCurrentContacts(contacts, null);
    }

    public void setCurrentContacts(List<Contact> contacts, @Nullable final ApiErrorResponse apiErrorResponse) {
        if (this.contacts == null) {
            this.contacts = new MutableLiveData<>();
        }

        this.contacts.setValue(new ResponseType<List<Contact>>(contacts, apiErrorResponse));
    }

    @Override
    public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
        List<Contact> contacts;
        if (response.isSuccessful()) {
            contacts = response.body();
            if (contacts != null && !contacts.isEmpty()) {
                setCurrentContacts(contacts);
            }
        } else {
            setCurrentContacts(null, new ApiErrorResponse(ApiErrorResponse.ERROR_SERVICE,
                    response.code()));
        }
    }

    @Override
    public void onFailure(Call<List<Contact>> call, Throwable throwable) {
        setCurrentContacts(new ArrayList<Contact>(), new ApiErrorResponse(
                ApiErrorResponse.ERROR_CONNECTION));
    }
}
