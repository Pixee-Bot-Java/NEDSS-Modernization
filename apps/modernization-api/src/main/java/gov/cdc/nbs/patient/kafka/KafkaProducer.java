package gov.cdc.nbs.patient.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import gov.cdc.nbs.message.patient.event.PatientRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, PatientRequest> kafkaPatientEventTemplate;

    @Value("${kafkadef.topics.patient.request}")
    private String patientTopic;

    public void requestPatientEventEnvelope(final PatientRequest request) {

        kafkaPatientEventTemplate.send(patientTopic, request.requestId(), request).addCallback(
                new ListenableFutureCallback<>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("Failed to send message key={} message={} error={}", request.requestId(), request,
                                ex);
                    }

                    @Override
                    public void onSuccess(SendResult<String, PatientRequest> result) {
                        log.info("Sent message key={} message={} result={}", request.requestId(), request, result);
                    }
                });
    }

}
