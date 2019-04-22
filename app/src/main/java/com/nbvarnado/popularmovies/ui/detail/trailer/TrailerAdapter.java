package com.nbvarnado.popularmovies.ui.detail.trailer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.TextView;

import com.nbvarnado.popularmovies.R;
import com.nbvarnado.popularmovies.data.database.trailers.Trailer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

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
        holder.mTrailerTextView.setText(trailer.getName());
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

        final TextView mTrailerTextView;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrailerTextView = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Trailer trailer = mTrailers.get(position);
            String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
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
