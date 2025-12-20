import java.util.Collection;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Collection<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertTrue(mondaySessions.contains(singleTrainingSession));
        //Проверить, что за вторник не вернулось занятий
        Collection<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Collection<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(mondayChildTrainingSession, mondaySessions.iterator().next());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Collection<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        List<TrainingSession> thursdayList = new ArrayList<>(thursdaySessions);
        assertEquals(thursdayChildTrainingSession, thursdayList.getFirst());
        assertEquals(thursdayAdultTrainingSession, thursdayList.get(1));
        // Проверить, что за вторник не вернулось занятий
        Collection<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        TrainingSession found =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertNotNull(found);
        assertEquals(singleTrainingSession, found);
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        TrainingSession notFound =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertNull(notFound);
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Nванов", "Nван", "Nванович");
        Coach coach2 = new Coach("Cидоров", "Cидор", "Cидорвич");

        Group group = new Group("Йога", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(18, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());

        CounterOfTrainings first = result.get(0);
        assertEquals(coach1, first.getCoach());
        assertEquals(3, first.getCount());

        CounterOfTrainings second = result.get(1);
        assertEquals(coach2, second.getCoach());
        assertEquals(2, second.getCount());
    }
}