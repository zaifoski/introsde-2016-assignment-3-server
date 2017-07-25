package introsde.assignment.soap.document.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.beanutils.converters.DateConverter;

import introsde.assignment.soap.document.dao.LifeCoachDao;

@Entity
@Cacheable(false)
@Table(name = "Measure")
@NamedQueries({
		@NamedQuery(name = "Measure.findAll", query = "SELECT m FROM Measure m"),
		@NamedQuery(name = "Measure.findByPersonId", query = "SELECT m FROM Measure m WHERE m.person = ?1"),
		@NamedQuery(name = "Measure.findByMeasureType", query = "SELECT m FROM Measure m WHERE m.person = ?1 AND m.measureType = ?2 AND m.isCurrent = 0"),
		@NamedQuery(name = "Measure.findMeasureTypes", query = "SELECT DISTINCT m.measureType FROM Measure m"),
		@NamedQuery(name = "Measure.findByMeasureMid", query = "SELECT m FROM Measure m WHERE m.person = ?1 AND m.measureType = ?2 AND m.idMeasure =?3"),
		@NamedQuery(name = "Measure.getCurrentMeasure", query = "SELECT m FROM Measure m WHERE m.person = ?1 AND m.measureType = ?2 AND m.isCurrent = 1") })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Measure implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_measure")
	@TableGenerator(name = "sqlite_measure", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Measure")
	@Column(name = "idMeasure")
	@XmlElement(name = "mid")
	private Long idMeasure;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateRegistered")
	@Convert(converter = DateConverter.class)
	private Date dateRegistered;

	@Column(name = "measureType")
	private String measureType;

	@Column(name = "measureValue")
	private String measureValue;

	@Column(name = "valueType")
	@XmlElement(name="measureValueType")
	private String valueType;

	@Column(name="isCurrent")
	@XmlTransient
	private int isCurrent; // 1 current Health, 0 history Health

	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	@XmlTransient
	private Person person;
	
	public Measure(){}
	
	public Measure(Date dRegistered, String mType, String mValue, String mValueType, Person person){
		this.dateRegistered = dRegistered;
		this.measureType = mType;
		this.measureValue = mValue;
		this.valueType = mValueType;
		this.person = person;
	}

	// Getters
	public Long getIdMeasure() {
		return idMeasure;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public String getMeasureType() {
		return measureType;
	}

	public String getMeasureValue() {
		return measureValue;
	}

	public String getValueType() {
		return valueType;
	}

	public int getIsCurrent() {
		return isCurrent;
	}
	
	public Person getPerson() {
		return person;
	}

	// Setters
	public void setIdMeasure(Long idMeasure) {
		this.idMeasure = idMeasure;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public void setIsCurrent(int isCurrent) {
		this.isCurrent = isCurrent;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}

	public String toString() {
		return "Measure ( " + idMeasure + ", " + dateRegistered + ", "
				+ measureType + ", " + measureValue + ", " + valueType + ", " + isCurrent
				+ " )";
	}

	// database operations
	public static Measure getMeasureById(Long measureId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Measure m = em.find(Measure.class, measureId);
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}

	public static List<Measure> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Measure> list = em.createNamedQuery("Measure.findAll",
				Measure.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	public static Measure saveMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(m);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}

	public static Measure updateMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		m = em.merge(m);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}

	public static void removeMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		m = em.merge(m);
		em.remove(m);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}

	/**
	 * Returns the list of the measures associated of one person
	 * @param p
	 * @return
	 */
	public static List<Measure> getAllMeasures(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Measure> measuresList = em.createNamedQuery("Measure.findByPersonId", Measure.class)
				.setParameter(1, p).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return measuresList;
	}
	
	/**
	 * Returns the history of a measureType for a person
	 * @param p
	 * @param md
	 * @return list of HealthMeasureHistory
	 */
	public static List<Measure> getHistoryMeasureByPerson(Person p, String measureType) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		TypedQuery<Measure> query = em.createNamedQuery(
				"Measure.findByMeasureType", Measure.class);
		query.setParameter(1, p);
		query.setParameter(2, measureType);
		List<Measure> list = query.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	/**
	 * Returns the list of the measureTypes
	 * @return list of the measureTypes
	 */
	public static List<String> getByMeasureTypes() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<String> list = em.createNamedQuery("Measure.findMeasureTypes",
				String.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	/**
	 * Given a person, measure type and mid, the function returns the corresponding Measure
	 * object.
	 * @param p
	 * @param measureType
	 * @param idMeasure
	 * @return
	 */
	public static Measure getByPersonMid(Person p, String measureType,Long idMeasure) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Measure measure = em
				.createNamedQuery("Measure.findByMeasureMid", Measure.class)
				.setParameter(1, p)
				.setParameter(2, measureType)
				.setParameter(3, idMeasure).getSingleResult();
		LifeCoachDao.instance.closeConnections(em);
		return measure;
	}

	/**
	 * Given a measure type and personId, the function returns the corresponding Measure
	 * object.
	 * @param person
	 * @param measureType
	 * @return Measure
	 */
	public static Measure foundCurrentMeasure(Person person, String measureType) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		try {
			Measure measure = em.createNamedQuery("Measure.getCurrentMeasure", Measure.class)
						.setParameter(1, person)
						.setParameter(2, measureType)
						.getSingleResult();
			LifeCoachDao.instance.closeConnections(em);
			return measure;
		} catch (Exception e) {
			return null;
		}
	}
}