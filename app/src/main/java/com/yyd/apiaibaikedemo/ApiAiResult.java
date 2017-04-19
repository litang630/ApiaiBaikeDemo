package com.yyd.apiaibaikedemo;

import java.util.HashMap;

/**
 * Created by pc on 2016/11/24.
 */

public class ApiAiResult {
    private String intent;
    private String action;
    private String query;
    private String response;
    private HashMap<String, String> parameters;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }
}
