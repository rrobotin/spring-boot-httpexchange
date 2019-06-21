package smart.league.project.objects;

public class Metadata {

    String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "score='" + score + '\'' +
                '}';
    }
}
