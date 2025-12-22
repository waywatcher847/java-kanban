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
        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size(), "MONDAY Ожидалось 1 занятие");
        assertEquals(singleTrainingSession, monday.get(0), "Занятие не совпадает");
        //Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesday.isEmpty(), "TUESDAY Ожидалось 0 занятий");
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
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "MONDAY Ожидалось 1 занятие");
        assertEquals(mondayChildTrainingSession, mondaySessions.get(0), "MONDAY Занятие не совпадает");
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size(), "THURSDAY Ожидалось 2 занятий");
        List<TrainingSession> thursdayList = new ArrayList<>(thursdaySessions);
        assertEquals(thursdayChildTrainingSession, thursdayList.get(0), "THURSDAY Ожидалось 1300");
        assertEquals(thursdayAdultTrainingSession, thursdayList.get(1), "THURSDAY  Ожидалось 2000");
        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "TUESDAY Ожидалось 0 занятий");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> found = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertNotNull(found);
        assertEquals(singleTrainingSession, found.get(0), "MONDAY Ожидалось 1 занятие в 1300");
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> notFound = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertEquals(0, notFound.size(), "MONDAY Ожидалось 0 0 занятий в 1400");
    }

    @Test
    public void testSortedInsertion() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession s12_1 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(12, 0));
        TrainingSession s14 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(14, 30));
        TrainingSession s12_2 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(12, 0));
        TrainingSession s09 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 15));

        timetable.addNewTrainingSession(s14);
        timetable.addNewTrainingSession(s12_1);
        timetable.addNewTrainingSession(s12_2);
        timetable.addNewTrainingSession(s09);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(4, monday.size());

        assertEquals(9, monday.get(0).getTimeOfDay().getHours());
        assertEquals(15, monday.get(0).getTimeOfDay().getMinutes());

        assertEquals(12, monday.get(1).getTimeOfDay().getHours());
        assertEquals(0, monday.get(1).getTimeOfDay().getMinutes());

        assertEquals(12, monday.get(2).getTimeOfDay().getHours());
        assertEquals(0, monday.get(2).getTimeOfDay().getMinutes());

        assertEquals(14, monday.get(3).getTimeOfDay().getHours());
        assertEquals(30, monday.get(3).getTimeOfDay().getMinutes());
    }

    @Test
    public void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);

        List<TrainingSession> found = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        assertEquals(2, found.size(), "MONDAY Ожидалось 2 занятий в 2000");

    }
}