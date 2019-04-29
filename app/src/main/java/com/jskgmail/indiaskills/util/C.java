package com.jskgmail.indiaskills.util;

import com.jskgmail.indiaskills.BuildConfig;

/**
 * Created by aditya.singh on 12/11/2018.
 */

public interface C {

    String ANDROID = "android";
    String LOGIN_USER = "login_user";
    String IMEI = "123445013f1230ffeee2";
    String OFFLINE = "1";
    String ONLINE = "0";
    String LANGUAGE = "en";
    String TEST_ID = "p";
    String SCHEDULE_ID_PK ="q" ;
    String USERNAME = "userName";
    String PASSWORD ="password" ;
    String TEST ="test" ;
    String ONLINE_TEST_LIST = "online_test_list";
    String DEBUG = "DEBUG";
    String ACTIVE_DETAILS ="active_details" ;
    String TAG_ID ="TAGID" ;
    String SELECTED_USERNAME ="Selectedusername" ;
    String ANNEXURE_DATA = "annexure";
    String IS_FEEDBACK_SUBMITTED = "is_feedback";
    String ONGOING_TEST = "ongoing_test";
    String MEPSCLogo = "MEPSCLogo";
    String tagscoresWithoutAssessor = "tagscoresWithoutAssessor";
    String Event_Bookmark = "bookmark";
    String Event_Answer_Change = "AnswerChange";
    String Event_Question_Visit = "QuestionVisit";
    String Event_Time_Taken = "TimeTaken";
    String Event_Change_Language = "ChangeLanguage";
    String TEST_DATA = "test_data";
    String YES ="1";
    String NO ="0";

    enum FRAGMENT {

    }

    String BASE_URL = BuildConfig.BASE_URL;
    String API_LOGIN = BASE_URL + "users/login";
    String API_VERSION = BASE_URL + "users/get_app_version";
    String API_TEST_LIST = BASE_URL + "users/TestList";
    String API_TEST_DETAILS = BASE_URL + "users/Get_scheduled_Test_details";
    String API_TEST_DETAILS_DOWNLOAD = BASE_URL + "users/Get_scheduled_Test_details";
    String API_TEST_DETAILS_UPLOAD = BASE_URL + "users/submit_test";
    String API_UPLOAD_VIDEO_AND_PHOTO = BASE_URL + "users/user_upload_video_photo";
    String API_ZIP_DOWNLOAD_URL= BASE_URL + "users/Get_Exam_media";
    String API_SUBMIT_FEEDBACK_URL= BASE_URL + "users/submitFeedback";
    String API_SUBMIT_QUESTION_LOG= BASE_URL + "users/submitQuestionLog";
    String API_GET_BATCH_LIST= BASE_URL + "users/get_batch_status";
    String API_BATCH_COMPLETE= BASE_URL + "users/mark_batch_complete";
    String API_MEDIA_UPLOAD= BASE_URL + "users/media_upload";


}
