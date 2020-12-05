package com.initializers.services.apiservices.repo.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;

import com.initializers.services.apiservices.model.ItemCategorySubCategory;
import com.initializers.services.apiservices.repo.ItemCategoryCustomRepo;

public class ItemCategoryCustomRepoImpl implements ItemCategoryCustomRepo {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ItemCategorySubCategory> findItemCategoryJoinSubCategory(Pageable pageable) {
		// TODO Auto-generated method stub
		LookupOperation lookupOperation = LookupOperation.newLookup().from("item_sub_category").localField("_id")
				.foreignField("categoryId").as("cat_join_subcat");
		
		ProjectionOperation projection = Aggregation.project(Fields.fields("_id","name","description","offer","imageLink").and(Fields.field("subCategoryID", "cat_join_subcat._id")));
//		.and(Fields.field("departments._id", "subID"))
//		ProjectionOperation projection = Aggregation.project("_id","name","description","offer","imageLink","departments.categoryId");
//		GroupOperation sumZips = Aggregation.group("departments").count().as("zipCount");
//		
//		AggregationOperation aggunwind = Aggregation.unwind("departments");
		  
		Aggregation aggregation = Aggregation.newAggregation(lookupOperation, projection,Aggregation.skip(pageable.getPageNumber() * pageable.getPageSize()),
				Aggregation.limit(pageable.getPageSize()));
		List<ItemCategorySubCategory> results = mongoTemplate.aggregate(aggregation, "item_category", ItemCategorySubCategory.class)
				.getMappedResults();
		return results;
	}

}
