package cl.supletanes.supletanes_app.foods.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cl.supletanes.supletanes_app.foods.dto.FoodDTO;
import cl.supletanes.supletanes_app.foods.dto.OpenFoodResponse;

@Service
public class FoodFactsService {
    private final RestTemplate restTemplate = new RestTemplate();

    public FoodDTO getProductByBarcode(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v2/product/" + barcode + ".json";
        OpenFoodResponse response = restTemplate.getForObject(url, OpenFoodResponse.class);

        if (response != null && response.getProduct() != null) {
            OpenFoodResponse.Product p = response.getProduct();
            OpenFoodResponse.Nutriments n = p.getNutriments();

            // Fallback para calorías
            Double calorias = n.getEnergy_kcal_100g();
            if (calorias == null || calorias == 0.0) {
                calorias = n.getEnergy_100g();
            }
            if (calorias == null || calorias == 0.0) {
                calorias = n.getEnergy_kcal_serving();
            }
            if (calorias == null) {
                calorias = 0.0;
            }

            return new FoodDTO(
                    p.getProduct_name(),
                    n.getEnergy_kcal_100g(),
                    n.getProteins_100g(),
                    n.getCarbohydrates_100g(),
                    n.getFat_100g());
        }
        return new FoodDTO(null, 0.0, 0.0, 0.0, 0.0);
    }
}