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

    public void register(String name, String email, String nif, String password, String ccNumber, String ccType, String ccValidity, AsyncHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("nif", nif);
        params.put("ccNumber", ccNumber);
        params.put("ccType", ccType);
        params.put("ccValidity", ccValidity);
        client.post(url + "/customers", params, responseHandler);
    }

    public void getShows(AsyncHttpResponseHandler responseHandler) {
        client.get(url + "/shows", responseHandler);
    }
}
