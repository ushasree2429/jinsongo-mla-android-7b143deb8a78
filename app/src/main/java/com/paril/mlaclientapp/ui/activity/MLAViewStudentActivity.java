package com.paril.mlaclientapp.ui.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.paril.mlaclientapp.model.MLAAdminDetails;
import com.github.reinaldoarrosi.maskededittext.MaskedEditText;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAStudentDetails;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.webservice.Api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by paril on 7/25/2017.
 */

public class MLAViewStudentActivity extends BaseActivity {
    private MLAStudentDetails userDetails;
    TextView txtUserId;
    EditText txtUserName;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmailId;
    MaskedEditText txtTelephone;
    EditText txtAliasMailId;
    EditText txtAddress;
    EditText txtHangoutId;
    EditText txtPassword;
    String groupKey,encryptedText;
    private static final String ALPHA_NUMERIC_STRING = "b14ca5898a4e4133bbce2ea2315a1965";
    private boolean isToAdd = false;

    private boolean enabledEditMode = false;
    private LinearLayout linUserIdCont, linUserNameCont, linPasswordCont;

    static final String TAG = "SimpleKeystoreApp";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    String alias ;
    KeyStore keyStore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_studentdisplay);
        isToAdd = getIntent().getBooleanExtra(CommonUtils.EXTRA_IS_TO_ADD, false);

        txtPassword = (EditText) findViewById(R.id.mla_student_txtPassword);
        txtUserId = (TextView) findViewById(R.id.mla_student_txtUserId);
        txtUserName = (EditText) findViewById(R.id.mla_student_txtUserName);
        txtFirstName = (EditText) findViewById(R.id.mla_student_txtFirstName);
        txtLastName = (EditText) findViewById(R.id.mla_student_txtLastName);
        txtEmailId = (EditText) findViewById(R.id.mla_student_txtEmailId);
        txtTelephone = (MaskedEditText) findViewById(R.id.mla_student_txtTelephone);
        txtAliasMailId = (EditText) findViewById(R.id.mla_student_txtAliasMailId);
        txtAddress = (EditText) findViewById(R.id.mla_student_txtAddress);
        txtHangoutId = (EditText) findViewById(R.id.mla_student_txtHangoutId);
        linUserIdCont = (LinearLayout) findViewById(R.id.activity_view_admin_linUserId);
        linUserNameCont = (LinearLayout) findViewById(R.id.activity_view_admin_linUserName);
        linPasswordCont = (LinearLayout) findViewById(R.id.activity_view_admin_linPassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (!isToAdd) {
            userDetails = (MLAStudentDetails) getIntent().getSerializableExtra(CommonUtils.EXTRA_USER_ADMIN_DATA);
            setToolbarTitle(userDetails.getFirstName() + " " + userDetails.getLastName());
            setUpData();
            enabledEditMode = getIntent().getBooleanExtra(CommonUtils.EXTRA_EDIT_MODE, false);
            enableFields(enabledEditMode);
            linUserIdCont.setVisibility(View.VISIBLE);
            linUserNameCont.setVisibility(View.VISIBLE);
            linPasswordCont.setVisibility(View.GONE);
        } else {
            setToolbarTitle("Add Student");
            linUserIdCont.setVisibility(View.GONE);
            txtUserName.setEnabled(true);
            linUserNameCont.setVisibility(View.VISIBLE);
            linPasswordCont.setVisibility(View.VISIBLE);
        }
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        }
        catch(Exception e) {}


    }

    private void setUpData() {
        txtUserId.setText("" + userDetails.getUserId());
        txtUserName.setText(userDetails.getIdStudent());
        txtFirstName.setText(userDetails.getFirstName());
        txtLastName.setText(userDetails.getLastName());
        txtEmailId.setText(userDetails.getEmailId());
        txtTelephone.setText(userDetails.getTelephone());
        txtAliasMailId.setText(userDetails.getAliasMailId());
        txtAddress.setText(userDetails.getAddress());
        txtHangoutId.setText(userDetails.getSkypeId());
    }
    public void createNewKeys() {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);
                KeyPair keyPair = generator.generateKeyPair();
                System.out.println("keypair"+keyPair);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void enableFields(boolean makeEditable) {
        txtAddress.setEnabled(makeEditable);
        txtAliasMailId.setEnabled(makeEditable);
        txtEmailId.setEnabled(makeEditable);
        txtFirstName.setEnabled(makeEditable);
        txtLastName.setEnabled(makeEditable);
        txtHangoutId.setEnabled(makeEditable);
        txtTelephone.setEnabled(makeEditable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isToAdd) {
            if (enabledEditMode) {
                menu.findItem(R.id.menu_edit).setVisible(false);
                menu.findItem(R.id.menu_cancel).setVisible(true);
                menu.findItem(R.id.menu_save).setVisible(true);
            } else {
                menu.findItem(R.id.menu_edit).setVisible(true);
                menu.findItem(R.id.menu_cancel).setVisible(false);
                menu.findItem(R.id.menu_save).setVisible(false);
            }
        } else {
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_cancel).setVisible(false);
            menu.findItem(R.id.menu_save).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_edit) {
            enabledEditMode = true;
            invalidateOptionsMenu();
            enableFields(enabledEditMode);
        } else if (item.getItemId() == R.id.menu_cancel) {
            enabledEditMode = false;
            invalidateOptionsMenu();
            enableFields(enabledEditMode);
            setUpData();
        } else if (item.getItemId() == R.id.menu_save) {

            hideKeyboard();
            if (isToAdd) {
                if (TextUtils.isEmpty(txtAddress.getText().toString()) ||
                        TextUtils.isEmpty(txtUserName.getText().toString()) ||
                        TextUtils.isEmpty(txtEmailId.getText().toString()) ||
                        TextUtils.isEmpty(txtAliasMailId.getText().toString()) ||
                        TextUtils.isEmpty(txtLastName.getText().toString()) ||
                        TextUtils.isEmpty(txtTelephone.getText(true).toString()) ||
                        TextUtils.isEmpty(txtHangoutId.getText().toString()) ||
                        TextUtils.isEmpty(txtFirstName.getText().toString()) ||
                        TextUtils.isEmpty(txtPassword.getText().toString())) {
                    showSnackBar(getString(R.string.enter_all_fields), findViewById(R.id.activity_view_student_coordinatorLayout));

                } else if (!CommonUtils.isValidMail(txtEmailId.getText().toString()) ||
                        !CommonUtils.isValidMail(txtAliasMailId.getText().toString()) ||
                        !CommonUtils.isValidMobile(txtTelephone.getText(true).toString())) {
                    if (!CommonUtils.isValidMail(txtEmailId.getText().toString())) {
                        txtEmailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));
                    } else if (!CommonUtils.isValidMail(txtAliasMailId.getText().toString())) {
                        txtAliasMailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_alt_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));
                    } else {
                        txtTelephone.requestFocus();
                        showSnackBar(getString(R.string.invalid_phone_no), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }
                } else {
                    if (CommonUtils.checkInternetConnection(MLAViewStudentActivity.this)) {
                        MLAAddStudentAPI addAdminTask = new MLAAddStudentAPI(MLAViewStudentActivity.this);
                        addAdminTask.execute();
                    } else {
                        showSnackBar(getString(R.string.check_connection), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }
                }

            } else {

                if (TextUtils.isEmpty(txtAddress.getText().toString()) ||
                        TextUtils.isEmpty(txtUserName.getText().toString()) ||
                        TextUtils.isEmpty(txtEmailId.getText().toString()) ||
                        TextUtils.isEmpty(txtAliasMailId.getText().toString()) ||
                        TextUtils.isEmpty(txtLastName.getText().toString()) ||
                        TextUtils.isEmpty(txtTelephone.getText(true).toString()) ||
                        TextUtils.isEmpty(txtHangoutId.getText().toString()) ||
                        TextUtils.isEmpty(txtFirstName.getText().toString()) ) {
                    showSnackBar(getString(R.string.enter_all_fields), findViewById(R.id.activity_view_student_coordinatorLayout));
                } else if (!CommonUtils.isValidMail(txtEmailId.getText().toString()) ||
                        !CommonUtils.isValidMail(txtAliasMailId.getText().toString()) ||
                        !CommonUtils.isValidMobile(txtTelephone.getText(true).toString())) {
                    if (!CommonUtils.isValidMail(txtEmailId.getText().toString())) {
                        txtEmailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));

                    } else if (!CommonUtils.isValidMail(txtAliasMailId.getText().toString())) {
                        txtAliasMailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_alt_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));
                    } else {
                        txtTelephone.requestFocus();
                        showSnackBar(getString(R.string.invalid_phone_no), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }

                } else {
                    if (CommonUtils.checkInternetConnection(MLAViewStudentActivity.this)) {
                        MLAUpdateStudentAPI updateAdminTask = new MLAUpdateStudentAPI(MLAViewStudentActivity.this);
                        updateAdminTask.execute();
                    } else {
                        showSnackBar(getString(R.string.check_connection), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class MLAUpdateStudentAPI extends AsyncTask<Void, Void, String> {
        Context context;
        private MLAStudentDetails details;

        public MLAUpdateStudentAPI(Context ctx) {
            context = ctx;
            details = new MLAStudentDetails();
            details.setAddress(txtAddress.getText().toString());
            details.setAliasMailId(txtAliasMailId.getText().toString());
            details.setEmailId(txtEmailId.getText().toString());
            details.setFirstName(txtFirstName.getText().toString());
            details.setIdStudent(txtUserName.getText().toString());
            details.setLastName(txtLastName.getText().toString());
            details.setSkypeId(txtHangoutId.getText().toString());
            details.setTelephone(txtTelephone.getText(true).toString());
            details.setUserId(Integer.parseInt(txtUserId.getText().toString()));
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("Update Student User Data...");
        }

        @Override
        protected void onPostExecute(String statusCode) {
            hideProgressDialog();
            if (statusCode.equals("202")) //the item is updated
            {
                finish();
            } else {
                showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_instructor_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Call<String> callUpdate = Api.getClient().updateStudent(details);
            try {
                Response<String> respUpdate = callUpdate.execute();
                return "202";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }


    class MLAAddStudentAPI extends AsyncTask<Void, Void, String> {
        Context context;
        MLAStudentDetails userDetails = new MLAStudentDetails();

        public MLAAddStudentAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userDetails.setTelephone(txtTelephone.getText(true).toString());
            userDetails.setIdStudent(txtUserName.getText().toString());
            userDetails.setSkypeId(txtHangoutId.getText().toString());
            userDetails.setLastName(txtLastName.getText().toString());
            userDetails.setAddress(txtAddress.getText().toString());
            userDetails.setAliasMailId(txtAliasMailId.getText().toString());
            userDetails.setFirstName(txtFirstName.getText().toString());
            userDetails.setEmailId(txtEmailId.getText().toString());
            userDetails.setPassword(txtPassword.getText().toString());
            try {
                alias=(userDetails.getIdStudent());
                createNewKeys();
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
                RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
                userDetails.setPublicKey(publicKey.getEncoded().toString());
                groupKey =ALPHA_NUMERIC_STRING ;   // For group generation
                encryptString(groupKey);
                userDetails.setGroupKey(encryptedText); //check this
            }
            catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            showProgressDialog("Add Student User Data...");
        }

        @Override
        protected void onPostExecute(String statusCode) {
            hideProgressDialog();
            if (statusCode.equals("201")) //the item is created
            {
                finish();
            } else {
                showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_student_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Call<MLAStudentDetails> callAddStudent = Api.getClient().addStudent(userDetails.getIdStudent(), userDetails.getPassword(), userDetails.getFirstName(), userDetails.getLastName(), userDetails.getTelephone(), userDetails.getAddress(), userDetails.getAliasMailId(), userDetails.getEmailId(), userDetails.getSkypeId(),userDetails.getPublicKey(),userDetails.getGroupKey());
            try {
                Response<MLAStudentDetails> respCallAdmin = callAddStudent.execute();
                return "" + respCallAdmin.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public void encryptString(String text) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            String initialText = text;
            if(initialText.isEmpty()) {
                Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                return;
            }

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] vals = outputStream.toByteArray();
            encryptedText = Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        System.out.println("EncryptedText"+encryptedText);
        //decryptString(encryptedText);
    }

}
