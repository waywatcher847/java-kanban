import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, List<TrainingSession>> schedule;

    public Timetable() {
        this.schedule = new EnumMap<>(DayOfWeek.class);
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        List<TrainingSession> list = schedule.get(day);
        if (list == null) {
            list = new ArrayList<TrainingSession>();
            schedule.put(day, list);
        }
        list.add(trainingSession);
        Collections.sort(list);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> list = schedule.get(dayOfWeek);
        return list == null ? Collections.emptyList() : new ArrayList<>(list);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek day, TimeOfDay time) {
        List<TrainingSession> list = schedule.get(day);
        if (list == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> result = new ArrayList<>();
        for (TrainingSession s : list) {
            if (s.getTimeOfDay().equals(time)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countMap = new HashMap<>();

        DayOfWeek[] days = DayOfWeek.values();
        for (DayOfWeek day : days) {
            List<TrainingSession> daySessions = schedule.get(day);
            if (daySessions != null) {
                for (TrainingSession daySession : daySessions) {
                    Coach coach = daySession.getCoach();
                    Integer cnt = countMap.get(coach);
                    countMap.put(coach, cnt == null ? 1 : cnt + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();

        Coach[] coaches = new Coach[countMap.size()];
        countMap.keySet().toArray(coaches);
        for (Coach coach : coaches) {
            result.add(new CounterOfTrainings(coach, countMap.get(coach)));
        }

        Collections.sort(result);
        return result;
    }
}