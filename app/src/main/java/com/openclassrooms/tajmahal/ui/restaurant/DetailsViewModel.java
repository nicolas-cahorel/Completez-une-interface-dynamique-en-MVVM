package com.openclassrooms.tajmahal.ui.restaurant;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.data.repository.ReviewRepository;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * MainViewModel is responsible for preparing and managing the data for the {@link DetailsFragment}.
 * It communicates with the {@link RestaurantRepository} to fetch restaurant details and provides
 * utility methods related to the restaurant UI.
 * <p>
 * This ViewModel is integrated with Hilt for dependency injection.
 */
@HiltViewModel
public class DetailsViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Constructor that Hilt will use to create an instance of MainViewModel.
     *
     * @param restaurantRepository The repository which will provide restaurant data.
     * @param reviewRepository     The repository which will provide review data.
     */
    @Inject
    public DetailsViewModel(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Fetches the details of the Taj Mahal restaurant.
     *
     * @return LiveData object containing the details of the Taj Mahal restaurant.
     */
    public LiveData<Restaurant> getTajMahalRestaurant() {
        return restaurantRepository.getRestaurant();
    }


    /**
     * Fetches the list of details of the reviews that can be modified.
     */
    MutableLiveData<List<Review>> currentReview = new MutableLiveData<List<Review>>();

    /**
     * Retrieves the current day of the week in French.
     *
     * @return A string representing the current day of the week in French.
     */
    public String getCurrentDay(Context context) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayString;

        switch (dayOfWeek) {
            case Calendar.MONDAY:
                dayString = context.getString(R.string.monday);
                break;
            case Calendar.TUESDAY:
                dayString = context.getString(R.string.tuesday);
                break;
            case Calendar.WEDNESDAY:
                dayString = context.getString(R.string.wednesday);
                break;
            case Calendar.THURSDAY:
                dayString = context.getString(R.string.thursday);
                break;
            case Calendar.FRIDAY:
                dayString = context.getString(R.string.friday);
                break;
            case Calendar.SATURDAY:
                dayString = context.getString(R.string.saturday);
                break;
            case Calendar.SUNDAY:
                dayString = context.getString(R.string.sunday);
                break;
            default:
                dayString = "";
        }
        return dayString;
    }


    public LiveData<List<Integer>> getStarsCount() {
        return reviewRepository.getReviews();
        // traiter les reviews pour  créer les nouvelles données
    }
    /**
     * Initialization of a MutableLiveData array to store the number of reviews for each star rating.
     *     MutableLiveData<int[]> starsCount = new MutableLiveData<>(new int[6]);
     */


    /**
     * Initialization of a MutableLiveData float to store the average rating of all reviews.
     */
    MutableLiveData<Float> averageRating = new MutableLiveData<>();

    /**
     * Initialization of a MutableLiveData boolean to indicate whether the last review has been reached.
     */
    MutableLiveData<Boolean> isLastReview = new MutableLiveData<>(false);


public void startDetails() {
    starsCount = reviewRepository.getReview()
}



}


// MAJ de la valeur contenue dans le LiveData
// currentReview.postValue(reviews.get(1));

// Lecture du contenu du LiveData
// currentReview.getValue();