package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.UserDetails;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class UserDetailsModelListener extends AbstractMongoEventListener<UserDetails>{
	
	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
    public UserDetailsModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<UserDetails> event) {
		if(event.getSource().getId() == null) {
			event.getSource().setId(sequenceGenerator.generateSequence(UserDetails.SEQUENCE_NAME));
		}
    }
}
