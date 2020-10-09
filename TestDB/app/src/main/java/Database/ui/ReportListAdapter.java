package Database.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdb.MainActivity;
import com.example.testdb.R;

import java.util.List;

import Database.Report;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Report> mReports;

    public ReportListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        ReportViewHolder reportViewHolder = new ReportViewHolder(itemView);
         return reportViewHolder;
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

    //Quando la lista dei report cambia aggiorna i valori della lista dei report
    public void setReports(List<Report> reports){
        mReports = reports;
        notifyDataSetChanged();
    }

    public class ReportViewHolder extends  RecyclerView.ViewHolder{

        private TextView reportItemView;
        private int mPosition;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportItemView = itemView.findViewById(R.id.txvNote);
        }

        public void setData(String descrizione, int position) {
            reportItemView.setText(descrizione);
            mPosition = position;
        }
    }
}
