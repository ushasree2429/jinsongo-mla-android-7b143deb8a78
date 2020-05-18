package com.paril.mlaclientapp.webservice;

import com.paril.mlaclientapp.model.MLAAdminDetails;
import com.paril.mlaclientapp.model.MLAFriendAddedData;
import com.paril.mlaclientapp.model.MLAGradeTask;
import com.paril.mlaclientapp.model.MLAGroupDetails;
import com.paril.mlaclientapp.model.MLAInstructorDetails;
import com.paril.mlaclientapp.model.MLAPostDetails;
import com.paril.mlaclientapp.model.MLARegisterUsers;
import com.paril.mlaclientapp.model.MLAScheduleDetailPostData;
import com.paril.mlaclientapp.model.MLAStudentDetails;
import com.paril.mlaclientapp.model.MLAStudentEnrollmentPostData;
import com.paril.mlaclientapp.model.MLASubjectDetails;
import com.paril.mlaclientapp.model.MLATaskDetails;
import com.paril.mlaclientapp.model.MlAAddPostDetails;
import com.paril.mlaclientapp.model.MlAFriendUsers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("api/Register/GetRegisterAuth")
    Call<List<MLARegisterUsers>> authenticate(@Query("userName") String userName, @Query("password") String password);

    @GET("api/Register/GetRegisterByUserName")
    Call<List<MLARegisterUsers>> getuser(@Query("userName") String userName);

    @GET("api/Admin/GetAdminByUserName")
    Call<List<MLAAdminDetails>> getAdminInfo(@Query("userName") String userName);

    @GET("api/AddPost")
    Call<List<MLAPostDetails>> getPost(@Query("userId") int userId);


    @GET("api/Student/GetStudentByUserName")
    Call<List<MLAStudentDetails>> getStudentInfo(@Query("userName") String userName);


    @GET("api/Instructor/GetInstructorByUserName")
    Call<List<MLAInstructorDetails>> getInstInfo(@Query("userName") String userName);

    @GET("api/Register/GetAllRegister")
    Call<List<MlAFriendUsers>> getRegUsers();


    @GET("api/Admin/GetAllAdmin")
    Call<List<MLAAdminDetails>> getAdminUsers();

    @POST("api/DeleteAdmin/PostAdminRmv")
    Call<String> removeAdmin(@Query("idAdminRmv") String adminUserName);

    @POST("api/Register/PostAddAdmin")
    Call<MLAAdminDetails> addAdmin(@Query("adminUserName") String adminUserName, @Query("adminPassword") String adminPassword, @Query("adminFirsName") String adminFirsName, @Query("adminLastName") String adminLastName, @Query("adminTelephone") String adminTelephone, @Query("adminAddress") String adminAddress, @Query("adminAliasMailId") String adminAliasMailId, @Query("adminEmailId") String adminEmailId, @Query("adminSkypeId") String adminSkypeId, @Query("publicKey") String publicKey, @Query("groupKey") String groupKey );

    @POST("api/Admin/PostAdminUpdate")
    Call<String> updateAdmin(@Body MLAAdminDetails userDetails);


    @GET("api/Instructor/GetAllInstructor")
    Call<List<MLAInstructorDetails>> getInstructors();

    @POST("api/DeleteInstructor/PostInstructorRmv")
    Call<String> removeInstructor(@Query("idInstructorRmv") String idInstructorRmv);

    @POST("api/Register/PostAddInstructor")
    Call<MLAInstructorDetails> addInst(@Query("instUserName") String userName, @Query("instPassword") String password, @Query("instFirsName") String instFirsName, @Query("instLastName") String instLastName, @Query("instTelephone") String instTelephone, @Query("instAddress") String instAddress, @Query("instAliasMailId") String instAliasMailId, @Query("instEmailId") String instEmailId, @Query("instSkypeId") String instSkypeId, @Query("publicKey") String publicKey,@Query("groupKey") String groupKey);

    @POST("api/Instructor/PostInstructorUpdate/")
    Call<String> updateInstructor(@Body MLAInstructorDetails userDetails);


    @GET("api/Student/GetAllStudent")
    Call<List<MLAStudentDetails>> getStudents();

    @GET("api/Subject/GetSubjectByStudent")
    Call<ArrayList<MLASubjectDetails>> getSubjForStudent(@Query("idStudent") String idStudent);

    @POST("api/DeleteStudent/PostStudentRmv")
    Call<String> removeStudent(@Query("idStudentRmv") String idInstructorRmv);

    @POST("api/Register/PostAddStudent")
    Call<MLAStudentDetails> addStudent(@Query("userName") String userName, @Query("password") String password, @Query("firsName") String instFirsName, @Query("lastName") String instLastName, @Query("telephone") String instTelephone, @Query("address") String instAddress, @Query("aliasMailId") String instAliasMailId, @Query("emailId") String instEmailId, @Query("skypeId") String instSkypeId,@Query("publicKey") String publicKey,@Query("groupKey") String groupKey );

    @POST("api/Student/PostStudentUpdate/")
    Call<String> updateStudent(@Body MLAStudentDetails userDetails);

    @POST("api/Register/PostRegisterPassUpdate")
    Call<String> changePassword(@Query("userName") String userName, @Query("password") String password);


    @GET("api/Subject/GetAllSubject")
    Call<List<MLASubjectDetails>> getAllSubject();

    @GET("api/Subject/GetAllSubjectWithTask")
    Call<List<MLASubjectDetails>> getAllSubjectWithTask(@Query("flag") String subjectId);

    @POST("api/Subject/PostSubject")
    Call<MLASubjectDetails> addSubject(@Body MLASubjectDetails subjectDetails);

    @POST("api/SubjectUpdate/PostSubjectUpdate")
    Call<String> updateSubject(@Body MLASubjectDetails subjectDetails);


    @POST("api/SubjectRmv/PostSubjectRmv")
    Call<String> removeSubject(@Query("subject_id") String idSubject);

    @GET("api/DeEnrollStudent/GetDeEnrollBySubject")
    Call<List<MLAStudentDetails>> getDeEnrollBySub(@Query("idSubject") String subjectId);


    @GET("api/EnrollStudent/GetEnrollBySubject")
    Call<List<MLAStudentDetails>> getEnrollBySub(@Query("idSubject") String subjectId);

    @POST("api/EnrollStudent/PostEnrollStudent")
    Call<MLAStudentEnrollmentPostData> enrollBySub(@Body MLAStudentEnrollmentPostData listStudentData);

    @POST("api/AddFriend/Post")
    Call<List<MLAFriendAddedData>> addfriend(@Query("userId") int userId,@Query("FriendUserId") int FriendUserId,@Query("groupkey") String groupkey);

    @GET("api/AddFriend/GetFriend")
    Call<List<MlAFriendUsers>> getfriend(@Query("userId") int userId);

    @GET("api/Group/GetFriendsList")
    Call<List<MlAFriendUsers>> getgroupfriends(@Query("userId") int userId);

    @POST("api/Group2/GroupList")
    Call<List<MLAGroupDetails>> getgroups(@Query("loggedid") int loggedid);

    @GET("api/ExitGroup/GetListForDelete")
    Call<List<MLAGroupDetails>> getdeletegroups(@Query("userId") int userId);

    @POST("api/ExitGroup/RemoveUser")
    Call<String> ExitGroup(@Query("userId") int userId,@Query("groupId") int groupId);

    @GET("api/Group2/GetSelfGid")
    Call<List<MLAGroupDetails>> getselfgroups(@Query("userId") int userId);

    @GET("api/Group2/GetGroupKey")
    Call<List<MLAGroupDetails>> getGroupKey(@Query("groupId") int groupId,@Query("userId") int userId);


    @POST("api/Group/CreateGroup")
    @FormUrlEncoded
    Call<String>  sendgroup(@Field("userId") int userId,@Field("groupName") String groupName,@Field("groupKey") String groupKey,@Field("friend_ids")  ArrayList<Integer> friend_ids,@Field("group_keys") ArrayList<String> group_keys);

    @POST("api/AddPost/Post")
    Call<List<MlAAddPostDetails>> callAddPost(@Query("post") String post,@Query("groupId") int groupId,@Query("userId") int userId,@Query("postType") String postType,@Query("SessionKey") String SessionKey,@Query("userName") String userName,@Query("digSig") String digSig);


    @POST("api/DeEnrollStudent/PostDeEnroll")
    Call<MLAStudentEnrollmentPostData> deEnrollBySub(@Body MLAStudentEnrollmentPostData listStudentData);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("api/EnrollStudent/GetSubjectByStd")
    Call<ArrayList<MLASubjectDetails>> getEnrolledSubjectForStudent(@Query("idStudent") String idStudent);

    @POST("api/Tasks/PostTask/")
    Call<String> addSchedule(@Body MLAScheduleDetailPostData details);


    @POST("api/ScheduleRmv/PostTaskRmv")
    Call<String> removeTasks(@Query("subject_id") String subjectId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("api/Tasks/PostTaskUpdate")
    Call<String> updateTaskData(@Query("idTask") String idTask, @Query("topic") String topic, @Query("description") String desc);


    @GET("api/UserTasks/GetTasksByUser")
    Call<List<MLATaskDetails>> getTasksByUser(@Query("userId") String userName, @Query("userType") String userType);


    @GET("api/Tasks/GetTasksBySubject")
    Call<List<MLATaskDetails>> getTasksBySubject(@Query("subjectId") String subjectId);

    @GET("api/Tasks/GetStudentByTask")
    Call<List<MLAGradeTask>> getGrades(@Query("task") String task, @Query("subjectid") String subjId);

    @GET("api/Tasks/GetTasksByStudent")
    Call<List<MLATaskDetails>> getListTaskForStudent(@Query("subject") String subject, @Query("studentId") String studentId);


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("api/Tasks/PostGradeUpdate")
    Call<String> updateGrade(@Query("task_id") String taskId, @Query("student_id") String student_id, @Query("grade") String grade);

    @GET("api/Tasks/GetTasksByStudent")
    Call<List<MLAGradeTask>> getGradesForStudent(@Query("studentId") String studentId, @Query("subject") String subject);

}