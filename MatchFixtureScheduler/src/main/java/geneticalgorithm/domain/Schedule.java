package geneticalgorithm.domain;

import geneticalgorithm.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Data structure for storing the Schedule
 *
 * @author Gokul Anantha Narayanan, Krupashankar Sundararajan, Raghavan Renganathan
 * @version 1.0
 * @since 04/10/2018
 */
public class Schedule {

    /**
     * This hash-map stores the probability of rain for each day at each location
     */
    private HashMap<String, HashMap<Location, Integer>> weatherIndex;

    /**
     * This stores the number of matches played by each team
     */
    private HashMap<Team, Integer> matchesPlayed;

    /**
     * This stores the number of matches played in home-ground by each team
     */
    private HashMap<Team, Integer> homeMatches;

    /**
     * This stores the number of matches played at each location
     */
    private HashMap<Location, Integer> matchesInLocation;

    /**
     * This stores the list of all fixtures in this schedule
     */
    private ArrayList<Fixture> fixtureList;

    /**
     * This stores the fitness of this schedule
     */
    private Double fitness;

    /**
     * This stores the total number of conflicts in this schedule
     */
    private int conflicts;

    /**
     * Data variable to store all the data
     */
    private Data data;

    /**
     * Creates an instance of Schedule based on the data sent
     *
     * @param data Data set using which the schedule has to be made
     */
    public Schedule(Data data) {
        fitness = (double) -1;
        fixtureList = new ArrayList<>();
        matchesPlayed = new HashMap<>();
        homeMatches = new HashMap<>();
        matchesInLocation = new HashMap<>();
        this.data = data;
        initialize();
    }

    /**
     * Creates an instance with empty data. Used when creating test cases, where data has to be filled manually
     */
    public Schedule() {
        fitness = (double) -1;
        fixtureList = new ArrayList<>();
        matchesPlayed = new HashMap<>();
        homeMatches = new HashMap<>();
        matchesInLocation = new HashMap<>();
        data = null;
    }

    /**
     * This method initializes schedule randomly based on the total data-set
     */
    private void initialize() {
        weatherIndex = data.getWeather();
        for (Team team : data.getTeamList()) {
            homeMatches.put(team, 0);
            matchesPlayed.put(team, 0);
        }
        for (Location location : data.getLocationList()) {
            matchesInLocation.put(location, 0);
        }

        int totalFixtures = data.getTeamList().size() * (data.getTeamList().size() - 1);

        for (int i = 0; i < totalFixtures; i++) {
            Team homeTeam = data.getTeamList().get((int) (data.getTeamList().size() * Math.random()));
            Team opponent;
            while ((opponent = data.getTeamList().get((int) (data.getTeamList().size() * Math.random()))).equals(homeTeam));
            Date d = data.getDates().get((int) (data.getDates().size() * Math.random()));

            fixtureList.add(new Fixture(d, homeTeam, opponent, homeTeam.getHomeGround()));
        }

        sortFixturesByDate();
    }

    /**
     * This function sorts the fixtures by the match's date
     */
    public void sortFixturesByDate() {
        fixtureList.sort((f1, f2) -> {
            if (f1.getDate().after(f2.getDate()))
                return 1;
            else if (f1.getDate().before(f2.getDate()))
                return -1;
            else
                return 0;
        });
    }

    /**
     * Getter method for {@code fixtureList}
     *
     * @return List of all fixtures in this schedule
     */
    public ArrayList<Fixture> getFixtureList() {
        return fixtureList;
    }

    /**
     * This method calculates and returns the fitness of this schedule
     *
     * @return fitness of this schedule
     */
    public Double getFitness() {
        fitness = computeFitness();
        return fitness;
    }

    /**
     * This method can be used to get the total number of conflicts in this schedule
     *
     * @return number of conflicts
     */
    public int getConflicts() {
        return conflicts;
    }

    /**
     * Setter method for {@code Data data}
     *
     * @param data The data that has to be used for this schedule
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * Setter method for the variable {@code HashMap<String, HashMap<Location, Integer>> weatherIndex}
     *
     * @param weatherIndex The HashMap of weather index (Probability for bad weather)
     */
    public void setWeatherIndex(HashMap<String, HashMap<Location, Integer>> weatherIndex) {
        this.weatherIndex = weatherIndex;
    }

    /**
     * This method gives the total number of fixtures in this schedule
     *
     * @return size of the fixtures list
     */
    public int size() {
        return fixtureList.size();
    }

