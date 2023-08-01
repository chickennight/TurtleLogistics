package class2.a204.dto;

import class2.a204.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogisticAnalysisDTO {
    @JsonProperty("product_num")
    private Integer productNum;
    private Integer stock;
    private String name;
    @JsonProperty("year")
    private Integer yearAvg;
    @JsonProperty("month")
    private Integer monthAvg;
    @JsonProperty("week")
    private Integer weekAvg;
    @JsonProperty("today")
    private Integer todayAmount;
    @JsonProperty("error_message")
    private String errorMessage;

    public LogisticAnalysisDTO(Product product) {
        this.productNum = product.getProductNum();
        this.name = product.getName();
        this.stock = product.getStock();
        yearAvg = 0;
        monthAvg = 0;
        weekAvg = 0;
        todayAmount = 0;
        errorMessage = "";
    }
}
