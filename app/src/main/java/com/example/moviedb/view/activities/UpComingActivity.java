package com.example.moviedb.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moviedb.R;
import com.example.moviedb.adapter.UpComingAdapter;
import com.example.moviedb.model.UpComing;
import com.example.moviedb.viewmodel.MovieViewModel;

public class UpComingActivity extends AppCompatActivity {

    private RecyclerView rv_up_coming;
    private MovieViewModel view_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming);
        rv_up_coming = findViewById(R.id.rv_up_coming);
        view_model = new ViewModelProvider(UpComingActivity.this).get(MovieViewModel.class);
        view_model.getUpComing();
        view_model.getResultUpComing().observe(UpComingActivity.this, showUpComing);
    }

    private Observer<UpComing> showUpComing = new Observer<UpComing>() {
        @Override
        public void onChanged(UpComing upComing) {
            rv_up_coming.setLayoutManager(new LinearLayoutManager(UpComingActivity.this));
            UpComingAdapter adapter = new UpComingAdapter(UpComingActivity.this);
            adapter.setListUpComing(upComing.getResults());
            rv_up_coming.setAdapter(adapter);
        }
    };
}