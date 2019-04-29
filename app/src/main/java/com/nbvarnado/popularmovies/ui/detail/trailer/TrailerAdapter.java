package com.nbvarnado.popularmovies.ui.detail.trailer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.data.database.trailers.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=" ;
    private static final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    private static final String THUMBNAIL_QUALITY = "/hqdefault.jpg";

    private List<Trailer> mTrailers;
    private Context mContext;

    public TrailerAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View trailerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, parent, false);
        return new TrailerViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);

        String trailerThumbnailUrl = THUMBNAIL_BASE_URL + trailer.getKey() + THUMBNAIL_QUALITY;
        Picasso.get()
                .load(trailerThumbnailUrl)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.error)
                .into(holder.mTrailerImageView);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) {
            return 0;
        }
        return mTrailers.size();
    }

    public void setTrailerData(List<Trailer> trailerData) {
        mTrailers = trailerData;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mTrailerImageView;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrailerImageView = itemView.findViewById(R.id.iv_trailer_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Trailer trailer = mTrailers.get(position);
            String url = YOUTUBE_BASE_URL + trailer.getKey();
            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            PackageManager packageManager = mContext.getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

            if (activities.size() > 0) {
                mContext.startActivity(intent);
            }
        }
    }
}
