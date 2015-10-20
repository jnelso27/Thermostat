package application.thermostat.test;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask 
{
	public double temp = 0.1;
	 
    @Override
    public void run() 
    {
        //System.out.println("Timer task started at:"+new Date());
        completeTask();
        //System.out.println("Timer task finished at:"+new Date());
    }
 
    private void completeTask() 
    {
        try 
        {
        	temp = temp*1.12;
            //assuming it takes 20 secs to complete the task
            Thread.sleep(1000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
     
    public static void main(String args[])
    {
        TimerTask timerTask = new MyTimerTask();
        
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 10*1000);
        System.out.println("TimerTask started");
        
        //cancel after sometime
        try 
        {
            Thread.sleep(120000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        
        timer.cancel();
        System.out.println("TimerTask cancelled");
        
        try 
        {
            Thread.sleep(30000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
 
}
