import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import java.util.*;

public class ShamirSecretSharingSolver {
    public static void main(String[] args) {
        String filePath = "input.json"; // Change this to your actual JSON file path

        try {
            // Read the JSON file
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject jsonObject = new JSONObject(content);

            // Extract keys
            JSONObject keys = jsonObject.getJSONObject("keys");
            int k = keys.getInt("k"); // Minimum required points
            Map<Integer, BigInteger> points = new HashMap<>();

            // Read and decode values
            for (String key : jsonObject.keySet()) {
                if (key.equals("keys")) continue;

                JSONObject rootData = jsonObject.getJSONObject(key);
                int base = rootData.getInt("base");
                String value = rootData.getString("value");

                // Convert value from its given base to decimal
                BigInteger decimalValue = new BigInteger(value, base);
                int x = Integer.parseInt(key); // Key is treated as X-coordinate

                points.put(x, decimalValue);

                // Stop after collecting k points
                if (points.size() == k) break;
            }

            // Compute the secret (constant term 'c') using Lagrange interpolation
            BigInteger secretC = lagrangeInterpolation(points);
            System.out.println("Secret (Constant term c): " + secretC);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid JSON input: " + e.getMessage());
        }
    }

    private static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points) {
        BigInteger result = BigInteger.ZERO;
        List<Integer> xValues = new ArrayList<>(points.keySet());

        for (int i = 0; i < xValues.size(); i++) {
            int xi = xValues.get(i);
            BigInteger yi = points.get(xi);
            BigInteger term = yi;

            // Compute Lagrange basis polynomial
            for (int j = 0; j < xValues.size(); j++) {
                if (i == j) continue;
                int xj = xValues.get(j);

                BigInteger numerator = BigInteger.valueOf(-xj);
                BigInteger denominator = BigInteger.valueOf(xi - xj);
                term = term.multiply(numerator).divide(denominator);
            }

            result = result.add(term);
        }
        return result;
    }
}
