package class2.a204.dto;

public class AnalysisRegionDto {
    private Integer region;
    private Long count;

    public AnalysisRegionDto(Integer region, Long count) {
        this.region = region;
        this.count = count;
    }

    public AnalysisRegionDto() {
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
