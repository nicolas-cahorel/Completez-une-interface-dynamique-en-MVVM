package com.openclassrooms.tajmahal.data.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.tajmahal.data.service.RestaurantApi;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * This is the repository class for managing review data. Repositories are responsible
 * for coordinating data operations from data sources such as network APIs, databases, etc.
 * <p>
 * Typically in an Android app built with architecture components, the repository will handle
 * the logic for deciding whether to fetch data from a network source or use data from a local cache.
 *
 * @see Restaurant
 * @see RestaurantApi
 */
@Singleton
public class ReviewRepository {

    // The API interface instance that will be used for network requests related to restaurant data.
    private final RestaurantApi restaurantApi;

    /**
     * Constructs a new instance of {@link RestaurantRepository} with the given {@link RestaurantApi}.
     *
     * @param restaurantApi The network API interface for fetching restaurant data.
     */
    @Inject
    public ReviewRepository(RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
    }

    /**
     * Fetches the review details.
     * <p>
     * This method will make a network call using the provided {@link RestaurantApi} instance
     * to fetch review data. Note that error handling and any transformations on the data
     * would need to be managed.
     *
     * @return MutableLiveData holding the review details.
     */
    public LiveData<List<Review>> getReviews() {
        return new MutableLiveData<>(restaurantApi.getReviews());
    }

}

