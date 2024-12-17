package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {

            System.out.print("Ingrese la moneda de origen (por ejemplo, USD): ");
            String monedaOrigen = scanner.nextLine().toUpperCase();


            System.out.print("Ingrese la moneda de destino (por ejemplo, EUR): ");
            String monedaDestino = scanner.nextLine().toUpperCase();


            System.out.print("Ingrese la cantidad a convertir: ");
            double cantidad = scanner.nextDouble();


            String apiKey = "9f9d91803c621d72a4232495";
            String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + monedaOrigen;


            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONObject rates = jsonResponse.getJSONObject("conversion_rates");


                if (rates.has(monedaDestino)) {
                    double tasaCambio = rates.getDouble(monedaDestino);
                    double resultado = cantidad * tasaCambio;

                    System.out.printf("Tasa de cambio %s -> %s: %.2f\n", monedaOrigen, monedaDestino, tasaCambio);
                    System.out.printf("Cantidad convertida: %.2f %s\n", resultado, monedaDestino);
                } else {
                    System.out.println("La moneda destino no está disponible en el API.");
                }
            } else {
                System.out.println("Error al conectar con el API. Código de estado: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
