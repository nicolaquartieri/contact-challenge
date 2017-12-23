package ar.com.nicolasquartieri.detail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import ar.com.nicolasquartieri.manager.ManagerService;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.remote.ApiErrorResponse;
import ar.com.nicolasquartieri.remote.ResponseType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This {@link ViewModel} retrieves the necessary data from the DataModel, applies the UI logic and
 * then exposes relevant data for the View to consume. Similar to the DataModel, the ViewModel exposes
 * the data via Observables.
 *
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ContactDetailViewModel extends ViewModel implements Callback<Contact> {
    private MutableLiveData<ResponseType<Contact>> contact;
    private ManagerService managerService = ManagerService.getManagerService();

    public void onCallService(String id) {
        managerService.callContactDetailService(id, this);
    }

    public MutableLiveData<ResponseType<Contact>> getCurrentContact() {
        if (contact == null) {
            contact = new MutableLiveData<>();
        }

        return contact;
    }

    private void setCurrentContact(Contact contact) {
        setCurrentContact(contact, null);
    }

    private void setCurrentContact(Contact contact, @Nullable final ApiErrorResponse apiErrorResponse) {
        if (this.contact != null) {
            this.contact = new MutableLiveData<>();
        }

        this.contact.setValue(new ResponseType<Contact>(contact, apiErrorResponse));
    }

    @Override
    public void onResponse(Call<Contact> call, Response<Contact> response) {
        Contact contact;
        if (response.isSuccessful()) {
            contact = response.body();
            if (contact != null) {
                setCurrentContact(contact);
            }
        } else {
            setCurrentContact(null, new ApiErrorResponse(ApiErrorResponse.ERROR_SERVICE,
                    response.code()));
        }
    }

    @Override
    public void onFailure(Call<Contact> call, Throwable t) {
        setCurrentContact(null, new ApiErrorResponse(
                ApiErrorResponse.ERROR_CONNECTION));
    }
}
