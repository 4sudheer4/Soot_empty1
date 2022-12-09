import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/** * * A simple example to send HTTP request from Java * */

public class httpcall_1 {
    public static void main(String args[]) throws IOException {
        URL url = new URL("http://www.google.com");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }
}