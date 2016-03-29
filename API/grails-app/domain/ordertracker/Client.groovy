package ordertracker;

class Client {
	String name
	String lastname

	String cuil
	String address

	Float lon;
	Float lat;

	String email;

	static hasMany = [entries:ScheduleEntry]
}
