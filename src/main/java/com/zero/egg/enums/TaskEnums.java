package com.zero.egg.enums;


public class TaskEnums {
	
	public enum Status {

        Execute(0, "执行中"), CANCELED(1, "已取消"), Finish(2, "执行完成"), Unexecuted(3, "已暂停");

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
		
		Unload(1,"卸货"),Shipment(2,"出货"),Retail(3,"零售出货");
		
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
