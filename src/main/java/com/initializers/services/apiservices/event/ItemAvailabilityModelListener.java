package com.initializers.services.apiservices.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.item.ItemAvailability;
import com.initializers.services.apiservices.service.ItemCategoryService;
import com.initializers.services.apiservices.service.ItemSubCategoryService;
import com.initializers.services.apiservices.service.SequenceGeneratorService;

@Component
public class ItemAvailabilityModelListener extends AbstractMongoEventListener<ItemAvailability>{
	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	@Autowired
	private ItemCategoryService itemCategoryService;
	
	@Autowired
	private ItemSubCategoryService itemSubCategoryService;
	
	@Autowired
    public ItemAvailabilityModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
	
	@Override
    public void onBeforeConvert(BeforeConvertEvent<ItemAvailability> event) {
		if(event.getSource().getId() == null) {
			event.getSource().setId(sequenceGenerator.generateSequence(ItemAvailability.SEQUENCE_NAME));
		}
		//if both discount and discount price exist then consider discount and calc discount price
		if(event.getSource().getDiscount() != null && event.getSource().getDiscountPrice() != null) {
			event.getSource().setDiscountPrice(event.getSource().getActualPrice() - 
					(float) Math.floor(event.getSource().getActualPrice() *	
							((float)event.getSource().getDiscount()/100)));
			//update subcategory offer
			itemSubCategoryService.updateItemSubCategoryOffer(event.getSource().getItemId(),
					event.getSource().getDiscount());
			//update category offer
			itemCategoryService.updateItemCategoryOffer(event.getSource().getItemId(),
					event.getSource().getDiscount());
		}
		//if discount is equals to null then calc discount
		if(event.getSource().getDiscount() == null) {
			event.getSource().setDiscount((long) Math.ceil((event.getSource().getActualPrice() - 
					event.getSource().getDiscountPrice()) / event.getSource().getActualPrice() * 100));	
			itemSubCategoryService.updateItemSubCategoryOffer(event.getSource().getItemId(),
					event.getSource().getDiscount());
			itemCategoryService.updateItemCategoryOffer(event.getSource().getItemId(),
					event.getSource().getDiscount());
		}
		//if discount price is equal to  null then calc discount price
		if(event.getSource().getDiscountPrice() == null) {
			event.getSource().setDiscountPrice(event.getSource().getActualPrice() - 
					(float) Math.floor(event.getSource().getActualPrice() *	
							((float)event.getSource().getDiscount()/100)));
			itemSubCategoryService.updateItemSubCategoryOffer(event.getSource().getItemId(),
					event.getSource().getDiscount());
			itemCategoryService.updateItemCategoryOffer(event.getSource().getItemId(),
					event.getSource().getDiscount());
		}
    }
}
