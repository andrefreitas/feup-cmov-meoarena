package org.feup.terminal.app;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

    public void validateTickets(String customerID, String tickets, AsyncHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("customerID", customerID);
        params.put("tickets", tickets);
        client.get(url + "/validateTickets", params, responseHandler);
    }

    public void validateOrders(String pin, String customerID, String vouchers, String products,
                               String quantity, String price, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("pin", pin);
        params.put("customerID", customerID);
        params.put("vouchers", vouchers);
        params.put("products", products);
        params.put("quantity", quantity);
        params.put("price", price);

        client.post(url + "/orders", params, responseHandler);
    }

}
