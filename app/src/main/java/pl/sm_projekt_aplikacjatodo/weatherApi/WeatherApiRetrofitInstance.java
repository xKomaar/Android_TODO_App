package pl.sm_projekt_aplikacjatodo.weatherApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiRetrofitInstance {
    private static Retrofit retrofit;
    public static final String BASE_URL = "https://api.api-ninjas.com/v1/";
    public static final String API_KEY = "gNPYXjsPS0huGuw4Otfdmg==slaTYz6WwtkmyGOA";

    public static Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    okhttp3.HttpUrl originalHttpUrl = original.url();

                    okhttp3.HttpUrl url = originalHttpUrl.newBuilder()
                            .build();

                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url)
                            .addHeader("accept", "application/json")
                            .addHeader("X-Api-Key", API_KEY);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();

        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        return retrofit;
    }
}