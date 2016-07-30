package ejemplo.sergio.almacenamientoandroid;


import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 3;
    private static Button guardar;
    private static Button mostrar;
    private static EditText escribir;
    private static TextView ver;
    GoogleApiClient mGoogleApiClient;
    File tarjeta = Environment.getExternalStorageDirectory();
    File fichero = new File(tarjeta.getAbsolutePath(), "casa.txt");
//creamos el fichero

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();




        setContentView(R.layout.activity_main);
        guardar = (Button) findViewById(R.id.button);
        mostrar = (Button) findViewById(R.id.button2);
        escribir = (EditText) findViewById(R.id.editText);
        ver = (TextView) findViewById(R.id.textView);

        //cuando hagamos click en el boton guardar ejecutara el metodo guardar
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        //cuando hagamos click en el boton mostrara ejecutara el metodo recuperar
        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperar();
            }
        });


    }




    public void guardar() {

        String palabra = escribir.getText().toString();
        try {

            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(fichero,true));
            osw.write("\n"+palabra);
            osw.close();
            Toast.makeText(this, "guardado correctamente",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            Toast.makeText(this, "No se pudo guardar",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void recuperar() {


        try {
            FileInputStream fIn = new FileInputStream(fichero);
            InputStreamReader archivo = new InputStreamReader(fIn);

            //lectura del fichero
            BufferedReader br = new BufferedReader(archivo);

            String linea = br.readLine(); //lectura libea a linea
            String todo = "";

            //mientras se encuentre alguna palabra recorremos el  fichero.
            while (linea != null) {
                todo = todo + linea + " ";
                linea = br.readLine();
                //guaramos  la lectura
            }
            br.close();
            archivo.close();
            ver.setText(todo); //procedemos a mostrar la lectura en pantalla

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //parte de acceso a Google Drive

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {

            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }



    }





}



