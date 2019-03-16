package com.hereticpurge.quickvat.depinjector;

import com.hereticpurge.quickvat.apiservice.ApiClient;

import dagger.Component;

@Component(modules = {ApiClientModule.class, ContextModule.class})
public interface ApiClientComponent {

    ApiClient getApiClient();
}
