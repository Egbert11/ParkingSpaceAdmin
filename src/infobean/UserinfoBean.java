package infobean;

import java.util.Date;

/**
 * 
 * @author huangjiaming
 * 车主和业主的信息
 */
public class UserinfoBean {
	private String objId;
	private String phone;
	private String name;
	private String password;
	private boolean isValid;
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
}
