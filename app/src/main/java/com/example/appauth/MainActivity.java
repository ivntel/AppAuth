package com.example.appauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import net.openid.appauth.*;

public class MainActivity extends AppCompatActivity {
    private static final int RC_AUTH = 100;
    private static final String LOGIN_HINT = "login_hint";
    private static final String CLIENT_ID = "native.code";
    private static final String USER_ID = "bob";

    // views
    Button mAuthorize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthorize = (Button) findViewById(R.id.loginButton);

        // wire click listener
        mAuthorize.setOnClickListener(new AuthorizeListener(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_AUTH) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
            AuthorizationException ex = AuthorizationException.fromIntent(data);
            // ... process the response or exception ...
            if (resp != null) {
                // authorization completed
                Log.i("ivan", "authorization completed2");
                Log.i("ivan", resp.jsonSerializeString());

            } else {
                // authorization failed, check ex for more details
                Log.i("ivan", "authorization failed2");
            }
        } else {
            // ...
        }
    }

    /**
     * Kicks off the authorization flow.
     */
    public static class AuthorizeListener implements Button.OnClickListener {

        private final MainActivity mMainActivity;

        public AuthorizeListener(@NonNull MainActivity mainActivity) {
            mMainActivity = mainActivity;
        }

        @Override
        public void onClick(View view) {

            AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                    Uri.parse("https://demo.identityserver.io/connect/authorize"),  //auth endpoint ,
                    Uri.parse("https://demo.identityserver.io/connect/token")  //token endpoint
            );

            AuthorizationService authorizationService = new AuthorizationService(view.getContext());
            Uri redirectUri = Uri.parse("io.identityserver.demo:/oauthredirect");
            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                    serviceConfiguration,
                    CLIENT_ID,
                    ResponseTypeValues.CODE,
                    redirectUri
            );

            builder.setScopes("openid", "profile");
            builder.setLoginHint(USER_ID);

            AuthorizationRequest request = builder.build();
            Intent authIntent = authorizationService.getAuthorizationRequestIntent(request);
            mMainActivity.startActivityForResult(authIntent, RC_AUTH);
        }
    }

}
