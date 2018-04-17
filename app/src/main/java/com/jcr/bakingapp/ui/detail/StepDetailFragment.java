package com.jcr.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.R;
import com.jcr.bakingapp.data.models.Step;
import com.jcr.bakingapp.databinding.FragmentStepDetailBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.jcr.bakingapp.ui.recipes.RecipesListActivity.EXTRA_RECIPE_ID;

public class StepDetailFragment extends Fragment {

    private static final String STATE_PLAYER_POSITION = "player_position";
    private static final String STATE_PLAYER_READY = "player_ready";
    private Context mContext;
    private StepDetailViewModel mViewModel;
    private int mStepId = 0;
    private FragmentStepDetailBinding mBinding;
    private SimpleExoPlayer mExoPlayer;
    private DataSource.Factory mediaDataSourceFactory;
    private BandwidthMeter bandwidthMeter;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private long mPlayerPosition;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "BakingApp"),
                (TransferListener<? super DataSource>) bandwidthMeter);
        if (savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong(STATE_PLAYER_POSITION);
            shouldAutoPlay = savedInstanceState.getBoolean(STATE_PLAYER_READY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_step_detail, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }

    private void initViewModel() {
        int recipeId = getActivity().getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        StepDetailViewModelFactory factory = Injection.provideStepDetailViewModel(getContext(), recipeId, mStepId);
        mViewModel = ViewModelProviders.of(this, factory).get(StepDetailViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initDisposable();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23)) {
            initDisposable();
        }
    }

    private void initDisposable() {
        mDisposable.add(mViewModel.getStep()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindStep));
        mBinding.executePendingBindings();
    }

    private void bindStep(Step step) {
        bindTexts(step.getShortDescription(), step.getDescription());

        bindThumbnailImage(step.getThumbnailURL());

        bindPlayer(step.getVideoURL());
    }

    private void bindTexts(String shortDescription, String description) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            mBinding.stepDescription.setText(description);
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(String.format(
                    getString(R.string.steps_detail_toolbar), shortDescription));
            if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                mBinding.stepDescription.setText(description);
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            }
        }
    }

    private void bindThumbnailImage(String thumbnailURL) {
        loadDefaultArtwork();
        if (thumbnailURL != null && !thumbnailURL.isEmpty()) {
            Picasso.get().load(thumbnailURL).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mBinding.playerView.setDefaultArtwork(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    loadDefaultArtwork();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }
    }

    private void loadDefaultArtwork() {
        mBinding.playerView.setDefaultArtwork(BitmapFactory.decodeResource(
                getContext().getResources(),
                R.drawable.list_item_place_holder));
    }

    public void setStep(int stepId) {
        mStepId = stepId;
    }

    private void bindPlayer(String videoURL) {
        if (mExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mBinding.playerView.setPlayer(mExoPlayer);

            if (videoURL != null && !videoURL.isEmpty()) {
                initializePlayer();
            } else {
                mBinding.playerView.hideController();
            }
        }
    }

    private void initializePlayer() {
        mExoPlayer.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mViewModel.getVideoUrl()),
                mediaDataSourceFactory, extractorsFactory, null, null);

        mExoPlayer.seekTo(mPlayerPosition);
        mExoPlayer.prepare(mediaSource);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            shouldAutoPlay = mExoPlayer.getPlayWhenReady();
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        mDisposable.clear();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_PLAYER_POSITION, mPlayerPosition);
        outState.putBoolean(STATE_PLAYER_READY, shouldAutoPlay);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
            trackSelector = null;
        }
    }
}
