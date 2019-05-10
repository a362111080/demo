package com.zero.egg.enums;


public class UserEnums {
	
	public enum Status {

		Normal("1","正常"),Disable("-1","离职");

	    private String index;
	    private String note;

	    Status(String index,String note){
	        this.index = index;
	        this.note = note;
	    }

	    public String index() {
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
	    
	    public static String index(String note) {
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

		Company(0, "企业端"), Pc(1, "PC客户端"), Boss(2, "Boss移动端"), Staff(3, "员工端"), Device(4, "设备端");
		
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
	public enum Sex {
		
		Man(0,"男"),Women(1,"女");
		
		private Integer index;
		private String note;
		
		Sex(Integer index,String note){
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
			Sex[] temp = Sex.values();
			for(int i=0;i<temp.length;i++){
				Sex item = temp[i];
				if(item.index.equals(index)){
					return item.note;
				}
			}
			return null;
		}
		
		public static Integer index(String note) {
			Sex[] temp = Sex.values();
			for(int i=0;i<temp.length;i++){
				Sex item = temp[i];
				if(item.note.equals(note)){
					return item.index;
				}
			}
			return null;
		}
	}
}
