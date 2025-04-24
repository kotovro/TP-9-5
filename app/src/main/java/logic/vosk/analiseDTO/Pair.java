package logic.vosk.analiseDTO;

public class Pair<T> {
    T first, second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public boolean equals(Pair speakerPair) {
        return first.equals(speakerPair.first) && second.equals(speakerPair.second);
    }
}
