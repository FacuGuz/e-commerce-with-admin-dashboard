package guzman.SalesDashboard.dtos;

import lombok.Data;

@Data
public class CreateProductDTO {
    private String name;
    private String description;
    private Double price;
    private String imagePath;
    private Long categoryId;
}