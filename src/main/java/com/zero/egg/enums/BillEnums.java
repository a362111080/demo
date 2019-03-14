package com.zero.egg.enums;


public class BillEnums {
	
	public enum Status {

		Normal(1,"挂账"),Disable(-1,"销账");

	    private Integer index;
	    private String note;

	    Status(Integer index,String note){
	        this.index = index;
	        this.note = note;
	    }

	    public Integer index() {
	        return index;
	    }

	    public String note() {
	        return note;
	    }

	    public static String note(Integer index){
	    	Status[] temp = Status.values();
	        for(int i=0;i<temp.length;i++){
	        	Status item = temp[i];
	            if(item.index.equals(index)){
	                return item.note;
	            }
	        }
	        return null;
	    }
	    
	    public static Integer index(String note) {
	    	Status[] temp = Status.values();
	    	for(int i=0;i<temp.length;i++){
	        	Status item = temp[i];
	            if(item.note.equals(note)){
	                return item.index;
	            }
	        }
	        return null;
		}
	}
}
