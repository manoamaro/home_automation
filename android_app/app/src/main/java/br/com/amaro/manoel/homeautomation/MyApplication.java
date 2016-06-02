package br.com.amaro.manoel.homeautomation;

import android.app.Application;

import javax.inject.Singleton;

import br.com.amaro.manoel.homeautomation.component.ApplicationModule;
import br.com.amaro.manoel.homeautomation.fragment.SignInFragment;
import dagger.Component;

/**
 * Created by manoel on 28/05/16.
 */

public class MyApplication extends Application {


    @Singleton
    @Component(modules = ApplicationModule.class)
    public interface ApplicationComponent {
        void inject(SignInFragment fragment);
    }


    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMyApplication_ApplicationComponent.builder()
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

}
