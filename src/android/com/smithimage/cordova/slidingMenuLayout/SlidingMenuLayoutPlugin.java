package com.smithimage.cordova.slidingMenuLayout;

import android.R;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import nl.arendjr.slidingmenulayout.SlidingMenuLayout;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 18/03/14.
 */
public class SlidingMenuLayoutPlugin extends CordovaPlugin {

    private ListView menuView = new ListView(cordova.getActivity());


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        initialize();
    }

    @Override
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {

        if("initialize".equals(action))
            initialize();


        return true;
    }

    private void initialize(){
        SlidingMenuLayout menu = new SlidingMenuLayout(cordova.getActivity());
        menu.addView(menuView);
        menu.addView(webView);

        List<String> items = new ArrayList<String>();
        items.add("Hej");
        items.add("Då");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(cordova.getActivity(), R.layout.simple_list_item_1, items);
        menuView.setAdapter(adapter);
        cordova.getActivity().setContentView(menu);
    }



}
