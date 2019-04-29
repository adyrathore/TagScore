package com.jskgmail.indiaskills.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jskgmail.indiaskills.Globalclass;
import com.jskgmail.indiaskills.pojo.LogHistoryTest;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databasename = "tagscore";
    public static final String tableInstruction = "Instructions";
    public static final String schdule_id_val = "schdule_id";
    public static final String tabletestdetails = "test_details";
    public static final String tablebatchdetails = "batch_details";
    public static final String tablequestions = "tbl_question";
    public static final String tableanswer = "answers";
    public static final String tableassessor_feedback_quesitons = "assessor_feedback_quesitons";
    public static final String id = "id";
    public static final String batch_id_fk = "batch_id_fk";
    public static final String training_center = "training_center";
    public static final String question_paper_id = "question_paper_id";
    public static final String test_duration = "test_duration";
    public static final String test_rendomClick = "test_rendomClick";
    public static final String test_name = "test_name";
    public static final String test_details = "test_details";
    public static final String target_candidates = "target_candidates";
    public static final String test_descriptions = "test_descriptions";
    public static final String user_id_fk = "user_id_fk";
    public static final String name = "name";
    public static final String username = "username";
    public static final String instruction = "instruction";
    public static final String id_question = "id_question";
    public static final String question = "question";
    public static final String type_of_question = "type_of_question";
    public static final String question_type = "question_type";
    public static final String language_code = "language_code";
    public static final String ImageHash = "ImageHash";
    public static final String parent_id = "parent_id";
    public static final String id_answer = "answer_id";
    public static final String answer = "answer";
    public static final String hashanswer = "hashanswer";
    public static final String id_feedback = "id_feedback";
    public static final String question_feedback = "question_feedback";
    public static final String tableuseranswer = "useranswer";
    public static final String answer_id = "answer_id";
    public static final String tablebookmark = "table_bookmark";
    public static final String bookmarkdate = "bookmarkdate";
    public static final String tablegpsdetails = "table_gps";
    public static final String latgps = "latgps";
    public static final String longgps = "longgps";
    public static final String table_imnagecapture = "table_imagecapure";
    public static final String hash_captur = "hashcapture";
    public static final String datetimestamp = "datetimestramp";
    public static final String table_userlogin = "table_userlogin";
    public static final String table_tagid = "table_tagid";
    public static final String col_password = "password";
    public static final String col_api_key = "api_key";
    public static final String Table_JSON_details = "Table_JSON_for_test";
    public static final String json_format = "jsonformt";
    public static final String json_testid = "testid";
    public static final String json_test_name = "json_test_name";
    public static final String usernaswer_userid = "userid";
    public static final String useranswer_datetime = "datetime";
    public static final String useranswerbookmarkcount = "bookmarkcount";
    public static final String useranswerchangecount = "changecount";
    public static final String jsonloopid_useranswer = "jsonloopid";
    public static final String completedpart = "completedpart";
    public static final String testid = "testid";
    public static final String imageurl_val = "imageurl";
    public static final String testid_val_imagesave = "testid";
    public static final String hastheory = "hastheory";
    public static final String haspractrical = "haspractrical";
    public static final String flagtheory = "flagtheory";
    public static final String flagpractrical = "flagpractrical";
    public static final String jsoniscomplete = "jsoniscomplete";
    public static final String imagetype_val = "imagetype";
    public static final String nameimage_val = "nameimage";
    public static final String userid_tagid = "tagid";
    public static final String latgpsval = "lat";
    public static final String longgpsval = "long";
    public static final String bookmarkflag_val = "bookmarkflag";
    public static final String table_vedio_got = "";
    public static final String annexure_val = "annexure";
    public static final String user_id_fk_val = "user_id_fk";
    public static final String question_id_val = "question_id";
    public static final String answer_val = "answer";
    public static final String range_marking = "range_marking";
    public static final String precentage = "precentage";
    public static final String tabletimeremaining = "tabletimeremaining";
    public static final String timeremaining = "timeremaining";
    public static final String flagcompleted = "flagcompleted";
    public static final String absentmarkflag = "absentmark";
    public static final String log_userid = "log_userid";
    public static final String log_testid = "log_testid";
    public static final String log_schduleid = "log_schduleid";
    public static final String log_login_user_id = "log_login_user_id";
    public static final String log_bookmark_values_test = "log_bookmark";
    public static final String log_question_id = "log_questionid";
    public static final String log_answer_id = "log_answergiven";
    public static final String table_logdetails = "logtableuser";
    public static final String useranswer_activedetails = "activeid";
    public static final String Remark = "Remark";
    public static final String tableoptionnoval = "optionnoval";
    public static final String testidno = "notestid";
    public static final String testQuestionnoval = "noquestionid";
    public static final String testoptionmarkno = "optionnoval";
    public static final String useridnoval = "useridno";
    public static final String schduleidno = "schdudelidno";
    public static final String activedetailsval = "activedetails";
    public static final String totalTime = "totalTime";
    public static final String tableLogHistory = "table_logs_history";
    public static final String LogHistoryID = "log_history_id";
    public static final String LogHistoryEvent = "log_history_event";
    public static final String LogHistoryQuestionId = "log_history_question_id";
    public static final String LogHistoryTimeStamp = "log_history_time_stamp";
    public static final String LogHistoryTestID = "log_history_test_id";
    public static final String LogHistoryUserID = "log_history_user_id";
    public DatabaseHelper(Context context) {
        super(context, databasename, null, 1);
    }

    public boolean insert_noanserval(String textid, String Questionid, String optionval, String userid, String schdule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(testidno, textid);
        contentValues.put(testQuestionnoval, Questionid);
        contentValues.put(testoptionmarkno, optionval);
        contentValues.put(useridnoval, userid);
        contentValues.put(schduleidno, schdule);
        long result = db.insert(tableoptionnoval, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tableoptionnoval + " (notestid text,noquestionid text,optionnoval text,useridno text,schdudelidno text)");
        db.execSQL("create table " + table_logdetails + "(log_userid text,log_testid text,log_schduleid text,log_login_user_id text,log_bookmark text,log_questionid text,log_answergiven text)");
        db.execSQL("create table " + tabletimeremaining + "(testid text ,userid text, schdule_id text,timeremaining text,activedetails text) ");
        db.execSQL("create table " + tableInstruction + " (id text,instruction text)");
        db.execSQL("create table " + tabletestdetails + "(id text,batch_id_fk text,training_center text,question_paper_id text,test_duration text,test_rendomClick text,test_name text,test_details text,target_candidates text,test_descriptions text)");
        db.execSQL("create table " + tablebatchdetails + " (batch_id_fk text,user_id_fk text,name text,username text,schdule_id text,flagcompleted text, absentmark text)");
        db.execSQL("create table " + tablequestions + " (id text,id_question text,question text,type_of_question text,question_type text,language_code text,ImageHash text,parent_id text)");
        db.execSQL("create table " + tableanswer + " (id text,id_question text,id_answer text,answer text,hashanswer text)");
        db.execSQL("create table " + tableuseranswer + " (id text,id_question text,answer_id text,userid text,datetime text,bookmarkcount text,bookmarkflag text,changecount text,jsonloopid text,lat text,long text,schdule_id text,precentage text,range_marking text,activeid text,optionnoval text, totalTime text)");
        db.execSQL("create table " + tablebookmark + " (id text,id_question text,bookmarkdate text,jsonloopid text)");
        db.execSQL("create table " + tablegpsdetails + " (id text,latgps text,longgps text)");
        db.execSQL("create table " + table_userlogin + " (table_tagid text,password text,api_key text)");
        db.execSQL("create table " + table_imnagecapture + " (imagetype text,hashcapture text,nameimage text,testid text,imageurl text,tagid text,schdule_id text)");
        db.execSQL("create table " + tableassessor_feedback_quesitons + " (annexure text,user_id_fk text,question_id text,answer text,schdule_id text, testid text,Remark text)");
        db.execSQL("create table " + Table_JSON_details + " (testid text,jsonformt text,json_test_name text,jsoniscomplete text,schdule_id text)");
        db.execSQL("create table " + completedpart + "(testid text,hastheory text,haspractrical text,flagtheory text,flagpractrical text,schdule_id text)");
        db.execSQL("create table " + tableLogHistory + "(log_history_id INTEGER PRIMARY KEY AUTOINCREMENT,log_history_question_id text,log_history_event text,log_history_test_id text,log_history_time_stamp text,activedetails text,log_history_user_id text)");

    }

    public int getTotalAnsweredGiven(String useridval, String schduleid, String activedetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tableuseranswer + " WHERE " + usernaswer_userid + " = ? and " + schdule_id_val + "= ? and " + useranswer_activedetails + "= ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{useridval, schduleid, activedetails});
        int c=cursorgetdetails.getCount();
        db.close();
        return c;
    }


    public Cursor getlogvalues(String testidval, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + table_logdetails + " WHERE " + log_testid + " = ? and " + log_schduleid + "= ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{testidval, schduleid});
        Log.e("select", cursorgetdetails.toString());
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursorgetdetails;
    }
    public boolean insertLogHistory( String Questionid, String event, String timestamp, String testid,String activedetails, String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LogHistoryQuestionId, Questionid);
        contentValues.put(LogHistoryEvent, event);
        contentValues.put(LogHistoryTimeStamp, timestamp);
        contentValues.put(LogHistoryTestID, testid);
        contentValues.put(activedetailsval, activedetails);
        contentValues.put(LogHistoryUserID, userID);

        long result = db.insert(tableLogHistory, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }
    public  List<LogHistoryTest> getLogHistory(String testId, String userids, String activedetails) {
         List<LogHistoryTest> historyTests = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tableLogHistory + " WHERE " + LogHistoryTestID + " = ? AND " + LogHistoryUserID + " = ? AND " + activedetailsval + "= ?";
        try{
        Cursor cursor = db.rawQuery(select, new String[]{testId, userids, activedetails});

        if (cursor.moveToFirst()) {
            do {
                LogHistoryTest logHistoryTest = new LogHistoryTest();
                logHistoryTest.setQuestion_id(cursor.getString(cursor.getColumnIndex(LogHistoryQuestionId)));
                logHistoryTest.setEvent(cursor.getString(cursor.getColumnIndex(LogHistoryEvent)));
                logHistoryTest.setTimestamp(cursor.getString(cursor.getColumnIndex(LogHistoryTimeStamp)));
                logHistoryTest.setCandidateId(cursor.getString(cursor.getColumnIndex(LogHistoryUserID)));
                historyTests.add(logHistoryTest);

            } while (cursor.moveToNext());
        }
        cursor.close();
            db.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return historyTests;
    }

    public  List<LogHistoryTest> getLogHistory(String testId) {
        List<LogHistoryTest> historyTests = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tableLogHistory + " WHERE " + LogHistoryTestID + " = ?";
        try{
            Cursor cursor = db.rawQuery(select, new String[]{testId});

            if (cursor.moveToFirst()) {
                do {
                    LogHistoryTest logHistoryTest = new LogHistoryTest();
                    logHistoryTest.setQuestion_id(cursor.getString(cursor.getColumnIndex(LogHistoryQuestionId)));
                    logHistoryTest.setEvent(cursor.getString(cursor.getColumnIndex(LogHistoryEvent)));
                    logHistoryTest.setTimestamp(cursor.getString(cursor.getColumnIndex(LogHistoryTimeStamp)));
                    logHistoryTest.setCandidateId(cursor.getString(cursor.getColumnIndex(LogHistoryUserID)));
                    historyTests.add(logHistoryTest);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyTests;
    }

    public boolean deleteLogHistoryOfUser(String testId, String userids, String activedetails) {
        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();
        int values = db.delete(tableLogHistory,  LogHistoryTestID + "=? AND " + LogHistoryUserID + "=? AND " +
                activedetailsval + "=?", new String[] {testId, userids, activedetails});
        if (values > 0) {
            success = true;
        } else {
            success = false;
        }
        db.close();
        return success;
    }
    public boolean deleteLogHistoryOfUser(String testId) {
        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();
        int values = db.delete(tableLogHistory,  LogHistoryTestID + "=?", new String[] {testId});
        if (values > 0) {
            success = true;
        } else {
            success = false;
        }
        db.close();
        return success;
    }

    public boolean insert_logdetails(String userids, String testids, String schduleid, String loginuserid, String bookmarkalue, String questionid, String answergiven) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(log_userid, userids);
        contentValues.put(log_testid, testids);
        contentValues.put(log_schduleid, schduleid);
        contentValues.put(log_login_user_id, loginuserid);
        contentValues.put(log_bookmark_values_test, bookmarkalue);
        contentValues.put(log_question_id, questionid);
        contentValues.put(log_answer_id, answergiven);
        long result = db.insert(table_logdetails, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean getbookmarkedvalues(String schduleidfinal, String batchidoffline, String userids, String activedetails) {
        boolean reutnval = false;
        SQLiteDatabase db = this.getWritableDatabase();
        // ContentValues contentValues = new ContentValues();
        // contentValues.put(schdule_id_val, schduleid);
        // contentValues.put(id,batchidoffline);
        // contentValues.put(jsonloopid_useranswer,guestioncount);
        // contentValues.put(usernaswer_userid,userid);
        // long result = db.insert(tableuseranswer,null,contentValues);

        String select = "SELECT * FROM " + tableuseranswer + " WHERE " + schdule_id_val + " = ? AND " + id + " = ? AND " + usernaswer_userid + "= ? and " + useranswer_activedetails + " = ? and " + useranswerbookmarkcount + " >= ?";

        Cursor cursor = db.rawQuery(select, new String[]{schduleidfinal, batchidoffline, userids, activedetails, "1"});

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            String str = cursor.getString(6);
            int strid = Integer.valueOf(str);
            if (strid >= 1) {
                reutnval = true;
            } else {
                reutnval = false;
            }
        } else {
            reutnval = false;
        }
        return reutnval;
    }


    public Cursor getdetailsforbatchAbsent(String testid, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tablebatchdetails + " WHERE " + batch_id_fk + " = ? AND " + username + " = ? ";

        Cursor cursor = db.rawQuery(select, new String[]{testid, userid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        // Cursor res = db.rawQuery("select * from "+Table_JSON_details + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursor;
    }


    public int getdetailsofvaluesbookmarkedornot(String schduleid, String batchidoffline, String guestioncount, String userid, String activedetails) {

        int reutnval = 0;
        SQLiteDatabase db = getWritableDatabase();
        // ContentValues contentValues = new ContentValues();
        // contentValues.put(schdule_id_val, schduleid);
        // contentValues.put(id,batchidoffline);
        // contentValues.put(jsonloopid_useranswer,guestioncount);
        // contentValues.put(usernaswer_userid,userid);
        // long result = db.insert(tableuseranswer,null,contentValues);

        String select = "SELECT * FROM " + tableuseranswer + " WHERE " + schdule_id_val + " = ? AND " + id + " = ? AND " + jsonloopid_useranswer + "= ? AND " + usernaswer_userid + "= ? and " + useranswer_activedetails + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{schduleid, batchidoffline, guestioncount, userid, activedetails});

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            String str = cursor.getString(6);
            int strval = Integer.valueOf(str);
            if (str.equalsIgnoreCase("1") || strval >= 1) {
                reutnval = 2;
            } else {
                reutnval = 1;
            }
        } else {
            reutnval = 0;
        }
        db.close();
        return reutnval;
    }


    public boolean getquestiongivenornot(String schduleid, String batchidoffline, String guestioncount, String userid, String activedetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tableuseranswer + " WHERE " + schdule_id_val + " = ? AND " + id + " = ? AND " + jsonloopid_useranswer + "= ? AND " + usernaswer_userid + "= ? and " + useranswer_activedetails + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{schduleid, batchidoffline, guestioncount, userid, activedetails});

        if (cursor.getCount() > 0)
            return true;
        else
            return false;

    }


    public boolean insert_timeremaining(String testidval, String useridval, String schdule_idval, String timeremainingval, String activedetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(testid, testidval);
        contentValues.put(usernaswer_userid, useridval);
        contentValues.put(schdule_id_val, schdule_idval);
        contentValues.put(timeremaining, timeremainingval);
        contentValues.put(activedetailsval, activedetails);

        long result = db.insert(tabletimeremaining, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean update_record_testtimeining(String testidval, String useridval, String schdule_idval, String timeremainingval, String activedetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(testid, testidval);
        contentValues.put(usernaswer_userid, useridval);
        contentValues.put(schdule_id_val, schdule_idval);
        contentValues.put(timeremaining, timeremainingval);
        contentValues.put(activedetailsval, activedetails);
        String whereClause = "testid=? and schdule_id = ? and userid = ?";
        String whereArgs[] = {testidval, schdule_idval, useridval};
        long result = db.update(tabletimeremaining, contentValues, whereClause, whereArgs);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor gettimereamining(String testidval, String schduleid, String useridval, String activedetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tabletimeremaining + " WHERE " + testid + " = ? and " + schdule_id_val + "= ? and " + usernaswer_userid + " = ? and " + activedetailsval + " = ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{testidval, schduleid, useridval, activedetails});
        Log.e("select", cursorgetdetails.toString());
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursorgetdetails;
    }

    public boolean getassessorfeedback(String testidval, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tableassessor_feedback_quesitons + " WHERE " + testid + " = ? and " + schdule_id_val + "= ? and " + annexure_val + "= ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{testidval, schduleid, "M"});

        if (cursorgetdetails.getCount() > 0) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }

    }

    public Cursor getAllfeedbackdatavalues(String testidval, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tableassessor_feedback_quesitons + " WHERE " + testid + " = ? and " + schdule_id_val + "= ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{testidval, schduleid});
        Log.e("select", cursorgetdetails.toString());
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursorgetdetails;
    }


    public Cursor getAllfeedbackdatavaluesforonline(String testidval, String schduleid, String userid) {

        //
        // s =521,t = 296
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tableassessor_feedback_quesitons + " WHERE " + testid + " = ? and " + schdule_id_val + "= ? and " + user_id_fk_val + " = ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{testidval, schduleid, userid});
        Log.e("select", cursorgetdetails.toString());
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursorgetdetails;
    }


    public boolean insert_feedbackform(String annexurevalu, String user_id_fk, String question_id, String answer, String schdule_id, String testidvbal, String remarksval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(annexure_val, annexurevalu);
        contentValues.put(user_id_fk_val, user_id_fk);
        contentValues.put(question_id_val, question_id);
        contentValues.put(schdule_id_val, schdule_id);
        contentValues.put(testid, testidvbal);
        contentValues.put(answer_val, answer);
        contentValues.put(Remark, remarksval);
        long result = db.insert(tableassessor_feedback_quesitons, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getbookmarkquestionvalues(String testid, String schduleid, String bookmark, String activedetailsval) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tableuseranswer + " WHERE " + usernaswer_userid + " = ? and " + bookmarkflag_val + ">= ? and " + schdule_id_val + "= ? and " + useranswer_activedetails + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{testid, bookmark, schduleid, activedetailsval});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursor;
    }


    public boolean getusercount(String schduleid, String testid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tableuseranswer + " WHERE " + schdule_id_val + "= ?";

        Cursor cursor = db.rawQuery(select, new String[]{schduleid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        int count = cursor.getCount();
        if (count > 0) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }

    public boolean deleteimageonebyone(String nameval) {
        SQLiteDatabase db = this.getWritableDatabase();
        //  String select = "SELECT * FROM " + table_imnagecapture + " WHERE " + nameimage_val + " = ?";
        boolean success;
        int tableimage = db.delete(table_imnagecapture, nameimage_val + "=" + nameval, null);
        if (tableimage > 0) {
            success = true;
        } else {
            success = false;
        }
        db.close();
        return success;
    }


    public boolean Deleteimage(String testidval, String schduleidval) {
        SQLiteDatabase db = this.getWritableDatabase();
        //  String select = "SELECT * FROM " + table_imnagecapture + " WHERE " + nameimage_val + " = ?";
        boolean success;
        int tableimage = db.delete(table_imnagecapture, schdule_id_val + "=" + schduleidval, null);
        if (tableimage > 0) {
            success = true;
        } else {
            success = false;
        }

        return success;

    }

    public boolean delete_feedbackbyid(String Testidupload, String schdid, String userid) {
        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();
        int values = db.delete(tableassessor_feedback_quesitons, schdule_id_val + "=" + schdid, null);
        if (values > 0) {
            success = true;
        } else {
            success = false;
        }
        db.close();
        return success;
    }

    public boolean deleteuseranswer(String Testidupload, String schdid, String userid) {
        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();
        int values = db.delete(tableuseranswer, schdule_id_val + "=" + schdid, null);
        if (values > 0) {
            success = true;
        } else {
            success = false;
        }
        db.close();
        return success;
    }

    public boolean delete_byID(String idTEST, String schduleid) {
        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();
        int values = db.delete(tableuseranswer, schdule_id_val + "=" + schduleid, null);
        int valuesjson = db.delete(Table_JSON_details, schdule_id_val + "=" + schduleid, null);
        int completedpartval = db.delete(completedpart, schdule_id_val + "=" + schduleid, null);
        //int tablegpsdetailsval = db.delete(tablegpsdetails, id+"="+idTEST, null);
        int tablebachdetailsval = db.delete(tablebatchdetails, schdule_id_val + "=" + schduleid, null);
        int deteimages = db.delete(table_imnagecapture, schdule_id_val + "=" + schduleid, null);
        int tablefeedback = db.delete(tableassessor_feedback_quesitons, schdule_id_val + "=" + schduleid, null);
        int logtale = db.delete(table_logdetails, log_schduleid + "=" + schduleid, null);
        if (valuesjson > 0 || completedpartval > 0 || tablebachdetailsval > 0 || values > 0 || logtale > 0) {
            success = true;
        } else {
            success = false;
        }
        db.close();
        return success;
    }

    public boolean insert_tablegpsdetails(String testidval, String Latgpsval, String longgpsval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, testidval);
        contentValues.put(latgps, Latgpsval);
        contentValues.put(longgps, longgpsval);

        long result = db.insert(tablegpsdetails, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insert_imagesval(String imagetype_value, String hashcapturevalues, String nameimage_values, String test_id_values, String imageurl_values, String userid_val, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(imagetype_val, imagetype_value);
        contentValues.put(hash_captur, hashcapturevalues);
        contentValues.put(nameimage_val, nameimage_values);
        contentValues.put(testid_val_imagesave, test_id_values);
        contentValues.put(imageurl_val, imageurl_values);
        contentValues.put(schdule_id_val, schduleid);
        contentValues.put(userid_tagid, userid_val);
        long result = db.insert(table_imnagecapture, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean getAllImages(String testid, String schduleid,String imageVal) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + table_imnagecapture + " WHERE " + testid_val_imagesave + " = ? and " + schdule_id_val + "= ? and " + imagetype_val + "= ?";
        Cursor cursor = db.rawQuery(select, new String[]{testid, schduleid,imageVal});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        if (cursor.getCount() > 0) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }
    public Cursor getSelfiImage(String testid, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + table_imnagecapture + " WHERE " + testid_val_imagesave + " = ? and " + schdule_id_val + "= ?";
        Cursor cursor = db.rawQuery(select, new String[]{testid, schduleid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        return cursor;
    }
    public Cursor getAllVauesofimages(String type, String schdule_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + table_imnagecapture + " WHERE " + imagetype_val + "= ? and " + schdule_id_val + "= ?";

        Cursor cursor = db.rawQuery(select, new String[]{type, schdule_id});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursor;
    }

    public Cursor getAllImagesbypath(String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + table_imnagecapture + " WHERE " + nameimage_val + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{path});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableInstruction);
        db.execSQL("DROP TABLE IF EXISTS " + tabletestdetails);
        db.execSQL("DROP TABLE IF EXISTS " + tablebatchdetails);
        db.execSQL("DROP TABLE IF EXISTS " + tablequestions);
        db.execSQL("DROP TABLE IF EXISTS " + tableanswer);
        db.execSQL("DROP TABLE IF EXISTS " + tableuseranswer);
        db.execSQL("DROP TABLE IF EXISTS " + tablebookmark);
        db.execSQL("DROP TABLE IF EXISTS " + tablegpsdetails);
        db.execSQL("DROP TABLE IF EXISTS " + table_userlogin);
        db.execSQL("DROP TABLE IF EXISTS " + table_imnagecapture);
        db.execSQL("DROP TABLE IF EXISTS " + tableassessor_feedback_quesitons);
        db.execSQL("DROP TABLE IF EXISTS " + Table_JSON_details);
        db.execSQL("Drop TABLE IF EXISTS " + completedpart);

        onCreate(db);
    }


    public boolean insert_useranswer(String idtest, String id_questionval, String answerid, String useridval, String datetimeval, String bookmarkcountval, String changecountval, String jsonloopid, String schduleid, int flagebookmark, String percentageval, String rangeflage, String activedetails, String optionnoval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(id_question, id_questionval);
        contentValues.put(id_answer, answerid);
        contentValues.put(usernaswer_userid, useridval);
        contentValues.put(useranswer_datetime, datetimeval);
        contentValues.put(useranswerbookmarkcount, bookmarkcountval);
        contentValues.put(useranswerchangecount, changecountval);
        contentValues.put(jsonloopid_useranswer, jsonloopid);
        contentValues.put(latgpsval, Globalclass.latss);
        contentValues.put(longgpsval, Globalclass.longss);
        contentValues.put(schdule_id_val, schduleid);
        contentValues.put(range_marking, rangeflage);
        contentValues.put(precentage, percentageval);
        contentValues.put(bookmarkflag_val, String.valueOf(flagebookmark));
        contentValues.put(useranswer_activedetails, activedetails);
        contentValues.put(testoptionmarkno, optionnoval);
        long result = db.insert(tableuseranswer, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insert_completedpart(String testidval, String hashtheoryvalue, String hashpractricalval, String flagprac, String flagtheoryval, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(testid, testidval);
        contentValues.put(hastheory, hashtheoryvalue);
        contentValues.put(haspractrical, hashpractricalval);
        contentValues.put(flagpractrical, flagprac);
        contentValues.put(schdule_id_val, schduleid);
        contentValues.put(flagtheory, flagtheoryval);
        long result = db.insert(completedpart, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean update_record_completedpart(String activedetailsval, String testidval, String flagprac, String flagtheoryval, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(testid, testidval);
        contentValues.put(schdule_id_val, schduleid);
        // contentValues.put(hastheory,hashtheoryvalue);
        // contentValues.put(haspractrical,hashpractricalval);
        if (activedetailsval.equalsIgnoreCase("0")) {
            contentValues.put(flagtheory, flagtheoryval);
        } else {
            contentValues.put(flagpractrical, flagprac);
        }


        String whereClause = "testid=? and schdule_id = ?";
        String whereArgs[] = {testidval, schduleid};
        long result = db.update(completedpart, contentValues, whereClause, whereArgs);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getcompletedvaluesflag(String testids, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + completedpart + " WHERE " + testid + " = ? and " + schdule_id_val + "= ?";
        Cursor cursor = db.rawQuery(select, new String[]{testids, schduleid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        // Cursor res = db.rawQuery("select * from "+Table_JSON_details + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursor;
    }

    public boolean getcompletedvaluesflaginserted(String testids, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + Table_JSON_details + " WHERE " + testid + " = ? and " + schdule_id_val + "= ?";
        Cursor cursor = db.rawQuery(select, new String[]{testids, schduleid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        // Cursor res = db.rawQuery("select * from "+Table_JSON_details + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        int cursorCount = cursor.getCount();
        cursor.close();

        if (cursorCount > 0) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }

    public boolean insert_bookmark(String idtest, String id_questionval, String bookmarkdateval, String jsonloopidval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(id_question, id_questionval);
        contentValues.put(bookmarkdate, bookmarkdateval);
        contentValues.put(jsonloopid_useranswer, jsonloopidval);
        long result = db.insert(tablebookmark, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertJSONFORMAT(String idtest, String testname, String JSON, String Iscomplete, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(json_testid, idtest);
        contentValues.put(json_test_name, testname);
        contentValues.put(json_format, JSON);
        contentValues.put(jsoniscomplete, Iscomplete);
        contentValues.put(schdule_id_val, schduleid);
        long result = db.insert(Table_JSON_details, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }
    public boolean updateJSONFORMAT(String idtest, String testname, String JSON, String Iscomplete, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(json_testid, idtest);
        contentValues.put(json_test_name, testname);
        contentValues.put(json_format, JSON);
        contentValues.put(jsoniscomplete, Iscomplete);
        contentValues.put(schdule_id_val, schduleid);
        String whereClause = "testid=? and schdule_id = ?";
        String whereArgs[] = {idtest, schduleid};
        long result = db.update(Table_JSON_details, contentValues, whereClause, whereArgs);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean update_JSONFORMAT(String idtest, String Iscomplete, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(json_testid,idtest);
        contentValues.put(jsoniscomplete, Iscomplete);
        contentValues.put(schdule_id_val, schduleid);
        String whereClause = "testid=? and schdule_id = ?";
        String whereArgs[] = {idtest, schduleid};
        long result = db.update(Table_JSON_details, contentValues, whereClause, whereArgs);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

   /* public boolean insertData_table_feedback(String idtest, String feedbackid, String feedbacktext, String remarkval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(id_feedback, feedbackid);
        contentValues.put(question_feedback, feedbacktext);
        contentValues.put(Remark, remarkval);
        long result = db.insert(tableassessor_feedback_quesitons, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }*/

    public boolean insertDate_table_details(String idtest, String batchid, String trainingcenter, String questionpaper, String testduration, String randomclick, String testname, String testdetails, String candidate, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(batch_id_fk, batchid);
        contentValues.put(training_center, trainingcenter);
        contentValues.put(question_paper_id, questionpaper);
        contentValues.put(test_duration, testduration);
        contentValues.put(test_rendomClick, randomclick);
        contentValues.put(test_name, testname);
        contentValues.put(test_details, testdetails);
        contentValues.put(target_candidates, candidate);
        contentValues.put(test_descriptions, description);
        long result = db.insert(tabletestdetails, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData_tableInstruction(String idtest, String inst) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(instruction, inst);
        long result = db.insert(tableInstruction, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData_tablebatchdetails(String batchid, String userid, String question, String usernames, String tagid, String schduleid, String flagcompletedflag, String absentmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(batch_id_fk, batchid);
        contentValues.put(user_id_fk, userid);
        contentValues.put(name, usernames);
        contentValues.put(username, tagid);
        contentValues.put(schdule_id_val, schduleid);
        contentValues.put(flagcompleted, flagcompletedflag);
        contentValues.put(absentmarkflag, absentmark);
        long result = db.insert(tablebatchdetails, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getdetailsforbatchfordropdown(String testid, String userid, String Flagidflag) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tablebatchdetails + " WHERE " + batch_id_fk + " = ? and " + flagcompleted + "= ?";

        Cursor cursor = db.rawQuery(select, new String[]{testid, Flagidflag});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        // Cursor res = db.rawQuery("select * from "+Table_JSON_details + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);


        return cursor;
    }

    public boolean getdetailsforbatch(String testid, String userid, String flagidflag) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tablebatchdetails + " WHERE " + batch_id_fk + " = ? AND " + username + " = ? and " + flagcompleted + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{testid, userid, flagidflag});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        // Cursor res = db.rawQuery("select * from "+Table_JSON_details + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }



    public boolean insertData_tablequestions(String idtest, String questid, String quest, String typeofquestion, String qtype, String langcode, String hash, String parentid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(id_question, questid);
        contentValues.put(question, quest);
        contentValues.put(type_of_question, typeofquestion);
        contentValues.put(question_type, qtype);
        contentValues.put(language_code, langcode);
        contentValues.put(ImageHash, hash);
        contentValues.put(parent_id, parentid);
        long result = db.insert(tablequestions, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;


    }

    public boolean insertData_tableanswer(String idtest, String questid, String idanswer, String ans, String hash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(id_question, questid);
        contentValues.put(id_answer, idanswer);
        contentValues.put(hashanswer, hash);
        contentValues.put(answer, ans);
        long result = db.insert(tableanswer, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insert_table_userlogin(String userid, String password, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_tagid, userid);
        contentValues.put(col_password, password);
        contentValues.put(col_api_key, key);
        long result = db.insert(table_userlogin, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean update_record_question_answer_given(String idtest, String id_questionval, String answerid, String useridval, String datetimeval, String bookmarkcountval, String changecountval, String jsonloopid, String schduleid, int flagbookmark, String percentageval, String rangeflage, String activedetails, String optionnoval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, idtest);
        contentValues.put(id_question, id_questionval);
        contentValues.put(id_answer, answerid);
        contentValues.put(usernaswer_userid, useridval);
        contentValues.put(useranswer_datetime, datetimeval);
        contentValues.put(useranswerbookmarkcount, bookmarkcountval);
        contentValues.put(useranswerchangecount, changecountval);
        contentValues.put(jsonloopid_useranswer, jsonloopid);
        contentValues.put(schdule_id_val, schduleid);
        contentValues.put(range_marking, rangeflage);
        contentValues.put(precentage, percentageval);
        contentValues.put(bookmarkflag_val, String.valueOf(flagbookmark));
        contentValues.put(useranswer_activedetails, activedetails);
        contentValues.put(testoptionmarkno, optionnoval);
        String whereClause = "id=? and userid = ? and id_question = ? and schdule_id = ?";
        String whereArgs[] = {idtest, useridval, id_questionval, schduleid};
        long result = db.update(tableuseranswer, contentValues, whereClause, whereArgs);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateTotalTimeTaken(String idtest, String useridval, String schduleid, String activedetails, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(totalTime, time);
        String whereClause = "id=? and userid = ? and schdule_id = ? and activeid = ?";
        String whereArgs[] = {idtest, useridval, schduleid, activedetails};
        long result = db.update(tableuseranswer, contentValues, whereClause, whereArgs);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean getdetailsvaluesquestion(String testid, String userid, String questionid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tableuseranswer + " WHERE " + id + " = ? AND " + usernaswer_userid + " = ?" + " AND " + id_question + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{testid, userid, questionid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        // Cursor res = db.rawQuery("select * from "+Table_JSON_details + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public Cursor getAllquestiondetilsval(String testid, String userid, String questionid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + tableuseranswer + " WHERE " + id + " = ? AND " + usernaswer_userid + " = ?" + " AND " + id_question + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{testid, userid, questionid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursor;
    }


    public Cursor getdetailsoflluseranswer() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tableuseranswer;

        Cursor cursorgetdetails = db.rawQuery(selectvalues, null);
        Log.e("select", cursorgetdetails.toString());
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursorgetdetails;
    }

    public Cursor getAllquestiondetilsvaluploadback(String testid, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tableuseranswer + " WHERE " + id + " = ? and " + schdule_id_val + "= ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{testid, schduleid});
        Log.e("select", cursorgetdetails.toString());
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursorgetdetails;
    }


    public Cursor getAllquestiondetilsvaluploadbackonline(String testid, String schduleid, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectvalues = "SELECT * FROM " + tableuseranswer + " WHERE " + id + " = ? and " + schdule_id_val + "= ? and " + usernaswer_userid + " = ?";

        Cursor cursorgetdetails = db.rawQuery(selectvalues, new String[]{testid, schduleid, userid});
        Log.e("select", cursorgetdetails.toString());
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        return cursorgetdetails;
    }


    public Cursor getAllDate_login(String tagid, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + table_userlogin + " WHERE " + table_tagid + " = ? AND " + col_password + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{tagid, password});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);
        return cursor;
    }

    public Cursor getAll_test_list() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Table_JSON_details, null);

        return res;
    }

    public boolean isTestCompletedOrNot(String scheduleID) {
        boolean tt = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + Table_JSON_details + " WHERE " + schdule_id_val + " = ?";
        Cursor res = db.rawQuery(select, new String[]{scheduleID});
        res.moveToNext();
        if (res.getCount() > 0) {
            String str = res.getString(3);
            if (str.equalsIgnoreCase("1"))
                tt = true;
            else if (str.equalsIgnoreCase("0"))
                tt = false;
            else
                tt = false;
        }
        db.close();
        return tt;
    }

    public Cursor gettest_details_json_string(String batchid, String schduleid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + Table_JSON_details + " WHERE " + json_testid + " = ? and " + schdule_id_val + "= ?";
        Cursor res = db.rawQuery(select, new String[]{batchid, schduleid});

        return res;
    }

    public boolean getAllDate_batchid(String schduleID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + Table_JSON_details + " WHERE " + schdule_id_val + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{schduleID});
        int cursorCount = cursor.getCount();
        cursor.close();
        if (cursorCount > 0) {
            db.close();
            return true;
        }

        return false;
    }

    public boolean checkUserdropdown(String tagid, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + table_userlogin + " WHERE " + table_tagid + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{tagid});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        int cursorCount = cursor.getCount();

        cursor.close();

        if (cursorCount > 0) {
            db.close();
            return true;
        }
        return false;
    }

    public boolean checkUser(String tagid, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + table_userlogin + " WHERE " + table_tagid + " = ? AND " + col_password + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{tagid, password});
        // Cursor res = db.rawQuery("select * from "+table_userlogin + "WHERE table_tagid = '"+tagid+"' and password ='"+password+"';",null);

        int cursorCount = cursor.getCount();

        cursor.close();

        if (cursorCount > 0) {
            db.close();
            return true;
        }
        return false;
    }
}

