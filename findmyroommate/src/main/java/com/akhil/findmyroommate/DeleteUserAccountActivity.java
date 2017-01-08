package com.akhil.findmyroommate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by akhil on 12/29/2016.
 */
public class DeleteUserAccountActivity extends AppCompatActivity {

    private static final String MESSAGE = "Are you sure you want to delete this account?";
    private static final String CONFIRMATION = "Yes, delete it!";
    private static final String REJECTION = "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
        deleteAccount();
    }

    private void deleteAccount() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(MESSAGE)
                .setPositiveButton(CONFIRMATION, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserAccount();
                    }
                })
                .setNegativeButton(REJECTION, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(DeleteUserAccountActivity.this, UserProfileActivity.class));
                        finish();
                    }
                })
                .create();

        dialog.show();
    }

    private void deleteUserAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(DeleteUserAccountActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
    }
}