    /**
     * This method calculates the fitness of this schedule with respect to the criteria needed.
     *
     * @return fitness of this schedule
     */
    public double computeFitness() {
        conflicts = 0;

        matchesPlayed.clear();
        matchesInLocation.clear();
        homeMatches.clear();
        for (int i = 0; i < fixtureList.size(); i++) {
            Fixture f1 = fixtureList.get(i);

            // Adding penalties for scheduling matches where the probability of raining is high
            int weatherIndex = this.weatherIndex.get(Data.dateFormat.format(f1.getDate())).get(f1.getLocation());
            if (weatherIndex > 70)
                conflicts++;

            // Calculation of total matches played by each team
            if (matchesPlayed.containsKey(f1.getHomeTeam()))
                matchesPlayed.put(f1.getHomeTeam(), matchesPlayed.get(f1.getHomeTeam()) + 1);
            else
                matchesPlayed.put(f1.getHomeTeam(), 1);

            if (matchesPlayed.containsKey(f1.getAwayTeam()))
                matchesPlayed.put(f1.getAwayTeam(), matchesPlayed.get(f1.getAwayTeam()) + 1);
            else
                matchesPlayed.put(f1.getAwayTeam(), 1);

            // Calculation of total matches played by each location
            if (matchesInLocation.containsKey(f1.getLocation()))
                matchesInLocation.put(f1.getLocation(), matchesInLocation.get(f1.getLocation()) + 1);
            else
                matchesInLocation.put(f1.getLocation(), 1);

            // Calculation of total matches played at home ground
            if (homeMatches.containsKey(f1.getHomeTeam()))
                homeMatches.put(f1.getHomeTeam(), homeMatches.get(f1.getHomeTeam()) + 1);
            else
                homeMatches.put(f1.getHomeTeam(), 1);

            // Two matches should not happen on the same day
            Fixture f2;
            int j = 0;
            while ((i + (++j)) < fixtureList.size()) {
                f2 = fixtureList.get(i + j);
                if (Data.dateFormat.format(f1.getDate()).equals(Data.dateFormat.format(f2.getDate()))) {
                    conflicts++;
                } else {
                    break;
                }
            }

            // Computing next day
            Date currentDate = f1.getDate();
            LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            localDateTime = localDateTime.plusDays(1);
            Date currentDatePlusOneDay = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // Same team should not play on consecutive dates
            j = 0;
            while ((i + (++j)) < fixtureList.size()) {
                f2 = fixtureList.get(i + j);
                if (Data.dateFormat.format(currentDatePlusOneDay).equals(Data.dateFormat.format(f2.getDate()))) {
                    if (f1.getAwayTeam().equals(f2.getAwayTeam()) ||
                            f1.getAwayTeam().equals(f2.getHomeTeam()) ||
                            f1.getHomeTeam().equals(f2.getHomeTeam()) ||
                            f1.getHomeTeam().equals(f2.getAwayTeam())) {
                        conflicts++;
                    }
                } else {
                    break;
                }
            }

            // Two matches should not be same
            j = 0;
            while ((i + (++j)) < fixtureList.size()) {
                f2 = fixtureList.get(i + j);
                if (f1.getHomeTeam().equals(f2.getHomeTeam()) && f1.getAwayTeam().equals(f2.getAwayTeam()))
                    conflicts++;
            }
        }

        // Each team should have played 2 matches with each other teams
        Iterator<Map.Entry<Team, Integer>> entries = matchesPlayed.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Team, Integer> entry = entries.next();

            if (entry.getValue() != (2 * data.getTeamList().size()) - 2) {
                conflicts++;
            }
        }

        // Each location should have hosted exactly one match for each team
        Iterator<Map.Entry<Location, Integer>> entries2 = matchesInLocation.entrySet().iterator();
        while (entries2.hasNext()) {
            Map.Entry<Location, Integer> entry2 = entries2.next();

            if (entry2.getValue() != (data.getTeamList().size() - 1)) {
                conflicts++;
            }
        }

        // Each team should have played exactly one match against each team on their home ground
        Iterator<Map.Entry<Team, Integer>> entries3 = homeMatches.entrySet().iterator();
        while (entries3.hasNext()) {
            Map.Entry<Team, Integer> entry3 = entries3.next();

            if (entry3.getValue() != (data.getTeamList().size() - 1)) {
                conflicts++;
            }
        }

        return (double) 1 / (1 + conflicts);
    }
}
