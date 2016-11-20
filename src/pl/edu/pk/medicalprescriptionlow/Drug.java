package pl.edu.pk.medicalprescriptionlow;

import java.util.Calendar;
import java.util.Date;

public class Drug{
	
	private String name;
	private int id;
	private int periodTime;
	private int amountOfDoses;
	private int remainingDoses;
	private Date dateOfFirstDose;
	private Date dateOfNextDose;
	private Date dateOfLastDose;
	private long eventID;

	public Drug(){}
	public Drug(String name,int periodTime,int amountOfDoses,Date dateOfFirstDose)
	{
		this.name=name;
		this.periodTime=periodTime;
		this.amountOfDoses=amountOfDoses;
		this.dateOfFirstDose=dateOfFirstDose;
		this.remainingDoses=amountOfDoses;
		this.dateOfNextDose=dateOfFirstDose;
	}
	public String getName()
	{
		return name;
	}
	public int getId()
	{
		return id;
	}
	public int getPeriodTime()
	{
		return periodTime;
	}

	public int getAmountOfDoses()
	{
		return amountOfDoses;
	}
	public int getRemainingDoses()
	{
		return remainingDoses;
	}
	public Date getDateOfFirstDose()
	{
		return dateOfFirstDose;
	}
	public Date getDateOfNextDose()
	{
		return dateOfNextDose;
	}
	public long getEventId()
	{
		return eventID;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void setId(int id )
	{
		this.id = id;
	}
	public void setPeriodTime(int periodTime)
	{
		this.periodTime=periodTime;
	}
	public void setAmountOfDoses(int amountOfDoses)
	{
		this.amountOfDoses=amountOfDoses;
	}
	public void setRemainingDoses(int remainingDoses)
	{
		this.remainingDoses=remainingDoses;
	}

	public void setDateOfFirstDose(Date dateOfFirstDose)
	{
		this.dateOfFirstDose=dateOfFirstDose;
	}
	public void setDateOfNextDose(Date dateOfNextDose)
	{
		this.dateOfNextDose=dateOfNextDose;
	}
	public void setDateOfLastDose(Date dateOfLastDose)
	{
		this.dateOfLastDose=dateOfLastDose;
	}
	public void setEventId(long eventID )
	{
		this.eventID = eventID;
	}

	public String toString(){
		return this.name +" " +DateConverter.dateToString(this.dateOfNextDose)+ " " + this.remainingDoses;

	}

	public Date calcNextDose(Date dateLast,int hour_to_add)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateLast);
		calendar.add(Calendar.HOUR_OF_DAY,hour_to_add);
		Date dateNew = calendar.getTime();
		return dateNew;
	}

	/**
	 * Change date next dose and decrement number of remain doses
	 * @return true if remaining doses are greater than 0 else false
     */
	public boolean takeDose()
	{
		if(this.remainingDoses > 0)
		{
			this.dateOfNextDose=calcNextDose(this.dateOfNextDose,this.periodTime);
			this.remainingDoses = this.remainingDoses-1;
			return true;
		}else{
			return false;
		}
	}

}