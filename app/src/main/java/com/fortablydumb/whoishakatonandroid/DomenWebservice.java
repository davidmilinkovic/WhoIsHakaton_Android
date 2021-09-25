package com.fortablydumb.whoishakatonandroid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DomenWebservice {
    @GET("/data?url={naziv}")
    Call<Domen> getDomen(@Path("naziv") String naziv);
}
