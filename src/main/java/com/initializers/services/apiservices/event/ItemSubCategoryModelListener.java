package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.item.ItemSubCategory;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class ItemSubCategoryModelListener extends AbstractMongoEventListener<ItemSubCategory>{

	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
    public ItemSubCategoryModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<ItemSubCategory> event) {
		if(event.getSource().getId() == null) {
			event.getSource().setId(sequenceGenerator.generateSequence(ItemSubCategory.SEQUENCE_NAME));
		}
    }
}
