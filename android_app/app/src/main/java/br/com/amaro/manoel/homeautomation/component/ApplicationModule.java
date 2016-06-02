package br.com.amaro.manoel.homeautomation.component;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import javax.inject.Singleton;

import br.com.amaro.manoel.homeautomation.R;
import dagger.Module;
import dagger.Provides;

/**
 * Created by manoel on 28/05/16.
 */

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    static FirebaseRemoteConfig provideRemoteConfig() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        firebaseRemoteConfig.fetch()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });

        return firebaseRemoteConfig;
    }
}
