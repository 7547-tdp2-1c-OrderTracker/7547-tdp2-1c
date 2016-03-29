package ordertracker;

class Vendor {
	static hasMany = [entries:ScheduleEntry]
	static belongsTo = [user:User]
	
	Boolean is_active;
}