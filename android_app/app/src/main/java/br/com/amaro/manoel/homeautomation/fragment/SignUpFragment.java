package br.com.amaro.manoel.homeautomation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

import br.com.amaro.manoel.homeautomation.R;
import br.com.amaro.manoel.homeautomation.listener.FragmentInteractionListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment implements OnSuccessListener<AuthResult>, OnFailureListener {

    public static final String TAG = "SignUpFragment";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private FirebaseAuth mAuth;
    private FragmentInteractionListener mInteractionListener;
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @BindView(R.id.email)
    EditText mEmailInput;
    @BindView(R.id.password)
    EditText mPasswordInput;
    @BindView(R.id.email_wrapper)
    TextInputLayout mEmailTextInput;
    @BindView(R.id.password_wrapper)
    TextInputLayout mPasswordTextInput;


    public SignUpFragment() {
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener)
            mInteractionListener = (FragmentInteractionListener) context;
    }

    @OnClick(R.id.email_sign_up_button)
    public void onClickSignUp() {
        if (validateEmail() && validatePassword()) {
            mAuth.createUserWithEmailAndPassword(mEmailInput.getText().toString(),
                    mPasswordInput.getText().toString())
                    .addOnSuccessListener(this.getActivity(), this)
                    .addOnFailureListener(this.getActivity(), this);
        }
    }

    @OnClick(R.id.sign_in_text)
    public void onClickSignIn() {
        mInteractionListener.onFragmentInteraction("openSignIn");
    }

    private boolean validatePassword() {
        if (mPasswordInput.getText().length() < 6) {
            mPasswordTextInput.setErrorEnabled(true);
            mPasswordTextInput.setError(getString(R.string.error_invalid_password));
            return false;
        } else {
            mPasswordTextInput.setErrorEnabled(false);
            mPasswordTextInput.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        if (pattern.matcher(mEmailInput.getText().toString()).matches()) {
            mEmailTextInput.setErrorEnabled(false);
            mEmailTextInput.setError(null);
            return true;
        } else {
            mEmailTextInput.setErrorEnabled(true);
            mEmailTextInput.setError(getString(R.string.error_invalid_email));
            return false;
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this.getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        authResult.getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(null).build())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mInteractionListener.onFragmentInteraction("loginSuccess");
                    }
                });
    }
}
