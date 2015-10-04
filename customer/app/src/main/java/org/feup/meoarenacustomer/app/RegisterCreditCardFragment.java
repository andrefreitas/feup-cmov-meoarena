package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.util.Log;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

public class RegisterCreditCardFragment extends Fragment {
    EditText numberEditText;
    EditText yearEditText;
    EditText monthEditText;
    ImageView typeImageView;
    Button registerButton;

    String ccNumber;
    String ccType;
    String ccValidity;
    String ccYear;
    String ccMonth;

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public boolean register(String ccNumber, String ccType, String ccValidity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_credit_card, container, false);

        updateNumber(rootView);
        register(rootView);

        return rootView;
    }

    public void updateNumber(View view){
        numberEditText = (EditText) view.findViewById(R.id.numberEditText);
        typeImageView = (ImageView) view.findViewById(R.id.typeImageView);

        numberEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                String type = Helpers.getCreditCardType(s.toString());

                    if (type.equals("visa")){
                        typeImageView.setImageResource(R.drawable.credit_card_visa);
                    } else if (type.equals("mastercard")){
                        typeImageView.setImageResource(R.drawable.credit_card_mastercard);
                    } else {
                        typeImageView.setImageResource(R.drawable.credit_card);
                    }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

    }

    public void register(View view){
        numberEditText = (EditText) view.findViewById(R.id.numberEditText);
        yearEditText = (EditText) view.findViewById(R.id.yearEditText);
        monthEditText = (EditText) view.findViewById(R.id.monthEditText);
        registerButton = (Button) view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccNumber = numberEditText.getText().toString();
                ccYear = yearEditText.getText().toString();
                ccMonth = monthEditText.getText().toString();
                ccType = Helpers.getCreditCardType(ccNumber);

                if(ccType.equals("invalid")){
                    Toast.makeText(getActivity(), R.string.invalid_credit_card_number, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Helpers.isValidMonth(ccMonth)){
                    Toast.makeText(getActivity(), R.string.invalid_credit_card_month, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Helpers.isValidYear(ccYear)){
                    Toast.makeText(getActivity(), R.string.invalid_credit_card_year, Toast.LENGTH_SHORT).show();
                    return;
                }

                ccValidity = ccMonth + "/" + ccYear;

                mCallback.register(ccNumber, ccType, ccValidity);

            }
        });


    }
}