/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fel.cvut.cz.Utilities;

import java.util.logging.Logger;

/**
 * Class contains useful utilities for the GUI part of the program.
 *
 * @author Ryzner
 */
public class utilsGUI {


    private static final Logger LOG = Logger.getLogger(utilsGUI.class.getName());

    /**
     * Method can take in any given number of seconds and creates a string representing it in a standard digital
     * time manner.
     *
     * @param timeGiven – time in seconds
     * @return String – digital clock format representation of the given time duration
     */
    public String timeConvertor(int timeGiven) {
        int hours = 0;
        int seconds = 0;
        int minutes = 0;
        String timeLeft;

        hours = timeGiven / 3600;
        minutes = (timeGiven % 3600) / 60;
        seconds = timeGiven % 60;

        timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return timeLeft;
    }

    /**
     * Method can take in any given number of milliseconds and creates a string representing it in a standard digital
     * time manner.
     *
     * @param millisecondsGiven – time in milliseconds
     * @return String – digital clock format representation of the given time duration
     */
    public String timeMiliConvertor(long millisecondsGiven){
        long seconds = (millisecondsGiven / 1000) % 60;
        long minutes = (millisecondsGiven / (1000 * 60)) % 60;
        long hours = (millisecondsGiven / (1000 * 60 * 60)) % 24;
        //long millisToDisplay = millisecondsGiven - hours*60*60*1000 - minutes*1000*60 - seconds*1000;

        String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return timeLeft;
    }

}
