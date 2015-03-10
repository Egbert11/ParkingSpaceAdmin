package infobean;

/**
 * @author huangjiaming
 * 保存小区的各类信息：
 * communityId: 小区id
 * address: 小区地址
 * latitude: 小区纬度
 * longitude： 小区经度
 */
public class CommunityInfoBean {
	private String communityId;
	private String address;
	private Number latitude;
	private Number longitude;
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Number getLatitude() {
		return latitude;
	}
	public void setLatitude(Number latitude) {
		this.latitude = latitude;
	}
	public Number getLongitude() {
		return longitude;
	}
	public void setLongitude(Number longitude) {
		this.longitude = longitude;
	}
}
