package com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.domains.A;
import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.domains.B;
import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.domains.C;
import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.repositories.ARepository;
import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.repositories.BRepository;
import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.repositories.CRepository;

@SpringBootApplication
@Transactional
public class AccessingDataJpaApplication {

	private static final Logger log = LoggerFactory.getLogger(AccessingDataJpaApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class);
	}

	@Bean
	public CommandLineRunner demo(ARepository aRepository, BRepository bRepository, CRepository cRepository) {
		return (args) -> {
			log.info("===== Persisting As and Bs");
			persistData(aRepository, bRepository, cRepository);
			readData(aRepository, bRepository, cRepository);
			log.info("===== Modifying some As and Bs");
			modifyData(aRepository, bRepository, cRepository);
			readData(aRepository, bRepository, cRepository);
			log.info("===== Deleting some As and Bs");
			deleteData(aRepository, bRepository, cRepository);
			readData(aRepository, bRepository, cRepository);
		};
	}
	
	private void readData(ARepository aRepository, BRepository bRepository, CRepository cRepository) {
		Iterable<A> As = aRepository.findAll();
		log.info("===== As");
		for(A a : As) {
			log.info(a.toString());
		}
		
		Iterable<B> Bs = bRepository.findAll();
		log.info("===== Bs");
		for(B b : Bs) {
			log.info(b.toString());
		}

		Iterable<C> Cs = cRepository.findAll();
		log.info("===== Cs");
		for(C c : Cs) {
			log.info(c.toString());
		}
	}
	
	private void persistData(ARepository aRepository, BRepository bRepository, CRepository cRepository) {
		//we build : a1.myMap{ c1 -> b1, c2 ->b2} and a2.myMap{ c1 -> b3, c2 ->b4}
		A a1 = new A("a1");
		A a2 = new A("a2");
		B b1 = new B("b1");
		B b2 = new B("b2");
		B b3 = new B("b3");
		B b4 = new B("b4");
		C c1 = new C("c1");
		C c2 = new C("c2");
	    Map<C,B> a1Map = new HashMap<C, B>();
	    a1Map.put(c1,b1);
	    a1Map.put(c2,b2);
	    Map<C,B> a2Map = new HashMap<C, B>();
	    a2Map.put(c1,b3);
	    a2Map.put(c2,b4);
		a1.setMyMap(a1Map);
		a2.setMyMap(a2Map);
		cRepository.save(c1);
		cRepository.save(c2);
		bRepository.save(b1);
		bRepository.save(b2);
		bRepository.save(b3);
		bRepository.save(b4);
		aRepository.save(a1);
		aRepository.save(a2);
		
		//we can build an A with an empty Map
		A a3 = new A("a3");
		aRepository.save(a3);
	}

	private void modifyData(ARepository aRepository, BRepository bRepository, CRepository cRepository) {
		//we switch the c2 entries in a1 and a2 so that we have : a1.myMap{ c1 -> b1, c2 ->b4} and a2.myMap{ c1 -> b3, c2 ->b2} (we switch b2 and b4)
		A a1 = aRepository.findByA("a1").get(0);
		A a2 = aRepository.findByA("a2").get(0);
		
		C c2 = cRepository.findByC("c2").get(0);
		B b12 = a1.getMyMap().remove(c2);
		B b22 = a2.getMyMap().remove(c2);
		a1.getMyMap().put(c2, b22);
		a2.getMyMap().put(c2, b12);
		
		
		aRepository.save(a1);
		aRepository.save(a2);
	}
	
	private void deleteData(ARepository aRepository, BRepository bRepository, CRepository cRepository) {
		//we delete references to b3 and c2 in A's map
		C c2 = cRepository.findByC("c2").get(0);
		B b3 = bRepository.findByB("b3").get(0);
		
		//removing c2
		aRepository.findAll().forEach((a) -> {
			a.getMyMap().remove(c2);
			aRepository.save(a);
		});
		aRepository.findAll().forEach((a) -> {
			for(C c : a.getMyMap().keySet()) {
				if(a.getMyMap().get(c).equals(b3)) {
					a.getMyMap().remove(c);
				}
			}
			aRepository.save(a);
		});
	}
}
