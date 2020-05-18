package com.paril.mlaclientapp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.daimajia.swipe.util.Attributes;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAAdminDetails;
import com.paril.mlaclientapp.model.MLAFriendAddedData;
import com.paril.mlaclientapp.model.MLARegisterUsers;
import com.paril.mlaclientapp.model.MlAFriendUsers;
import com.paril.mlaclientapp.ui.activity.MLAHomeActivity;
import com.paril.mlaclientapp.ui.adapter.MLARegisteredUserAdapter;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.webservice.Api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MLAFriendViewFragment extends Fragment {

    List<MlAFriendUsers> userDetails = new ArrayList<>();
    ArrayList<Integer> userNames;
    ArrayList<String> aliasnames;
    String[] UserName;
    Integer[] UserId;
    String[] userType;
    MLARegisteredUserAdapter mlaUserDisplayAdapter;
    View view;
    ListView listViewStudent;
    Button btnAdd;
    MLARegisterUsers register;
    int loggedid;
    KeyStore keyStore;
    String GroupKey="b14ca5898a4e4133bbce2ea2315a1965",addername;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mla_friendslist, container, false);
        listViewStudent = (ListView) view.findViewById(R.id.mla_user_display_listView);
        listViewStudent.setEmptyView(view.findViewById(R.id.empty_text_view));
        btnAdd = (Button) view.findViewById(R.id.mla_user_btnEnroll);

        MLAGetUserInformationAPI getUserDetails = new MLAGetUserInformationAPI();
        Bundle bundle = getActivity().getIntent().getExtras();
        getUserDetails.execute(bundle.get("userName").toString(), bundle.get("userType").toString());

        MLAGetAllUserAPI mlagetalluserapi = new MLAGetAllUserAPI(this.getActivity());
        mlagetalluserapi.execute();

        try {
            System.out.print("here");
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            System.out.print("here");

        }
        catch(Exception e) {}




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mlaUserDisplayAdapter != null && mlaUserDisplayAdapter.getCount() > 0) {
                    // the userDisplayadapter include the boolean values and the userName of the student.
                    int listSize = mlaUserDisplayAdapter.getCount();//this is the list size
                    MlAFriendUsers userDisplayCheckbxProvider;
                    userNames = new ArrayList<>();
                    aliasnames=new  ArrayList<>();
                    for (int i = 0; i < listSize; i++) {
                        userDisplayCheckbxProvider = (MlAFriendUsers) mlaUserDisplayAdapter.getItem(i);
                        if (userDisplayCheckbxProvider.getCheck()) {
                            userNames.add(userDisplayCheckbxProvider.getUserId());
                            aliasnames.add(userDisplayCheckbxProvider.getUserName());
                        }
                    }
                    if (userNames.size() > 0) {
                        MLAAddFriendAPI addFriend = new MLAAddFriendAPI(MLAFriendViewFragment.this.getActivity());
                        addFriend.execute();
                    }

                }

            }
        });

        return view;
    }


    class MLAGetAllUserAPI extends AsyncTask<Void, Void, List<MlAFriendUsers>> {
        Context context;
        ArrayList<Integer> userNameData;

        public MLAGetAllUserAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userNameData = userNames;
            ((MLAHomeActivity) getActivity()).showProgressDialog("Fetching all Users...");
        }

        @Override
        protected void onPostExecute(List<MlAFriendUsers> apiResponse) {
            ((MLAHomeActivity) getActivity()).hideProgressDialog();
            if (apiResponse != null) {
                userDetails = apiResponse;
                UserId = new Integer[userDetails.size()];
                userType = new String[userDetails.size()];
                UserName = new String[userDetails.size()];

                for (int i = 0; i < userDetails.size(); i++) {
                    UserId[i] = userDetails.get(i).userId;
                    UserName[i] = userDetails.get(i).userName;
                    userType[i] = userDetails.get(i).userType;
                    // System.out.println("hiii" + " " + UserName[i] + " " + UserId[i] + " " + userType[i]);
                }
                List<MlAFriendUsers> listUserDisplayCheckb = new ArrayList<>();
                for (int i = 0; i < userDetails.size(); i++) {
                    final MlAFriendUsers usersDisplayProvider = new MlAFriendUsers(UserId[i], UserName[i], userType[i], false);
                    listUserDisplayCheckb.add(usersDisplayProvider);
                }

                mlaUserDisplayAdapter = new MLARegisteredUserAdapter(context, listUserDisplayCheckb);
                listViewStudent = (ListView) view.findViewById(R.id.mla_user_display_listView);
                listViewStudent.setAdapter(mlaUserDisplayAdapter);
            } else {

                ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.server_error), getView().findViewById(R.id.fragment_user_coordinatorLayout));
            }
        }

        @Override
        protected List<MlAFriendUsers> doInBackground(Void... params) {
            //register.getUserId(); loggedid
            System.out.println("check"+"   "+loggedid);

            try {

                Call<List<MlAFriendUsers>> callUserData = Api.getClient().getfriend(loggedid);
                Response<List<MlAFriendUsers>> responseUser = callUserData.execute();
                System.out.println("Response" + responseUser);
                if (responseUser.isSuccessful() && responseUser.body() != null) {
                    return responseUser.body();
                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
                return null;

            } catch (IOException e) {
                return null;
            }
        }

    }

    class MLAAddFriendAPI extends AsyncTask<Void, Void, String> {
        Context context;
        ArrayList<Integer> userNameData;
        ArrayList<String> names;

        public MLAAddFriendAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userNameData = userNames;
            names=aliasnames;
        }

        @Override
        protected void onPostExecute(String statusCode) {

            if (statusCode.equals("added")) //the item is created
            {
                MLAGetAllUserAPI mlaAPI = new MLAGetAllUserAPI(MLAFriendViewFragment.this.getActivity());
                mlaAPI.execute();
                ((MLAHomeActivity) getActivity()).showSnackBar("Friends added.", view.findViewById(R.id.fragment_user_coordinatorLayout));
            } else {
                ((MLAHomeActivity) getActivity()).showSnackBar("Error while adding friends.", view.findViewById(R.id.fragment_user_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            for (int i = 0; i < userNameData.size(); i++) {
                final MLAFriendAddedData postData = new MLAFriendAddedData();
                postData.setFriendUserId(userNameData.get(i));
                postData.setUserId(loggedid);



                String encryptid=names.get(i);
                String selfencryptid=addername;

                try {
                    Call <List<MLAFriendAddedData>> callEnrollSubjectData = Api.getClient().addfriend(postData.getUserId(), postData.getFriendUserId(), CommonUtils.encryptString(GroupKey,encryptid,keyStore));
                    Response <List<MLAFriendAddedData>> responseSubjectData = callEnrollSubjectData.execute();
                    Call <List<MLAFriendAddedData>> switchfriendadd = Api.getClient().addfriend(postData.getFriendUserId(), postData.getUserId(), CommonUtils.encryptString(GroupKey,selfencryptid,keyStore));
                    Response <List<MLAFriendAddedData>> responsefriend = switchfriendadd.execute();
                    System.out.println("response is:" + responseSubjectData);

                } catch (MalformedURLException e) {
                    return null;

                } catch (IOException e) {
                    return null;
                }
            }
            return "added";
        }
    }


    class MLAGetUserInformationAPI extends AsyncTask<String, Void, Void> {
        MLARegisterUsers UserDetails = new MLARegisterUsers();

        String userType;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Call<List<MLARegisterUsers>> callAdminData = Api.getClient().getuser(params[0]);
                Response<List<MLARegisterUsers>> responseAdminData = callAdminData.execute();
                if (responseAdminData.isSuccessful() && responseAdminData.body() != null) {
                    register = responseAdminData.body().get(0);
                    loggedid = Integer.parseInt(register.getUserId());
                    addername=register.getUserName();
                    System.out.println(register.getUserId());
                }

            } catch (MalformedURLException e) {
                return null;

            } catch (IOException e) {
                return null;
            }
            return null;
        }
    }
}

