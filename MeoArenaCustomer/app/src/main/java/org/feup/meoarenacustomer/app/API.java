package org.feup.meoarenacustomer.app;
import com.loopj.android.http.*;


public class API {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    private final String PRODUCTION_URL = "http://neo.andrefreitas.pt:8081/api";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public API(String url ){
        this.url = url;
    }

    public API(){
        this.url = PRODUCTION_URL;
    }


    public void login(String email, String password, AsyncHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        client.post(url + "/login", params, responseHandler);
    }
}
