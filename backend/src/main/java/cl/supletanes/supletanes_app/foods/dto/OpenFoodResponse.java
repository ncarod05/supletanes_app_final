package cl.supletanes.supletanes_app.foods.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Modelo para la respuesta de Open Food Facts
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenFoodResponse {
    private Product product;

    // getters y setters
    @Data
    public static class Product {
        private String product_name;
        private Nutriments nutriments;

        // getters y setters
    }

    @Data
    public static class Nutriments {
        private Double energy_kcal_100g;
        private Double energy_100g; // alternativa
        private Double energy_kcal_serving; // otra variante
        private Double proteins_100g;
        private Double carbohydrates_100g;
        private Double fat_100g;

    }
}