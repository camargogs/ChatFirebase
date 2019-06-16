package br.ufc.quixada.trabmobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufc.quixada.trabmobile.R;
import br.ufc.quixada.trabmobile.models.ChatData;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    static final class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;

        ChatViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.item_username);
            message = (TextView) view.findViewById(R.id.item_message);
        }
    }

    private List<ChatData> mContent = new ArrayList<>();

    public void clearData() {
        mContent.clear();
    }

    public void addData(ChatData data) {
        mContent.add(data);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);
        return new ChatViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatData data = mContent.get(position);

        holder.message.setText(data.getMessage());
        holder.name.setText(data.getName());
    }
}
