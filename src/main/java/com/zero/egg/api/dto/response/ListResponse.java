package com.zero.egg.api.dto.response;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zero.egg.api.Apiconstants;
import com.zero.egg.api.dto.BaseResponse;

import lombok.Data;


public class ListResponse<T> extends BaseResponse<ListResponse.Body<T>> {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	public ListResponse(){
        this.code = Apiconstants.ResponseCode.SUCCESS;
        this.data = new ListResponse.Body<>();
    }

    public ListResponse(Integer code){
        this.code = code;
        this.data = new ListResponse.Body<>();
    }
    
    public ListResponse(Integer code,String msg){
        this.code = code;
        this.data = new ListResponse.Body<>();
    }

    public int getCode(){
        if((this.data.getData() == null || this.data.getData().size() == 0) && this.code == 200){
            this.code = Apiconstants.ResponseCode.NULL_DATA;
        }
        return this.code;
    }

    public String getMsg(){
        if((this.data.getData() == null || this.data.getData().size() == 0) && StringUtils.isBlank(this.msg)){
            this.msg = "暂时数据";
        }
        return this.msg;
    }
    @Data
    public static class Body<T> {

    	private int page = 1;
    	private int limit = 20;
        private Long total;
        private List<T> data;

        public Long getTotal() {
        	if((total == null || total == 0) && data !=null && data.size() > 0) {
        		total = (long)data.size();
        	}
			return total;
		}

		
    }
}
