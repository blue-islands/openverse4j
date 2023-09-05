package openverse4j;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * OpenverseImageAPI provides methods to interact with the Openverse Image API.
 * It allows users to search for images using a specific query.
 */
public class OpenverseImageAPI {

    private static final String       BASE_URL = "https://api.openverse.engineering/v1/images/";

    private static final OkHttpClient client   = new OkHttpClient();

    /**
     * The main method to demonstrate the image search process.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(final String[] args) {

        final var accessToken = "YOUR_ACCESS_TOKEN"; // 事前に取得したアクセストークンを設定
        final var query = "test"; // 検索クエリ

        // 画像検索
        final var searchResult = OpenverseImageAPI.searchImages(accessToken, query);
        System.out.println(searchResult.toString());
    }


    /**
     * Searches for images on Openverse using the provided access token and query.
     *
     * @param accessToken The access token obtained from Openverse API.
     * @param query       The search query to find images.
     * @return A JSONObject containing the search results.
     */
    private static JSONObject searchImages(final String accessToken, final String query) {

        try {
            final var urlBuilder = HttpUrl.parse(OpenverseImageAPI.BASE_URL).newBuilder();
            urlBuilder.addQueryParameter("q", query);

            final var request = new Request.Builder()
                    .url(urlBuilder.build())
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            final var response = OpenverseImageAPI.client.newCall(request).execute();
            final var responseBody = response.body().string();

            return new JSONObject(responseBody);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
