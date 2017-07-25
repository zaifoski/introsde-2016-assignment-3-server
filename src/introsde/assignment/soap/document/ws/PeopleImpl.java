package introsde.assignment.soap.document.ws;

import java.util.List;

import javax.jws.WebService;

import introsde.assignment.soap.document.model.LifeStatus;
import introsde.assignment.soap.document.model.Person;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.document.ws.People",
	serviceName="PeopleService")
public class PeopleImpl implements People {

	/*
	 * Method #2: readPerson(Long id) => Person | should give
	 *  all the Personal information plus current measures of one 
	 *  Person identified by {id} (e.g., current measures means 
	 *  current healthProfile)
	 */
	@Override
	public Person readPerson(int id) {
		System.out.println("---> Reading Person by id = "+id);
		Person p = Person.getPersonById(id);
		if (p!=null) {
			System.out.println("---> Found Person by id = "+id+" => "+p.getName());
		} else {
			System.out.println("---> Didn't find any Person with  id = "+id);
		}
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
     * Method #10: updatePersonMeasure(Long id, Measure m) => Measure | should
     *  update the measure identified with {m.mid}, related to the Person 
     *  identified by {id}
     */
	@Override
	public int updatePersonHP(int id, LifeStatus hp) {
		LifeStatus ls = LifeStatus.getLifeStatusById(hp.getIdMeasure());
		if (ls.getPerson().getIdPerson() == id) {
			LifeStatus.updateLifeStatus(hp);
			return hp.getIdMeasure();
		} else {
			return -1;
		}
	}

}