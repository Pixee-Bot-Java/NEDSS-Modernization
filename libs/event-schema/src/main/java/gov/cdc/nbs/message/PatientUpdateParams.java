package gov.cdc.nbs.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PatientUpdateParams {
	private PatientInput updatePerson;
	private List<TemplateInput> templateInputs;

}