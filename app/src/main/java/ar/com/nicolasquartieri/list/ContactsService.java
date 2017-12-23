package ar.com.nicolasquartieri.list;

import java.util.List;

import ar.com.nicolasquartieri.model.Contact;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ContactsService {
    @GET("contacts")
    Call<List<Contact>> getContacts();
}
