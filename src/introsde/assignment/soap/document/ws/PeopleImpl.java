package introsde.assignment.soap.document.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import introsde.assignment.soap.document.model.Measure;
//import introsde.assignment.soap.document.model.LifeStatus;
import introsde.assignment.soap.document.model.Person;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.document.ws.People",
	serviceName="PeopleService")
public class PeopleImpl implements People {
	
	@Override
	public void clean() {
		Person p = Person.getPersonById(1);
		List<Measure> measures = Measure.getAll();
		for(Measure measure:measures){
			System.out.println(measure.getIdMeasure());
			measure.setPerson(p);
			Measure.updateMeasure(measure);
			System.out.println(Measure.getMeasureById(measure.getIdMeasure()).getPerson());
		}
	}

	/*
	 * Method #2: readPerson(Long id) => Person | should give
	 *  all the Personal information plus current measures of one 
	 *  Person identified by {id} (e.g., current measures means 
	 *  current healthProfile)
	 */
	@Override
	public Person readPerson(int id) {
		Person p = Person.getPersonById(id);
		return p;
	}

	/*
	 *  Method #1: readPersonList() => List | should list 
	 *  all the people in the database (see below 
	 *  Person model to know what data to return here) 
	 *  in your database
	 */
	@Override
	public List<Person> getPeople() {
		return Person.getAll();
	}
	 
    /*
     * Method #4: createPerson(Person p) => Person | should 
     * create a new Person and return the newly created Person 
     * with its assigned id (if a health profile is included, 
     * create also those measurements for the new Person).
     */
	@Override
	public int addPerson(Person person) {
		Person.savePerson(person);
		return person.getIdPerson();
	}

    /*
     * Method #3: updatePerson(Person p) => Person | should update 
     * the Personal information of the Person identified by {id} 
     * (e.g., only the Person's information, not the measures of the 
     * health profile)
     */
	@Override
	public int updatePerson(Person person) {
		Person.updatePerson(person);
		return person.getIdPerson();
	}

    /*
     * Method #5: deletePerson(Long id) should delete the 
     * Person identified by {id} from the system
     */
	@Override
	public int deletePerson(int id) {
		Person p = Person.getPersonById(id);
		if (p!=null) {
			Person.removePerson(p);
			return 0;
		} else {
			return -1;
		}
	}
	
    /*
     * Method #6: readPersonHistory(Long id, String measureType) => List 
     * should return the list of values (the history) of {measureType}
     *  (e.g. weight) for Person identified by {id}
     */
    @Override
    public List<String> readPersonHistory(int id, String measureType){
    	List<String> measureTypes = new ArrayList<String>();
    	Person p = Person.getPersonById(id);
    	List<Measure> measures = Measure.getAll();
    	for(Measure measure:measures){
    		if(measure.getMeasureType().equals(measureType)
    				&& measure.getPerson()!=null &&
    						measure.getPerson().getIdPerson()==id)
    			measureTypes.add(measure.getMeasureValue());
    	}
    	return measureTypes;
    }
    
    /*
     * Method #7: readMeasureTypes() => List should return the list 
     * of measures
     */
    @Override
    public List<Measure> readMeasureTypes(){
		return Measure.getAll();    	
    }
    
    /*
     * Method #8: readPersonMeasure(Long id, String measureType, Long mid) => Measure
     *  should return the value of {measureType} (e.g. weight) identified by 
     *  {mid} for Person identified by {id}
     */
    @Override
    public String readPersonMeasure(int id, String measureType, int mid){
    	try{
	    	Measure measure = Measure.getMeasureById(new Long(mid));
    		System.out.println(measure.getMeasureValue()+measure.getMeasureType());
    		System.out.println(measure.getPerson().getIdPerson());
	    	if(measure!= null && (measure.getMeasureType()).equals(measureType) && 
	    			measure.getPerson().getIdPerson() == id){
	    		System.out.println("IFFFFFFFF");
	    		return measure.getMeasureValue();
	    	}
	    	else
	    		return "ERROR ELSE";
    	} catch(Exception e){return "ERROR EXCEPTION";}
	}
    
    /*
     * Method #9: savePersonMeasure(Long id, Measure m) =>should save a new 
     * measure object {m} (e.g. weight) of Person identified by {id} and 
     * archive the old value in the history
     */
    @Override
    public Long savePersonMeasure(int id, Measure newMeasure){
    	try{
	    	Date now = new Date();
	    	newMeasure.setDateRegistered(now);
	    	Person p = Person.getPersonById(id);
	    	newMeasure.setPerson(p);
			Measure savedMeasure = Measure.saveMeasure(newMeasure);
	    	p.addMeasure(savedMeasure);
			
			return savedMeasure.getIdMeasure();
    	} catch(Exception e){
    		return new Long(-1);
    	}
	}
    
    /*
     * Method #10: updatePersonMeasure(Long id, Measure m) => Measure | should
     *  update the measure identified with {m.mid}, related to the Person 
     *  identified by {id}
     */
	@Override
	public Long updatePersonHP(int id, Measure m){
		try{
			Person p = Person.getPersonById(id);
			Measure oldMeasure = Measure.getMeasureById(m.getIdMeasure());
			for(Measure measure: p.getMeasures()){
				if(measure.getIdMeasure()==oldMeasure.getIdMeasure()){
		    		p.removeMeasure(measure);
					Date now = new Date();	        	
					measure.setDateRegistered(now);   		
					measure.setPerson(p);
					measure.setMeasureValue(m.getMeasureValue());
		    		Measure updated = Measure.updateMeasure(m);
		    		p.addMeasure(measure);
		    		return updated.getIdMeasure();
				}
			}return new Long(-2);
		} catch(Exception e){return new Long(-1);}
    	
	}

}