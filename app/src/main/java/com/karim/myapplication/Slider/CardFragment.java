package com.karim.myapplication.Slider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.karim.myapplication.R;

public class CardFragment extends Fragment{
    private CardView cardView;
   static boolean noChoice;

    public CardFragment(boolean noChoice) {
        this.noChoice = noChoice;
    }

    public CardFragment() {
    }

    public static Fragment getInstance(int position) {
        CardFragment f = new CardFragment(noChoice);
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = view.findViewById(R.id.title);
        Button button = view.findViewById(R.id.button);
        if(noChoice)
            button.setVisibility(View.GONE);
       // title.setText(String.format("Card %d", getArguments().getInt("position")));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Button in Card " + getArguments().getInt("position")
                        + "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }

}
