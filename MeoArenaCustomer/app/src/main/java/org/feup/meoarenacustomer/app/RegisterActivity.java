package org.feup.meoarenacustomer.app;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View.OnTouchListener;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import android.content.Intent;

import org.apache.http.Header;
import org.json.JSONObject;


public class RegisterActivity extends FragmentActivity implements RegisterDetailsFragment.OnHeadlineSelectedListener, RegisterCreditCardFragment.OnHeadlineSelectedListener{



    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private String name;
    private String nif;
    private String email;
    private String password;

    API api;

    public void goToCreditCard() {
        mPager.setCurrentItem(1);
    }

    public void setDetails(String name, String nif, String email, String password){
        this.name = name;
        this.nif = nif;
        this.email = email;
        this.password = password;
    }

    public boolean register(String ccNumber, String ccType, String ccValidity){

        if(name == null || nif == null || email == null || password == null){
            Toast.makeText(getApplicationContext(), R.string.missing_personal_details, Toast.LENGTH_SHORT).show();
            return false;
        }


        api.register(name, email, nif, password, ccNumber, ccType, ccValidity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    String id = response.getString("id");
                    String pin = response.getString("pin");

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("pin", pin);
                    startActivity(intent);


                } catch (Exception e) {
                    Log.e("RegisterActivity", "Failed to get JSON Object String 'id' ");
                    Toast.makeText(getApplicationContext(), "Failed to get JSON Object String 'id' or 'pin' ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), R.string.email_already_exists, Toast.LENGTH_SHORT).show();
            }

        });


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        api = new API();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if (position == 0){
                fragment = new RegisterDetailsFragment();
            }
            else {
                fragment = new RegisterCreditCardFragment();
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
