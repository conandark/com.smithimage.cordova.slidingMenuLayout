package com.smithimage.cordova.slidingMenuLayout;

import android.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import nl.arendjr.slidingmenulayout.SlidingMenuLayout;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robert on 18/03/14.
 */
public class SlidingMenuLayoutPlugin extends CordovaPlugin implements View.OnClickListener {

    private SlidingMenuLayout menu;
    private List<String> items = new ArrayList<String>();
    private CallbackContext callbackContext = null;
    private ArrayAdapter<String> adapter;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.initialize(cordova);
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

        if("addMenuItems".equals(action)){
            addItems(jsonArray, callbackContext);
        }
    }

    private void addItems(JSONArray jsonArray, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        for(int i = 0; i < jsonArray.length(); i++){
            items.add(jsonArray.getString(i));
        }
        adapter.notifyDataSetChanged();
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

        adapter = new ArrayAdapter<String>(cordova.getActivity(),
                R.layout.simple_list_item_1, items);
        menuView.setAdapter(adapter);

        cordova.getActivity().setContentView(menu);
    }


    @Override
    public void onClick(View view) {
        TextView item = (TextView) view;
        Map<String, String> resultMap = new HashMap<String, String>();
        String id = String.format("%s", item.getText());
        resultMap.put("clickedItem", id);
        PluginResult result = new PluginResult(PluginResult.Status.OK, new JSONObject(resultMap));
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
    }
}

