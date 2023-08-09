package com.bsoftgroup.springcloudmsreactsecuritymongo.configuration.dto;

public class ApiResponse {
	
	private String statuscode;
	private String detalle;
	private String other;
	
	
	public ApiResponse(String statuscode, String detalle, String other) {
		super();
		this.statuscode = statuscode;
		this.detalle = detalle;
		this.other = other;
	}
	public String getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	

}
