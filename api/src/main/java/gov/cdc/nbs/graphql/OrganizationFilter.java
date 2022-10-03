package gov.cdc.nbs.graphql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationFilter {
    private GraphQLPage page = new GraphQLPage(50, 0);
    private Long id;
    private String displayNm;
    private String streetAddr1;
    private String streetAddr2;
    private String cityCd;
    private String cityDescTxt;
    private String stateCd;
    private String zipCd;
}
