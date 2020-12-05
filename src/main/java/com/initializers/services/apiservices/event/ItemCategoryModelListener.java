package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.item.ItemCategory;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class ItemCategoryModelListener extends AbstractMongoEventListener<ItemCategory>{

	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
    public ItemCategoryModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<ItemCategory> event) {
		if(event.getSource().getId() == null) {
			event.getSource().setId(sequenceGenerator.generateSequence(ItemCategory.SEQUENCE_NAME));
		}
    }
}
