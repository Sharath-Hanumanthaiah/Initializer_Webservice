package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.UserOrderSet;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class UserOrderModelListener extends AbstractMongoEventListener<UserOrderSet>{

	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
    public UserOrderModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<UserOrderSet> event) {
		if(event.getSource().getId() == null) {
			event.getSource().setId(sequenceGenerator.generateSequence(UserOrderSet.SEQUENCE_NAME));
		}
    }
}
