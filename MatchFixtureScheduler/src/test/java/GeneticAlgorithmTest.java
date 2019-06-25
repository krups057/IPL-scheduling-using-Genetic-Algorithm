import geneticalgorithm.Data;
import geneticalgorithm.Population;
import geneticalgorithm.domain.Fixture;
import geneticalgorithm.domain.Location;
import geneticalgorithm.domain.Schedule;
import geneticalgorithm.domain.Team;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * This class contains test cases for testing the fitness function
 *
 * @author Gokul Anantha Narayanan, Krupashankar Sundararajan, Raghavan Renganathan
 * @version 1.0
 * @since 04/10/2018
 */
public class GeneticAlgorithmTest {
    private Data data;

    /**
     * Sets up data for each test case
     */
    @Before
    public void setUp() {
        Location location1 = new Location("Chennai");
        Location location2 = new Location("Banglore");
        Location location3 = new Location("Kolkata");

        Team team1 = new Team("Chennai Super Kings", location1);
        Team team2 = new Team("Royal Challengers Banglore", location2);
        Team team3 = new Team("Kolkata Knight Riders", location3);

        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();
        Date date4 = new Date();
        Date date5 = new Date();
        Date date6 = new Date();
        Date date7 = new Date();
        Date date8 = new Date();
        Date date9 = new Date();
        Date date10 = new Date();
        Date date11 = new Date();
        Date date12 = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date1 = format.parse("04/15/2018");
            date2 = format.parse("04/16/2018");
            date3 = format.parse("04/17/2018");
            date4 = format.parse("04/18/2018");
            date5 = format.parse("04/19/2018");
            date6 = format.parse("04/20/2018");
            date7 = format.parse("04/21/2018");
            date8 = format.parse("04/22/2018");
            date9 = format.parse("04/23/2018");
            date10 = format.parse("04/24/2018");
            date11 = format.parse("04/25/2018");
            date12 = format.parse("04/26/2018");
        } catch (Exception e) {
            System.out.println("Exception occurred when parsing Date: " + e.getMessage());
        }

        data = new Data();
        data.getTeamList().clear();
        data.getTeamList().addAll(Arrays.asList(team1, team2, team3));

        data.getLocationList().clear();
        data.getLocationList().addAll(Arrays.asList(location1, location2, location3));

        data.getDates().clear();
        data.getDates().addAll(Arrays.asList(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10, date11, date12));

