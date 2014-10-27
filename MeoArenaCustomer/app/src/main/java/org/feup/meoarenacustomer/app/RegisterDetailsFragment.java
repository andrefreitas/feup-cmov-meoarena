package org.feup.meoarenacustomer.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;


public class RegisterDetailsFragment extends Fragment {
    EditText nameEditText;
    EditText nifEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button continueRegistrationButton;
    String name;
    String email;
    String nif;
    String password;
    String confirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_details, container, false);

        continueRegistration(rootView);

        return rootView;
    }

    public void continueRegistration(View v){
        nameEditText = (EditText) v.findViewById(R.id.nameEditText);
        nifEditText = (EditText) v.findViewById(R.id.nifEditText);
        emailEditText = (EditText) v.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) v.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) v.findViewById(R.id.confirmPasswordEditText);
        continueRegistrationButton = (Button) v.findViewById(R.id.continueRegistrationButton);



        continueRegistrationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                name = nameEditText.getText().toString();
                nif = nifEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                if(!Helpers.isValidName(name)){
                    Toast.makeText(getActivity(), R.string.invalid_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Helpers.isValidNif(nif)){
                    Toast.makeText(getActivity(), R.string.invalid_nif, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Helpers.isValidEmail(email)) {
                    Toast.makeText(getActivity(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Helpers.isValidPassword(password)) {
                    Toast.makeText(getActivity(), R.string.invalid_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!confirmPassword.equals(password)){
                    Toast.makeText(getActivity(), R.string.invalid_confirm_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                getActivity()

            }
        });



    }
}