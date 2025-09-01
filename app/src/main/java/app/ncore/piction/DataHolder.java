package app.ncore.piction;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Random;

public class DataHolder {
    private static ArrayList<Category> categoriesInUse;



    private static boolean keepScore;
    private static Team team1;
    private static boolean useTimer;
    private static boolean allowResetTimer;
    private static boolean hideResetTimer;
    private static boolean alternateTurns;
    private static boolean animations = true;


    private static Category objects;
    private static Category persons;
    private static Category actions;
    private static Category cAbstract;
    private static Category celebrities;
    private static Category places;
    private static Category animals;
    private static boolean allplayLastTurn = false;
    private static Team team2;
    private static boolean allPlay;
    private static int timerLength = 65;
    private static Uri timeUpRing;


    public static Category getActionsCategory() {
        return actions;
    }

    public static void setActionsCategory(Category actions) {
        DataHolder.actions = actions;
    }


    public static Category getcAbstractCategory() {
        return cAbstract;
    }

    public static void setcAbstractCategory(Category cAbstract) {
        DataHolder.cAbstract = cAbstract;
    }

    public static Category getCelebritiesCategory() {
        return celebrities;
    }

    public static void setCelebritiesCategory(Category celebrities) {
        DataHolder.celebrities = celebrities;
    }

    public static Category getPlacesCategory() {
        return places;
    }

    public static void setPlacesCategory(Category places) {
        DataHolder.places = places;
    }

    public static Category getAnimalsCategory() {
        return animals;
    }

    public static void setAnimalsCategory(Category animals) {
        DataHolder.animals = animals;
    }




    public static boolean isAnimations() {
        return animations;
    }

    public static void setAnimations(boolean animations) {
        DataHolder.animations = animations;
    }

    public static Uri getTimeUpRing() {
        return timeUpRing;
    }

    public static void setTimeUpRing(Uri timeUpRing) {
        DataHolder.timeUpRing = timeUpRing;
    }

    public static void resetAllGameSettings(){
        keepScore = false;
        team1 = null;
        team2= null;
        useTimer =false;
        allowResetTimer =false;
        hideResetTimer = false;
        alternateTurns = false;
        objects = null;
        persons = null;
        allplayLastTurn = false;
        allPlay = false;
        timerLength = 65;
    }
    public static void setObjectsCategory(Category objects) {
        DataHolder.objects = objects;
    }

    public static void setPersonsCategory(Category persons) {
        DataHolder.persons = persons;
    }



    public static boolean isAlternateTurns() {
        return alternateTurns;
    }

    public static void setAlternateTurns(boolean alternateTurns) {
        DataHolder.alternateTurns = alternateTurns;
    }
    public static ArrayList<Category> getCategoriesInUse() {
        return categoriesInUse;
    }

    public static void setCategoriesInUse(ArrayList<Category> categoriesInUse) {
        DataHolder.categoriesInUse = categoriesInUse;
    }

    public static Category getObjectsCategory() {
        return objects;
    }

    public static Category getPersonsCategory() {
        return persons;
    }
    public static void setTeam1(Team team1) {
        DataHolder.team1 = team1;
    }

    public static void setTeam2(Team team2) {
        DataHolder.team2 = team2;
    }

    public static Team getTeam1() {
        return team1;
    }

    public static Team getTeam2() {
        return team2;
    }






    public static boolean isHideResetTimer() {
        return hideResetTimer;
    }

    public static void setHideResetTimer(boolean hideResetTimer) {
        DataHolder.hideResetTimer = hideResetTimer;
    }




    public static void setSkipLimit(int skipLimit) {
        DataHolder.team1.setSkips(skipLimit);
        DataHolder.team2.setSkips(skipLimit);
    } //-1 will indicate infinite


    public static void setKeepScore(boolean KeepScore) {
        keepScore = KeepScore;
    }

    public static boolean isKeepScore() {
        return keepScore;
    }


    public static boolean isAllPlay() {
        return allPlay;
    }

    public static void setAllPlay(boolean allPlay) {
        DataHolder.allPlay = allPlay;
    }

    public static boolean isUseTimer() {
        return useTimer;
    }

    public static void setUseTimer(boolean useTimer) {
        DataHolder.useTimer = useTimer;
    }

    public static int getTimerLength() {
        return timerLength;
    }
    public static boolean randomAllPlay(){
        Random rnd = new Random();
        allplayLastTurn = (rnd.nextInt(6)==5)&&(!allplayLastTurn);//1 in 5 chance of all play occuring if it does then allplay Last turn will be set to true so the next turn knows
        return allplayLastTurn;

    }

    public static void setTimerLength(int timerLength) {
        DataHolder.timerLength = timerLength*1000;
    }

}
