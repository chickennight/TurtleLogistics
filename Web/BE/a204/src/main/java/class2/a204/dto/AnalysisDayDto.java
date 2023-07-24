package class2.a204.dto;

public class AnalysisDayDto {
    private Integer day;
    private Integer count;

    public AnalysisDayDto(Integer day, Integer count) {
        this.day = day;
        this.count = count;
    }

    public AnalysisDayDto() {
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
