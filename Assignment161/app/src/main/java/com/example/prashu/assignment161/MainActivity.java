package com.example.prashu.assignment161;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textview;
    EditText edittext;
    Button send , delete;
    String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (Button)findViewById(R.id.ok);  // initialization of all varoables
        delete = (Button)findViewById(R.id.delete);
        edittext = (EditText)findViewById(R.id.editText);
        textview = (TextView)findViewById(R.id.textView);
        send.setOnClickListener(this);
        delete.setOnClickListener(this);  //setting the onclick listeners of add and delete bittons

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ok){  //if the add button is clicked
               data = edittext.getText().toString();    //get the value from edit text and pass into the variable
               new getData().execute(data,String.valueOf(1));  //pass the data into get data method along with the write request
        }

        if(view.getId() == R.id.delete){  //if the delete button is clicked then
               new getData().execute("",String.valueOf(2));  //pass empty string along with the delete request
        }

    }

    private class getData extends AsyncTask<String,String ,String >{  //get data method which extends async task

        String data = null;
        int received = 0;
        File file;
        String string;

        @Override
        protected String doInBackground(String... strings) {

            data = strings[0];
            received = Integer.parseInt(strings[1]);
            file = createFile();
            string = readFile(file);

            if (!data.isEmpty() && received == 1) {
  //write data into the file
                writeFile(file, data + "\n");
                string = readFile(file); //read from the file and return that string
                return string;

            } else if (received == 2) {
                // delete the file.
                deletetheFile(file);

            } else if (data.isEmpty()) {
                string = readFile(file);  //if the data is empty then return empty string
                return "";
            }

            return "kabskbckjb";
        }

        private void writeFile(File file, String s) {
            if (!file.exists())
                file = createFile();
            try {
                // use FileOutputStream to enter data in file.
                FileOutputStream f = new FileOutputStream(file, true);
                f.write(s.getBytes());
                f.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String readFile(File file) {
            String s = "";
            try {

                FileInputStream fileInputStream = new FileInputStream(file);  //create object for file input stream
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    s = s + strLine + "\n";
                }
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        private void deletetheFile(File file) {

            if (file.exists())
                file.delete();
        }

        private File createFile() {
            File root = android.os.Environment.getExternalStorageDirectory();
            File folder = new File(root.getAbsolutePath() + "/folder");
            folder.mkdirs();
            File newFile = new File(folder, "test.txt");
            return newFile;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            textview.setVisibility(View.VISIBLE);
            if (s.equals(""))
                Toast.makeText(MainActivity.this, "Empty String", Toast.LENGTH_SHORT).show();
            else if (s.equals("kabskbckjb"))
                textview.setVisibility(View.INVISIBLE);
            textview.setText(string);
            edittext.setText("");
        }
    }
}
