CRUD operations on a ManyToMany relationship with a java Map<Entity1, Entity2> type with Eager fetch.<br/>
An instance of A has a Map<C, B> (C being the key type and B the value type).<br/>
Also, A has a String a, B has a String b, C has a String c.<br/>
<br/>
compile & execute :<br/>
mvn spring-boot:run<br/>
compile into fat jar then execute :<br/>
mvn clean package<br/>
java -jar target/manyToManyRelationshipMapKeyEntityValueOtherEntityEager-0.0.1-SNAPSHOT.jar<br/>
<br/>
To Compile from within Eclipse or any other IDE, you need to install Lombok : https://projectlombok.org/setup/overview<br/>
<br/>
<br/>
A, B and C are entities.<br/>
A entity holds a Map of C and B.<br/>
persisting/modifying/deleting an A will persist/modify/delete the corresponding relation with C and B in the join table.
--A.java<br/>
@ManyToMany(fetch = FetchType.EAGER)<br/>
private Map&lt;C, B&gt; myMap;<br/>

--AccessingDataJpaApplication.java (main class)<br/>
log.info("===== Persisting As and Bs");<br/>
persistData(aRepository, bRepository, cRepository);<br/>
readData(aRepository, bRepository, cRepository);<br/>
log.info("===== Modifying some As and Bs");<br/>
modifyData(aRepository, bRepository, cRepository);<br/>
readData(aRepository, bRepository, cRepository);<br/>
log.info("===== Deleting some As and Bs");<br/>
deleteData(aRepository, bRepository, cRepository);<br/>
readData(aRepository, bRepository, cRepository);<br/>
...<br/>
<b>persistData(){</b><br/>
&nbsp;&nbsp;A a1 = new A("a1");<br/>
&nbsp;&nbsp;A a2 = new A("a2");<br/>
&nbsp;&nbsp;B b1 = new B("b1");<br/>
&nbsp;&nbsp;B b2 = new B("b2");<br/>
&nbsp;&nbsp;B b3 = new B("b3");<br/>
&nbsp;&nbsp;B b4 = new B("b4");<br/>
&nbsp;&nbsp;C c1 = new C("c1");<br/>
&nbsp;&nbsp;C c2 = new C("c2");<br/>
&nbsp;&nbsp;Map<C,B> a1Map = new HashMap<C, B>();<br/>
&nbsp;&nbsp;a1Map.put(c1,b1);<br/>
&nbsp;&nbsp;a1Map.put(c2,b2);<br/>
&nbsp;&nbsp;Map<C,B> a2Map = new HashMap<C, B>();<br/>
&nbsp;&nbsp;a2Map.put(c1,b3);<br/>
&nbsp;&nbsp;a2Map.put(c2,b4);<br/>
&nbsp;&nbsp;a1.setMyMap(a1Map);<br/>
&nbsp;&nbsp;a2.setMyMap(a2Map);<br/>
&nbsp;&nbsp;cRepository.save(c1);<br/>
&nbsp;&nbsp;cRepository.save(c2);<br/>
&nbsp;&nbsp;bRepository.save(b1);<br/>
&nbsp;&nbsp;bRepository.save(b2);<br/>
&nbsp;&nbsp;bRepository.save(b3);<br/>
&nbsp;&nbsp;bRepository.save(b4);<br/>
&nbsp;&nbsp;aRepository.save(a1);<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
}<br/>
<b>modifyData(){</b><br/>
&nbsp;&nbsp;//we switch the c2 entries in a1 and a2 so that we have : a1.myMap{ c1 -> b1, c2 ->b4} and a2.myMap{ c1 -> b3, c2 ->b2} (we switch b2 and b4)<br/>
&nbsp;&nbsp;A a1 = aRepository.findByA("a1").get(0);<br/>
&nbsp;&nbsp;A a2 = aRepository.findByA("a2").get(0);<br/>
&nbsp;&nbsp;C c2 = cRepository.findByC("c2").get(0);<br/>
&nbsp;&nbsp;B b12 = a1.getMyMap().remove(c2);<br/>
&nbsp;&nbsp;B b22 = a2.getMyMap().remove(c2);<br/>
&nbsp;&nbsp;a1.getMyMap().put(c2, b22);<br/>
&nbsp;&nbsp;a2.getMyMap().put(c2, b12);<br/>
&nbsp;&nbsp;aRepository.save(a1);<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
}<br/>
<b>deleteData(){</b><br/>
&nbsp;&nbsp;//we delete references to b3 and c2 in A's map<br/>
&nbsp;&nbsp;C c2 = cRepository.findByC("c2").get(0);<br/>
&nbsp;&nbsp;B b3 = bRepository.findByB("b3").get(0);<br/>
&nbsp;&nbsp;//removing c2<br/>
&nbsp;&nbsp;aRepository.findAll().forEach((a) -> {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;a.getMyMap().remove(c2);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;aRepository.save(a);<br/>
&nbsp;&nbsp;});<br/>
&nbsp;&nbsp;aRepository.findAll().forEach((a) -> {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;for(C c : a.getMyMap().keySet()) {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if(a.getMyMap().get(c).equals(b3)) {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a.getMyMap().remove(c);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
&nbsp;&nbsp;aRepository.save(a);<br/>
&nbsp;&nbsp;});<br/>
}<br/>