package id.univmulia.technews.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.List;

import id.univmulia.technews.Activities.CreatePostActivity;
import id.univmulia.technews.Activities.DetailPostActivity;
import id.univmulia.technews.Activities.LoginActivity;
import id.univmulia.technews.Activities.PostActivity;
import id.univmulia.technews.Models.Post;
import id.univmulia.technews.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyviewHolder> {

    private PostActivity listener;
    private Context mContext;
    private List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.listener =(PostActivity) mContext;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.item_postingan,parent,false);
        return new MyviewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {

        holder.TvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).circleCrop().into(holder.imgPostProfile);

        holder.TvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.delPost(mData.get(position),position);
                return true;
            }
        });

        holder.imgPostProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginAct = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(loginAct);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView TvTitle;
        ImageView imgPost;
        ImageView imgPostProfile;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            TvTitle = itemView.findViewById(R.id.posted_title);
            imgPost = itemView.findViewById(R.id.posted_img);
            imgPostProfile = itemView.findViewById(R.id.posted_img_profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailAct = new Intent(mContext, DetailPostActivity.class);
                    int position = getAdapterPosition();

                    postDetailAct.putExtra("Key",mData.get(position).getKey());
                    postDetailAct.putExtra("title",mData.get(position).getTitle());
                    postDetailAct.putExtra("picture",mData.get(position).getPicture());
                    postDetailAct.putExtra("description",mData.get(position).getDescription());
                    postDetailAct.putExtra("userName",mData.get(position).getUserName());
                    postDetailAct.putExtra("userPhoto",mData.get(position).getUserPhoto());
                    long timeStamp = (long) mData.get(position).getTimeStamp();
                    postDetailAct.putExtra("postDate",timeStamp);
                    mContext.startActivity(postDetailAct);
                }
            });
        }
    }
}
