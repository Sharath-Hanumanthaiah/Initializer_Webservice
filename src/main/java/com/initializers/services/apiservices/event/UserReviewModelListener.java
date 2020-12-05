package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.UserReview;
import com.initializers.services.apiservices.service.SequenceGeneratorService;


public class UserReviewModelListener extends AbstractMongoEventListener<UserReview>{
	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
    public UserReviewModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<UserReview> event) {
//		event.getSource().setId(sequenceGenerator.generateSequence(UserReview.SEQUENCE_NAME));
    }
}
