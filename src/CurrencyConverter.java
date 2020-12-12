import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConverter {

    public static void main(String [] args) throws IOException {

        HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();

        //add currency codes
        currencyCodes.put(1, "EUR");
        currencyCodes.put(2, "USD");
        currencyCodes.put(3, "BRL");
        currencyCodes.put(4, "GBP");
        currencyCodes.put(5, "JPY");

        String fromCode, toCode;
        double amount;

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the currency converter!");

        System.out.println("Currency converting FROM?");
        System.out.println("1: EUR (Euro) \t 2: USD (US Dollar) \t 3: BRL (Brazilian Real) \t 4: GBP (Pound Sterling) \t" +
                "5: JPY (Japanese Yen)");
        fromCode = currencyCodes.get(sc.nextInt());

        System.out.println("Currency converting TO?");
        System.out.println("1: EUR (Euro) \t 2: USD (US Dollar) \t 3: BRL (Brazilian Real) \t 4: GBP (Pound Sterling) \t" +
                "5: JPY (Japanese Yen)");
        toCode = currencyCodes.get(sc.nextInt());

        System.out.println("Amount you wish to convert?");
        amount = sc.nextDouble();

        sendHttpGETRequest(fromCode, toCode, amount);

        System.out.println("Thank you for using the currency converter!");
    }

    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException {

        DecimalFormat f = new DecimalFormat("00.00");
        String GET_URL = "https://api.exchangeratesapi.io/latest?base=" + toCode + "&symbols=" + fromCode;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK) { //success

            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in.close();

            JSONObject obj = new JSONObject(response.toString());
            Double exchangeRate = obj.getJSONObject("rates").getDouble(fromCode);
            System.out.println(obj.getJSONObject("rates"));
            System.out.println(exchangeRate); //keep for debugging
            System.out.println();
            System.out.println(f.format(amount) + " " + fromCode + " = " + f.format(amount/exchangeRate) + " " + toCode);
        } else {
            System.out.println("GET request failed");
        }
    }
}
