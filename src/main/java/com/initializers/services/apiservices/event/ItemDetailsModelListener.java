package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.item.ItemDetails;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class ItemDetailsModelListener  extends AbstractMongoEventListener<ItemDetails>{
	@Autowired
	private SequenceGeneratorService sequenceGenerator;

	@Autowired
    public ItemDetailsModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

	@Override
	public void onBeforeConvert(BeforeConvertEvent<ItemDetails> event) {
		if (event.getSource().getId() == null) {
			event.getSource().setId(sequenceGenerator.generateSequence(ItemDetails.SEQUENCE_NAME));
		}
//		if(event.getSource().getDiscount() == null && event.getSource().getDiscountPrice() != null) {
//			event.getSource().setDiscount((long) Math.ceil((event.getSource().getActualPrice() - 
//					event.getSource().getDiscountPrice()) / event.getSource().getActualPrice() * 100));		
//		}
//		if(event.getSource().getDiscountPrice() == null && event.getSource().getDiscount() != null) {
//			event.getSource().setDiscountPrice(event.getSource().getActualPrice() - 
//					(float) Math.floor(event.getSource().getActualPrice() *	
//							((float)event.getSource().getDiscount()/100)));
//		}
	}
}
