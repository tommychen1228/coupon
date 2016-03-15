package com.myideaway.coupon.model.common;

import android.util.Base64;
import com.myideaway.coupon.Config;

import com.myideaway.easyapp.core.exception.RemoteServiceException;
import com.myideaway.easyapp.core.service.StringRemoteService;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.util.Ln;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cdm
 * Date: 13-11-22
 * Time: AM11:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class CouponJSONStringRemoteService extends StringRemoteService {
    private HashMap<String, Object> mainParams = new HashMap<String, Object>();
    private JSONObject requestData = new JSONObject();

    @Override
    public Object onExecute() throws RemoteServiceException {
        String encodedResponse = (String) super.onExecute();

        Ln.d("Encoded response " + encodedResponse);

        String decodedResponse = decodeResponseData(encodedResponse);

        Ln.d("Decoded response " + decodedResponse);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(decodedResponse);
        } catch (JSONException e) {
            Ln.e("Parse response json error", e);
        }

        return jsonObject;
    }

    @Override
    protected Map<String, Object> getParams() {
        whenPutParams();

        mainParams.put("i_type", 0);
        mainParams.put("r_type", 0);
        mainParams.put("soft_type", "futuan");
        mainParams.put("dev_type", "android");
        mainParams.put("requestData", encodeRequestData());
        return mainParams;
    }

    public abstract void whenPutParams();

    protected void putParam(String key, Object value) {
        try {
            requestData.put(key, value);
        } catch (JSONException e) {
            Ln.e("Put request param error", e);
        }
    }

    private String encodeRequestData() {
        String requestDataStr = requestData.toString();

        Ln.d("Request data " + requestDataStr);

        String encodedStr = Base64.encodeToString(requestDataStr.getBytes(), Base64.DEFAULT);

        Ln.d("Encoded request data " + encodedStr);

        return encodedStr;
    }

    private String decodeResponseData(String response) {
        byte[] decodedData = Base64.decode(response, Base64.DEFAULT);
        String decodedStr = new String(decodedData);
        return decodedStr;
    }

    @Override
    protected String getURL() {
        return Config.URL_SERVER;
    }
}
