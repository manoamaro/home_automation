package br.com.amaro.manoel.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.amaro.manoel.homeautomation.fragment.SignInFragment;
import br.com.amaro.manoel.homeautomation.fragment.SignUpFragment;
import br.com.amaro.manoel.homeautomation.listener.FragmentInteractionListener;

/**
 * Created by manoel on 28/05/16.
 */

public class LoginActivity extends AppCompatActivity implements FragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, SignInFragment.newInstance(), SignInFragment.TAG)
                .commit();

    }

    @Override
    public void onFragmentInteraction(String uri) {
        switch (uri) {
            case "openSignUp":
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SignUpFragment.newInstance(), SignUpFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                break;
            case "openSignIn":
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SignInFragment.newInstance(), SignInFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                break;
            case "loginSuccess":
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
