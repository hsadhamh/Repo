package labs.factor.simplecalendardesign;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.*;
import android.view.View.OnClickListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.Camera.PreviewCallback;
import android.text.format.DateFormat;






import java.util.ArrayList;
import java.util.Locale;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;


public class CalendarMainActivity extends Activity implements OnClickListener {

	private static final String tag = "CalendarMainActivity";
	
	private GridView calendarView;
	private TextView currentMonth;
	private Button NextButton; 
	private Button PrevButton;
	
	private Calendar _calendar;
	private int month, year;
	
	BaseAdapter adapter;
	
	//
	//	Calendar Day Month Year Information. 
	//
	int m_nDaySelected = 1;
	
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat(); 
	private static final String dateTemplate = "MMMM yyyy";
	private static final String dayTemplate = "dd";
	
	private final int[] daysOfMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30,
		    31, 30, 31 };
	
	ArrayList<String> oData = new ArrayList<String>();

	//private GridCellAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_main);
		
		_calendar = Calendar.getInstance(Locale.getDefault());
		
		calendarView = (GridView) this.findViewById(R.id.calendar);
		NextButton = (Button) this.findViewById(R.id.buttonNext);
		PrevButton = (Button) this.findViewById(R.id.buttonPrevious);
		currentMonth = (TextView) this.findViewById(R.id.MonthView);
		
		currentMonth.setText(DateFormat.format(dateTemplate, 
				_calendar.getTime()));
		NextButton.setOnClickListener(NextPrevListener);
		PrevButton.setOnClickListener(NextPrevListener);
				 
		m_nDaySelected = _calendar.get(Calendar.DAY_OF_WEEK);
		month = _calendar.get(Calendar.MONTH) + 1; 
		year = _calendar.get(Calendar.YEAR); 
		
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);
		
		prepareMonthView(month, year);
		
		adapter = new CalendarAdapter(getApplicationContext(), oData);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	private View.OnClickListener NextPrevListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String sTag = (String) v.getTag();
			
			String[] arrButtonInfo = sTag.split("|");
			
			StringTokenizer oStringTokens = new StringTokenizer(sTag, "|");
			
			int ValuesFromTags[] = {0,0,0};
			int i=0;
			while(oStringTokens.hasMoreElements())
			{
				ValuesFromTags[i] = Integer.parseInt((String)oStringTokens.nextElement());
				i++;
			}
			
			if(ValuesFromTags[0] == 0)
			{
				int nMonth = ValuesFromTags[1];
				int nYear = ValuesFromTags[2];
				
				prepareMonthView(nMonth, nYear);
				
				GregorianCalendar oCal = new GregorianCalendar(nYear, nMonth-1, 1);
				currentMonth.setText(DateFormat.format(dateTemplate, 
						oCal.getTime()));
				
				adapter = new CalendarAdapter(getApplicationContext(), oData);
				adapter.notifyDataSetChanged();
				calendarView.setAdapter(adapter);
			}
			if(ValuesFromTags[0] == 1)
			{
				int nMonth = ValuesFromTags[1];
				int nYear = ValuesFromTags[2];
				
				GregorianCalendar oCal = new GregorianCalendar(nYear, nMonth-1, 1);
				currentMonth.setText(DateFormat.format(dateTemplate, 
						oCal.getTime()));
				
				prepareMonthView(nMonth, nYear);
				adapter = new CalendarAdapter(getApplicationContext(), oData);
				adapter.notifyDataSetChanged();
				calendarView.setAdapter(adapter);
			}
		
		}
		
	};
	
	private boolean prepareMonthView(int month, int year)
	{
		try
		{
			oData.clear();
			
			int nDaysFromPrevMonth = 0;
			int nDaysFromNextMonth = 0;
			
			int prevYear = year, nextYear = year;
			
			int prevMonth = month -1, nextMonth = month +1;
			if(month == 1)
			{
				prevMonth = 12;
				prevYear--;
			}
			
			if(month == 12)
			{
				nextMonth = 1;
				nextYear++;
			}
			
			
			String prevButtonTagInfo = "0|"+prevMonth+"|"+prevYear;
			String nextButtonTagInfo = "1|"+nextMonth+"|"+nextYear;
			
			NextButton.setTag(nextButtonTagInfo);
			PrevButton.setTag(prevButtonTagInfo);
			
			
			int nPrevMonthDays = daysOfMonth[prevMonth];
			int nDaysOfCurrentMonth = daysOfMonth[month];
			int nNextMonthDays = daysOfMonth[nextMonth];
			
			GregorianCalendar oGregCalendar = new GregorianCalendar(year, month-1, 1);
			
			nDaysFromPrevMonth = oGregCalendar.get(Calendar.DAY_OF_WEEK) - 1;
			nDaysFromNextMonth = ((nDaysFromPrevMonth + nDaysOfCurrentMonth) % 7);
			int nTotalDaysInView = nDaysFromNextMonth + nDaysOfCurrentMonth + nDaysFromPrevMonth;
			
			Log.d("prepareMonth",	"days : "+ nDaysOfCurrentMonth +" prev :"+ nDaysFromPrevMonth +" next : "+ nDaysFromNextMonth +" total :"+ 
						nTotalDaysInView +" NP:"+ nPrevMonthDays +" NN:"+ 
					nNextMonthDays +" NC: " + nDaysOfCurrentMonth);
			
			// Previous month days.
			for(int iter=nPrevMonthDays - nDaysFromPrevMonth+1  ; iter <= nPrevMonthDays; iter++ )
			{
				oData.add(String.valueOf(iter));
			}
			
			for(int iter=1; iter <= nDaysOfCurrentMonth; iter++ )
			{
				oData.add(String.valueOf(iter));
			}
			
			if(nDaysFromNextMonth > 0)
			{
				for(int iter=1; iter <= (7 - nDaysFromNextMonth); iter++ )
				{
					oData.add(String.valueOf(iter));
				}
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String sTag = (String)v.getTag();
		
		
	}
}
