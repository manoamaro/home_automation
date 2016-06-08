package br.com.amaro.manoel.homeautomation.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;

import javax.inject.Inject;

import br.com.amaro.manoel.homeautomation.MyApplication;

public class MqttService extends Service implements Listener, Callback<Void> {

    @Inject
    FirebaseRemoteConfig mRemoteConfig;
    @Inject
    FirebaseAuth mFirebaseAuth;

    private CallbackConnection mMqttConnection;
    private final MqttServiceBinder mBinder = new MqttServiceBinder();
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private Runnable onDisconnected;

    public MqttService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApplication) getApplication()).getComponent().inject(this);
        if (mFirebaseAuth.getCurrentUser() != null) {
            mRemoteConfig.fetch()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRemoteConfig.activateFetched();
                            connectMqtt(mRemoteConfig.getString("mqtt_server_url"), mFirebaseAuth.getCurrentUser().getUid());
                        }
                    });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMqttConnection.disconnect(null);
        return super.onUnbind(intent);
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

    public void publish(String topic, String value) {
        if (mMqttConnection != null) {
            mMqttConnection.publish(topic, value.getBytes(), QoS.AT_LEAST_ONCE, true, new Callback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }

                @Override
                public void onFailure(final Throwable throwable) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MqttService.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void subscribe(String topic, Callback<byte[]> callback) {
        if (mMqttConnection != null) {
            Topic[] topics = new Topic[] {
                    new Topic(topic, QoS.EXACTLY_ONCE)
            };
            mMqttConnection.subscribe(topics, callback);
        }
    }

    @Override
    public void onSuccess(Void aVoid) {
    }

    @Override
    public void onConnected() {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MqttService.this, "Connected to the Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnected() {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MqttService.this, "Disconnected to the Server", Toast.LENGTH_SHORT).show();
                if (MqttService.this.onDisconnected != null) {
                    MqttService.this.onDisconnected.run();
                }
            }
        });
    }

    @Override
    public void onPublish(UTF8Buffer utf8Buffer, Buffer buffer, Runnable runnable) {

    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void setOnDisconnected(Runnable onDisconnected) {
        this.onDisconnected = onDisconnected;
    }

    public class MqttServiceBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }
    }

}
