package br.com.sbrwgjd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Greeting {

    private long id;
    private String content;

    public Greeting(long id, String content){
        this.id = id;
        this.content = content;
    }
}
