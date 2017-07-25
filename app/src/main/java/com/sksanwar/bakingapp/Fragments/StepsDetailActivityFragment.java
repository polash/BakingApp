package com.sksanwar.bakingapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sksanwar.bakingapp.Pojo.Recipe;
import com.sksanwar.bakingapp.Pojo.Step;
import com.sksanwar.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by POLASH on 24-Jul-17.
 */

public class StepsDetailActivityFragment extends Fragment implements ExoPlayer.EventListener {


    private static MediaSessionCompat mediaSession;
    @BindView(R.id.recipe_step_video)
    SimpleExoPlayerView playerView;
    @BindView(R.id.recipe_short_desc)
    TextView shortDesc;
    @BindView(R.id.recipe_step_desc)
    TextView descpView;
    @BindView(R.id.previous_step_btn)
    Button previousBtn;
    @BindView(R.id.next_step_btn)
    Button nextBtn;
    @BindView(R.id.recipe_step_image)
    ImageView recipeStepImage;
    @BindBool(R.bool.is_landscape)
    boolean isLandscape;
    @BindView(R.id.recipe_step_desc_card)
    CardView descriptionCard;
    @BindBool(R.bool.is_tablet)
    boolean isTablet;
    Step steps;
    private ArrayList<Recipe> recipeList;
    private ArrayList<Step> stepList;
    private int index;
    private SimpleExoPlayer exoPlayer;
    private PlaybackStateCompat.Builder stateBuilder;

    public StepsDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_detail_activity, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {

            stepList = savedInstanceState.getParcelableArrayList(RecipeFragment.RECIPE_LIST);
            index = savedInstanceState.getInt(RecipeFragment.POSITION);
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RecipeFragment.RECIPE_LIST, stepList);
        outState.putInt(RecipeFragment.POSITION, index);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getting extra data into StepList list with the position
        stepList = getActivity().getIntent().getParcelableArrayListExtra(RecipeFragment.RECIPE_LIST);
        index = getActivity().getIntent().getExtras().getInt(RecipeFragment.POSITION);

        steps = stepList.get(index);

        if (steps != null) {
            setUpView(steps);
        }
    }

    //Set the views with data
    private void setUpView(Step steps) {
        descpView.setText(steps.getDescription());
        shortDesc.setText(steps.getShortDescription());

        //Getting image url
        String imageUrl = steps.getThumbnailURL();
        setupImageView(imageUrl);

        //getting video url
        String videoUrl = steps.getVideoURL();
        setupVideoView(videoUrl);

    }

    @OnClick(R.id.next_step_btn)
    void doNextStep() {
        if (index == stepList.size() - 1) {
            if (nextBtn.isEnabled()) nextBtn.setEnabled(false);
        } else {
            index++;

            if (!previousBtn.isEnabled()) previousBtn.setEnabled(true);

            steps = stepList.get(index);
            releasePlayer();
            setUpView(steps);
        }
    }

    @OnClick(R.id.previous_step_btn)
    void doPreviousStep() {
        if (index == 0) {
            if (previousBtn.isEnabled()) previousBtn.setEnabled(false);
        } else {
            index--;

            if (!nextBtn.isEnabled()) nextBtn.setEnabled(true);

            steps = stepList.get(index);
            releasePlayer();
            setUpView(steps);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    private void setupVideoView(String videoUrl) {
        if (videoUrl != null && !videoUrl.isEmpty()) {

            // Init and show video view
            setViewVisibility(playerView, true);
            initializeMediaSession();
            initializePlayer(Uri.parse(videoUrl));

            if (isLandscape && !isTablet) {
                // Expand video, hide description, hide system UI
                expandVideoView(playerView);
                setViewVisibility(descriptionCard, false);
                hideSystemUI();
            }

        } else {
            // Hide video view
            setViewVisibility(playerView, false);
        }
    }

    // Src: https://developer.android.com/training/system-ui/immersive.html
    private void hideSystemUI() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void expandVideoView(SimpleExoPlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), "RecipeStepSinglePageFragment");

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }


    private void setupImageView(String imageUrl) {
        if (imageUrl.isEmpty()) {
            Picasso.with(getActivity())
                    .load(R.drawable.recipe_placeholder)
                    .into(recipeStepImage);
        } else {
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .error(R.drawable.recipe_placeholder)
                    .into(recipeStepImage);
        }
    }

    private void setViewVisibility(View view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
