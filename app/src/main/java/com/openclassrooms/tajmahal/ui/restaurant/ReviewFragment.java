package com.openclassrooms.tajmahal.ui.restaurant;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.databinding.FragmentReviewBinding;
import com.openclassrooms.tajmahal.domain.model.Review;
import com.openclassrooms.tajmahal.views.ReviewAdapter;

import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment that displays a form for users to submit a review for a restaurant.
 * This fragment is responsible for handling user input, validating the input, and adding the new review to the local list of reviews.
 */
@AndroidEntryPoint
public class ReviewFragment extends Fragment {

    // Binding object instance corresponding to the fragment_review.xml layout
    private FragmentReviewBinding binding;

    // ViewModel instance for handling the business logic related to this fragment
    private DetailsViewModel detailsViewModel;

    // URL for the avatar image
    private String avatarUrl;

    /**
     * Initializes the ViewModel for this activity.
     */
    private void setupViewModel() {
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    /**
     * This method is called when the fragment is first created.
     * It's used to perform one-time initialization.
     *
     * @param savedInstanceState A bundle containing previously saved instance state.
     *                           If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself but return it.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Returns the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * This method is called immediately after `onCreateView()`.
     * Use this method to perform final initialization once the fragment views have been inflated.
     *
     * @param view               The View returned by `onCreateView()`.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Calls the superclass' onViewCreated() method to ensure proper initialization and default behavior before performing specific operations in the current fragment
        super.onViewCreated(view, savedInstanceState);
        // Prepares the ViewModel for the fragment
        setupViewModel();
        // Sets up user interface components
        setupUI();
        // Observes changes in the reviews data and updates the UI accordingly
        detailsViewModel.getTajMahalReviews().observe(requireActivity(), this::updateUIWithReviews);
        binding.tvRestaurantNameInReview.setText(getString(R.string.restaurant_name));
        binding.buttonBack.setEnabled(true);
        binding.tvNewReviewName.setText(getString(R.string.new_reviewers_name));
        this.avatarUrl = "https://s3-alpha-sig.figma.com/img/02e6/6d63/e35d4fc4ab41421bdc4ea8ec50940749?Expires=1714348800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=kMBgvHNZs3pb4gMB8uACW-mXV-Wbo2gfLwtfkCN~8LNpAGpafP5DSMu76ucdJ5B6OJkx8C5bxGKJESzwpnk7pKAqbAiUqdJVFm7kDCg5lMRFXt1Wf2U9EVonpsMUiY2-C2QGHMUJwQGGDFdov3RWDH2HV0gJIMM7-OK4Iag0e0sijV0qmGve8Uo1arI6IV-yLBrfkYUxOpy23swcUmY85EcaW1hNpv1RoMvQYlwtlsrGBysgQuq0K48saCS94gYFSAH8jv2KACACo1pXFhWVWMQ5yOXPY6CCnH4JZXvLDl~NU9xRhmkKXkwIPSrkbjhtA4-D9MPS7JSDQ1PG6Kx1Fw__";
        Glide.with(binding.getRoot())
                .load(avatarUrl)
                .into(binding.ivNewReviewAvatar);
        binding.buttonValidate.setEnabled(true);
        binding.buttonValidate.setText(getString(R.string.button_validate));
        binding.buttonValidate.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)));

        binding.buttonValidate.setOnClickListener(v -> {
            detailsViewModel.addReview(createNewReview());
        });

        // Sets an OnClickListener for the back button. When clicked, it replaces the current fragment with the DetailsFragment
        // by using a FragmentTransaction, allowing the user to navigate back to the details view.
        binding.buttonBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailsFragment detailsFragment = new DetailsFragment();
            fragmentTransaction.replace(R.id.container, detailsFragment);
            fragmentTransaction.commit();
        });
        binding.tietNewReviewComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateButtonState();
            }
        });
        binding.rbNewReviewRate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> updateButtonState());

    }

    /**
     * Updates the enabled state and background color of the 'Validate' button based on the user input.
     */
    private void updateButtonState() {
        float rbNewReviewRate = binding.rbNewReviewRate.getRating();
        String tietNewReviewComment = Objects.requireNonNull(binding.tietNewReviewComment.getText()).toString();

        if (rbNewReviewRate == 0 && tietNewReviewComment.isEmpty()) {
            binding.buttonValidate.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)));
            binding.buttonValidate.setEnabled(false);
        } else if (rbNewReviewRate == 0) {
            binding.buttonValidate.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)));
            binding.buttonValidate.setEnabled(false);

        } else if (tietNewReviewComment.isEmpty()) {
            binding.buttonValidate.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)));
            binding.buttonValidate.setEnabled(false);

        } else {
            binding.buttonValidate.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red)));
            binding.buttonValidate.setEnabled(true);

        }
    }

    /**
     * Creates a new Review instance using the user input from the form.
     */
    private Review createNewReview() {
        // Retrieves the author of the new review
        TextView tvNewReviewName = binding.tvNewReviewName;
        String author = tvNewReviewName.getText().toString();
        // Retrieves the rating of the new review
        RatingBar rbNewReviewRate = binding.rbNewReviewRate;
        float ratingFloat = rbNewReviewRate.getRating();
        int rating = (int) ratingFloat;
        // Retrieves the comment of the new review
        TextInputEditText tietNewReviewComent = binding.tietNewReviewComment;
        String content = Objects.requireNonNull(tietNewReviewComent.getText()).toString();

        // Creates a new instance of the object to be added
        return new Review(author, avatarUrl, content, rating);
    }

    /**
     * Updates the UI with the provided list of reviews.
     *
     * @param reviews List of reviews to display in the RecyclerView
     */
    private void updateUIWithReviews(List<Review> reviews) {
        Log.d("ReviewFragment", "Number of reviews: " + reviews.size());
        binding.fragmentReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ReviewAdapter adapter = new ReviewAdapter(reviews);
        binding.fragmentReviewRecyclerView.setAdapter(adapter);
    }

    /**
     * Sets up the UI-specific properties, such as system UI flags and status bar color.
     */
    private void setupUI() {
        Window window = requireActivity().getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        // Change the status bar color to white
        window.setStatusBarColor(Color.WHITE);
        // Use dark mode for the status bar text
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Change the navigation bar color to white
        window.setNavigationBarColor(Color.WHITE);
    }
}
