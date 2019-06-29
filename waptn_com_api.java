import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;


public class WaptnApi {

    /** 短视频解析接口地址 */
    private static final String waptnVideoURL = "http://www.waptn.com/api/index.php";

    /** 分配的客户ID,用户后台http://www.waptn.com/openapi.php 查看 */
    private static final String clientId = "58d4259475f52a55";

    /** 分配的客户密钥,用户后台http://www.waptn.com/openapi.php 查看 */
    private static final String clientSecretKey = "4e426e4d1de2475763057c8383fb62a2";

    /** 实例HttpClient，根据需要自行调参 */
    private static HttpClient httpClient = createHttpClient(100, 20, 10000, 2000, 2000);


    public static void main(String[] args) {
        String response = downloadVideo("http://v.XXXX.com/hq8aoA/");
        System.out.println(response);
    }


    public static String downloadVideo(String link) {
        Long timestamp = System.currentTimeMillis();
        String sign = DigestUtils.md5Hex(link + timestamp + clientSecretKey);
        Map<String, String> params = new HashMap<>();
        params.put("link", link);
        params.put("clientId", clientId);
        params.put("timestamp", String.valueOf(timestamp));
        params.put("sign", sign);
        return sendPost(httpClient, waptnVideoURL, params, Consts.UTF_8);
    }

    /**
     * 发送post请求
     * @param url 请求地址
     * @param params 请求参数
     * @param encoding 编码
     */
    public static String sendPost(HttpClient httpClient, String url, Map<String, String> params, Charset encoding) {
        String resp = "";
        HttpPost httpPost = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> formParams = new ArrayList<>();
            Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formParams, encoding);
            httpPost.setEntity(postEntity);
        }
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            resp = EntityUtils.toString(response.getEntity(), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resp;
    }


    /**
     * 实例化HttpClient
     * @param maxTotal
     * @param maxPerRoute
     * @param socketTimeout
     * @param connectTimeout
     * @param connectionRequestTimeout
     * @return
     */
    public static HttpClient createHttpClient(int maxTotal, int maxPerRoute, int socketTimeout, int connectTimeout,
                                              int connectionRequestTimeout) {
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(defaultRequestConfig).build();
        return httpClient;
    }
}
