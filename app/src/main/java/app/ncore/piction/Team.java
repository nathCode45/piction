package app.ncore.piction;


public class Team {
    private int score;
    private String name;
    private int skips;

    public void setScore(int score) {
        this.score = score;
    }


    public Team(int score, String name, int skips) {
        this.score = score;
        this.name = name;
        this.skips = skips;
    }

    public Team() {
        this.score = 0;
        this.name ="Team";
        this.skips = 0;
    }

    public Team(String name) {
        this.name = name;
    }

    public void setSkips(int skips) {
        this.skips = skips;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getSkips() {
        return skips;

    }
    public void incrementScore(){
        score+=1;
    }
    public void useSkip(){
        skips--;
    }
}
