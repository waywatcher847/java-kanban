import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    private Timetable timetable;
    private Coach coach1, coach2, coach3;
    private Group group, groupAdult;
    private TimeOfDay time0, time1, time2, time3, time4, time5;

    @BeforeEach
    void setUp() {
        timetable = new Timetable();

        // Тренеры
        coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        coach2 = new Coach("Иванов", "Иван", "Иванович");
        coach3 = new Coach("Сидоров", "Сидор", "Сидорович");

        // Группы
        group = new Group("Акробатика для детей", Age.CHILD, 60);
        groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        // Время
        time0 = new TimeOfDay(9, 15);
        time1 = new TimeOfDay(10, 0);
        time2 = new TimeOfDay(13, 0);
        time3 = new TimeOfDay(14, 0);
        time4 = new TimeOfDay(18, 30);
        time5 = new TimeOfDay(20, 0);
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        TrainingSession singleTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, time2);

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
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.THURSDAY, time5);

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession mondayChildTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, time2);
        TrainingSession thursdayChildTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.THURSDAY, time2);
        TrainingSession saturdayChildTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.SATURDAY, time1);

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
        TrainingSession singleTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, time2);

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> found = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time2);
        assertNotNull(found);
        assertEquals(singleTrainingSession, found.get(0), "MONDAY Ожидалось 1 занятие в 1300");
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> notFound = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time3);
        assertEquals(0, notFound.size(), "MONDAY Ожидалось 0 0 занятий в 1400");
    }

    @Test
    public void testSortedInsertion() {
        TrainingSession s12_1 = new TrainingSession(group, coach1, DayOfWeek.MONDAY, time2);
        TrainingSession s14 = new TrainingSession(group, coach1, DayOfWeek.MONDAY, time3);
        TrainingSession s12_2 = new TrainingSession(group, coach1, DayOfWeek.MONDAY, time2);
        TrainingSession s09 = new TrainingSession(group, coach1, DayOfWeek.MONDAY, time0);

        timetable.addNewTrainingSession(s14);
        timetable.addNewTrainingSession(s12_1);
        timetable.addNewTrainingSession(s12_2);
        timetable.addNewTrainingSession(s09);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(4, monday.size());

        assertEquals(9, monday.get(0).getTimeOfDay().getHours());
        assertEquals(15, monday.get(0).getTimeOfDay().getMinutes());

        assertEquals(13, monday.get(1).getTimeOfDay().getHours());
        assertEquals(0, monday.get(1).getTimeOfDay().getMinutes());

        assertEquals(13, monday.get(2).getTimeOfDay().getHours());
        assertEquals(0, monday.get(2).getTimeOfDay().getMinutes());

        assertEquals(14, monday.get(3).getTimeOfDay().getHours());
        assertEquals(0, monday.get(3).getTimeOfDay().getMinutes());
    }

    @Test
    public void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();

        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, time5);
        TrainingSession mondayChildTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, time5);
        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);

        List<TrainingSession> found = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time5);
        assertEquals(2, found.size(), "MONDAY Ожидалось 2 занятий в 2000");

    }

    @Test
    void testGetCountByCoachesShouldBeSorted() {
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, time1));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1, DayOfWeek.WEDNESDAY, time2));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.FRIDAY, time1));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, time1));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SATURDAY, time1));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach3, DayOfWeek.SUNDAY, time2));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(3, result.size(), "Должно быть 3 тренера");

        CounterOfTrainings c1 = result.get(0);
        CounterOfTrainings c2 = result.get(1);
        CounterOfTrainings c3 = result.get(2);

        assertTrue(coach2.getSurname().compareTo(coach3.getSurname()) < 0,
                "Иванов раньше Сидорова");

        assertEquals(coach2, c1.getCoach());
        assertEquals(1, c1.getCount());

        assertEquals(coach3, c2.getCoach());
        assertEquals(2, c2.getCount());

        assertEquals(coach1, c3.getCoach());
        assertEquals(3, c3.getCount());

    }

    @Test
    void testGetCountByCoachesShouldBeEmptyList() {
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertTrue(result.isEmpty(), "должен быть пустым");
    }

    @Test
    void testGetCountByCoachesShouldBe3() {
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, time1));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, time1));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, time1));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertEquals(1, result.size());
        CounterOfTrainings counter = result.get(0);
        assertEquals(coach1, counter.getCoach());
        assertEquals(3, counter.getCount());
    }
}