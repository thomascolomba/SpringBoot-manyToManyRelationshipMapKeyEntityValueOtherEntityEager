package com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.domains;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "A")
@NoArgsConstructor
@Setter @Getter
@EqualsAndHashCode
public class A implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String a;

    @ManyToMany(fetch = FetchType.EAGER)
    private Map<C, B> myMap;

    public A(String a) {
        this.a = a;
    }

    @Override
    public String toString() {
        String toReturn = "A{" +
                "id=" + id +
                ", a='" + a + "' Bs : ";
        for(C c : myMap.keySet()) {
        	toReturn += "c:"+c.getC()+" -> b:"+myMap.get(c).getB()+" ";
        }
        toReturn += "}";
        return toReturn;
    }
}