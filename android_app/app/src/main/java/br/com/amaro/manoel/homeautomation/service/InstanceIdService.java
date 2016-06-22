package br.com.amaro.manoel.homeautomation.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by manoel on 28/05/16.
 */

public class InstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "INSTANCE_ID_SERVICE";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, refreshedToken);
    }
}
