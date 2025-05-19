package logic.text_tests;

import javafx.scene.image.Image;
import logic.general.Replica;
import logic.general.Speaker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

class ReplicaTest {
    @Test
    void findAllOccurrences_shouldFindAllMatches() {
        Speaker speaker = new Speaker("Ivan", getImage("/images/default_users/undefined.png"), 1);
        Replica replica = new Replica("hello hello world hello", speaker);

        ArrayList<Integer> result = replica.findAllOccurrences("hello");

        assertEquals(3, result.size());
        assertEquals(0, result.get(0));
        assertEquals(6, result.get(1));
        assertEquals(18, result.get(2));
    }

    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        Speaker speaker1 = new Speaker("Pert", getImage("/images/default_users/undefined.png"), 2);
        Speaker speaker2 = new Speaker("Pert", getImage("/images/default_users/undefined.png"), 3);

        Replica replica = new Replica();
        replica.setText("test");
        replica.setSpeaker(speaker2);

        assertEquals("test", replica.getText());
        assertEquals(speaker2, replica.getSpeaker());
    }
}