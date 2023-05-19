package gov.cdc.nbs.entity.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NestedRace {
    private String raceCd;
    private String raceCategoryCd;
    private String raceDescTxt;
}
