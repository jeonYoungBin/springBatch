package com.hello.springbatch.job.FileDataReadWrite;

import com.hello.springbatch.job.FileDataReadWrite.dto.Player;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PlayerFieldSetMapper implements FieldSetMapper<Player> {
    @Override
    public Player mapFieldSet(FieldSet fieldSet) {
        Player player = new Player();

        player.setID(fieldSet.readString(0));
        player.setLastName(fieldSet.readString(1));
        player.setFirstName(fieldSet.readString(2));
        player.setBirthYear(fieldSet.readInt(3));
        player.setDebutYear(fieldSet.readInt(4));

        return player;
    }
}
