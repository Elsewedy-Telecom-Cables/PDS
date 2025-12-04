package com.etc.pdsapp.services;

import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.OracleIntegration;
import com.etc.pdsapp.model.PdsReportResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiCaller {

    public static OracleIntegration callApi(String endpointUrl, String method, String jsonInput) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(endpointUrl))
                    .header("Content-Type", "application/json");

            switch (method.toUpperCase()) {
                case "POST":
                    requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonInput));
                    break;
                case "PUT":
                    requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(jsonInput));
                    break;
                case "GET":
                    requestBuilder.GET();
                    break;
                case "DELETE":
                    requestBuilder.DELETE();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported method: " + method);
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON to WorkOrder
            ObjectMapper mapper = new ObjectMapper();
//            OracleWrapper wrapper = mapper.readValue(response.body(), OracleWrapper.class);

            //
            List<OracleIntegration> wrapperList = mapper.readValue(response.body(), new TypeReference<List<OracleIntegration>>() {});
            OracleIntegration firstWrapper = wrapperList.isEmpty() ? null : wrapperList.get(0);

           // return firstWrapper.getWorkOrder();
            return firstWrapper;
           // return wrapper.getSub_workOrder();
            //    return response ;


        } catch (Exception ex) {
            Logging.logException("ERROR", ApiCaller.class.getName(), "callApi", ex);
            return null;
        }
    }


    public static PdsReportResponse fetchPdsReport(String workOrder) {
        String endpoint = ConfigLoader.getProperty("ORACLE.PDS.URL") + workOrder;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(endpoint))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {

              //  Logging.logError("API Error: " + response.statusCode() + " - " + response.body());
                System.out.println("API Error: " + response.statusCode() + " - " + response.body());
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), PdsReportResponse.class);

        } catch (Exception ex) {
            Logging.logException("ERROR", ApiCaller.class.getName(), "fetchPdsReport", ex);
            return null;
        }
    }



}




