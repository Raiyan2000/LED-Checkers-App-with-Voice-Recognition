package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String TAG_CONFIDENCE = "Confidence Level";
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    String temp = "";

    String readBitOne = "";
    String readBitTwo = "";

    CheckBox bluetoothConnection;

    String readBitThree = "";
    String readBitFour = "";
    String readbitFive = "";

    String player_winner = "";

    boolean receive_state = false;

    private TextView textViewuno;
    private Button voiceCmdButton;
    private Button voiceStopButton;

    private Button devbtn;
    private TextView commandOutput;

    TextView instructionOutput;


    private Button yourMom;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;

    IntentFilter intentFilter;

    static int count = 0;

    boolean choice;

    InputStream inputStream;
    OutputStream outputStream;

    RxThread rxThread;

    private Boolean isActivated = false;

    SpeechRecognizer sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        voiceCmdButton = findViewById(R.id.VoiceCmdbutton);
        voiceStopButton = findViewById(R.id.StopVoiceButton);
        commandOutput = findViewById(R.id.VoiceCommandtextView);
        //listeningProgressBar = findViewById(R.id.progressBar);
        devbtn = findViewById(R.id.button2);
        bluetoothConnection = findViewById(R.id.checkBox);

        yourMom = findViewById(R.id.connectbutton);

        //buttonsInUse = findViewById(R.id.returnbits);

        textViewuno = findViewById(R.id.textView7);
        instructionOutput = findViewById(R.id.textView8);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        final MediaPlayer anyBtn = MediaPlayer.create(this, R.raw.anybutton);

        rxThread = new RxThread();

        registerReceiver(Btreceiver, intentFilter);

        //bluetoothBtn.setEnabled(false);


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != (PackageManager.PERMISSION_GRANTED))
//        {
//            checkPermission();
//        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != (PackageManager.PERMISSION_GRANTED))
        {
            checkPermission();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());

        voiceCmdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anyBtn.start();
                //listeningProgressBar.setVisibility(View.VISIBLE);
                //listeningProgressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
                sr.startListening(intent);
            }
        });

        voiceStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anyBtn.start();
                Intent intent = new Intent(MainActivity.this, GameOver.class);
                startActivity(intent);
                finish();
            }
        });

        yourMom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anyBtn.start();
                Log.d(TAG, "onClickBluetooth: enabling/Disabling Bluetooth");
                //enableDisableBT();

                try{
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    bluetoothSocket.connect();
                    inputStream = bluetoothSocket.getInputStream();
                    outputStream = bluetoothSocket.getOutputStream();
                    rxThread.start();
                    Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_SHORT).show();
                    bluetoothConnection.setChecked(true);
                    bluetoothConnection.setText("Connected");

                }catch(Exception e)
                {
                    bluetoothConnection.setChecked(false);
                    bluetoothConnection.setText("Not Connected");
                }
            }
        });

        devbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anyBtn.start();
                Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice dev : devices) {
                    if (dev.getName().equals("HC-05")) {
                        bluetoothDevice = dev;
                        mBluetoothAdapter.cancelDiscovery();
                        break;
                    }
                }
            }
        });
    }

//    public void callspeechrecognizer()
//    {
//        //listeningProgressBar.setVisibility(View.VISIBLE);
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5);
//        //intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10);
//        //intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
//        sr.startListening(intent);
//    }

    class listener implements RecognitionListener
    {
        MediaPlayer beep = MediaPlayer.create(MainActivity.this, R.raw.beep);
        boolean isEndOfSpeech = false;
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
            instructionOutput.setText(temp + ": Ready to listen");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
            if(isActivated.equals(true))
            {
                instructionOutput.setText("Listening...");
            }
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            //isEndOfSpeech = true;
            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
//            if (!isEndOfSpeech)
//                return;
            Log.d(TAG,  "error " +  error);
            //voiceCmdButton.performClick();
            //instructionOutput.setText("Error" + error);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
            sr.startListening(intent);
        }
        public void onResults(Bundle results)
        {
            String str = new String();
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            str = (String) data.get(0);
            if(str != null)
            {
                if(isActivated) {
                    commandOutput.setText(str);
                    instructionOutput.setText("Your command is outputted");
                    isActivated = false;
//                    try {
//                        outputStream.write((commandOutput.getText() + "\r\n").getBytes());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//
//                    }
                }

                if (str.equals("ready"))
                {
                    isActivated = true;
                    beep.start();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
                    sr.startListening(intent);
                }

                if(isActivated != true)
                {
                    //voiceCmdButton.performClick();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
                    sr.startListening(intent);
                }
                else
                {
                    commandOutput.setText(str);
                }
//                float [] confidence = results.getFloatArray(sr.CONFIDENCE_SCORES);
//                Log.d(TAG_CONFIDENCE, String.valueOf(confidence[0]));
//                if (confidence != null){
//                    if (confidence.length > 0){
//                        Log.d(TAG_CONFIDENCE, String.valueOf(confidence[0]));
//                    } else {
//                        Log.d(TAG_CONFIDENCE + " confidence score not available", "unknown confidence");
//                    }
//                } else {
//                    Log.d(TAG_CONFIDENCE, "confidence not found");
//                }
            }
        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
            String str = new String();
            ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }

    private void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, (temp+" turn"));
        startActivityForResult(intent, 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final MediaPlayer recognize = MediaPlayer.create(this, R.raw.afterrecognizer);

        if (requestCode == 111 && resultCode == RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            commandOutput.setText(result.get(0));
            //listeningProgressBar.setVisibility(View.INVISIBLE);
            recognize.start();
            try {
                outputStream.write((commandOutput.getText() + "\r\n").getBytes());
            } catch (Exception e) {
            }
        }
    }


    /*
     *
     *
     *
     *
     *           BLUETOOOTTH IMPLEEMENTATION
     *
     *
     *
     *
     * */

