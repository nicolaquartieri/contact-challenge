package ar.com.nicolasquartieri.detail;

import ar.com.nicolasquartieri.model.Contact;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ContactsDetailService {
    @GET("contacts/{id}")
    Call<Contact> getContactDetail(@Path("id") String id);
}
