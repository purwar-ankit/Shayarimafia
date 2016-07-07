package utils;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobinationapps.com.shayarimafia.AppController;
import mobinationapps.com.shayarimafia.R;


/**
 * Created by ankit.purwar on 6/7/2016.
 */

public class Utility {


    /**
     * Check whether Internet Connection is available or not(e.g., connected or
     * Disconnected)
     *
     * @param context
     *            : Context of current Class
     * @return Boolean: true if connected false otherwise.
     */
    public static final boolean isNetWork(Context context) {

        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        /** to get info of WIFI N/W : */
        final android.net.NetworkInfo wifi = conMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        /** to get info of mobile N/W : */
        final android.net.NetworkInfo mobile = conMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi.isAvailable() && wifi.isConnected())
                || (mobile.isAvailable() && mobile.isConnected())) {
            Log.i("Is Net work?", "isNetWork:in 'isNetWork_if' is N/W Connected:"
                    + NetworkInfo.State.CONNECTED);
            return true;
        } else if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }


    public static boolean checkNetworkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void receiveJsonObj() {
        String tag_json_obj = "json_obj_req";

        String url = "http://api.androidhive.info/volley/person_object.json";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Constants.APPLICATION_TAG, response.toString());
                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Constants.APPLICATION_TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                // pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private static int currentApiVersion = Build.VERSION.SDK_INT;

    public static void toShare(String text_to_share, Context context){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text_to_share);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.share_title)));
    }

    public static void toShareExcludingFb(String text_to_share, Context context) {
        // get available share intents
        List<Intent> targets = new ArrayList<Intent>();
        Intent template = new Intent(Intent.ACTION_SEND);
        template.setType("text/plain");
        List<ResolveInfo> candidates = context.getPackageManager()
                .queryIntentActivities(template, 0);

        // remove facebook which has a broken share intent
        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.facebook.katana")) {
                Intent target = new Intent(Intent.ACTION_SEND);
                target.setType("text/plain");
                target.putExtra(Intent.EXTRA_TEXT, text_to_share);
                target.setPackage(packageName);
                targets.add(target);
            }
        }
        Intent chooser = Intent.createChooser(targets.remove(0), context.getResources().getString(R.string.share_title)/*text_to_share*/);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                targets.toArray(new Parcelable[]{}));

        context.startActivity(chooser);
    }

    /**
     * method to copy text to clipboard
     **/
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void toCopy(String text_to_copy, Context context) {
        // TODO Auto-generated method stub

        currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text_to_copy);
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            clipboard.setText(text_to_copy);
        }
        Toast.makeText(context, context.getResources().getString(R.string.copy_quote_msg),
                Toast.LENGTH_SHORT).show();

    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static float setSelectedFontsSize(String fontSize, Context context) {
        float selectedFontsSize = 15f;

        if (fontSize.equalsIgnoreCase(context.getResources().getString(R.string.small))) {
            selectedFontsSize = pixelsToSp(context, context.getResources().getDimension(R.dimen.text_size_15));
        } else if (fontSize.equalsIgnoreCase(context.getResources().getString(R.string.medium))) {
            selectedFontsSize = pixelsToSp(context, context.getResources().getDimension(R.dimen.text_size_18));
        } else if (fontSize.equalsIgnoreCase(context.getResources().getString(R.string.large))) {
            selectedFontsSize = pixelsToSp(context, context.getResources().getDimension(R.dimen.text_size_22));
        }

        return selectedFontsSize;
    }

    public static Typeface setSelectedTypeface(String quoteTypeface, Context context) {
        Typeface selectedTypeface = null;

        if (quoteTypeface.equalsIgnoreCase(context.getResources().getString(R.string.serif))) {
            selectedTypeface = Typeface.SERIF;
        } else if (quoteTypeface.equalsIgnoreCase(context.getResources().getString(R.string.sans))) {
            selectedTypeface = Typeface.SANS_SERIF;
        } else if (quoteTypeface.equalsIgnoreCase(context.getResources().getString(R.string.monospace))) {
            selectedTypeface = Typeface.MONOSPACE;

        }
        return selectedTypeface;

    }

    public static Spanned setTextWithHtml(String text, String quoteFont, Context context) {
        Spanned stringToReturn = null;
        if (quoteFont.equalsIgnoreCase(context.getResources().getString(R.string.normal))) {
            stringToReturn = Html.fromHtml(text);

        } else if (quoteFont.equalsIgnoreCase(context.getResources().getString(R.string.bold))) {
            stringToReturn = Html.fromHtml("<b>" + text + "</b>");

        } else if (quoteFont.equalsIgnoreCase(context.getResources().getString(R.string.italic))) {
            stringToReturn = Html.fromHtml("<i>" + text + "</i>");

        } else if (quoteFont.equalsIgnoreCase(context.getResources().getString(R.string.bold_italic))) {
            stringToReturn = Html.fromHtml("<i><b>" + text + "</b></i>");

        }
        return stringToReturn;
    }

    public static void rateUs(Context context)
    {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