//    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
//                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
//
//                switch (state) {
//                    case BluetoothAdapter.STATE_OFF:
//                        Log.d(TAG, "onReceive: STATE OFF");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
//                        break;
//                    case BluetoothAdapter.STATE_ON:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_ON:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
//                        break;
//                }
//            }
//        }
//    };

//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: called.");
//        super.onDestroy();
//        unregisterReceiver(mBroadcastReceiver1);
//    }
//
//    public void enableDisableBT() {
//        if (mBluetoothAdapter == null) {
//            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
//        }
//        if (!mBluetoothAdapter.isEnabled()) {
//            Log.d(TAG, "enableDisableBT: enabling BT.");
//            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
//            }
//            startActivity(enableBTIntent);
//
//            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//            registerReceiver(mBroadcastReceiver1, BTIntent);
//        }
//        if (mBluetoothAdapter.isEnabled()) {
//            Log.d(TAG, "enableDisableBT: disabling BT.");
//            mBluetoothAdapter.disable();
//
//            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//            registerReceiver(mBroadcastReceiver1, BTIntent);
//        }
//
//    }

    class RxThread extends Thread{

        public boolean isRunning;

        byte[] rx;

        RxThread()
        {
            isRunning = true;
            rx = new byte[10];

        }

        @Override
        public void run() {
            while(isRunning)
            {
                try{
                    if(inputStream.available() > 0)
                    {
                        inputStream.read(rx);
                        temp = new String(rx);
                        receive_state = true;
                        //textViewuno.setText(temp);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            if(temp != "")
                            {
//                                if (temp.substring(0, 5).equals("Islam")) {
//                                    Toast.makeText(getApplicationContext(), "Invalid Move", Toast.LENGTH_SHORT).show();
//                                    temp = "noooo";
//                                }

                                if(receive_state)
                                {
                                    readBitOne = temp.substring(0, 1);
                                    readBitTwo = temp.substring(1, 2);
                                    readBitThree = temp.substring(2,3);
                                    readBitFour = temp.substring(3, 4);
                                    readbitFive = temp.substring(4, 5);

                                    if(readBitThree.equals("0"))
                                    {
                                        //buttonsInUse.setText("Voice Command in use");
                                        if(readBitOne.equals("1"))
                                        {
                                            temp = "Player Blue";
                                            instructionOutput.setText("Player BLUE: Please voice your command");
                                            textViewuno.setTextColor(Color.parseColor("#ADD8E6"));
                                            textViewuno.setText(temp);
                                        }
                                        else {
                                            temp = "Player Red";
                                            instructionOutput.setText("Player RED: Please voice your command");
                                            textViewuno.setTextColor(Color.parseColor("#FF7276"));
                                            textViewuno.setText(temp);
                                        }


                                        if(readBitTwo.equals("0"))
                                        {
                                            Toast.makeText(getApplicationContext(), "Invalid Move", Toast.LENGTH_LONG).show();
                                            instructionOutput.setText(temp+ ": Invalid Move. Please try again in 5 seconds");
                                            try {
                                                Thread.sleep(3000);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
//                                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                                            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
//                                            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
//                                            sr.startListening(intent);
                                            //voiceCmdButton.performClick();
                                        }

                                        if(readBitFour.equals("1"))
                                        {
                                            receive_state = false;
                                            //buttonsInUse.setText("Red player winner");

                                            if(readbitFive.equals("1"))
                                            {
                                                player_winner = "red"; //Player red Won

                                            }
                                            else
                                            {
                                                player_winner = "Blue"; //Player blue Won
                                                //buttonsInUse.setText("Blue player winner");
                                            }

                                            Intent intent = new Intent(MainActivity.this, GameOver.class);
                                            intent.putExtra("message_key", player_winner);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    else
                                    {
                                        //buttonsInUse.setText("Array is in use");
                                    }

                                    receive_state = false;
                                }
                            }
                        }
                    });

                    Thread.sleep(10);

                }catch(Exception e){}

            }
        }
    }

    BroadcastReceiver Btreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //bluetoothBtn.setEnabled(true);
                            yourMom.setEnabled(true);
                        }
                    });
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    rxThread.isRunning = false;
                    break;
            }
        }
    };

    public void checkPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Permission need for your mom")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]  {Manifest.permission.RECORD_AUDIO}, SPEECH_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        }
        else {
            ActivityCompat.requestPermissions(this, new String[]  {Manifest.permission.RECORD_AUDIO}, SPEECH_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SPEECH_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT);
            }
        }
    }
}

