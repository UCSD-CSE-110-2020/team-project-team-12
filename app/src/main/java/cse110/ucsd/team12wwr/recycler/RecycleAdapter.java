package cse110.ucsd.team12wwr.recycler;

import cse110.ucsd.team12wwr.ProposedWalkScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
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

        protected Button voteYes, voteNo, voteNo1;
        private TextView tvName;
        private View containerView;

        public MyViewHolder(final View itemView) {
            super(itemView);

            containerView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.vote_name);
            voteYes = itemView.findViewById(R.id.yes);
            voteNo = itemView.findViewById(R.id.bad_time);
            voteNo1 = itemView.findViewById(R.id.dont_want);

            voteYes.setTag(R.integer.vote_yes_view, itemView);
            voteNo.setTag(R.integer.vote_no_view, itemView);
            voteNo1.setTag(R.integer.vote_no1_view, itemView);
            voteYes.setOnClickListener(this);
            voteNo.setOnClickListener(this);
            voteNo1.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            RadioButton dontWant = containerView.findViewById(R.id.dont_want);
            RadioButton badTime = containerView.findViewById(R.id.bad_time);
            RadioButton yes = containerView.findViewById(R.id.yes);
            boolean checked = ((RadioButton)view).isChecked();
            int buttonId = view.getId();

            switch(buttonId) {
                case R.id.yes:
                    if (checked) {
                        yes.setButtonDrawable(R.drawable.button_yes_on);
                        dontWant.setButtonDrawable(R.drawable.button_no_off);
                        badTime.setButtonDrawable(R.drawable.button_no_off);
                    }
                    break;
                case R.id.bad_time:
                    if (checked) {
                        yes.setButtonDrawable(R.drawable.button_yes_off);
                        dontWant.setButtonDrawable(R.drawable.button_no_off);
                        badTime.setButtonDrawable(R.drawable.button_no_on);
                    }
                    break;
                case R.id.dont_want:
                    if (checked) {
                        yes.setButtonDrawable(R.drawable.button_yes_off);
                        dontWant.setButtonDrawable(R.drawable.button_no_on);
                        badTime.setButtonDrawable(R.drawable.button_no_off);
                    }
                    break;
           }
        }
    }

}
