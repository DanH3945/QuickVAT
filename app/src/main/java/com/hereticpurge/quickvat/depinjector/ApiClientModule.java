package com.hereticpurge.quickvat.depinjector;

import android.content.Context;

import com.hereticpurge.quickvat.apiservice.ApiClient;
import com.hereticpurge.quickvat.apiservice.ApiReference;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiClientModule {

    @Provides
    public Cache okHttpCache(@ActivityContextQualifier Context context) {
        File cacheFile = new File(context.getCacheDir(), "okhttpcache");
        cacheFile.mkdirs();

        // 10MB cache
        return new Cache(cacheFile, 10 * 10 * 1000);
    }

    @Provides
    public OkHttpClient okHttpClient(Cache cache) {

        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    public ApiClient apiClient(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiReference.ApiUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiClient.class);
    }
}
