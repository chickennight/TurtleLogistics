package class2.a204.dto;

public class AnalysisRegionDto {
    private String region;
    private Long count;

    public AnalysisRegionDto(String region, Long count) {
        this.region = region;
        this.count = count;
    }

    public AnalysisRegionDto() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
