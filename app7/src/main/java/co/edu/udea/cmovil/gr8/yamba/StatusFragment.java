package co.edu.udea.cmovil.gr8.yamba;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "StatusActivity";

    private EditText editStatus;
    private Button buttonTweet;
    private TextView textCount;
    private int defaultTextColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_status, container, false);
        editStatus = (EditText) view.findViewById(R.id.editStatus);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        textCount = (TextView) view.findViewById(R.id.textCount);

        buttonTweet.setOnClickListener(this);

        buttonTweet.setEnabled(false);
        defaultTextColor = textCount.getTextColors().getDefaultColor();
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - editStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.GREEN);
                buttonTweet.setEnabled(count != 0);

                if (count < 50)
                    textCount.setTextColor(Color.RED);
                else
                    textCount.setTextColor(defaultTextColor);
            }
        });

        buttonTweet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(StatusFragment.this.getActivity(), "has hecho un largo click", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        Log.d(TAG, "ha pasado por el metodo onCreate");

        return view;
    }

    @Override
    public void onCreate(Bundle estado) {
        super.onCreate(estado);
        Toast.makeText(StatusFragment.this.getActivity(), "onCreate llamado en el fragment 1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(StatusFragment.this.getActivity(), "onPause llamado en el fragment1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(StatusFragment.this.getActivity(), "onResume llamado en el fragment1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(StatusFragment.this.getActivity(), "onStop llamado en el fragment1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(StatusFragment.this.getActivity(), "onDestroy llamado en el fragment1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        Toast.makeText(StatusFragment.this.getActivity(), "onDestroy llamado en el fragment1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        String estado = editStatus.getText().toString();
        new PostTask().execute(estado);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editStatus.getWindowToken(), 0);
        editStatus.setText("");
    }

    private final class PostTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                String username=sharedPreferences.getString("username", "");
                String password=sharedPreferences.getString("password", "");
                if(TextUtils.isEmpty(username))username="student";
                if(TextUtils.isEmpty(password))password="password";
                YambaClient cliente = new YambaClient(username, password);
                cliente.postStatus(params[0]);
                return "Se posteo bien";
            } catch (YambaClientException ex) {
                ex.printStackTrace();
                Toast.makeText(getActivity(), "Error al enviar, posiblemente no se haya podido establecer la conexion" , Toast.LENGTH_LONG).show();
                Log.e(TAG, "error al enviar");
                return "Fallo al postear";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(StatusFragment.this.getActivity(), s, Toast.LENGTH_LONG).show();
        }
    }
}
