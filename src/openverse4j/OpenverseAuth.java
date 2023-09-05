package openverse4j;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * OpenverseAuth provides methods to authenticate with the Openverse API.
 * It allows users to register their application and retrieve an access token.
 */
public class OpenverseAuth {

    private static final String       REGISTER_URL = "https://api.openverse.engineering/v1/auth_tokens/register/";

    private static final String       TOKEN_URL    = "https://api.openverse.engineering/v1/auth_tokens/token/";

    private static final OkHttpClient client       = new OkHttpClient();

    /**
     * The main method to demonstrate the registration and token retrieval process.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(final String[] args) {

        // アプリ情報やメールを設定
        final var appName = "YOUR_APP_NAME";
        final var appDescription = "YOUR_APP_DESCRIPTION";
        final var email = "YOUR_EMAIL";

        // 1. APIキーの取得
        final var credentials = OpenverseAuth.register(appName, appDescription, email);
        final var clientId = credentials[0];
        final var clientSecret = credentials[1];
        System.out.println("ClientId: " + clientId);
        System.out.println("ClientSecret: " + clientSecret);

        // 2. アクセストークンの取得
        final var accessToken = OpenverseAuth.getAccessToken(clientId, clientSecret);
        System.out.println("Access Token: " + accessToken);
    }


    /**
     * Registers an application with the Openverse API.
     *
     * @param appName        The name of the application.
     * @param appDescription A brief description of the application.
     * @param email          The email address associated with the application.
     * @return An array containing the client ID and client secret.
     */
    public static String[] register(final String appName, final String appDescription, final String email) {

        try {
            final var jsonParam = new JSONObject();
            jsonParam.put("name", appName);
            jsonParam.put("description", appDescription);
            jsonParam.put("email", email);

            final var body = RequestBody.create(jsonParam.toString(), MediaType.parse("application/json; charset=utf-8"));

            final var request = new Request.Builder()
                    .url(OpenverseAuth.REGISTER_URL)
                    .post(body)
                    .build();

            final var response = OpenverseAuth.client.newCall(request).execute();
            final var responseBody = response.body().string();

            final var jsonResponse = new JSONObject(responseBody);
            return new String[] { jsonResponse.getString("client_id"), jsonResponse.getString("client_secret") };
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Retrieves an access token using the provided client ID and client secret.
     *
     * @param clientId     The client ID obtained during registration.
     * @param clientSecret The client secret obtained during registration.
     * @return The access token.
     */
    public static String getAccessToken(final String clientId, final String clientSecret) {

        try {
            final var formBody = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .add("client_id", clientId)
                    .add("client_secret", clientSecret)
                    .build();

            final var request = new Request.Builder()
                    .url(OpenverseAuth.TOKEN_URL)
                    .post(formBody)
                    .build();

            final var response = OpenverseAuth.client.newCall(request).execute();
            final var responseBody = response.body().string();

            final var jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getString("access_token");
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
