import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {

    public static double getExchangeRate(String baseCurrency, String targetCurrency) throws IOException {
        String urlStr = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        String responseBody = response.toString();
        double exchangeRate = parseExchangeRate(responseBody, targetCurrency);
        return exchangeRate;
    }

    public static double parseExchangeRate(String responseBody, String targetCurrency) {
        int start = responseBody.indexOf(targetCurrency) + 5;
        int end = responseBody.indexOf(",", start);
        String rateStr = responseBody.substring(start, end);
        return Double.parseDouble(rateStr);
    }

    public static double convertCurrency(double amount, double exchangeRate) {
        return amount * exchangeRate;
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the base currency code: ");
            String baseCurrency = reader.readLine().toUpperCase();

            System.out.print("Enter the target currency code: ");
            String targetCurrency = reader.readLine().toUpperCase();

            System.out.print("Enter the amount to convert: ");
            double amount = Double.parseDouble(reader.readLine());

            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
            double convertedAmount = convertCurrency(amount, exchangeRate);

            System.out.printf("%.2f %s is equal to %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
