package cse110.ucsd.team12wwr.ui.routes_tab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import cse110.ucsd.team12wwr.R;
import cse110.ucsd.team12wwr.RouteInfoActivity;
import cse110.ucsd.team12wwr.TeamIndividRoutes;

/**
 * A placeholder fragment containing a simple view.
 */
public class PersonalRoutesFragment extends Fragment {

    private static final String TAG = "PersonalRoutesFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PersonalRoutesFragment newInstance(int index) {
        PersonalRoutesFragment fragment = new PersonalRoutesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Entered Personal Routes Tab!");
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);
    }

    public void launchRouteInfoActivity() {
        Log.d(TAG, "launchRouteInfoActivity: launching the route information page");
        Intent intent = new Intent(getActivity(), RouteInfoActivity.class);

        startActivity(intent);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_individ_routes, container, false);

        FloatingActionButton button = root.findViewById(R.id.add_fab2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRouteInfoActivity();
            }
        });

        return root;
    }
}