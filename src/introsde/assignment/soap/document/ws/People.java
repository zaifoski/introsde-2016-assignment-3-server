package introsde.assignment.soap.document.ws;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import introsde.assignment.soap.document.model.Measure;
//import introsde.assignment.soap.document.model.LifeStatus;
import introsde.assignment.soap.document.model.Person;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) //optional
public interface People {

	@WebMethod(operationName="cleanDb")
	public void clean();
    
	/*
	 * Method #2: readPerson(Long id) => Person | should give
	 *  all the Personal information plus current measures of one 
	 *  Person identified by {id} (e.g., current measures means 
	 *  current healthProfile)
	 */
	@WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="personId") int id);
    
	/*
	 *  Method #1: readPersonList() => List | should list 
	 *  all the people in the database (see below 
	 *  Person model to know what data to return here) 
	 *  in your database
	 */
    @WebMethod(operationName="readPersonList")
    @WebResult(name="people") 
    public List<Person> getPeople();
 
    /*
     * Method #4: createPerson(Person p) => Person | should 
     * create a new Person and return the newly created Person 
     * with its assigned id (if a health profile is included, 
     * create also those measurements for the new Person).
     */
    @WebMethod(operationName="createPerson")
    @WebResult(name="personId") 
    public int addPerson(@WebParam(name="person", targetNamespace = "http://ws.document.soap.assignment.introsde/") Person person);
    
    /*
     * Method #3: updatePerson(Person p) => Person | should update 
     * the Personal information of the Person identified by {id} 
     * (e.g., only the Person's information, not the measures of the 
     * health profile)
     */
    @WebMethod(operationName="updatePerson")
    @WebResult(name="personId") 
    public int updatePerson(@WebParam(name="person", targetNamespace = "http://ws.document.soap.assignment.introsde/") Person person);
    
    /*
     * Method #5: deletePerson(Long id) should delete the 
     * Person identified by {id} from the system
     */
    @WebMethod(operationName="deleteP")
    @WebResult(name="success") 
    public int deletePerson(@WebParam(name="personId") int id);

    
    /*
     * Method #10: updatePersonMeasure(Long id, Measure m) => Measure | should
     *  update the measure identified with {m.mid}, related to the Person 
     *  identified by {id}
     */
    @WebMethod(operationName="updatePersonHealthProfile")
    @WebResult(name="hpId") 
    //public int updatePersonHP(@WebParam(name="personId") int id,@WebParam(name="healthProfile") LifeStatus hp);
    public Long updatePersonHP(int id, Measure m);
    
    /*
     * Method #6: readPersonHistory(Long id, String measureType) => List 
     * should return the list of values (the history) of {measureType}
     *  (e.g. weight) for Person identified by {id}
     */
    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="measuresList") 
    public List<String> readPersonHistory(int id, String measureType);

    /*
     * Method #7: readMeasureTypes() => List should return the list of measures
     */
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measuresList") 
    public List<Measure> readMeasureTypes();
    
    /*
     * Method #8: readPersonMeasure(Long id, String measureType, Long mid) => Measure
     *  should return the value of {measureType} (e.g. weight) identified by 
     *  {mid} for Person identified by {id}
     */
    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="measureType") 
    public String readPersonMeasure(int id, String measureType, int mid);
    
    /*
     * Method #9: savePersonMeasure(Long id, Measure m) =>should save a new 
     * measure object {m} (e.g. weight) of Person identified by {id} and 
     * archive the old value in the history
     */
    @WebMethod(operationName="savePersonMeasure")
    @WebResult(name="measureId") 
    public Long savePersonMeasure(int id, Measure m);
}