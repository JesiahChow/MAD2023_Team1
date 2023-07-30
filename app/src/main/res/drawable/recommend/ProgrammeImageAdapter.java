package sg.edu.np.mad.recommend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProgrammeImageAdapter extends RecyclerView.Adapter<ProgrammeImageAdapter.ViewHolder> {
        private String[] data;
        private ThumbnailApiService thumbnailApiService;

       public  boolean check_URL(String str) {
           try {
               new URL(str).toURI();
               return true;
           } catch (Exception e) {
               return false;
           }
       }
       public ProgrammeImageAdapter(String[] data) {

            this.data = data;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.stb.gov.sg/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            thumbnailApiService = retrofit.create(ThumbnailApiService.class);
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String item = data[position];
            if(check_URL(item))
            {
                Picasso.get().load(item).into(holder.image);
            }
            else{
                loadThumbnailImage(holder, item);
            }

        }

        @Override
        public int getItemCount() {
            return data.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
            }
        }
        private void loadThumbnailImage(ViewHolder holder, String uuid) {
            // API call to fetch thumbnail image based on UUID
            Call<ResponseBody> call = thumbnailApiService.getThumbnailImage(uuid, "Default");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Decode binary data into a Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        // Load the Bitmap into the ImageView
                        holder.image.setImageBitmap(bitmap);
                    } else {
                        // Handle the error if needed
                        Log.e("ProgrammeImageAdapter", "Failed to fetch thumbnail image: " + response);
                    }
                }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle the error if needed
                Log.e("ProgrammeAdapter", "Error fetching thumbnail image: " + t.getMessage());
            }
        });
    }
    }


