package br.com.amaro.manoel.homeautomation.fragment;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import br.com.amaro.manoel.homeautomation.MyApplication;
import br.com.amaro.manoel.homeautomation.R;
import br.com.amaro.manoel.homeautomation.listener.FragmentInteractionListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInFragment extends Fragment implements OnSuccessListener<AuthResult>, OnFailureListener {

    public static final String TAG = "SignInFragment";
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

    private ProgressDialog mAuthProgressDialog;


    public SignInFragment() {
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);

        getActivity().setProgressBarIndeterminateVisibility(true);

        mAuthProgressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        mAuthProgressDialog.setIndeterminate(true);
        mAuthProgressDialog.setMessage("Authenticating");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener)
            mInteractionListener = (FragmentInteractionListener) context;
    }

    @OnClick({R.id.email_sign_in_button, R.id.email_sign_up_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                if (validateEmail() && validatePassword()) {
                    mAuth.signInWithEmailAndPassword(mEmailInput.getText().toString(), mPasswordInput.getText().toString())
                            .addOnSuccessListener(this.getActivity(), this)
                            .addOnFailureListener(this.getActivity(), this);
                    mAuthProgressDialog.show();
                }
                break;
            case R.id.email_sign_up_button:
                mInteractionListener.onFragmentInteraction("openSignUp");
                break;
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        mAuthProgressDialog.dismiss();
        Toast.makeText(this.getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        mAuthProgressDialog.dismiss();
        Toast.makeText(this.getActivity(), "Login success", Toast.LENGTH_LONG).show();
        mInteractionListener.onFragmentInteraction("loginSuccess");
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
}
