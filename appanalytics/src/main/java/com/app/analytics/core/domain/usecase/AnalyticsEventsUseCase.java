package com.app.analytics.core.domain.usecase;


import com.app.analytics.core.base.BaseUseCase;
import com.app.analytics.core.data.repository.AppDataRepositoryImpl;
import com.app.analytics.core.domain.abstraction.AppDataRepository;
import com.app.analytics.core.domain.model.AnalyticEvent;

import io.reactivex.Observable;

/**
 * Created by sanjay.k on 01-Aug-17.
 */

public class AnalyticsEventsUseCase extends BaseUseCase<String> {

    private final AppDataRepositoryImpl appDataRepository;

    public AnalyticsEventsUseCase(AppDataRepositoryImpl analyticsEventRepository) {
        super(analyticsEventRepository);
        this.appDataRepository = analyticsEventRepository;
    }

    @Override
    protected Observable<String> createObservable() {
        return null;
    }

    @Override
    protected Observable<String> createObservable(Object objectKey) {
        // return null;
        return appDataRepository.postEvent((AnalyticEvent) objectKey);
    }
}
