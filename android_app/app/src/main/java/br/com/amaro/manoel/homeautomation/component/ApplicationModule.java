package br.com.amaro.manoel.homeautomation.component;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by manoel on 28/05/16.
 */

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
