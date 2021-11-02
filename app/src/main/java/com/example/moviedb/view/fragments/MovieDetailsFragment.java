package com.example.moviedb.view.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviedb.R;
import com.example.moviedb.helper.Const;
import com.example.moviedb.model.Movies;
import com.example.moviedb.viewmodel.MovieViewModel;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        progressDialog = ProgressDialog.show(getActivity(), "Loading Progress", "Loading...", true);
        progressDialog.show();
    }

    private TextView lbl_title, lbl_genre, lbl_vote, lbl_average, lbl_sinopsis, lbl_popularity, lbl_releasedate, lbl_tagline;
    private String movie_id, genre, vote, tagline, title, average, releaseDate, popularity, sinopsis;
    private ImageView img_poster,img_backdrop;
    private LinearLayout linearLayout;
    private MovieViewModel viewModel;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);

        img_poster = view.findViewById(R.id.img_poster_details);
        lbl_title = view.findViewById(R.id.lbl_details_title);
        lbl_tagline = view.findViewById(R.id.lbl_details_tagline);
        img_backdrop = view.findViewById(R.id.img_backdrop_details);
        lbl_genre = view.findViewById(R.id.lbl_details_genre);
        lbl_releasedate = view.findViewById(R.id.lbl_details_releasedate);
        lbl_popularity = view.findViewById(R.id.lbl_details_popularity);
        lbl_vote = view.findViewById(R.id.lbl_details_vote);
        lbl_average = view.findViewById(R.id.lbl_details_average);
        linearLayout = view.findViewById(R.id.sv_details_company);
        lbl_sinopsis = view.findViewById(R.id.lbl_details_overview);

        movie_id = getArguments().getString("movieId");

        viewModel.getMovieById(movie_id);
        viewModel.getResultGetMovieById().observe(getActivity(), showMoviesResults);

        return view;
    }

    private Observer<Movies> showMoviesResults = new Observer<Movies>() {
        @Override
        public void onChanged(Movies movies) {
            genre = "";
            vote = String.valueOf(movies.getVote_count());
            tagline = movies.getTagline();
            title = movies.getTitle();
            average = String.valueOf(movies.getVote_average());
            releaseDate = movies.getRelease_date();
            popularity = String.valueOf(movies.getPopularity()) + " Views";
            sinopsis = movies.getOverview();
            for (int i = 0; i < movies.getGenres().size(); i++) {
                if (i == movies.getGenres().size() -1)
                {
                    genre += movies.getGenres().get(i).getName();
                }else{
                    genre += movies.getGenres().get(i).getName()+", ";
                }
            }
            Glide.with(getActivity()).load(Const.IMG_URL + movies.getPoster_path().toString()).into(img_poster);
            Glide.with(getActivity()).load(Const.IMG_URL + movies.getBackdrop_path().toString()).into(img_backdrop);

            lbl_title.setText(title);
            lbl_sinopsis.setText(sinopsis);
            lbl_genre.setText(genre);
            lbl_tagline.setText(tagline);
            lbl_vote.setText(vote);
            lbl_average.setText(average);
            lbl_popularity.setText(popularity);
            lbl_releasedate.setText(releaseDate);

            for (int i = 0; i < movies.getProduction_companies().size(); i++){
                ImageView image = new ImageView(linearLayout.getContext());
                String logo = Const.IMG_URL + movies.getProduction_companies().get(i).getLogo_path();
                String company = movies.getProduction_companies().get(i).getName();
                if(movies.getProduction_companies().get(i).getLogo_path() == null){
                    image.setImageDrawable(getResources().getDrawable(R.drawable.swag, getActivity().getTheme()));
                }else{
                    Glide.with(getActivity()).load(logo).into(image);
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
                image.setBackgroundColor(Color.parseColor("#F2D096"));
                layoutParams.setMargins(30,0,30,0);
                image.setPadding(20,20,20,20);
                image.setLayoutParams(layoutParams);
                linearLayout.addView(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar bar = Snackbar.make(view, company, Snackbar.LENGTH_LONG);
                        bar.setAnchorView(R.id.bottom_nav_main_menu);
                        bar.show();
                    }
                });
            }
            progressDialog.dismiss();
        }
    };
}