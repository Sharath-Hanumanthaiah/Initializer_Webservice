package com.initializers.services.apiservices.others;

public class JavaTestApp {

	public static void main(String[] args) {
		
		Float actualPrice = 189F;
//		Float discountPrice = 179F;
//		
//		Long tempDiscount = (long) Math.ceil((actualPrice - discountPrice) / actualPrice * 100);
//		
//		System.out.println(tempDiscount);
		
		Long discount = 6L;
	
		Float tempdiscountPrice = (float) Math.floor(actualPrice * ((float)discount/100));
		
		Float discountPrice = actualPrice - tempdiscountPrice;
		
		System.out.println(actualPrice +" "+discountPrice);
		
		
	}

}
