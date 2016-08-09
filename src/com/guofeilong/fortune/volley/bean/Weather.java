package com.guofeilong.fortune.volley.bean;


	public class Weather {

		private Weatherinfo weatherinfo;

		public Weatherinfo getWeatherinfo() {
			return weatherinfo;
		}

		public void setWeatherinfo(Weatherinfo weatherinfo) {
			this.weatherinfo = weatherinfo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((weatherinfo == null) ? 0 : weatherinfo.hashCode());
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
			Weather other = (Weather) obj;
			if (weatherinfo == null) {
				if (other.weatherinfo != null)
					return false;
			} else if (!weatherinfo.equals(other.weatherinfo))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Weather [weatherinfo=" + weatherinfo + "]";
		}

	}
