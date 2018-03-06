/* 

Thesaurus.java implements a simple example to retrieve synonyms using web service http://thesaurus.altervista.org/service

===============================================================================
IMPORTANT NOTICE, please read:

This software is licensed under the terms of the GNU GENERAL PUBLIC LICENSE,
please read the enclosed file license.txt or http://www.gnu.org/licenses/licenses.html

Note that this software is freeware and it is not designed, licensed or intended
for use in mission critical, life support and military purposes.

The use of this software is at the risk of the user.
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.simple.JSONObject; // json package, download available at https://code.google.com/archive/p/json-simple/downloads
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class Thesaurus {
    final static String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";
    final static String api_key = "test_only";// replace "test_only" with your own key (http://thesaurus.altervista.org/mykey)

	public static void main(String[] args) {
		SendRequest("peace", "en_US", api_key);
    }

    public static void SendRequest(String word, String language, String key) {
        try {
            URL serverAddress = new URL(endpoint + "?word=" + URLEncoder.encode(word, "UTF-8") + "&language=" + language + "&key=" + key + "&output=json");
            HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection();
            connection.connect();
            int rc = connection.getResponseCode();
            if (rc == 200) {
                BufferedReader br  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line).append('\n');
                JSONObject obj = (JSONObject)JSONValue.parse(sb.toString());
                JSONArray array = (JSONArray)obj.get("response");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject list = (JSONObject)((JSONObject)array.get(i)).get("list");
                    System.out.println(list.get("category") + ":" + list.get("synonyms"));
                }
            } else {
				System.out.println("HTTP error:" + rc);
			}
            connection.disconnect();
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (java.net.ProtocolException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

} // end of Thesaurus
