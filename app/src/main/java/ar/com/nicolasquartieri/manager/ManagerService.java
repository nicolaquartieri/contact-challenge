package ar.com.nicolasquartieri.manager;

import android.support.annotation.NonNull;

import java.util.List;

import ar.com.nicolasquartieri.detail.ContactDetailViewModel;
import ar.com.nicolasquartieri.detail.ContactsDetailService;
import ar.com.nicolasquartieri.list.ContactsService;
import ar.com.nicolasquartieri.model.Contact;
import ar.com.nicolasquartieri.ui.utils.RetrofitProvider;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Manage the service layer for all of the available services of the app.
 *
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ManagerService {
    private static volatile ManagerService instance;
    private ContactsService contactsService;
    private ContactsDetailService contactDetailService;

    private ManagerService() {
        // Create the Services.
        Retrofit retrofit = RetrofitProvider.getRetrofitClient();
        contactsService = retrofit.create(ContactsService.class);
        contactDetailService = retrofit.create(ContactsDetailService.class);
    }

    public static ManagerService getManagerService() {
        // Prevent race-condition on creation from multiple places.
        if (instance == null) {
            synchronized (ManagerService.class) {
                if (instance == null) {
                    instance = new ManagerService();
                }
            }
        }
        return instance;
    }

    /**
     * Get the list of available list of {@link Contact}.
     *
     * @param callback the list is returned in this {@link Callback}
     */
    public void callContactService(@NonNull Callback<List<Contact>> callback) {
        contactsService.getContacts().enqueue(callback);
    }

    /**
     * Get the requested {@link Contact} base on the user id.
     *
     * @param id the id of user user.
     * @param callback the list is returned in this {@link Callback}.
     */
    public void callContactDetailService(@NonNull final String id, @NonNull Callback<Contact> callback) {
        contactDetailService.getContactDetail(id).enqueue(callback);
    }
}
