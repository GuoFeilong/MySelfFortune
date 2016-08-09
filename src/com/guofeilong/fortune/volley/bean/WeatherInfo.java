package com.guofeilong.fortune.volley.bean;

/**
 * 天气对象
 * 
 * @author guofl
 * 
 */
class Weatherinfo {
	String city;
	String cityid;
	String temp;
	String WD;
	String time;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityID() {
		return cityid;
	}

	public void setCityID(String cityID) {
		this.cityid = cityID;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getWind() {
		return WD;
	}

	public void setWind(String wind) {
		this.WD = wind;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((cityid == null) ? 0 : cityid.hashCode());
		result = prime * result + ((temp == null) ? 0 : temp.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((WD == null) ? 0 : WD.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Weatherinfo other = (Weatherinfo) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (cityid == null) {
			if (other.cityid != null)
				return false;
		} else if (!cityid.equals(other.cityid))
			return false;
		if (temp == null) {
			if (other.temp != null)
				return false;
		} else if (!temp.equals(other.temp))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (WD == null) {
			if (other.WD != null)
				return false;
		} else if (!WD.equals(other.WD))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Weatherinfo [city=" + city + ", cityID=" + cityid + ", temp=" + temp + ", wind=" + WD + ", time=" + time + "]";
	}

}
