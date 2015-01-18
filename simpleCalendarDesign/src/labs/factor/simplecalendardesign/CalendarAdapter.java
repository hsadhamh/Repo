package labs.factor.simplecalendardesign;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class CalendarAdapter extends BaseAdapter{

	ArrayList<String> m_RowData = null;
	Context m_Context;
	int m_nGridRowValue;
	Activity m_oActivity;
	Button m_oDayButton; 
	
	CalendarAdapter(Context ContentView, ArrayList<String> data)
	{
		super();
		m_Context = ContentView;
		m_RowData = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_RowData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_RowData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View textViewHolder = convertView;
		
		try
		{
			if(convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				textViewHolder = inflater.inflate(R.layout.calendar_row_grid, parent, false);
			}

			m_oDayButton = (Button) textViewHolder.findViewById(R.id.day_month_year);
			m_oDayButton.setText(m_RowData.get(position));
			m_oDayButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String sDate = (String) m_oDayButton.getText(); 
					Toast.makeText(m_Context, "Date:" + sDate, 15).show();
				}
			});
		}
		catch(Exception ex)
		{
			Log.d("getview", "Exception : " + ex.getMessage());
		}
		return textViewHolder;
	}

}
