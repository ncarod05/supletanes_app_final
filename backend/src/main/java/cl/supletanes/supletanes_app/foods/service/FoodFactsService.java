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

            // Fallback para calorías y demas
            Double calorias = n.getEnergy_kcal_100g();
            if (calorias == null || calorias == 0.0)
                calorias = n.getEnergy_100g();
            if (calorias == null || calorias == 0.0)
                calorias = n.getEnergy_kcal_serving();

            Double proteinas = n.getProteins_100g();
            if (proteinas == null || proteinas == 0.0)
                proteinas = n.getProteins_serving();

            Double carbohidratos = n.getCarbohydrates_100g();
            if (carbohidratos == null || carbohidratos == 0.0)
                carbohidratos = n.getCarbohydrates_serving();

            Double grasas = n.getFat_100g();
            if (grasas == null || grasas == 0.0)
                grasas = n.getFat_serving();

            return new FoodDTO(
                    p.getProduct_name(),
                    calorias != null ? calorias : 0.0,
                    proteinas != null ? proteinas : 0.0,
                    carbohidratos != null ? carbohidratos : 0.0,
                    grasas != null ? grasas : 0.0
                );
        }
        return new FoodDTO(null, 0.0, 0.0, 0.0, 0.0);
    }
}