package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.TransientUserDetails;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class TransientUserDetailsModelListener extends AbstractMongoEventListener<TransientUserDetails>{
	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
    public TransientUserDetailsModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<TransientUserDetails> event) {
		event.getSource().setId(sequenceGenerator.generateSequence(TransientUserDetails.SEQUENCE_NAME));
    }
}
