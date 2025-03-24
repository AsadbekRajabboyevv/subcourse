package uz.asadbek.course.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AverageRatingDTO {
    private int countValuer;
    private int oneStarPercent;
    private int twoStarPercent;
    private int threeStarPercent;
    private int fourStarPercent;
    private int fiveStarPercent;
    private float averageStar;
}
