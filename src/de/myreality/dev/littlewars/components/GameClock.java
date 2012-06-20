package de.myreality.dev.littlewars.components;

public class GameClock {
	
	public static final int US = 0, EU = 1;
	
	private int hours, minutes, format;
	private Timer counter;
	private boolean pm;
	
	public GameClock(int format) {
		this.format = format;				
		hours = 12;
		pm = true;
		minutes = 0;
		counter = new Timer();
	}
	
	public void start() {		
		counter.start();
	}

	public void tick(int delta) {
		counter.update(delta);
		if (counter != null) {
			if (counter.getMiliseconds() > 100) {
				++minutes;
				counter.reset();
			}
			
			if (minutes > 59) {
				minutes = 0;
				hours++;
			}
			
			switch (format) {
				case EU:
					if (hours > 23) {
						hours = 0;
					}
					break;
				case US:
					if (hours > 11) {
						hours = 0;
						pm = !pm;
					}
					break;
			}
		}		
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getTimeFormat() {
		return format;
	}
	
	
	public String getTimeString() {
		String content = "";
		
		String hourString = "";
		String minuteString = "";
		
		if (minutes < 10) {
			minuteString = "0" + String.valueOf(minutes);
		} else {
			minuteString = String.valueOf(minutes);
		}
		
		if (hours < 10) {
			hourString = "0" + String.valueOf(hours);
		} else {
			hourString = String.valueOf(hours);
		}
		
		switch (format) {
			case EU:
				content = hourString + ":" + minuteString + " Uhr";
				break;
			case US:
				if (pm) {
					content = hourString + ":" + minuteString + "pm";
				} else {
					content = hourString + ":" + minuteString + "am";
				}
				break;
		}
		return content;
	}
}
