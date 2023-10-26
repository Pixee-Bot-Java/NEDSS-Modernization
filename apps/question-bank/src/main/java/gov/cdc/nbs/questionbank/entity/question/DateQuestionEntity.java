package gov.cdc.nbs.questionbank.entity.question;

import static gov.cdc.nbs.questionbank.util.PageBuilderUtil.requireNonNull;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import gov.cdc.nbs.questionbank.question.command.QuestionCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue(DateQuestionEntity.DATE_QUESION_TYPE)
public class DateQuestionEntity extends WaQuestion {
    static final String DATE_QUESION_TYPE = "DATE";

    @Column(name = "mask", length = 50)
    private String mask;

    @Override
    public String getDataType() {
        return DateQuestionEntity.DATE_QUESION_TYPE;
    }

    public DateQuestionEntity(QuestionCommand.AddDateQuestion command) {
        super(command);

        this.mask = requireNonNull(command.mask().toString(), "Mask");
        this.futureDateIndCd = command.allowFutureDates() ? 'T' : 'F';


        // Audit
        created(command);

        // Reporting
        setReportingData(command);

        // Messaging
        setMessagingData(command.messagingData());
    }

    @Override
    public void update(QuestionCommand.Update command) {
        // Update general fields
        update(command.questionData());

        // Date fields
        this.mask = requireNonNull(command.mask(), "Mask");
        this.futureDateIndCd = command.allowFutureDates() ? 'T' : 'F';

        // Reporting
        setReportingData(command);

        // Messaging
        setMessagingData(command.messagingData());

        // Audit
        changed(command);
    }

}
