package class2.a204.dto;

public class AnalysisDayDto {
    private String day;
    private Long count;

    public AnalysisDayDto(String day, Long count) {
        this.day = day;
        this.count = count;
    }

    public AnalysisDayDto() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
