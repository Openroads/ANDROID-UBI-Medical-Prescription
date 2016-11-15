package pl.pk.edu.MedicalPrescription;
import java.util.Date;

public class Drug{
	
	private String name;
	private int periodTime;
	private int amountOfDoses;
	private Date dateOfFirstDose;
	private Date dateOfNextDose;
	private Date dateOfLastDose;
	private int markStatus;

	public Drug(){}
	public Drug(String name,int periodTime,int amountOfDoses,Date dateOfFirstDose)
	{
		this.name=name;
		this.periodTime=periodTime;
		this.amountOfDoses=amountOfDoses;
		this.dateOfFirstDose=dateOfFirstDose;
	}
	public String getName()
	{
		return name;
	}
	public int getPeriodTime()
	{
		return periodTime;
	}

	public int getAmountOfDoses()
	{
		return amountOfDoses;
	}
	public Date getDateOfFirstDose()
	{
		return dateOfFirstDose;
	}
	public int getMarkStatus()
	{
		return this.markStatus;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void setPeriodTime(int periodTime)
	{
		this.periodTime=periodTime;
	}
	public void setAmountOfDoses(int amountOfDoses)
	{
		this.amountOfDoses=amountOfDoses;
	}

	public void setDateOfFirstDose(Date dateOfFirstDose)
	{
		this.dateOfFirstDose=dateOfFirstDose;
	}
	public void setMarkStatus(int markStatus)
	{
		this.markStatus=markStatus;
	}

	public String toString(){
		return name+" " +periodTime+" "+amountOfDoses;

	}


}