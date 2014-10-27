package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterCreditCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterCreditCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterCreditCardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_credit_card, container, false);

        return rootView;
    }
}