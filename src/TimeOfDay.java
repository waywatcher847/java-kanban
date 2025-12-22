import java.util.Objects;

public class TimeOfDay implements Comparable<TimeOfDay> {

    //часы (от 0 до 23)
    private int hours;
    //минуты (от 0 до 59)
    private int minutes;

    public TimeOfDay(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeOfDay that)) return false;
        return hours == that.getHours() && minutes == that.getMinutes();
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        return hours + ":" + minutes;
    }

    @Override
    public int compareTo(TimeOfDay other) {
        if (other == null) return 1;

        if (other.getHours() != hours) {
            return other.getHours() > hours ? -1 : 1;
        }

        if (other.getMinutes() != minutes) {
            return other.getMinutes() > minutes ? -1 : 1;
        }

        return 0;
    }
}

