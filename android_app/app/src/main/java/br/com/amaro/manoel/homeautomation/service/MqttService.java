package br.com.amaro.manoel.homeautomation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class MqttService extends Service {

    @Inject
    FirebaseAuth mAuth;

    public MqttService() {
    }
    

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
