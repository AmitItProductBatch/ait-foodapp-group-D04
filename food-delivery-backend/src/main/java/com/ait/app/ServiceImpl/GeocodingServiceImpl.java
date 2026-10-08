package com.ait.app.ServiceImpl;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.GeocodingService;
import com.ait.app.customExceptionHandler.PriceCalculationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeocodingServiceImpl implements GeocodingService {

     final HttpClient httpClient = HttpClient.newHttpClient();

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public double[] getCoordinates(String address) {

        try {
        	
        	String addr=address+", Maharashtra, India";
        	

            System.out.println("Geocoding address....: " + addr);

//            String encodedAddress = URLEncoder.encode(addr, StandardCharsets.UTF_8);
            String encodedAddress = URLEncoder.encode("Pune, Maharashtra, India", StandardCharsets.UTF_8);

            String url = "https://nominatim.openstreetmap.org/search"
                    + "?q=" + encodedAddress
                    + "&format=jsonv2"
                    + "&limit=1";

            System.out.println("Nominatim URL: " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent",
                            "FoodDeliveryApp/1.0 (archana@gmail.com)")
                    .header("Accept", "application/json")
                    .header("Accept-Language", "en")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());

            System.out.println("Nominatim HTTP status: "+ response.statusCode());

            System.out.println("Nominatim response: " + response.body());

            
            if (response.statusCode() != 200
                    || response.body().equals("[]")) {

                throw new PriceCalculationException("Address could not be resolved",HttpStatus.BAD_REQUEST);
            }

            List<Map<String, Object>> result =objectMapper.readValue( response.body(),
                            new TypeReference<List<Map<String, Object>>>() {}
                    );

            double latitude =Double.parseDouble(result.get(0).get("lat").toString());

            double longitude = Double.parseDouble(result.get(0).get("lon").toString());

            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);

            return new double[] { latitude, longitude };

        } 
        catch (PriceCalculationException p) {
            throw p;
        }
         catch (Exception e) {

        	 throw new PriceCalculationException("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}