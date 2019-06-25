# Match Fixture Scheduler - Team 309
Final Project - Program Structures and Algorithms (INFO 6205)

### Problem Description
The problem deals with scheduling the fixtures for Sports Leagues. 
While scheduling the fixtures, the following criteria have to be satisfied.

- No two matches should happen on the same day
- No team should play the on two consecutive days
- Each team should have played exactly 2 matches with every other teams 
- Each team should have played exactly one match on their home ground with each opponent
- Try to avoid scheduling a fixture on a day with bad weather

### Features implemented

- Implemented multi-threading for running the algorithm on colonies grouped from the total population (GeneticAlgorithmDriver.java)
- Created UI to see the improvements in each generation using swing (GeneticAlgorithmDriverWithUI.java)
- Using properties file for configuring parameters for the Genetic Algorithm
- Using log4j to log the outputs from each generation

### How to run the program

- The maven project is located inside the MatchFixtureScheduler folder
- Load the maven dependencies
- Build the application with the test cases provided
- Run GeneticAlgorithmDriver.java to run the algorithm with multithreading
- Run GeneticAlgorithmDriverWithUI.java to run the algorithm with User Interface


