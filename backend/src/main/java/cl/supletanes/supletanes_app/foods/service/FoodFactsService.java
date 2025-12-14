package cl.supletanes.supletanes_app.foods.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cl.supletanes.supletanes_app.foods.dto.FoodDTO;

@Service
public class FoodFactsService {
    private final RestTemplate restTemplate = new RestTemplate();

    public FoodDTO getProductByBarcode(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v2/product/" + barcode + ".json";
        return restTemplate.getForObject(url, FoodDTO.class);
    }
}