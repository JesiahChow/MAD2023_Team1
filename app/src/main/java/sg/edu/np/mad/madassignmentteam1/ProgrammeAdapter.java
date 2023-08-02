package sg.edu.np.mad.madassignmentteam1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProgrammeAdapter extends RecyclerView.Adapter<ProgrammeAdapter.ProgrammeViewHolder> {

    private final Context context;
    private final List<Programme> programmeList;
    private final ThumbnailApiService thumbnailApiService;

    public ProgrammeAdapter(Context context, List<Programme> programmeList) {
        this.context = context;
        this.programmeList = programmeList;


        // Create the Retrofit instance for fetching thumbnail URLs
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stb.gov.sg/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        thumbnailApiService = retrofit.create(ThumbnailApiService.class);
    }

    @NonNull
    @Override
    public ProgrammeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.thumbnail, parent, false);
        return new ProgrammeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Programme programme = programmeList.get(position);
        holder.programmeTitle.setText(programme.getName());
        holder.ratingCount.setText(Double.toString(programme.getRating()));// sync with ratingbar later
        holder.category.setText(programme.getCategoryDescription());
        holder.ratingBar.setRating(programme.getRating().floatValue());

        // Set the visibility of RatingBar and ratingCount based on rating value
        if (programme.getRating() > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingCount.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(programme.getRating().floatValue());
            holder.ratingCount.setText(String.valueOf(programme.getRating()));
        } else {
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.ratingCount.setVisibility(View.INVISIBLE);
        }

        // Check if the URL is directly available in ImageInfo
        ImageInfo imageInfo = programme.getThumbnails().get(0);
        if (imageInfo.getUrl() != null && !imageInfo.getUrl().isEmpty()) {
            // If URL is available, load the image directly
            String imageUrl = imageInfo.getUrl();
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            // If URL is not directly available, fetch the thumbnail URL based on UUID
            String uuid = imageInfo.getUuid();
            loadThumbnailImage(holder, uuid);
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PROGRAMMEADAPTER", "THis file Triumps");
                Intent intent = new Intent(context.getApplicationContext(), DetailedViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",programme.getName());
                bundle.putString("description",programme.getDescription());
                bundle.putString("category",programme.getCategoryDescription());
                if (programme.getAddress()  != null){
                    bundle.putString("address", programme.getAddress().toString());
                }
                else{
                    bundle.putString("address","");
                }
                bundle.putString("rating",programme.getRating().toString());
                Log.i("test",programme.getImages().toString());
                bundle.putStringArray("images",programme.getImagesArray());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return programmeList.size();
    }

    public static class ProgrammeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView programmeTitle, ratingCount, category;
        RatingBar ratingBar;
        ItemViewInterface itemViewInterface;


        public ProgrammeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            programmeTitle = itemView.findViewById(R.id.programmeTitle);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingCount = itemView.findViewById(R.id.ratingCount);
            category = itemView.findViewById(R.id.category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            itemViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }



    private void loadThumbnailImage(ProgrammeViewHolder holder, String uuid) {
        // API call to fetch thumbnail image based on UUID
        Call<ResponseBody> call = thumbnailApiService.getThumbnailImage(uuid, "Default");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Decode binary data into a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    // Load the Bitmap into the ImageView
                    holder.imageView.setImageBitmap(bitmap);
                } else {
                    // Handle the error if needed
                    Log.e("ProgrammeAdapter", "Failed to fetch thumbnail image: " + response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                // Handle the error if needed
                Log.e("ProgrammeAdapter", "Error fetching thumbnail image: " + t.getMessage());
            }
        });
    }
}
