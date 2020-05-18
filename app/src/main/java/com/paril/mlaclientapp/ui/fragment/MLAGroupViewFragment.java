package com.paril.mlaclientapp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.paril.mlaclientapp.R;

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

public class MLAGroupViewFragment extends Fragment {
    List<MlAFriendUsers> userDetails = new ArrayList<>();
    ArrayList<Integer> userNames;
    ArrayList<String> Gkeys,aliasNames;
    String[] UserName;
    Integer[] UserId;
    String[] userType;
    MLARegisteredUserAdapter mlaUserDisplayAdapter;
    View view;
    ListView listViewStudent;
    Button btnAdd;
    MLARegisterUsers register;
    int loggedid;
    EditText groupname;
    String gname;
    String groupKey,loggedusername;
    private static final String ALPHA_NUMERIC_STRING = "huhijnowm23p56kqkefdmc";
    KeyStore keyStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mla_groupslist, container, false);
        listViewStudent = (ListView) view.findViewById(R.id.mla_user_display_listView);
        listViewStudent.setEmptyView(view.findViewById(R.id.empty_text_view));
        btnAdd = (Button) view.findViewById(R.id.mla_user_btnEnroll);
        groupname=(EditText) view.findViewById(R.id.mla_subject_txtGroupName);

        MLAGetUserInformationAPI getUserDetails = new MLAGetUserInformationAPI();
        Bundle bundle = getActivity().getIntent().getExtras();
        getUserDetails.execute(bundle.get("userName").toString(), bundle.get("userType").toString());

        MLAGetAllGroupsAPI mlagetalluserapi= new MLAGetAllGroupsAPI(this.getActivity());
        mlagetalluserapi.execute();

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            // decryptString(iil);
        } catch (Exception e) {
            e.printStackTrace();

        }



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupname.getText().toString().isEmpty()){
                    ((MLAHomeActivity) getActivity()).showSnackBar("Groupname is mandatory.", getView().findViewById(R.id.fragment_update_password_coordinatorLayout));
                }
               else if (mlaUserDisplayAdapter != null && mlaUserDisplayAdapter.getCount() > 0) {
                    // the userDisplayadapter include the boolean values and the userName of the student.
                    int listSize = mlaUserDisplayAdapter.getCount();//this is the list size
                    MlAFriendUsers userDisplayCheckbxProvider;
                    userNames = new ArrayList<>();
                    aliasNames=new ArrayList<>();
                    for (int i = 0; i < listSize; i++) {
                        userDisplayCheckbxProvider = (MlAFriendUsers) mlaUserDisplayAdapter.getItem(i);
                        if (userDisplayCheckbxProvider.getCheck()) {
                            userNames.add(userDisplayCheckbxProvider.getUserId());
                            aliasNames.add(userDisplayCheckbxProvider.getUserName());
                        }
                    }

                    // if the students are selected from the list then call the API.
                    if(userNames.size()>0){
                        gname=groupname.getText().toString();
                        groupKey = DefaultKey(12);
                       MLAAddFriendAPI enrollStudent = new MLAAddFriendAPI(MLAGroupViewFragment.this.getActivity());
                        enrollStudent.execute();
                    } else{
                        ((MLAHomeActivity) getActivity()).showSnackBar("Select atleast one friend.", getView().findViewById(R.id.fragment_user_coordinatorLayout));
                    }


                }

            }
        });

        return view;
    }

    public static String DefaultKey(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    class MLAGetUserInformationAPI extends AsyncTask<String, Void, Void> {

        String userType;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
           // loggedid = Integer.parseInt(register.getUserId());

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Call<List<MLARegisterUsers>> callAdminData = Api.getClient().getuser(params[0]);
                Response<List<MLARegisterUsers>> responseAdminData = callAdminData.execute();
                if (responseAdminData.isSuccessful() && responseAdminData.body() != null) {
                    register = responseAdminData.body().get(0);
                    loggedid = Integer.parseInt(register.getUserId());
                    loggedusername=register.getUserName();
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

    class MLAGetAllGroupsAPI extends AsyncTask<Void, Void, List<MlAFriendUsers>> {
        Context context;
        ArrayList<Integer> userNameData;

        public MLAGetAllGroupsAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userNameData = userNames;
            ((MLAHomeActivity) getActivity()).showProgressDialog("Getting Friends...");
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

            try {

                Call<List<MlAFriendUsers>> callPostData = Api.getClient().getgroupfriends(loggedid);//lloggedid
                Response<List<MlAFriendUsers>> responseAdminUser = callPostData.execute();
                //System.out.println("response" +responseAdminUser);
                if (responseAdminUser.isSuccessful() && responseAdminUser.body() != null) {
                     System.out.println("response body:" +responseAdminUser);
                    return responseAdminUser.body();
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

        public MLAAddFriendAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userNameData = userNames;
        }

        @Override
        protected void onPostExecute(String statusCode) {

            if (statusCode.equals("added")) //the item is created
            { 
                groupname.setText("");
                MLAGetAllGroupsAPI mlaAPI = new MLAGetAllGroupsAPI(MLAGroupViewFragment.this.getActivity());
                mlaAPI.execute();
                ((MLAHomeActivity) getActivity()).showSnackBar("Group Created.", view.findViewById(R.id.fragment_user_coordinatorLayout));
            } else {
                ((MLAHomeActivity) getActivity()).showSnackBar("Error while adding friends.", view.findViewById(R.id.fragment_user_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Gkeys= new ArrayList<>();
            for (int i = 0; i < userNameData.size(); i++) {
                 System.out.println(userNameData.get(i));
                 String alias=aliasNames.get(i);
                 Gkeys.add(CommonUtils.encryptString(groupKey,alias,keyStore));
                 System.out.println("////////////"+Gkeys);
            }
                try {
                    Call <String> callEnrollSubjectData = Api.getClient().sendgroup(loggedid,gname,CommonUtils.encryptString(groupKey,loggedusername,keyStore), userNameData,Gkeys);
                    Response <String> responseSubjectData = callEnrollSubjectData.execute();
                    System.out.println("response is:" + responseSubjectData);

                } catch (MalformedURLException e) {
                    return null;

                } catch (IOException e) {
                    return null;
                }

            return "added";
        }
    }

}
