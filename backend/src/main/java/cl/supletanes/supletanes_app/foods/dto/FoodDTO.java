package cl.supletanes.supletanes_app.foods.dto;

import lombok.Data;

@Data
public class FoodDTO {
    private String nombre;
    private Double calorias;
    private Double proteinas;
    private Double carbohidratos;
    private Double grasas;
}