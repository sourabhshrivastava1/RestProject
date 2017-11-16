package com.dvn.util;


public class Util {

	
	public static String removeComman(String field)
	{
		return field.replace(",", "");
	}
	
	
	public static void main(String[] args) {
		
		String Iid = ",image";
		
		System.out.println("Iid before remove commaa" +Iid);
	
	System.out.println("cid after remove commaa" +removeComman(Iid));
	
	
}}

