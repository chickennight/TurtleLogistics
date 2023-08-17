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

    public void addStatus(AnalysisGetDTO analysisGetDTO) {
        this.yearAvg = analysisGetDTO.getYearAvg().intValue() / 365;
        this.monthAvg = analysisGetDTO.getMonthAvg().intValue() / 30;
        this.weekAvg = analysisGetDTO.getWeekAvg().intValue() / 7;
        this.todayAmount = analysisGetDTO.getTodayAmount().intValue();
        if (stock <= 7 * Math.max(Math.max(yearAvg, monthAvg), weekAvg))
            errorMessage = ("재고 소진 임박");

        if (todayAmount >= 3 * Math.max(Math.max(yearAvg, monthAvg), weekAvg)) {
            if (errorMessage.isEmpty())
                errorMessage = "주문 폭주";
            else
                errorMessage = "주문 폭주! 재고 즉시 확인";
        }
    }
}
