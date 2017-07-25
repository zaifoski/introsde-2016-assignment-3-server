package introsde.assignment.soap.document.client;
 
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import introsde.assignment.soap.document.model.Person;
import introsde.assignment.soap.document.ws.People;
 
public class PeopleClient{
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost:6907/ws/people?wsdl");
        //1st argument service URI, refer to wsdl document above
		//2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ws.document.soap.assignment.introsde/", "PeopleService");
        Service service = Service.create(url, qname);
        
        People people = service.getPort(People.class);
        Person p = people.readPerson(1);
        List<Person> pList = people.getPeople();
        System.out.println("Result ==> "+p);
        System.out.println("Result ==> "+pList);
        System.out.println("First Person in the list ==> "+pList.get(0).getName());
    }
}