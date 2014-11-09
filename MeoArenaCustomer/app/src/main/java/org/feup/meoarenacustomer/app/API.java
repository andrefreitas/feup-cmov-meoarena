package org.feup.meoarenacustomer.app;
import android.app.DownloadManager;

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

    public void buyTickets(String customerID, String showID, Integer tickets_number, String pin, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("customerID", customerID);
        params.put("showID", showID);
        params.put("quantity", tickets_number);
        params.put("pin", pin);
        client.post(url + "/tickets", params, responseHandler);
    }

    public void getVouchers(String customerID, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("customerID", customerID);
        client.get(url + "/vouchers", params, responseHandler);
    }

    public void getProducts(AsyncHttpResponseHandler responseHandler) {
        client.get(url + "/products", responseHandler);
    }

    public void getTransactions(String customerID, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("customerID", customerID);
        client.get(url + "/transactions", params, responseHandler);
    }

    public void checkValidPin() {

    }
}
