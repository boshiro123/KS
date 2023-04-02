import java.io.Serializable;

public class DanceTeam implements Serializable {
    private String teamName;
    private String city;
    private int juryScore;
    private int auidScore;
    private String ageGroup;

    public DanceTeam(String teamName, String city, int juryScore, int auidScore, String ageGroup) {
        this.teamName = teamName;
        this.city = city;
        this.juryScore = juryScore;
        this.auidScore = auidScore;
        this.ageGroup = ageGroup;
    }
    public DanceTeam(){}

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getJuryScore() {
        return juryScore;
    }

    public void setJuryScore(int juryScore) {
        this.juryScore = juryScore;
    }

    public int getAuidScore() {
        return auidScore;
    }

    public void setAuidScore(int auidScore) {
        this.auidScore = auidScore;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    @Override
    public String toString() {
        return "Название пары: " + teamName + " | Город: " + city + " | Оценка жюри: " + juryScore + " | Оценка зрителей: " + auidScore+
                " | Возрастаная группа: " + ageGroup;
    }
}
