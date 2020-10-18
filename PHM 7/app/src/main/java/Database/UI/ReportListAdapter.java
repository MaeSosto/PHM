package Database.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phm.MainActivity;
import com.example.phm.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import Database.Report;
import Database.ReportViewModel;

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
            holder.setData(report.getNota(), position);
            holder.setListeners();
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
        private ImageView IMGDelete, IMGEdit;

        //COSTRUTTORE
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportItemView = itemView.findViewById(R.id.TXVReport);
            IMGDelete = itemView.findViewById(R.id.IMGDelete);
            IMGEdit = itemView.findViewById(R.id.IMGEdit);
        }

        public void setData(String descrizione, int position) {
             reportItemView.setText(descrizione);
             mPosition = position;
        }

        public void setListeners() {
            IMGEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditReportActivity.class);
                    int id = mReports.get(mPosition).getId();
                    intent.putExtra("report_id", id);
                    ((Activity)mContext).startActivity(intent);
                }

            });

            IMGDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Report report = mReports.get(mPosition);
                    MainActivity.reportViewModel.deleteReport(report);

                    /*
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
        R.string.email_archived, Snackbar.LENGTH_SHORT);
mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
mySnackbar.show();
                     */
                    SnackbarUndo SU = new SnackbarUndo();
                    SU.reportRimosso(report);
                    Snackbar snackbar = Snackbar.make(v, "Report rimosso", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setAction("Cancella operazione", SU);
                    snackbar.show();
                }
            });

        }
    }
}
