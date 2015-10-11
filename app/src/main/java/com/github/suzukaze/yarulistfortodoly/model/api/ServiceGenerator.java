/**
 * Original source at http://futurestud.io/blog/android-basic-authentication-with-retrofit/
 */
package com.github.suzukaze.yarulistfortodoly.model.api;

import android.util.Base64;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class ServiceGenerator {

  private static final String TAG = ServiceGenerator.class.getSimpleName();

  public static final int READ_TIMEOUT = 10;
  public static final int CONNECT_TIMEOUT = 10;

  // No need to instantiate this class.
  private ServiceGenerator() {
  }

  public static <S> S createService(Class<S> serviceClass, String endpoint) {
    // Call basic auth generator method without user and pass
    return createService(serviceClass, endpoint, null, null);
  }

  public static <S> S createService(Class<S> serviceClass, String endpoint, String username, String password) {
    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
    okHttpClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

    // Set endpoint url and use OkHTTP as HTTP client
    RestAdapter.Builder builder = new RestAdapter.Builder()
        .setEndpoint(endpoint)
        .setConverter(new GsonConverter(new Gson()))
        .setClient(new OkClient(okHttpClient));

    if (username != null && password != null) {
      // Concatenate username and password with colon for authentication
      final String credentials = username + ":" + password;

      builder.setRequestInterceptor(new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
          // Create Base64 encoded string
          String string = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
          request.addHeader("Authorization", string);
          request.addHeader("Accept", "application/json");
        }
      });
    }

    RestAdapter adapter = builder.build();

    return adapter.create(serviceClass);
  }
}
