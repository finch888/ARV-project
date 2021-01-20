package Data;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


public class APIservice {

    private APIservice () {}

    private static final APIservice instance = new APIservice();

    public static APIservice getInstance() {
        return instance;
    }

    public String getEvents() {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL("https://api.tippmixpro.hu/api/sportsbetting/v2/events");
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory());
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Connection", "keep-alive");
            http.setRequestProperty("Accept", "application/json, text/plain, */*");
            http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
            http.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            http.setRequestProperty("Origin", "https://www.tippmixpro.hu");
            http.setRequestProperty("Sec-Fetch-Site", "same-site");
            http.setRequestProperty("Sec-Fetch-Mode", "cors");
            http.setRequestProperty("Sec-Fetch-Dest", "empty");
            http.setRequestProperty("Referer", "https://www.tippmixpro.hu/");
            http.setRequestProperty("Accept-Language", "hu-HU,hu;q=0.9,en-US;q=0.8,en;q=0.7,de;q=0.6");

            String data = "{\"sportId\":20,\"eventType\":\"prematch\",\"pageSize\":50}";

            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = http.getOutputStream();
            stream.write(out);

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());

            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            http.disconnect();
        } catch (IOException  ioe) {
            ioe.printStackTrace();
        }

        return response.toString();
    }

    private SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }

}
