package ordertracker;

class ScheduleEntry {
	Date plannedDate
	Boolean visited

	static belongsTo = [Client, Vendor]
}