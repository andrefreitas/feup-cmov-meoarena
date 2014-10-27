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

public class RegisterCreditCardFragment extends Fragment {
    EditText numberEditText;
    ImageView typeImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_credit_card, container, false);

        updateNumber(rootView);

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


                Log.d("RegisterCreditCardFragment", s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

    }
}