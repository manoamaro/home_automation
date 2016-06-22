package br.com.amaro.manoel.homeautomation;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Singleton;

import br.com.amaro.manoel.homeautomation.component.ApplicationModule;
import br.com.amaro.manoel.homeautomation.fragment.SignInFragment;
import br.com.amaro.manoel.homeautomation.service.MqttService;
import dagger.Component;

/**
 * Created by manoel on 28/05/16.
 */

public class MyApplication extends Application {


    @Singleton
    @Component(modules = ApplicationModule.class)
    public interface ApplicationComponent {
        void inject(SignInFragment fragment);
        void inject(MqttService service);
        void inject(AuthActivity activity);
    }

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMyApplication_ApplicationComponent.builder()
                .build();

        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true)
                .build());
    }

    public ApplicationComponent getComponent() {
        return component;
    }

}
