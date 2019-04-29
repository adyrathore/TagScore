package com.jskgmail.indiaskills;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Globalclass {
    private SharedPreferences prefs;
    public static String apikeys, userids = "", tagid, password, batchidoffline = "", activedetails = "";
    public static int guestioncount = 0,tempQuestionNo=0;
    public static String latss = "28.4976712", longss = "77.1586012";
    public static int total_no_of_quest_val = 0;
    public static String languagecode = "";
    public static String schduleid = "";
    public static String capture = "0";
    public static String name;
    public static String batchidvalues;
    public static String onlineassessorgiven = "0";
    public static String bookmardepractrical = "0";
    public static String countlanguage = "0";
    public static String hindipresent = "0";
    public static String id_login = "";
    public static String Testidupload, schduleidfinal;
    public static String looptestlistviewval = "0";
    public static String uplodingflagvalues = "0";
    public static String roleval = "";
    public static String testname;
    public static String spinnerstringlang = "en";
    public static String completetype = "";
    public static String imagecapturevalidation = "0";
    public static String feedbackgiven = "0";
    public static String testpostuniqueid;
    public static String imei = "123445013f1230ffeee2";
    public static String idcandidate = "0";
    public static String evidencecapture = "hola";
    public static String ddusernameselected = "non";
    public static String onlinjson = "";
    public static String lastpicturecandidate = "0";
    public static String instructionhn = "\n" +
            "परीक्षण शुरू करने से पहले कृपया निम्नलिखित निर्देशों को ध्यान से पढ़ें:\n" +
            "\n" +
            "1. 'स्टार्ट टेस्ट' बटन देखने के लिए नियम और शर्तें स्वीकार करें।\n" +
            "2. परीक्षण प्रारम्भ करने के लिए स्क्रीन के नीचे 'परीक्षण प्रारम्भ करें' बटन पर क्लिक करें।\n" +
            "3. परीक्षण में कोई नकारात्मक अंकन नहीं है।\n" +
            "4. परीक्षण समाप्त होने पर स्वचालित रूप से ही सबमिट हो जायेगा।\n" +
            "5. प्रश्नों के बीच नेविगेट करने के लिए 'पिछला' तथा 'अगला' बटन का प्रयोग करें।\n" +
            "6. दाएं हाथ की ओर पैनल प्रश्नों की स्थिति इंगित करता है I हरे रंग का बॉक्स इंगित करता है कि प्रश्नों का उत्तर दे दिया गया है जबकि ग्रे रंग का बॉक्स इंगित करता है कि इन पृष्ठों का उत्तर दिया जाना बाकि है, तथा नीले रंग का बॉक्स इंगित करता है कि वर्तमान में आप किस प्रश्न को हल कर रहे हैं।\n" +
            "7. बुकमार्क बटन पर क्लिक करके आप दूसरे प्रश्नों को हल कर सकते हैं और टेस्ट सबमिट करने से पहले उस प्रश्न को फिर से प्रयास कर सकते है।\n"+
            "8. आप परीक्षण तभी समाप्त कर सकते हैं जब आपने सभी प्रश्नों को हल (एटेम्पट) किया हो।\n" +
            "9. परीक्षण समाप्त करने के लिए सबमिट बटन पर क्लिक करें।\n" +
            "10. आपको केवल थ्योरी के प्रश्न हल करने होंगे और टेस्ट देने के बाद मूल्यांकनकर्ता को व्यावहारिक प्रश्न लेने के लिए टेबलेट देना होगा।\n" +
            "\n" +
            "कृपया निर्देशों को ध्यान से पढ़ें क्योंकि आप इस परीक्षा को लेने के लिए केवल एकबार ही हकदार होंगे।\n"+
            "शुभकामनाएँ\n";

    public Globalclass(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setApikey(String apikey) {
        prefs.edit().putString("apikey", apikey);
    }

    public String getApikey() {
        String apikey = prefs.getString("apikey", "");
        return apikey;
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getusename() {
        String usename = prefs.getString("usename", "");
        return usename;
    }


}
