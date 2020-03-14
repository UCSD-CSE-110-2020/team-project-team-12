package cse110.ucsd.team12wwr.recycler;

import cse110.ucsd.team12wwr.MainActivity;
import cse110.ucsd.team12wwr.ProposedWalkScreen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

import cse110.ucsd.team12wwr.R;
import cse110.ucsd.team12wwr.firebase.DaoFactory;
import cse110.ucsd.team12wwr.firebase.Schedule;
import cse110.ucsd.team12wwr.firebase.ScheduleDao;
import cse110.ucsd.team12wwr.firebase.User;
import cse110.ucsd.team12wwr.firebase.UserDao;

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
            String userEmail = MainActivity.userEmail;
            UserDao dao = DaoFactory.getUserDao();
            dao.findUserByID(userEmail, task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User u = document.toObject(User.class);
                        String currUserFullName = u.firstName + " " + u.lastName;
                        votingButtonUpdater(view, currUserFullName);
                    }
                }
            });
        }

        private void votingButtonUpdater(View view, String currUserFullName) {
            RadioButton dontWant = containerView.findViewById(R.id.dont_want);
            RadioButton badTime = containerView.findViewById(R.id.bad_time);
            RadioButton yes = containerView.findViewById(R.id.yes);
            boolean checked = ((RadioButton)view).isChecked();
            int buttonId = view.getId();

            TextView name = containerView.findViewById(R.id.vote_name);
            String displayedName = name.getText().toString();

            Schedule.Vote vote = Schedule.Vote.ABSTAINED;
            if (displayedName.equals(currUserFullName)) {
                switch(buttonId) {
                    case R.id.yes:
                        if (checked) {
                            yes.setButtonDrawable(R.drawable.button_yes_on);
                            dontWant.setButtonDrawable(R.drawable.button_no_off);
                            badTime.setButtonDrawable(R.drawable.button_no_off);
                            vote = Schedule.Vote.ACCEPTED;
                        }
                        break;
                    case R.id.bad_time:
                        if (checked) {
                            yes.setButtonDrawable(R.drawable.button_yes_off);
                            dontWant.setButtonDrawable(R.drawable.button_no_off);
                            badTime.setButtonDrawable(R.drawable.button_no_on);
                            vote = Schedule.Vote.DECLINED_CONFLICT;
                        }
                        break;
                    case R.id.dont_want:
                        if (checked) {
                            yes.setButtonDrawable(R.drawable.button_yes_off);
                            dontWant.setButtonDrawable(R.drawable.button_no_on);
                            badTime.setButtonDrawable(R.drawable.button_no_off);
                            vote = Schedule.Vote.DECLINED_DIFFICULTY;
                        }
                        break;
                }
                final Schedule.Vote finalVote = vote;

                DaoFactory.getUserDao().findUserByID(MainActivity.userEmail, task-> {
                    if (task.isSuccessful())  {
                        User u = null;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            u = doc.toObject(User.class);
                        }
                        if (u != null) {
                            String teamId = u.teamID;
                            ScheduleDao scheduleDao = DaoFactory.getScheduleDao();
                            scheduleDao.findScheduleByTeam(teamId, task2 -> {
                               if (task2.isSuccessful()) {
                                   Schedule s = null;
                                   for (QueryDocumentSnapshot doc : task2.getResult()) {
                                       s = doc.toObject(Schedule.class);
                                   }
                                   if (s != null) {
                                       Map<String, Schedule.Vote> userVoteMap = s.userVoteMap;
                                       userVoteMap.put(MainActivity.userEmail, finalVote);
                                       scheduleDao.updateVoteMap(teamId, userVoteMap);
                                   }
                               }
                            });
                        }
                    }
                });
            }
        }
    }
}