        HashMap<String, HashMap<Location, Integer>> weather = data.getWeather();
        weather.clear();
        for (Date date : data.getDates()) {
            HashMap<Location, Integer> index = new HashMap<>();
            for (Location loc : Arrays.asList(location1, location2, location3)) {
                index.put(loc, 0);
            }
            weather.put(Data.dateFormat.format(date), index);
        }
    }

    /**
     * This test case tests the fitness function for 0 conflict solution
     */
    @Test
    public void FitnessTest1() {
        Population testPop = new Population(10, data, false);
        ArrayList<Schedule> scheduleList = testPop.getSchedules();
        scheduleList.add(new Schedule());
        Schedule s1 = scheduleList.get(0);
        s1.setData(data);
        s1.setWeatherIndex(data.getWeather());

        Location location1 = data.getLocationList().get(0);
        Location location2 = data.getLocationList().get(1);
        Location location3 = data.getLocationList().get(2);

        Team team1 = data.getTeamList().get(0);
        Team team2 = data.getTeamList().get(1);
        Team team3 = data.getTeamList().get(2);

        Date date1 = data.getDates().get(0);
        Date date2 = data.getDates().get(2);
        Date date3 = data.getDates().get(4);
        Date date4 = data.getDates().get(6);
        Date date5 = data.getDates().get(8);
        Date date6 = data.getDates().get(10);

        Fixture f1 = new Fixture(date1, team1, team2, location1);
        Fixture f2 = new Fixture(date2, team1, team3, location1);
        Fixture f3 = new Fixture(date3, team2, team1, location2);
        Fixture f4 = new Fixture(date4, team2, team3, location2);
        Fixture f5 = new Fixture(date5, team3, team2, location3);
        Fixture f6 = new Fixture(date6, team3, team1, location3);

        s1.getFixtureList().add(f1);
        s1.getFixtureList().add(f2);
        s1.getFixtureList().add(f3);
        s1.getFixtureList().add(f4);
        s1.getFixtureList().add(f5);
        s1.getFixtureList().add(f6);

        int expected = 0;
        double ex = (double) 1 / (1 + expected);
        assertEquals(ex, s1.computeFitness(), .001);
    }

    /**
     * This test case tests the fitness function for conflict due to two matches on same day
     */
    @Test
    public void FitnessTest2() {
        Population testPop = new Population(10, data, false);
        ArrayList<Schedule> scheduleList = testPop.getSchedules();
        scheduleList.add(new Schedule());
        Schedule s1 = scheduleList.get(0);
        s1.setData(data);
        s1.setWeatherIndex(data.getWeather());

        Location location1 = data.getLocationList().get(0);
        Location location2 = data.getLocationList().get(1);
        Location location3 = data.getLocationList().get(2);

        Team team1 = data.getTeamList().get(0);
        Team team2 = data.getTeamList().get(1);
        Team team3 = data.getTeamList().get(2);

        Date date1 = data.getDates().get(0);
        Date date2 = data.getDates().get(2);
        Date date3 = data.getDates().get(2);
        Date date4 = data.getDates().get(2);
        Date date5 = data.getDates().get(4);
        Date date6 = data.getDates().get(6);

        Fixture f1 = new Fixture(date1, team1, team2, location1);
        Fixture f2 = new Fixture(date2, team1, team3, location1);
        Fixture f3 = new Fixture(date3, team2, team1, location2);
        Fixture f4 = new Fixture(date4, team2, team3, location2);
        Fixture f5 = new Fixture(date5, team3, team2, location3);
        Fixture f6 = new Fixture(date6, team3, team1, location3);

        s1.getFixtureList().add(f1);
        s1.getFixtureList().add(f2);
        s1.getFixtureList().add(f3);
        s1.getFixtureList().add(f4);
        s1.getFixtureList().add(f5);
        s1.getFixtureList().add(f6);

        int expected = 3;
        double ex = (double) 1 / (1 + expected);
        assertEquals(ex, s1.computeFitness(), .001);

    }

    /**
     * This test case tests the fitness function for conflicts due to the same team playing for 3 consecutive days
     */
    @Test
    public void FitnessTest3() {
        Population testPop = new Population(10, data, false);
        ArrayList<Schedule> scheduleList = testPop.getSchedules();
        scheduleList.add(new Schedule());
        Schedule s1 = scheduleList.get(0);
        s1.setData(data);
        s1.setWeatherIndex(data.getWeather());

        Location location1 = data.getLocationList().get(0);
        Location location2 = data.getLocationList().get(1);
        Location location3 = data.getLocationList().get(2);

        Team team1 = data.getTeamList().get(0);
        Team team2 = data.getTeamList().get(1);
        Team team3 = data.getTeamList().get(2);

        Date date1 = data.getDates().get(0);
        Date date2 = data.getDates().get(1);
        Date date3 = data.getDates().get(2);
        Date date4 = data.getDates().get(3);
        Date date5 = data.getDates().get(5);
        Date date6 = data.getDates().get(7);

        Fixture f1 = new Fixture(date1, team1, team2, location1);
        Fixture f2 = new Fixture(date2, team1, team3, location1);
        Fixture f3 = new Fixture(date3, team2, team1, location2);
        Fixture f4 = new Fixture(date4, team2, team3, location2);
        Fixture f5 = new Fixture(date5, team3, team2, location3);
        Fixture f6 = new Fixture(date6, team3, team1, location3);

        s1.getFixtureList().add(f1);
        s1.getFixtureList().add(f2);
        s1.getFixtureList().add(f3);
        s1.getFixtureList().add(f4);
        s1.getFixtureList().add(f5);
        s1.getFixtureList().add(f6);

        int expected = 3;
        double ex = (double) 1 / (1 + expected);
        assertEquals(ex, s1.computeFitness(), .001);
    }

    /**
     * This test case tests the fitness function for conflicts due to a team playing more(less) than 2 matches with each team
     */
    @Test
    public void FitnessTest4() {
        Population testPop = new Population(10, data, false);
        ArrayList<Schedule> scheduleList = testPop.getSchedules();
        scheduleList.add(new Schedule());
        Schedule s1 = scheduleList.get(0);
        s1.setData(data);
        s1.setWeatherIndex(data.getWeather());

        Location location1 = data.getLocationList().get(0);
        Location location2 = data.getLocationList().get(1);
        Location location3 = data.getLocationList().get(2);

        Team team1 = data.getTeamList().get(0);
        Team team2 = data.getTeamList().get(1);
        Team team3 = data.getTeamList().get(2);

        Date date1 = data.getDates().get(0);
        Date date2 = data.getDates().get(2);
        Date date3 = data.getDates().get(4);
        Date date4 = data.getDates().get(6);
        Date date5 = data.getDates().get(8);
        Date date6 = data.getDates().get(10);

        Fixture f1 = new Fixture(date1, team1, team2, location1);
        Fixture f2 = new Fixture(date2, team2, team3, location1);
        Fixture f3 = new Fixture(date3, team2, team1, location2);
        Fixture f4 = new Fixture(date4, team2, team3, location2);
        Fixture f5 = new Fixture(date5, team3, team2, location3);
        Fixture f6 = new Fixture(date6, team3, team1, location3);

        s1.getFixtureList().add(f1);
        s1.getFixtureList().add(f2);
        s1.getFixtureList().add(f3);
        s1.getFixtureList().add(f4);
        s1.getFixtureList().add(f5);
        s1.getFixtureList().add(f6);

        int expected = 5;
        double ex = (double) 1 / (1 + expected);
        assertEquals(ex, s1.computeFitness(), .001);
    }

    /**
     * This test case tests the fitness function for conflicts due to a location hosting multiple matches for same team
     */
    @Test
    public void FitnessTest5() {
        Population testPop = new Population(10, data, false);
        ArrayList<Schedule> scheduleList = testPop.getSchedules();
        scheduleList.add(new Schedule());
        Schedule s1 = scheduleList.get(0);
        s1.setData(data);
        s1.setWeatherIndex(data.getWeather());

        Location location1 = data.getLocationList().get(0);
        Location location2 = data.getLocationList().get(1);
        Location location3 = data.getLocationList().get(2);

        Team team1 = data.getTeamList().get(0);
        Team team2 = data.getTeamList().get(1);
        Team team3 = data.getTeamList().get(2);

        Date date1 = data.getDates().get(0);
        Date date2 = data.getDates().get(2);
        Date date3 = data.getDates().get(4);
        Date date4 = data.getDates().get(6);
        Date date5 = data.getDates().get(8);
        Date date6 = data.getDates().get(10);

        Fixture f1 = new Fixture(date1, team1, team2, location1);
        Fixture f2 = new Fixture(date2, team1, team3, location1);
        Fixture f3 = new Fixture(date3, team2, team1, location2);
        Fixture f4 = new Fixture(date4, team2, team3, location2);
        Fixture f5 = new Fixture(date5, team3, team2, location2);
        Fixture f6 = new Fixture(date6, team3, team1, location3);

        s1.getFixtureList().add(f1);
        s1.getFixtureList().add(f2);
        s1.getFixtureList().add(f3);
        s1.getFixtureList().add(f4);
        s1.getFixtureList().add(f5);
        s1.getFixtureList().add(f6);

        int expected = 2;
        double ex = (double) 1 / (1 + expected);
        assertEquals(ex, s1.computeFitness(), .001);
    }
}
