public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private final Coach coach;
    private final int count;

    public  CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public  Coach getCoach() {
        return  coach;
    }

    public int getCount() {
        return  count;
    }

    @Override
    public int compareTo(CounterOfTrainings other) {
        int countCompare = Integer.compare(other.count, this.count);
        if (countCompare != 0) return countCompare;

        Coach coach1 = this.coach;
        Coach coach2 = other.coach;

        int cmp = coach1.getSurname().compareTo(coach2.getSurname());
        if (cmp != 0) return cmp;

        cmp = coach1.getName().compareTo(coach2.getName());
        if (cmp != 0) return cmp;

        return coach1.getMiddleName().compareTo(coach2.getMiddleName());
    }
}
