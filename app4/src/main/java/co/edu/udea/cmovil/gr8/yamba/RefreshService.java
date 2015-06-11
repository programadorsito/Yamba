package co.edu.udea.cmovil.gr8.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;

import java.util.List;

public class RefreshService extends IntentService {
    
    public static final String ACTION_FOO = "co.edu.udea.cmovil.gr8.yamba.action.FOO";
    public static final String ACTION_BAZ = "co.edu.udea.cmovil.gr8.yamba.action.BAZ";
    public static final String EXTRA_PARAM1 = "co.edu.udea.cmovil.gr8.yamba.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "co.edu.udea.cmovil.gr8.yamba.extra.PARAM2";
    private static final String TAG = "RefreshService";

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);

            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);

            }
        }

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String username=sharedPreferences.getString("username", "");
        String password=sharedPreferences.getString("password", "");
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) Toast.makeText(this, "actualice su informacion porfavor", Toast.LENGTH_SHORT);
        if(TextUtils.isEmpty(username))username="student";
        if(TextUtils.isEmpty(password))password="password";
        YambaClient cliente = new YambaClient(username, password);

        try {
            List<YambaClient.Status> timeline=cliente.getTimeline(20);
            for(YambaClient.Status status:timeline){
                Log.d(TAG, String.format("%s:%s", status.getUser(), status.getMessage()));
            }
        }catch(Exception e){
            Log.e(TAG, "Fallo en tomar la linea de tiempo");
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyd");
    }
}
