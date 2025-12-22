import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> schedule;

    public Timetable() {
        this.schedule = new EnumMap<>(DayOfWeek.class);
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {

        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        /* IDE настаивает на computeIfAbsent (Alt+Shift+Enter) */
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = schedule.computeIfAbsent(day, k -> new TreeMap<>());
        /* IDE настаивает на computeIfAbsent (Alt+Shift+Enter) */
        List<TrainingSession> sessionsAtTime = dayMap.computeIfAbsent(time, k -> new ArrayList<>());

        sessionsAtTime.add(trainingSession);
        Collections.sort(sessionsAtTime);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = schedule.get(dayOfWeek);
        if (dayMap == null || dayMap.isEmpty()) {
            return Collections.emptyList();
        }
        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> list : dayMap.values()) {
            result.addAll(list);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek day, TimeOfDay time) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = schedule.get(day);
        if (dayMap == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> list = dayMap.get(time);
        return list == null ? Collections.emptyList() : new ArrayList<>(list);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countMap = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : schedule.values()) {
            if (dayMap == null) continue;
            for (List<TrainingSession> sessions : dayMap.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    /* IDE настаивает на merge method call Alt+Shift+Enter */
                    countMap.merge(coach, 1, Integer::sum);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : countMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(result);
        return result;
    }
}