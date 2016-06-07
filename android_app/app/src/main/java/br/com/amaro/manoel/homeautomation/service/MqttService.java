package br.com.amaro.manoel.homeautomation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;

import java.net.URISyntaxException;

import javax.inject.Inject;

import br.com.amaro.manoel.homeautomation.MyApplication;

public class MqttService extends Service implements Listener, Callback<Void> {

    @Inject
    FirebaseRemoteConfig mRemoteConfig;
    @Inject
    FirebaseAuth mFirebaseAuth;

    private CallbackConnection mMqttConnection;

    public MqttService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApplication) getApplication()).getComponent().inject(this);
        if (mFirebaseAuth.getCurrentUser() != null) {
            mRemoteConfig.fetch()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mRemoteConfig.activateFetched();
                            connectMqtt(mRemoteConfig.getString(""), mFirebaseAuth.getCurrentUser().getUid());
                        }
                    });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void connectMqtt(String host, String clientId) {
        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(host);
            mqtt.setClientId(clientId);
            mMqttConnection = mqtt.callbackConnection();
            mMqttConnection.listener(this);
            mMqttConnection.connect(this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Void aVoid) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onPublish(UTF8Buffer utf8Buffer, Buffer buffer, Runnable runnable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
