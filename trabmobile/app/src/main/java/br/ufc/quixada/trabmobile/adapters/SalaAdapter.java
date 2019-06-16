package br.ufc.quixada.trabmobile.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import br.ufc.quixada.trabmobile.ActivityCallback;
import br.ufc.quixada.trabmobile.R;
import br.ufc.quixada.trabmobile.models.Sala;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalaAdapter extends RecyclerView.Adapter<SalaAdapter.ViewHolder> {

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    private List<Sala> mContent = new ArrayList<>();

    public void clearSala() {
        mContent.clear();
    }

    public void addSala(Sala sala) {
        mContent.add(sala);
    }

    public SalaAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images ) {
        mImageNames = imageNames;
        mImages = images;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sala, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(0))
                .into(holder.image);

        Sala sala = mContent.get(position);
        holder.imageName.setText(sala.getNome());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();

                ActivityCallback mCallback = (ActivityCallback) view.getContext();
                mCallback.abrirChat(mImageNames.get(position));

            }
        });
        /*holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView imageName;
        //RelativeLayout parentLayout;
        CardView parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_sala);
            imageName = itemView.findViewById(R.id.item_sala);
            parentLayout = itemView.findViewById(R.id.layout_sala);
        }
    }
}