package com.emmanuelmuturia.mymoneyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
//testing
public class AddCategoryActivity extends AppCompatActivity {

    //We declare an EditText...
    private EditText addCat;
    private Button addBtn;

    private DatabaseReference categoryRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //Initializing the EditText

        mAuth = FirebaseAuth.getInstance();
        categoryRef = FirebaseDatabase.getInstance().getReference().child("categories").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        addCat = findViewById(R.id.input_category);
        addBtn = findViewById(R.id.btn_add_category);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = addCat.getText().toString();
                if(TextUtils.isEmpty(category)){
                    addCat.setError("Category Name required!");
                }else{
                    loader.setMessage("Adding Category");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = categoryRef.push().getKey();

                    Category category1 = new Category(category);
                    categoryRef.child(id).setValue(category1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddCategoryActivity.this, "Category Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddCategoryActivity.this, CategoryActivity.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(AddCategoryActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:12345678"));
            startActivity(intent);
        } else if (id == R.id.log_out) {
            mAuth.signOut();
            Intent intent = new Intent(AddCategoryActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}