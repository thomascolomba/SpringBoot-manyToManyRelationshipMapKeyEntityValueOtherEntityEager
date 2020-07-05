package com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.domains;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "C")
@NoArgsConstructor
@Setter @Getter
@EqualsAndHashCode
public class C implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String c;
	
	public C(String c) {
        this.c = c;
    }
	
	@Override
    public String toString() {
        String toReturn = "C{" +
                "id=" + id +
                ", c='" + c + "}";
        return toReturn;
    }

}
