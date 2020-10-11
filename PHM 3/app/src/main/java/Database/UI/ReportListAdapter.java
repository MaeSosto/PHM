package Database.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phm.MainActivity;
import com.example.phm.R;

import java.util.List;

import Database.Report;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {

    //VARIABILI
    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Report> mReports;

    //COSTRUTTORE
    public ReportListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        ReportViewHolder viewHolder = new ReportViewHolder(itemView);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        if(mReports != null){
            Report report = mReports.get(position);
            holder.setData(report.getDescrizione(), position);
        }
        else {
            holder.reportItemView.setText(R.string.no_report);
        }
    }

    @Override
    public int getItemCount() {
        if(mReports != null)
            return mReports.size();
        else return 0;
    }

    public void setReports(List<Report> reports) {
        mReports = reports;
        notifyDataSetChanged();
    }

    //VIEW HOLDER
    public class ReportViewHolder extends RecyclerView.ViewHolder{

        private TextView reportItemView;
        private int mPosition;

        //COSTRUTTORE
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportItemView = itemView.findViewById(R.id.TXVReport);
        }

        public void setData(String descrizione, int position) {
             reportItemView.setText(descrizione);
             mPosition = position;
        }
    }
}
