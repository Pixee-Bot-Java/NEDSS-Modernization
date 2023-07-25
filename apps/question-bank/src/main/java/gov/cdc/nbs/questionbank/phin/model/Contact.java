package gov.cdc.nbs.questionbank.phin.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Contact {

    private List<Telecom> telecom;

}