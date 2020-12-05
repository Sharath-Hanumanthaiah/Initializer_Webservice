package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.Address;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class AddressModelListener extends AbstractMongoEventListener<Address>{
	
	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
    public AddressModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<Address> event) {
		//do not create new data for updating
		if( event.getSource().getId() == null) {
			event.getSource().setId(sequenceGenerator.generateSequence(Address.SEQUENCE_NAME));
		}
		
    }
}
