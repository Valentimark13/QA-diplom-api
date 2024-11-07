package dto;

import java.util.List;

public class OrderCreationDTO {
    public final List<String> ingredients;

    public OrderCreationDTO(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}