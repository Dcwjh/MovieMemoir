package com.monash.moviememoir.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.monash.moviememoir.R;
import com.monash.moviememoir.database.CustomerDatabase;
import com.monash.moviememoir.entity.Customer;

import java.util.List;

public class Crud extends AppCompatActivity {

    CustomerDatabase db = null;

    EditText editText = null;

    TextView textView_insert = null;

    TextView textView_read = null;

    TextView textView_delete = null;

    TextView textView_update = null;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crud);

        db = CustomerDatabase.getInstance(this);

        Button addButton = findViewById(R.id.addButton);

        editText = findViewById(R.id.editText);

        textView_insert = findViewById(R.id.textView);

        Button readButton = findViewById(R.id.readButton);

        textView_read = findViewById(R.id.textView_read);

        Button deleteButton = findViewById(R.id.deleteButton);

        textView_delete = findViewById(R.id.textView_delete);

        Button updateButton = findViewById(R.id.updateButton);

        textView_update = findViewById(R.id.textView_update);

        addButton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                if (!(editText.getText().toString().isEmpty())) {

//splitting three parts of the text based on the space between them

                    String[] details = editText.getText().toString().split(" ");

                    if (details.length == 3) {

                        InsertDatabase addDatabase = new InsertDatabase();
                        addDatabase.execute(details);

                    }

                }

            }

        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

//including onClick() method


            public void onClick(View v) {

                DeleteDatabase deleteDatabase = new DeleteDatabase();
                deleteDatabase.execute();

            }

        });

        readButton.setOnClickListener(new View.OnClickListener() {

//including onClick() method

            public void onClick(View v) {

                ReadDatabase readDatabase = new ReadDatabase();

                readDatabase.execute();
            }

        });

        updateButton.setOnClickListener(new View.OnClickListener() {

//including onClick() method

            public void onClick(View v) {

                String[] details= editText.getText().toString().split(" ");

                if (details.length==4) {

                    UpdateDatabase updateDatabase = new UpdateDatabase();

                    updateDatabase.execute(details);

                }

            }

        });

    }

    private class InsertDatabase extends AsyncTask<String, Void, String> {

        @Override

        protected String doInBackground(String... params) {

            Customer customer = new Customer(params[0], params[1],
                    Double.parseDouble(params[2]));

            long id = db.customerDao().insert(customer);

            return (id + " " + params[0] + " " + params[1] + " " +
                    params[2]);

        }

        @Override

        protected void onPostExecute(String details) {

            textView_insert.setText("Added Record: " + details);

        }
    }
    private class ReadDatabase extends AsyncTask<Void, Void, String> {

        @Override

        protected String doInBackground(Void... params) {

            List<Customer> users = db.customerDao().getAll();

            if (!(users.isEmpty() || users == null) ){

                String allUsers = "";

                for (Customer temp : users) {

                    String userstr = (temp.getId() + " " + temp.getFirstName() +
                            " " + temp.getLastName() + " "+ temp.getSalary()+ " , " );

                    allUsers = allUsers + System.getProperty("line.separator") +
                            userstr;

                }

                return allUsers;

            }

            else

                return "";

        }

        @Override

        protected void onPostExecute(String details) {

            textView_read.setText("All data: " + details);

        }
    }
    private class DeleteDatabase extends AsyncTask<Void, Void, Void> {

        @Override

        protected Void doInBackground(Void... params) {

            db.customerDao().deleteAll();

            return null;

        }

        protected void onPostExecute(Void param) {

            textView_delete.setText("All data was deleted");

        }
    }
    private class UpdateDatabase extends AsyncTask<String, Void, String> {

        @Override protected String doInBackground(String... params) {

            Customer user=null;
            int id = Integer.parseInt(params[0]);

            user = db.customerDao().findByID(id);

            if (user!=null) {

                user.setFirstName(params[1]);

                user.setLastName(params[2]);

                user.setSalary(new Double(params[3]).doubleValue());

                db.customerDao().updateCustomers(user);

                return (params[0] + " " + params[1] + " " + params[2] + " "+ params[3]);

            }

            return "";

        }

        @Override

        protected void onPostExecute(String details) {

            textView_update.setText("Updated details: "+ details);

        }
    }
}
