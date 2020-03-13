package cse110.ucsd.team12wwr.recycler;

import cse110.ucsd.team12wwr.ProposedWalkScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import cse110.ucsd.team12wwr.R;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;

    public RecycleAdapter(Context ctx) {
        this.inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vote_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvName.setText(ProposedWalkScreen.itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return ProposedWalkScreen.itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Button voteYes, voteNo;
        private TextView tvName;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.vote_name);
            voteYes = itemView.findViewById(R.id.yes);
            voteNo = itemView.findViewById(R.id.bad_time);

            voteYes.setTag(R.integer.vote_yes_view, itemView);
            voteNo.setTag(R.integer.vote_no_view, itemView);
            voteYes.setOnClickListener(this);
            voteNo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if ( view.getId() == voteYes.getId() ) {
                voteYes.setBackground(view.getContext().getDrawable(R.drawable.button_yes_on));
            } else if ( view.getId() == voteYes.getId() && view.getBackground().equals(view.getContext().getDrawable(R.drawable.button_yes_on)) ) {
                voteYes.setBackgroundResource(R.drawable.button_yes_off);
            } else if ( view.getId() == voteNo.getId() && view.getBackground().equals(view.getContext().getDrawable(R.drawable.button_no_off)) ) {
                voteNo.setBackgroundResource(R.drawable.button_no_on);
            } else if ( view.getId() == voteNo.getId() && view.getBackground().equals(view.getContext().getDrawable(R.drawable.button_no_on))) {
                voteNo.setBackgroundResource(R.drawable.button_no_off);
            }
        }
    }

}
