
public class Entry {
	String date;
	float temperature;
	float humidity;
	float precipitation;
	
	
	public Entry() {}
	public void setTemperature(float t) {
		this.temperature=t;
	}
	public float getTemperature() {
		return this.temperature;
	}
	public void setHumidity(float t) {
		this.humidity=t;
	}
	public float getHumidity() {
		return this.humidity;
	}
	public void setPrecipitation(float t) {
		this.precipitation=t;
	}
	public float getPrecipitation() {
		return this.precipitation;
	}
	public void setDate(String d) {
		this.date=d;
	}
	public String getDate() {
		return this.date;
	}
	
}
