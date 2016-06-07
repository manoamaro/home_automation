package br.com.amaro.manoel.homeautomation.component;

import com.google.firebase.auth.FirebaseAuth;
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
        return firebaseRemoteConfig;
    }

    @Provides
    @Singleton
    static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
