public class TrainingSession implements Comparable<TrainingSession> {

    //группа
    private Group group;
    //тренер
    private Coach coach;
    //день недели
    private DayOfWeek dayOfWeek;
    //время начала занятия
    private TimeOfDay timeOfDay;

    public TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        this.group = group;
        this.coach = coach;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    @Override
    public int compareTo(TrainingSession other) {
        if (other == null) {
            return 1;
        }

        TimeOfDay thisTime = this.timeOfDay;
        TimeOfDay otherTime = other.getTimeOfDay();

        int hoursDiff = thisTime.getHours() - otherTime.getHours();
        if (hoursDiff != 0) {
            return hoursDiff;
        }

        return thisTime.getMinutes() - otherTime.getMinutes();
    }
}
