package com.zero.egg.enums;


public class BrokenGoodsEnums {
	
	public enum Status {

		Normal(1,"未完成"),Disable(-1,"已完成");

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
	public enum Type {
		
		BrokenBySelf(1,"自损"),BrokenByCustomer(2,"报损");
		
		private Integer index;
		private String note;
		
		Type(Integer index,String note){
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
			Type[] temp = Type.values();
			for(int i=0;i<temp.length;i++){
				Type item = temp[i];
				if(item.index.equals(index)){
					return item.note;
				}
			}
			return null;
		}
		
		public static Integer index(String note) {
			Type[] temp = Type.values();
			for(int i=0;i<temp.length;i++){
				Type item = temp[i];
				if(item.note.equals(note)){
					return item.index;
				}
			}
			return null;
		}
	}
}
