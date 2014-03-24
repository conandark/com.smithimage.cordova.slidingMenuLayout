package com.smithimage.cordova.slidingMenuLayout;

import android.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import nl.arendjr.slidingmenulayout.SlidingMenuLayout;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 18/03/14.
 */
public class SlidingMenuLayoutPlugin extends CordovaPlugin implements View.OnClickListener {

    private CordovaWebView webView;
    private SlidingMenuLayout menu;
    private List<String> items;
    private CallbackContext clickCallback = null;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        this.webView = webView;
        initialize(cordova);
    }

    @Override
    public boolean execute(final String action, final JSONArray jsonArray, final CallbackContext callbackContext) throws JSONException {

        if("initialize".equals(action)){
            initialize(cordova);
            return true;
        }

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    invokeMenu(action, jsonArray, callbackContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return true;
    }

    private void invokeMenu(String action, JSONArray jsonArray, CallbackContext callbackContext) throws JSONException {
        if("open".equals(action))
            open();

        if("close".equals(action))
            close();

        if("toggle".equals(action))
            toggle();

        if("addMenuItem".equals(action)){
            clickCallback = callbackContext;
            items.add(jsonArray.getString(0));
        }
    }

    private void close() {
        menu.closeMenu();
    }

    private void open() {
        menu.openMenu();
    }

    public void toggle(){
        menu.toggleMenu();
    }

    private void initialize(CordovaInterface cordova){
        menu = new SlidingMenuLayout(cordova.getActivity());
        menu.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));

        ListView menuView =  new ListView(cordova.getActivity());
        menuView.setOnClickListener(this);
        menu.addView(menuView);
        ((ViewGroup)webView.getParent()).removeView(webView);
        menu.addView(webView);

        items = new ArrayList<String>();
        items.add("Hej");
        items.add("Då");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(cordova.getActivity(), R.layout.simple_list_item_1, items);
        menuView.setAdapter(adapter);
        cordova.getActivity().setContentView(menu);
    }


    @Override
    public void onClick(View view) {
        PluginResult result = new PluginResult(PluginResult.Status.OK, new JSONObject());
        result.setKeepCallback(true);
        clickCallback.sendPluginResult(result);
    }
}
